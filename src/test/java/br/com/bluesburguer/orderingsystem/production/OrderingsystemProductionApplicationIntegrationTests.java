package br.com.bluesburguer.orderingsystem.production;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;

class OrderingsystemProductionApplicationIntegrationTests extends ApplicationIntegrationSupport {

	@Test
	void context() {
		assertThat(super.hashCode()).isNotZero();
	}
}
