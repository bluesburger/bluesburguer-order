package br.com.bluesburguer.order.adapters.in.order.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
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
	
	@JsonProperty(value = "items")
	private List<OrderItemRequest> items = new ArrayList<>();

	@JsonProperty(value = "user")
	private UserRequest user;
}
