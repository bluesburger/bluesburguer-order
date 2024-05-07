package br.com.bluesburguer.order.core.domain;

import java.io.Serializable;
import java.util.Objects;

import br.com.bluesburguer.order.adapters.out.CpfInvalidException;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode 
@Embeddable
public class Cpf implements Serializable {

	private static final long serialVersionUID = 6911888938392251986L;
	
	@NotNull
	@NonNull
	private String value;
	
	public Cpf(String value) {
		validate(value);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	private void validate(String value) {
		if(Objects.isNull(value)) {
			throw new CpfInvalidException();
		}
		
		// FIXME: validar o cpf
	}
}
