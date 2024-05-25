package br.com.bluesburguer.order.application.dto.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.application.dto.item.OrderItemDto;
import br.com.bluesburguer.order.application.dto.user.UserDto;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.support.OrderMocks;

@ExtendWith(MockitoExtension.class)
class OrderDtoUnitTests {
	private static final UUID ORDER_ID = UUID.fromString("eac3c363-24c9-43d3-b39c-6ad0da620a14");

	@Test
	void whenInstance_thenShouldHaveValues() {
		var step = OrderStep.DELIVERY;
		var fase = OrderFase.REGISTERED;
		
		var items = List.of(new OrderItemDto(1L,  1));
		var user = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		var orderDto = new OrderDto(ORDER_ID, step, fase, items, user);
		assertThat(orderDto)
			.hasFieldOrPropertyWithValue("id", ORDER_ID)
			.hasFieldOrPropertyWithValue("step", step)
			.hasFieldOrPropertyWithValue("fase", fase)
			.hasFieldOrPropertyWithValue("items", items)
			.hasFieldOrPropertyWithValue("user", user)
			.hasToString(String.format("OrderDto(id=%s, step=%s, fase=%s, items=%s, user=%s)", ORDER_ID, step, fase, items, user))
			.hasSameHashCodeAs(orderDto);
	}
	
	@Test
	void whenInstance_thenCouldGetFields() {
		var step = OrderStep.DELIVERY;
		var fase = OrderFase.REGISTERED;
		
		var items = List.of(new OrderItemDto(1L,  1));
		var user = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		var orderDto = new OrderDto(ORDER_ID, step, fase, items, user);
		assertThat(orderDto.getId()).isEqualTo(ORDER_ID);
		assertThat(orderDto.getStep()).isEqualTo(step);
		assertThat(orderDto.getFase()).isEqualTo(fase);
		assertThat(orderDto.getItems()).isEqualTo(items);
		assertThat(orderDto.getUser()).isEqualTo(user);
	}
	
	@Test
	void whenInstance_thenCouldSetId() {
		var orderDto = new OrderDto(ORDER_ID, OrderStep.DELIVERY, OrderFase.REGISTERED, List.of(), null);
		assertThat(orderDto.getId()).isEqualTo(ORDER_ID);
		var newId = UUID.fromString("6a555fb2-5f5f-46f8-9aa2-1d89208b9b9c");
		orderDto.setId(newId);
		assertThat(orderDto.getId()).isEqualTo(newId);
	}
	
	@Test
	void whenInstance_thenCouldSetOrderStep() {
		var orderDto = new OrderDto(ORDER_ID, OrderStep.DELIVERY, OrderFase.REGISTERED, List.of(), null);
		assertThat(orderDto.getStep()).isEqualTo(OrderStep.DELIVERY);
		orderDto.setStep(OrderStep.KITCHEN);
		assertThat(orderDto.getStep()).isEqualTo(OrderStep.KITCHEN);
	}
	
	@Test
	void whenInstance_thenCouldSetOrderFase() {
		var orderDto = new OrderDto(ORDER_ID, OrderStep.DELIVERY, OrderFase.REGISTERED, List.of(), null);
		assertThat(orderDto.getFase()).isEqualTo(OrderFase.REGISTERED);
		orderDto.setFase(OrderFase.CONFIRMED);
		assertThat(orderDto.getFase()).isEqualTo(OrderFase.CONFIRMED);
	}
	
	@Test
	void whenInstance_thenCouldSetItems() {
		var orderDto = new OrderDto(ORDER_ID, OrderStep.DELIVERY, OrderFase.REGISTERED, List.of(), null);
		assertThat(orderDto.getItems()).isEqualTo(List.of());
		var newItem = List.of(new OrderItemDto(1L, 1));
		orderDto.setItems(newItem);
		assertThat(orderDto.getItems()).isEqualTo(newItem);
	}
	
	@Test
	void whenInstance_thenCouldSetUser() {
		var orderDto = new OrderDto(ORDER_ID, OrderStep.DELIVERY, OrderFase.REGISTERED, List.of(), null);
		assertThat(orderDto.getUser()).isNull();
		var newUser = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		orderDto.setUser(newUser);
		assertThat(orderDto.getUser()).isEqualTo(newUser);
	}
}
