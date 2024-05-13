package br.com.bluesburguer.order.adapters.in.order.dto;

import java.util.Optional;

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
	
	private final UserMapper userMapper;

	public OrderDto to(Order order) {
		var items = order.getItems().stream().map(this::to).toList();
		
		var userDto = new UserDto(order.getUser().getId(), 
						Optional.ofNullable(order.getUser().getCpf()).orElse(null), 
						order.getUser().getEmail());
		
		return new OrderDto(order.getId(), order.getStep(), order.getFase(), items, userDto);
	}
	
	public OrderItemDto to(OrderItem orderItem) {
		return new OrderItemDto(orderItem.getId(), orderItem.getQuantity());
	}
	
	public Order from(OrderDto orderDto) {
		var user = Optional.ofNullable(orderDto.getUser())
				.map(userMapper::from)
				.orElseThrow(UserNotFoundException::new);
		var order = new Order(orderDto.getFase(), user);
		order.setId(orderDto.getId());
		order.setStep(orderDto.getStep());
		order.setFase(orderDto.getFase());
		var items = orderDto.getItems().stream()
			.map(itemDto -> new OrderItem(itemDto.getId(), order))
			.toArray(size -> new OrderItem[size]);
		order.add(items);
		return order;
	}
}
