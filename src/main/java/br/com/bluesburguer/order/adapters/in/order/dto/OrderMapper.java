package br.com.bluesburguer.order.adapters.in.order.dto;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserMapper;
import br.com.bluesburguer.order.adapters.out.UserNotFoundException;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	
	private final UserMapper userAssembler;

	public OrderDto to(Order order) {
		var items = order.getItems().stream().map(this::to).toList();
		
		var userDto = Optional.ofNullable(order.getUser())
			.map(user -> new UserDto(user.getId(), 
					Optional.ofNullable(user.getCpf()).orElse(null), 
					user.getEmail()))
			.orElseThrow(UserNotFoundException::new);
		
		return new OrderDto(order.getId(), order.getStep(), order.getFase(), items, userDto);
	}
	
	public OrderItemDto to(OrderItem orderItem) {
		return new OrderItemDto(orderItem.getId(), orderItem.getQuantity());
	}
	
	public Order from(OrderDto orderDto) {
		var user = Optional.ofNullable(orderDto.getUser())
				.map(userAssembler::from)
				.orElseThrow(UserNotFoundException::new);
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
