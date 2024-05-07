package br.com.bluesburguer.order.adapters.in.order.dto;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserMapper;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;

@Component
public class OrderMapper {
	
	@Autowired
	private UserMapper userAssembler;

	public OrderDto to(Order order) {
		var dto = new OrderDto();
		dto.setFase(order.getFase());
		dto.setId(order.getId()); 
		dto.setStep(order.getStep());
		order.getItems().forEach(item -> 
			dto.getItems().add(this.to(item))
		);
		
		dto.setUser(Optional.ofNullable(order.getUser())
			.map(user -> new UserDto(user.getId(), user.getCpf(), user.getEmail()))
			.orElseThrow(() -> new RuntimeException("Usuário não definido")));
		return dto;
	}
	
	public OrderItemDto to(OrderItem orderItem) {
		var item = new OrderItemDto(orderItem.getId(), orderItem.getQuantity());
		// TODO: consultar valor no contexto de Menu
		// item.setItemValue(orderItem.getItemValue());
		return item;
	}
	
	public Order from(OrderDto orderDto) {
		var user = Optional.ofNullable(orderDto.getUser())
				.map(userAssembler::from)
				.orElse(null);
		var order = new Order(orderDto.getFase(), user);
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
