package br.com.bluesburguer.order.application.dto.order;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.application.dto.item.OrderItemDto;
import br.com.bluesburguer.order.application.dto.user.UserDto;
import br.com.bluesburguer.order.application.dto.user.UserMapper;
import br.com.bluesburguer.order.domain.entity.Order;
import br.com.bluesburguer.order.domain.entity.OrderItem;
import br.com.bluesburguer.order.domain.exception.UserNotFoundException;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	
	private final UserMapper userMapper;
	
	public Order toOrder(OrderEntity entity) {
		return new Order(entity.getId(), entity.getStep(), entity.getFase(), entity.getUser());
	}

	public OrderDto toOrderDto(OrderEntity order) {
		var items = order.getItems().stream().map(this::toOrderItemDto).toList();
		
		var userDto = new UserDto(order.getUser().getId(), 
						Optional.ofNullable(order.getUser().getCpf()).orElse(null), 
						order.getUser().getEmail());
		
		return new OrderDto(UUID.fromString(order.getId()), order.getStep(), order.getFase(), items, userDto);
	}
	
	public OrderItemDto toOrderItemDto(OrderItemEntity orderItem) {
		return new OrderItemDto(orderItem.getOrderItemId(), orderItem.getQuantity());
	}
	
	public OrderEntity toOrderEntity(OrderDto orderDto) {
		var user = Optional.ofNullable(orderDto.getUser())
				.map(userMapper::from)
				.orElseThrow(UserNotFoundException::new);
		var order = new OrderEntity(orderDto.getFase(), user);
		order.setId(orderDto.getId().toString());
		order.setStep(orderDto.getStep());
		order.setFase(orderDto.getFase());
		var items = orderDto.getItems().stream()
			.map(itemDto -> new OrderItemEntity(itemDto.getId(), order))
			.toArray(size -> new OrderItemEntity[size]);
		order.add(items);
		return order;
	}
	
	public OrderItem toOrderItem(OrderItemEntity entity) {
		return new OrderItem(entity.getId(), entity.getQuantity());
	}
}
