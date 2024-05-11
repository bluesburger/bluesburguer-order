package br.com.bluesburguer.order.adapters.out;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.support.OrderMocks;

class OrderUnitTests {

	@Test
	void givenOrderInstance_WhenAddOrderItem_ThenShouldHaveItemAdded() {
		var order = OrderMocks.order(1L);
		var item = OrderMocks.orderItem(1L, order);
		
		order.add(item);
		
		assertThat(order.getItems())
			.anyMatch(i -> i.equals(item));
	}
	
	@Test
	void givenOrderInstance_WhenRemoveOrderItem_ThenShouldHaveItemRemoved() {
		var order = OrderMocks.order(1L);
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
