package br.com.bluesburguer.order.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;

public interface OrderPort {

	List<OrderEntity> getAllByStep(OrderStep step, List<OrderFase> fases);
	
	Optional<OrderEntity> getById(UUID orderId);
	
	Optional<OrderItemEntity> getItemById(Long orderItemId);
	
	Optional<OrderEntity> createNewOrder(OrderRequest command);
	
	Optional<OrderEntity> updateStepAndFase(UUID orderId, OrderStep step, OrderFase fase);

	Optional<OrderEntity> updateFase(UUID orderId, OrderFase fase);
	
	Optional<OrderEntity> updateOrderItems(UUID orderId, List<OrderItemRequest> orderItems);
	
	OrderItemEntity saveItem(OrderItemRequest itemRequest, OrderEntity order);
	
	void deleteById(UUID orderId);
	
	OrderItemEntity addItem(UUID orderId, OrderItemRequest itemRequest);
}
