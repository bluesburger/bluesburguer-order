package br.com.bluesburguer.orderingsystem.order.interfaces.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class OrderRequest implements Serializable {
	
	private static final long serialVersionUID = -621830335594903665L;

	@JsonProperty(value = "fase")
	private String fase;
	
	@JsonProperty(value = "items")
	private List<Long> items = new ArrayList<>();
}
