package br.com.bluesburguer.order.adapters.in.user.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest implements Serializable {
	
	private static final long serialVersionUID = 3695870866440519918L;

	@NonNull
	@NotNull
	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty(value = "cpf")
	@Embedded
	private Cpf cpf;
	
	@JsonProperty(value = "email")
	@Embedded
	private Email email;
}
