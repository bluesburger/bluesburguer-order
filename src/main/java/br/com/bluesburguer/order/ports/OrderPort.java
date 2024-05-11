package br.com.bluesburguer.order.ports;

import java.util.List;
import java.util.Optional;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;

public interface OrderPort {

	List<Order> getAllByStep(OrderStep step, List<OrderFase> fases);
	
	Optional<Order> getById(Long orderId);
	
	Optional<OrderItem> getItemById(Long orderItemId);
	
	Optional<Order> createNewOrder(OrderRequest command);
	
	Optional<Order> updateStepAndFase(Long orderId, OrderStep step, OrderFase fase);

	Optional<Order> updateFase(Long orderId, OrderFase fase);
	
	Optional<Order> updateOrderItems(Long orderId, List<OrderItemRequest> orderItems);
	
	OrderItem saveItem(OrderItemRequest itemRequest, Order order);
	
	void deleteById(Long orderId);
	
	OrderItem addItem(OrderItem item);
}
