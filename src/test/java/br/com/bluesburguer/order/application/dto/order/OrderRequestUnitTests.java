package br.com.bluesburguer.order.application.dto.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.support.UserMocks;

class OrderRequestUnitTests {

	@Test
	void givenItemAndUser_WhenInstance_ThenCreateIt() {
		var itemRequest = new OrderItemRequest(1L, 1);
		var user = UserMocks.userRequest();
		var orderRequest = new OrderRequest(List.of(itemRequest), user);
		
		assertThat(orderRequest)
			.hasFieldOrPropertyWithValue("user.id", user.getId())
			.hasFieldOrPropertyWithValue("user.cpf", user.getCpf())
			.hasFieldOrPropertyWithValue("user.email", user.getEmail());
		
		assertThat(orderRequest.getItems())
			.anyMatch(i -> i.equals(itemRequest));
	}
}
