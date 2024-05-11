package br.com.bluesburguer.order.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigUnitTests {

	@Test
	void shouldInstance() {
		assertThat(new OpenApiConfig()).isNotNull();
	}
}
