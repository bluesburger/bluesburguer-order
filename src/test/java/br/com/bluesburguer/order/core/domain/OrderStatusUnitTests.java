package br.com.bluesburguer.order.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderStatusUnitTests {

	@Test
	void shouldNotInstanceWithAllParameteresInvalid() {
		assertThrows(NullPointerException.class, () -> new OrderStatus(null, null));
	}

	@ParameterizedTest
	@EnumSource(OrderStep.class)
	void shouldNotInstanceWithoutFase(OrderStep step) {
		assertThrows(NullPointerException.class, () -> new OrderStatus(step, null));
	}
	
	@ParameterizedTest
	@EnumSource(OrderFase.class)
	void shouldNotInstance_WithoutStep(OrderFase fase) {
		assertThrows(NullPointerException.class, () -> new OrderStatus(null, fase));
	}
	
	@ParameterizedTest
	@EnumSource(OrderStep.class)
	void shouldInstance_WithAllPossibleParameters(OrderStep step) {
		OrderFase fase = OrderFase.PENDING;
		assertThat(new OrderStatus(step, fase))
			.hasFieldOrPropertyWithValue("step", step)
			.hasFieldOrPropertyWithValue("fase", fase)
			.hasToString(String.format("OrderStatus(step=%s, fase=%s)", step, fase));
	}
}
