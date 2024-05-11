package br.com.bluesburguer.order.core.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.out.OrderNotFoundException;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.ports.OrderPort;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class OrderService implements OrderPort {
	
	private final UserService userService;

	private final OrderItemRepository orderItemRepository;

	private final OrderRepository orderRepository;

	public List<Order> getAll() {
		return orderRepository.findAll();
	}
	
	public List<Order> getAllByStep(OrderStep step, List<OrderFase> fases) {
		if (fases == null || fases.isEmpty()) {
			fases = Stream.of(OrderFase.values()).toList();
		}
		return orderRepository.findAllByStepAndFaseInOrderByCreatedTime(step, fases);
	}

	public Optional<Order> getById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	public Optional<OrderItem> getItemById(Long orderItemId) {
		try {
			return Optional.ofNullable(orderItemRepository.getReferenceById(orderItemId));
		} catch (EntityNotFoundException e) {
			log.error("Pedido não encontrado", e);
			return Optional.empty();
		}
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> createNewOrder(OrderRequest command) {
		var orderUser = Optional.ofNullable(command.getUser())
				.map(userService::saveIfNotExist)
				.orElse(userService.createAnonymous());
		
		var newOrder = new Order(OrderFase.PENDING, orderUser);
		
		var savedOrder = orderRepository.save(newOrder);
		command.getItems().stream()
			.map(item -> saveItem(item, savedOrder))
			.forEach(savedOrder::add);
		
		return Optional.of(savedOrder);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> updateStepAndFase(Long orderId, OrderStep step, OrderFase fase) {
		return getById(orderId)
			.map(order -> {
				order.setStep(step);
				order.setFase(fase);
				return orderRepository.save(order);
			});
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> updateFase(Long orderId, OrderFase fase) {
		return getById(orderId)
			.map(order -> {
				order.setFase(fase);
				return orderRepository.save(order);
			});
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> updateOrderItems(Long orderId, List<OrderItemRequest> orderItems) {
		return getById(orderId)
				.map(order -> {
					orderItemRepository.deleteAllByOrderId(order.getId());
					orderItems.stream()
						.map(item -> saveItem(item, order))
						.forEach(order::add);
					return order;
				});
	}
	
	@Transactional(
			rollbackOn = OrderNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem saveItem(OrderItemRequest itemRequest, Order order) {
		var item = new OrderItem();
		item.setId(itemRequest.getId());
		item.setOrder(order);
		item.setQuantity(itemRequest.getQuantity());
		return orderItemRepository.save(item);
	}

	@Transactional(
			rollbackOn = OrderNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public void deleteById(Long orderId) {
		var order = getById(orderId).orElseThrow(OrderNotFoundException::new);
		orderItemRepository.deleteAllByOrderId(orderId);
		orderRepository.delete(order);
	}

	@Transactional(
			rollbackOn = OrderNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem addItem(OrderItem item) {
		return Optional.ofNullable(item.getOrder())
				.map(o -> orderItemRepository.save(item))
				.orElseThrow(OrderNotFoundException::new);
	}
}
