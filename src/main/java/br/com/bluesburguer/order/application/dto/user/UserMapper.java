package br.com.bluesburguer.order.application.dto.user;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;

@Component
public class UserMapper {
	
	public OrderUserEntity from(UserDto userDto) {
		return new OrderUserEntity(userDto.getId(), userDto.getCpf(), userDto.getEmail(), null, List.of());
	}

	public UserDto to(OrderUserEntity user) {
		return new UserDto(user.getId(), user.getCpf(), user.getEmail());
	}
}
