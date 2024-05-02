package br.com.bluesburguer.orderingsystem.order.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.orderingsystem.domain.Fase;
import br.com.bluesburguer.orderingsystem.order.infra.OrderItemRepository;
import br.com.bluesburguer.orderingsystem.order.infra.OrderRepository;
import br.com.bluesburguer.orderingsystem.order.infra.UserRepository;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderItemRequest;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderRequest;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.UserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * 
 * FIXME: trocar IllegalArgumentException para uma exceção específica, que retorne 404
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class OrderService {

	private final OrderItemRepository orderItemRepository;

	private final OrderRepository orderRepository;
	
	private final UserRepository userRepository;

	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> getById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	public Optional<OrderItem> getItemById(Long orderItemId) {
		try {
			return Optional.of(orderItemRepository.getReferenceById(orderItemId));
		} catch (EntityNotFoundException e) {
			log.error("", e);
			return Optional.empty();
		}
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order createNewOrder(OrderRequest command) {
		var newOrder = new Order();
		newOrder.setFase(Fase.PENDING);
		
		Optional.ofNullable(command.getUser())
			.map(this::saveUserIfNotExistant)
			.ifPresent(newOrder::setUser);
		
		var savedOrder = orderRepository.save(newOrder);
		command.getItems().stream()
			.map(item -> saveItem(item, savedOrder))
			.forEach(savedOrder::add);
		
		return savedOrder;
	}
	
	private User saveUserIfNotExistant(UserRequest userRequest) {
		var optionalCpf = Optional.ofNullable(userRequest.getCpf());
		var optionalEmail = Optional.ofNullable(userRequest.getEmail());
		
		var cpf = optionalCpf.orElse(null);
		var email = optionalEmail.orElse(null);
		return userRepository.findByCpfOrEmail(cpf, email)
				.map(user -> {
					user.setCpf(cpf);
					user.setEmail(email);
					return userRepository.save(user);
				})
				.orElseGet(() -> createIdentifiedUser(cpf, email));
	}
	
	private User createIdentifiedUser(String cpf, String email) {
		var newUser = new User();
		newUser.setCpf(cpf);
		newUser.setEmail(email);
		return userRepository.save(newUser);
	}
	
	/*
	private Double calcularValorTotalPedido(Order order) {
		return order.getItems().stream()
			.map(i -> i.getId())
			.map(menuService::findPriceByItemId)
			.collect(Collectors.summingDouble(Double::doubleValue));
	}
	*/

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order updateFase(Long orderId, Fase fase) {
		return getById(orderId)
			.map(order -> {
				order.setFase(fase);
				return orderRepository.save(order);
			}).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order update(Long orderId, List<OrderItemRequest> orderItems) {
		return getById(orderId)
				.map(order -> {
					orderItemRepository.deleteAllByOrderId(order.getId());
					orderItems.stream()
						.map(item -> saveItem(item, order))
						.forEach(order::add);
					return order;
				}).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem saveItem(OrderItemRequest itemRequest, Order order) {
		var item = new OrderItem();
		item.setId(itemRequest.getId());
		item.setOrder(order);
		item.setQuantity(itemRequest.getQuantity());
		return orderItemRepository.save(item);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public void delete(Long orderId) {
		var order = getById(orderId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
		orderItemRepository.deleteAllByOrderId(orderId);
		orderRepository.delete(order);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem addItem(OrderItem item) {
		if (Objects.isNull(item.getOrder())) {
			throw new IllegalArgumentException("Item necessita de uma ordem");
		}
		return orderItemRepository.save(item);
	}
}
