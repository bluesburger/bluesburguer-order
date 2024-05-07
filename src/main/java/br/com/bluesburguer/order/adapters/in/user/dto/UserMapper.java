package br.com.bluesburguer.order.adapters.in.user.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;

@Component
public class UserMapper {
	
	public OrderUser from(UserDto userDto) {
		return new OrderUser(userDto.getId(), userDto.getCpf(), userDto.getEmail(), null, List.of());
	}

	public UserDto to(OrderUser user) {
		return new UserDto(user.getId(), user.getCpf(), user.getEmail());
	}
}
