package br.com.bluesburguer.order.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.bluesburguer.order.adapters.out.EmailInvalidException;

class EmailUnitTests {

	@Test
	void shouldInstanceFormattedValue() {
		String emailAddress = "email@server.com";
		var email = new Email(emailAddress);
		
		assertThat(email)
			.isNotNull()
			.hasToString(emailAddress)
			.hasSameHashCodeAs(new Email(emailAddress));
	}
	
	@Test
	void shouldThrowsError_WhenInstanceNullEmail() {
		String emailAddress = null;
		assertThrows(EmailInvalidException.class, () -> new Email(emailAddress));
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "", "server.com"})
	void shouldThrowsError_WhenInstanceInvalidEmail(String emailAddress) {
		assertThrows(EmailInvalidException.class, () -> new Email(emailAddress));
	}
}
