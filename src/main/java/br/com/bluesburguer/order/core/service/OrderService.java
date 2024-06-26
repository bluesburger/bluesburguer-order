package br.com.bluesburguer.order.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.out.OrderNotFoundException;
import br.com.bluesburguer.order.adapters.out.UserNotFoundException;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.ports.OrderPort;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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

	public Optional<Order> getById(UUID orderId) {
		return orderRepository.findById(orderId.toString());
	}
	
	public Optional<OrderItem> getItemByOrderItemId(Long orderItemId) {
		return orderItemRepository.findByOrderItemId(orderItemId);
	}

	public Optional<OrderItem> getItemById(Long orderItemId) {
		return orderItemRepository.findById(orderItemId);
	}

	@Transactional(
			rollbackOn = UserNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> createNewOrder(OrderRequest command) {
		var orderUser = Optional.ofNullable(command.getUser())
				.map(userService::saveIfNotExist)
				.orElseThrow(UserNotFoundException::new);
		
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
	public Optional<Order> updateStepAndFase(UUID orderId, OrderStep step, OrderFase fase) {
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
	public Optional<Order> updateFase(UUID orderId, OrderFase fase) {
		return getById(orderId)
			.map(order -> {
				order.setFase(fase);
				return orderRepository.save(order);
			});
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<Order> updateOrderItems(UUID orderId, List<OrderItemRequest> orderItems) {
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
		var item = new OrderItem(itemRequest.getId(), order);
		item.setQuantity(itemRequest.getQuantity());
		return orderItemRepository.save(item);
	}

	@Transactional(
			rollbackOn = OrderNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public void deleteById(UUID orderId) {
		var order = getById(orderId).orElseThrow(OrderNotFoundException::new);
		orderItemRepository.deleteAllByOrderId(order.getId());
		orderRepository.deleteById(order.getId());
	}

	@Transactional(
			rollbackOn = OrderNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem addItem(UUID orderId, OrderItemRequest itemRequest) {
		return Optional.ofNullable(orderId)
				.map(oId -> orderRepository.findById(oId.toString()).orElseThrow(OrderNotFoundException::new))
				.map(order -> {
					var item = new OrderItem(itemRequest.getId(), order);
					item.setQuantity(itemRequest.getQuantity());
					return orderItemRepository.save(item);
				})
				.orElseThrow(OrderNotFoundException::new);
	}
}
