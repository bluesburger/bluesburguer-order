package br.com.bluesburguer.order.infra.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.support.OrderMocks;

class OrderEntityUnitTests {
	
	private static final UUID ORDER_ID = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");

	@Test
	void givenOrderInstance_WhenAddOrderItem_ThenShouldHaveItemAdded() {
		
		var order = OrderMocks.orderEntity(ORDER_ID);
		var item = OrderMocks.orderItem(1L, order);
		
		order.add(item);
		
		assertThat(order.getItems())
			.anyMatch(i -> i.equals(item));
	}
	
	@Test
	void givenOrderInstance_WhenRemoveOrderItem_ThenShouldHaveItemRemoved() {
		var order = OrderMocks.orderEntity(ORDER_ID);
		var item = OrderMocks.orderItem(1L, order);
		var item2 = OrderMocks.orderItem(2L, order);
		
		order.add(item);
		order.add(item2);
		order.remove(item);
		
		assertThat(order.getItems())
			.noneMatch(i -> i.equals(item))
			.anyMatch(i -> i.equals(item2));
	}
}
