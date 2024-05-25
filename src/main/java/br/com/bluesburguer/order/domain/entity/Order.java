package br.com.bluesburguer.order.domain.entity;

import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {

	private String id;
	
	private OrderStep step;
	
	private OrderFase fase;
	
	private OrderUserEntity user;
}
