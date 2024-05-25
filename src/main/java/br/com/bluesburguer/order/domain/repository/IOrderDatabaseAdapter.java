package br.com.bluesburguer.order.domain.repository;

import java.util.List;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.domain.entity.Order;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;

public interface IOrderDatabaseAdapter {
	
	List<Order> getAll();
	
	List<Order> getAllByStep(OrderStep step, List<OrderFase> fases);
	
	OrderEntity getById(String id);
	
	OrderDto createNewOrder(OrderRequest command);
	
	Order updateStepAndFase(String orderId, OrderStep step, OrderFase fase);
	
	Order updateFase(String orderId, OrderFase fase);
	
	Order updateOrderItems(String orderId, List<OrderItemRequest> orderItems);
	
	void deleteAllItemsByOrderId(String orderId);
	
	void deleteById(String orderId);
}
