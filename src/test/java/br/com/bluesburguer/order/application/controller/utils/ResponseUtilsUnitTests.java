package br.com.bluesburguer.order.application.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ResponseUtilsUnitTests {

	@Test
	void shouldDefineCreated() {
		URI location = URI.create("http://localhost");
		assertThat(ResponseUtils.created(location))
			.isNotNull()
			.isInstanceOf(ResponseEntity.class);
	}
}
