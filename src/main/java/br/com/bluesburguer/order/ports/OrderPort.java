package br.com.bluesburguer.order.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;

public interface OrderPort {

	List<Order> getAllByStep(OrderStep step, List<OrderFase> fases);
	
	Optional<Order> getById(UUID orderId);
	
	Optional<OrderItem> getItemById(Long orderItemId);
	
	Optional<Order> createNewOrder(OrderRequest command);
	
	Optional<Order> updateStepAndFase(UUID orderId, OrderStep step, OrderFase fase);

	Optional<Order> updateFase(UUID orderId, OrderFase fase);
	
	Optional<Order> updateOrderItems(UUID orderId, List<OrderItemRequest> orderItems);
	
	OrderItem saveItem(OrderItemRequest itemRequest, Order order);
	
	void deleteById(UUID orderId);
	
	OrderItem addItem(UUID orderId, OrderItemRequest itemRequest);
}
