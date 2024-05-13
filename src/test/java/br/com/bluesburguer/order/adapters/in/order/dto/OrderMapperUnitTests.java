package br.com.bluesburguer.order.adapters.in.order.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doCallRealMethod;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.adapters.in.user.dto.UserMapper;
import br.com.bluesburguer.order.support.OrderMocks;

@ExtendWith(MockitoExtension.class)
class OrderMapperUnitTests {
	
	@Mock
	private UserMapper userMapper;
	
	@InjectMocks
	private OrderMapper orderMapper;

	@Nested
	class ToOrderDto {
		@Test
		void givenOrder_WhenToOrderDto_ThenReturnOrderDto() {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			assertThat(orderMapper.to(order))
				.hasFieldOrPropertyWithValue("id", order.getId())
				.hasFieldOrPropertyWithValue("step", order.getStep())
				.hasFieldOrPropertyWithValue("fase", order.getFase())
				.hasFieldOrProperty("items")
				.hasFieldOrProperty("user");
		}
	}
	
	@Nested
	class ToOrderItemDto {
		
		@Test
		void givenOrderItem_WhenToOrderItemDto_ThenReturnOrderItemDto() {
			var order = OrderMocks.order(1L);
			var orderItem = OrderMocks.orderItem(1L, order);
			
			assertThat(orderMapper.to(orderItem))
				.hasFieldOrPropertyWithValue("id", orderItem.getId())
				.hasFieldOrPropertyWithValue("quantity", orderItem.getQuantity());
		}
	}
	
	@Nested
	class FromOrderDto {
		
		@Test
		void givenOrderDto_WhenFromOrderDto_ThenReturnOrder() {
			
			var orderDto = OrderMocks.orderDto(1L);
			var user = orderDto.getUser();
			
			doCallRealMethod()
				.when(userMapper).from(user);
			
			assertThat(orderMapper.from(orderDto))
				.hasFieldOrPropertyWithValue("id", orderDto.getId())
				.hasFieldOrProperty("createdTime")
				.hasFieldOrProperty("updatedTime")
				.hasFieldOrPropertyWithValue("step", orderDto.getStep())
				.hasFieldOrPropertyWithValue("fase", orderDto.getFase())
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", user.getId())
				.hasFieldOrPropertyWithValue("user.cpf", user.getCpf())
				.hasFieldOrPropertyWithValue("user.email", user.getEmail())
				.hasFieldOrProperty("items");
		}
	}
}
