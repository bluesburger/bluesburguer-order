package br.com.bluesburguer.order.infra.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderMapper;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.application.sqs.events.OrderCreatedEvent;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.domain.exception.OrderNotFoundException;
import br.com.bluesburguer.order.domain.exception.UserNotFoundException;
import br.com.bluesburguer.order.domain.service.OrderPort;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;
import br.com.bluesburguer.order.infra.sqs.events.OrderEventPublisher;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class OrderAdapter implements OrderPort {
	
	private final UserAdapter userService;

	private final OrderItemRepository orderItemRepository;

	private final OrderRepository orderRepository;
	
	private final OrderMapper orderMapper;
	
	private final OrderEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher;

	public List<OrderEntity> getAll() {
		return orderRepository.findAll();
	}
	
	public List<OrderEntity> getAllByStep(OrderStep step, List<OrderFase> fases) {
		if (fases == null || fases.isEmpty()) {
			fases = Stream.of(OrderFase.values()).toList();
		}
		return orderRepository.findAllByStepAndFaseInOrderByCreatedTime(step, fases);
	}

	public Optional<OrderEntity> getById(UUID orderId) {
		return orderRepository.findById(orderId.toString());
	}
	
	public Optional<OrderItemEntity> getItemByOrderItemId(Long orderItemId) {
		return orderItemRepository.findByOrderItemId(orderItemId);
	}

	public Optional<OrderItemEntity> getItemById(Long orderItemId) {
		return orderItemRepository.findById(orderItemId);
	}

	@Transactional(
			rollbackOn = UserNotFoundException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<OrderEntity> createNewOrder(OrderRequest command) {
		var orderUser = Optional.ofNullable(command.getUser())
				.map(userService::saveIfNotExist)
				.orElseThrow(UserNotFoundException::new);
		
		var newOrder = new OrderEntity(OrderFase.CREATED, orderUser);
		
		var savedOrder = orderRepository.save(newOrder);
		command.getItems().stream()
			.forEach(item -> {
				var optionalItem = orderItemRepository.findById(item.getId());
				optionalItem.orElse(saveItem(item, savedOrder));
			});
//			.forEach(savedOrder::add);
		
		var event = OrderCreatedEvent.builder().orderId(savedOrder.getId()).build();
		orderCreatedEventPublisher.publish(event);
		return Optional.of(savedOrder);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<OrderEntity> updateStepAndFase(UUID orderId, OrderStep step, OrderFase fase) {
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
	public Optional<OrderEntity> updateFase(UUID orderId, OrderFase fase) {
		return getById(orderId)
			.map(order -> {
				order.setFase(fase);
				return orderRepository.save(order);
			});
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Optional<OrderEntity> updateOrderItems(UUID orderId, List<OrderItemRequest> orderItems) {
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
	public OrderItemEntity saveItem(OrderItemRequest itemRequest, OrderEntity order) {
		var item = new OrderItemEntity(itemRequest.getId(), order);
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
	public OrderItemEntity addItem(UUID orderId, OrderItemRequest itemRequest) {
		return Optional.ofNullable(orderId)
				.map(oId -> orderRepository.findById(oId.toString()).orElseThrow(OrderNotFoundException::new))
				.map(order -> {
					var item = new OrderItemEntity(itemRequest.getId(), order);
					item.setQuantity(itemRequest.getQuantity());
					return orderItemRepository.save(item);
				})
				.orElseThrow(OrderNotFoundException::new);
	}
}
