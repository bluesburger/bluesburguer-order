package br.com.bluesburguer.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;

class BluesBurguerOrderApplicationIntegrationTests extends ApplicationIntegrationSupport {

	@Test
	void contextLoad() {
		assertThat(super.hashCode()).isNotZero();
	}
}
