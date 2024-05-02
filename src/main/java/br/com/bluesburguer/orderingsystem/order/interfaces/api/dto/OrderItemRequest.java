package br.com.bluesburguer.orderingsystem.order.interfaces.api.dto;

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
public class OrderItemRequest implements Serializable {
	
	private static final long serialVersionUID = 3943960330875077167L;

	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty(value = "quantity")
	private Integer quantity;
}
