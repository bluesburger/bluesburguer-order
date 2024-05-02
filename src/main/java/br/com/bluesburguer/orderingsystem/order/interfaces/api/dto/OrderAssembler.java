package br.com.bluesburguer.orderingsystem.order.interfaces.api.dto;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import br.com.bluesburguer.orderingsystem.order.domain.Order;
import br.com.bluesburguer.orderingsystem.order.domain.OrderItem;

@Component
public class OrderAssembler {

	public OrderDto to(Order order) {
		var dto = new OrderDto();
		dto.setFase(order.getFase());
		dto.setId(order.getId()); 
		dto.setStep(order.getStep());
		order.getItems().forEach(item -> 
			dto.getItems().add(this.to(item))
		);
		return dto;
	}
	
	public OrderItemDto to(OrderItem orderItem) {
		var item = new OrderItemDto(orderItem.getId(), orderItem.getQuantity());
		// TODO: consultar valor no contexto de Menu
		// item.setItemValue(orderItem.getItemValue());
		return item;
	}
	
	public Order from(OrderDto orderDto) {
		var order = new Order(orderDto.getFase());
		var items = orderDto.getItems().stream()
			.map(itemDto -> {
				var item = new OrderItem();
				item.setId(itemDto.getId());
				return item;				
			})
			.toArray(size -> new OrderItem[size]);
		order.add(items);
		return order;
	}
	
	public OrderItem from(OrderItem item) {
		throw new NotImplementedException();
	}	
}
