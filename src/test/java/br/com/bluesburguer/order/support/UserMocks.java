package br.com.bluesburguer.order.support;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMocks {
	
	private static final String CPF = "997.307.080-10";
	private static final String EMAIL = "email.usuario@server.com";
	private static final UUID EXISTANT_ORDER_ID = UUID.fromString("b9881aef-ca1a-4ce6-843b-0717f0f0b90a");
	
	public static OrderUser user() {
		return new OrderUser(1L, CPF, EMAIL, null, List.of());
	}

	public static OrderUser user(long userId) {
		var orders = List.of(OrderMocks.order(EXISTANT_ORDER_ID));
		return new OrderUser(userId, CPF, EMAIL, LocalDateTime.now(), orders);
	}
	
	public static OrderUser user(long userId, List<Order> orders) {
		return new OrderUser(userId, CPF, EMAIL, LocalDateTime.now(), orders);
	}
	
	public static UserRequest userRequest() {
		return new UserRequest(1L, new Cpf(CPF), new Email(EMAIL));
	}

	public static Cpf cpf() {
		return new Cpf(CPF);
	}

	public static Email email() {
		return new Email(EMAIL);
	}

	public static UserDto userDto() {
		return new UserDto(1L, CPF, EMAIL);
	}
}
