package br.com.bluesburguer.order.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderStepUnitTests {

	@Test
	void shouldValidToString() {
		assertThat(OrderStep.ORDER).hasToString("ORDER");
		assertThat(OrderStep.KITCHEN).hasToString("KITCHEN");
		assertThat(OrderStep.DELIVERY).hasToString("DELIVERY");
	}
}
