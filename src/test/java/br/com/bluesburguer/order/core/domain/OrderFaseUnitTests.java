package br.com.bluesburguer.order.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderFaseUnitTests {

	@Test
	void shouldValidToString() {
		assertThat(OrderFase.PENDING).hasToString("PENDING");
		assertThat(OrderFase.IN_PROGRESS).hasToString("IN_PROGRESS");
		assertThat(OrderFase.DONE).hasToString("DONE");
	}
}
