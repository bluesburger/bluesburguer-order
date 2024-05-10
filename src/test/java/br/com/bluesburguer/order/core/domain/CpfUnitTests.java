package br.com.bluesburguer.order.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.bluesburguer.order.adapters.out.CpfInvalidException;

class CpfUnitTests {

	@Test
	void shouldInstanceFormattedValue() {
		var cpfValue = "665.822.700-44";
		var cpf = new Cpf(cpfValue);
		assertThat(cpf)
			.isNotNull()
			.hasToString(cpfValue)
			.hasSameHashCodeAs(cpf);
		
		assertThat(cpf.getValue())
			.isEqualTo(cpfValue);
	}
	
	@Test
	void shouldInstanceUnformattedValue() {
		var cpfValue = "66582270044";
		var cpf = new Cpf(cpfValue);
		assertThat(cpf)
			.isNotNull()
			.hasToString(cpfValue)
			.hasSameHashCodeAs(cpf);
	}
	
	@Test
	void shouldThrowExceptionIfInstanceNull() {
		String cpfValue = null;
		assertThrows(CpfInvalidException.class, () -> new Cpf(cpfValue));
	}
	
	@Test
	void shouldThrowExceptionIfInstanceInvalid() {
		var cpfValue = "00000000000";
		assertThrows(CpfInvalidException.class, () -> new Cpf(cpfValue));
	}
}
