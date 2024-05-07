package br.com.bluesburguer.order.core.domain;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Objects;

import br.com.bluesburguer.order.adapters.out.CpfInvalidException;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
		if (Objects.isNull(value)) {
			throw new CpfInvalidException();
		}
		
		var unormalizedCpf = CpfNormalizer.unnormalize(value);

		if (!CpfValidator.isCpf(unormalizedCpf)) {
			throw new CpfInvalidException();
		}
		
		var normalizedCpf = CpfNormalizer.normalize(value);
		this.value = normalizedCpf;
	}
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static class CpfNormalizer {
		
		public static String unnormalize(String cpf) {
			cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
			var unnormalized = cpf.replaceAll("$1.$2.$3-$4", "(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
			return unnormalized;
		}

		public static String normalize(String cpf) {
			cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
			var normalized = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
			return normalized;
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static class CpfValidator {

		private static boolean isCpf(String cpf) {
			cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos

			if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
				return false; // CPF deve ter 11 dígitos e não pode ser formado por dígitos repetidos
			}

			// Cálculo do dígito verificador
			int[] digits = new int[11];
			for (int i = 0; i < 11; i++) {
				digits[i] = cpf.charAt(i) - '0';
			}
			int sum = 0;
			for (int i = 0; i < 9; i++) {
				sum += digits[i] * (10 - i);
			}
			int remainder = sum % 11;
			int digit1 = (remainder < 2) ? 0 : (11 - remainder);

			sum = 0;
			for (int i = 0; i < 10; i++) {
				sum += digits[i] * (11 - i);
			}
			remainder = sum % 11;
			int digit2 = (remainder < 2) ? 0 : (11 - remainder);

			return (digit1 == digits[9] && digit2 == digits[10]);
		}

		public static String imprimeCPF(String cpf) {
			return (cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-"
					+ cpf.substring(9, 11));
		}
	}
}
