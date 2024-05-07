package br.com.bluesburguer.order.adapters.in.user.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.core.domain.Cpf;

@Component
public class UserMapper {
	
	public OrderUser from(UserDto userDto) {
		return new OrderUser(userDto.getId(), userDto.getCpf().getValue(), userDto.getEmail(), null, List.of());
	}

	public UserDto to(OrderUser user) {
		return new UserDto(user.getId(), new Cpf(user.getCpf()), user.getEmail());
	}
}
