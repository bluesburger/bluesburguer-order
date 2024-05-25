package br.com.bluesburguer.order.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderFaseUnitTests {

	@Test
	void shouldValidToString() {
		assertThat(OrderFase.REGISTERED).hasToString("REGISTERED");
		assertThat(OrderFase.CONFIRMED).hasToString("CONFIRMED");
		assertThat(OrderFase.CANCELED).hasToString("CANCELED");
		assertThat(OrderFase.FAILED).hasToString("FAILED");
	}
}
