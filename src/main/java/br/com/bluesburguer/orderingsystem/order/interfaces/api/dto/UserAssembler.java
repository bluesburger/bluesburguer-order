package br.com.bluesburguer.orderingsystem.order.interfaces.api.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.orderingsystem.order.domain.User;

@Component
public class UserAssembler {
	
	public User from(UserDto userDto) {
		return new User(userDto.getId(), userDto.getCpf(), userDto.getEmail(), null, List.of());
	}

	public UserDto to(User user) {
		return new UserDto(user.getId(), user.getCpf(), user.getEmail());
	}
}
