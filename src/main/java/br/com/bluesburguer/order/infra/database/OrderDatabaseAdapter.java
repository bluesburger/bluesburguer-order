package br.com.bluesburguer.order.infra.database;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderMapper;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.domain.entity.Order;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.domain.exception.OrderNotFoundException;
import br.com.bluesburguer.order.domain.exception.UserNotFoundException;
import br.com.bluesburguer.order.domain.repository.IOrderDatabaseAdapter;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderDatabaseAdapter implements IOrderDatabaseAdapter {
	
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	
	private final UserAdapter userAdapter;
	private final OrderMapper orderMapper;
	
	@Override
	public List<Order> getAll() {
		return orderRepository.findAll().stream()
				.map(orderMapper::toOrder)
				.toList();
	}

	@Override
	public OrderEntity getById(String id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public List<Order> getAllByStep(OrderStep step, List<OrderFase> fases) {
		if (fases == null || fases.isEmpty()) {
			fases = Stream.of(OrderFase.values()).toList();
		}
		return orderRepository
				.findAllByStepAndFaseInOrderByCreatedTime(step, fases).stream()
				.map(orderMapper::toOrder)
				.toList();
	}

	@Override
	public OrderDto createNewOrder(OrderRequest command) {
		var orderUser = Optional.ofNullable(command.getUser())
				.map(userAdapter::saveIfNotExist)
				.orElseThrow(UserNotFoundException::new);
		
		var newOrder = new OrderEntity(OrderFase.REGISTERED, orderUser);
		
		var savedOrder = orderRepository.save(newOrder);
		command.getItems().stream()
			.map(item -> saveItem(item, savedOrder))
			.forEach(savedOrder::add);
		
		return orderMapper.toOrderDto(savedOrder);
	}

	@Override
	public Order updateStepAndFase(String orderId, OrderStep step, OrderFase fase) {
		return Optional.ofNullable(getById(orderId))
				.map(order -> {
					order.setStep(step);
					order.setFase(fase);
					return orderRepository.save(order);
				})
				.map(orderMapper::toOrder)
				.orElse(null);
	}

	@Override
	public Order updateFase(String orderId, OrderFase fase) {
		return Optional.ofNullable(getById(orderId))
				.map(order -> {
					order.setFase(fase);
					return orderRepository.save(order);
				})
				.map(orderMapper::toOrder)
				.orElse(null);
	}

	@Override
	public Order updateOrderItems(String orderId, List<OrderItemRequest> orderItems) {
		return Optional.ofNullable(getById(orderId))
				.map(order -> {
					deleteAllItemsByOrderId(order.getId());
					orderItems.stream()
						.map(item -> saveItem(item, order))
						.forEach(order::add);
					return order;
				})
				.map(orderMapper::toOrder)
				.orElse(null);
	}
	
	@Override
	public void deleteAllItemsByOrderId(String orderId) {
		var order = Optional.ofNullable(getById(orderId))
				.orElseThrow(OrderNotFoundException::new);
		order.getItems().forEach(item -> orderItemRepository.deleteById(item.getId()));
	}

	@Override
	public void deleteById(String orderId) {
		var order = Optional.ofNullable(getById(orderId))
				.orElseThrow(OrderNotFoundException::new);
		deleteAllItemsByOrderId(orderId);
		orderRepository.deleteById(order.getId());
	}
	
	private OrderItemEntity saveItem(OrderItemRequest itemRequest, OrderEntity order) {
		var item = new OrderItemEntity(itemRequest.getId(), order);
		item.setQuantity(itemRequest.getQuantity());
		return orderItemRepository.save(item);
	}
}
