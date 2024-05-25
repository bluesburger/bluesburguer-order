package br.com.bluesburguer.order.infra.database;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderMapper;
import br.com.bluesburguer.order.domain.entity.OrderItem;
import br.com.bluesburguer.order.domain.exception.OrderNotFoundException;
import br.com.bluesburguer.order.domain.repository.IOrderItemDatabaseAdapter;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemDatabaseAdapter implements IOrderItemDatabaseAdapter {
	
	private final OrderDatabaseAdapter orderDatabaseAdapter;
	private final OrderItemRepository orderItemRepository;
	private final OrderMapper orderMapper;

	@Override
	public OrderItem getItemByOrderItemId(Long orderId) {
		return orderItemRepository.findByOrderItemId(orderId)
				.map(orderMapper::toOrderItemEntity)
				.orElse(null);
	}

	@Override
	public OrderItem getItemById(Long orderItemId) {
		return orderItemRepository.findById(orderItemId)
				.map(orderMapper::toOrderItemEntity)
				.orElse(null);
	}

	@Override
	public OrderItemEntity saveItem(OrderItemRequest itemRequest, OrderEntity order) {
		var item = new OrderItemEntity(itemRequest.getId(), order);
		item.setQuantity(itemRequest.getQuantity());
		return orderItemRepository.save(item);
	}

	@Override
	public OrderItem addItem(String orderId, OrderItemRequest itemRequest) {
		return Optional.ofNullable(orderDatabaseAdapter.getById(orderId))
				.map(order -> {
					var item = new OrderItemEntity(itemRequest.getId(), order);
					item.setQuantity(itemRequest.getQuantity());
					return orderItemRepository.save(item);
				})
				.map(orderMapper::toOrderItemEntity)
				.orElseThrow(OrderNotFoundException::new);
		
	}

	@Override
	public void deleteById(Long itemId) {
		orderItemRepository.deleteById(itemId);
	}
}
