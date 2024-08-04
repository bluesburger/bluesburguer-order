package br.com.bluesburguer.order.domain.repository;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.domain.entity.OrderItem;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;

public interface IOrderItemDatabaseAdapter {
	
	OrderItem getItemByOrderItemId(Long orderItemId);
	
	OrderItem getItemById(Long orderItemId);

	OrderItemEntity saveItem(OrderItemRequest itemRequest, OrderEntity order);
	
	OrderItem addItem(String orderId, OrderItemRequest item);

	void deleteById(Long itemId);
}
