package br.com.bluesburguer.order.adapters.in.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.bluesburguer.order.adapters.in.order.utils.ResponseUtils;

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
