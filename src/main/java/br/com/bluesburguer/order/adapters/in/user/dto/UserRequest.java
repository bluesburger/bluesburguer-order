package br.com.bluesburguer.order.adapters.in.user.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest implements Serializable {
	
	private static final long serialVersionUID = 3695870866440519918L;

	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty(value = "cpf")
	private String cpf;
	
	@JsonProperty(value = "email")
	private String email;
}
