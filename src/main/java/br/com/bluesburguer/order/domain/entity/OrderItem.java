package br.com.bluesburguer.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {

	private Long id;
	
	private String orderId;
	
	private Integer quantity;
}
