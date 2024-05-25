package br.com.bluesburguer.order.application.dto.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doCallRealMethod;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.application.dto.user.UserMapper;
import br.com.bluesburguer.order.support.OrderMocks;

@ExtendWith(MockitoExtension.class)
class OrderMapperUnitTests {
	
	private static final UUID ORDER_ID = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
	
	@Mock
	private UserMapper userMapper;
	
	@InjectMocks
	private OrderMapper orderMapper;
	
	@Nested
	class ToOrder {
		@Test
		void givenOrderEntity_WhenToOrder_ThenShouldReturnOrder() {
			var orderEntity = OrderMocks.orderEntity(ORDER_ID);
			
			assertThat(orderMapper.toOrder(orderEntity))
				.hasFieldOrPropertyWithValue("id", orderEntity.getId())
				.hasFieldOrPropertyWithValue("step", orderEntity.getStep())
				.hasFieldOrPropertyWithValue("fase", orderEntity.getFase())
				// FIXME: items?
				.hasFieldOrProperty("user");
		}
	}

	@Nested
	class ToOrderDto {
		@Test
		void givenOrder_WhenToOrderDto_ThenReturnOrderDto() {
			var order = OrderMocks.orderEntity(ORDER_ID);
			
			assertThat(orderMapper.toOrderDto(order))
				.hasFieldOrPropertyWithValue("id", UUID.fromString(order.getId()))
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
			var order = OrderMocks.orderEntity(ORDER_ID);
			var orderItem = OrderMocks.orderItem(1L, order);
			
			assertThat(orderMapper.toOrderItemDto(orderItem))
				.hasFieldOrPropertyWithValue("id", orderItem.getId())
				.hasFieldOrPropertyWithValue("quantity", orderItem.getQuantity());
		}
	}
	
	@Nested
	class ToOrderEntity {
		
		@Test
		void givenOrderDto_WhenFromOrderDto_ThenReturnOrder() {
			
			var orderDto = OrderMocks.orderDto(ORDER_ID);
			var user = orderDto.getUser();
			
			doCallRealMethod()
				.when(userMapper).from(user);
			
			assertThat(orderMapper.toOrderEntity(orderDto))
				.hasFieldOrPropertyWithValue("id", orderDto.getId().toString())
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
	
	@Nested
	class ToOrderItemEntity {
		
		@Test
		void givenOrderItemEntity_WhenToOrderItem_ThenShouldReturnOrderItem() {
			var orderEntity = OrderMocks.order();
			var orderItemEntity = OrderMocks.orderItem(1L, orderEntity);
			
			assertThat(orderMapper.toOrderItemEntity(orderItemEntity))
				.hasFieldOrPropertyWithValue("id", orderItemEntity.getId())
				.hasFieldOrPropertyWithValue("orderId", orderEntity.getId())
				.hasFieldOrPropertyWithValue("quantity", orderItemEntity.getQuantity());
		}
	}
}
