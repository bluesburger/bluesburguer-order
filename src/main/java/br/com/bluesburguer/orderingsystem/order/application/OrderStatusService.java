package br.com.bluesburguer.orderingsystem.order.application;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.orderingsystem.domain.event.OrderStatusUpdated;

@Service
public class OrderStatusService {

	public boolean update(OrderStatusUpdated orderStatus) {
		// TODO: consumir feignClient do product
		return true;
	}
}
