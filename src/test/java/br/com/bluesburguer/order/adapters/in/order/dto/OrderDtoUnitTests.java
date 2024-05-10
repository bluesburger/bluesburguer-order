package br.com.bluesburguer.order.adapters.in.order.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.support.OrderMocks;

@ExtendWith(MockitoExtension.class)
class OrderDtoUnitTests {

	@Test
	void whenInstance_thenShouldHaveValues() {
		var orderId = 1L;
		var step = OrderStep.DELIVERY;
		var fase = OrderFase.PENDING;
		
		var items = List.of(new OrderItemDto(1L,  1));
		var user = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		var orderDto = new OrderDto(orderId, step, fase, items, user);
		assertThat(orderDto)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("step", step)
			.hasFieldOrPropertyWithValue("fase", fase)
			.hasFieldOrPropertyWithValue("items", items)
			.hasFieldOrPropertyWithValue("user", user)
			.hasToString(String.format("OrderDto(id=%d, step=%s, fase=%s, items=%s, user=%s)", orderId, step, fase, items, user))
			.hasSameHashCodeAs(orderDto);
	}
	
	@Test
	void whenInstance_thenCouldGetFields() {
		var orderId = 1L;
		var step = OrderStep.DELIVERY;
		var fase = OrderFase.PENDING;
		
		var items = List.of(new OrderItemDto(1L,  1));
		var user = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		var orderDto = new OrderDto(orderId, step, fase, items, user);
		assertThat(orderDto.getId()).isEqualTo(orderId);
		assertThat(orderDto.getStep()).isEqualTo(step);
		assertThat(orderDto.getFase()).isEqualTo(fase);
		assertThat(orderDto.getItems()).isEqualTo(items);
		assertThat(orderDto.getUser()).isEqualTo(user);
	}
	
	@Test
	void whenInstance_thenCouldSetId() {
		var orderDto = new OrderDto(1L, OrderStep.DELIVERY, OrderFase.PENDING, List.of(), null);
		assertThat(orderDto.getId()).isEqualTo(1L);
		orderDto.setId(2L);
		assertThat(orderDto.getId()).isEqualTo(2L);
	}
	
	@Test
	void whenInstance_thenCouldSetOrderStep() {
		var orderDto = new OrderDto(1L, OrderStep.DELIVERY, OrderFase.PENDING, List.of(), null);
		assertThat(orderDto.getStep()).isEqualTo(OrderStep.DELIVERY);
		orderDto.setStep(OrderStep.KITCHEN);
		assertThat(orderDto.getStep()).isEqualTo(OrderStep.KITCHEN);
	}
	
	@Test
	void whenInstance_thenCouldSetOrderFase() {
		var orderDto = new OrderDto(1L, OrderStep.DELIVERY, OrderFase.PENDING, List.of(), null);
		assertThat(orderDto.getFase()).isEqualTo(OrderFase.PENDING);
		orderDto.setFase(OrderFase.IN_PROGRESS);
		assertThat(orderDto.getFase()).isEqualTo(OrderFase.IN_PROGRESS);
	}
	
	@Test
	void whenInstance_thenCouldSetItems() {
		var orderDto = new OrderDto(1L, OrderStep.DELIVERY, OrderFase.PENDING, List.of(), null);
		assertThat(orderDto.getItems()).isEqualTo(List.of());
		var newItem = List.of(new OrderItemDto(1L, 1));
		orderDto.setItems(newItem);
		assertThat(orderDto.getItems()).isEqualTo(newItem);
	}
	
	@Test
	void whenInstance_thenCouldSetUser() {
		var orderDto = new OrderDto(1L, OrderStep.DELIVERY, OrderFase.PENDING, List.of(), null);
		assertThat(orderDto.getUser()).isNull();
		var newUser = new UserDto(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail());
		orderDto.setUser(newUser);
		assertThat(orderDto.getUser()).isEqualTo(newUser);
	}
}
