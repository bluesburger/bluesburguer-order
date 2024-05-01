package br.com.bluesburguer.orderingsystem.order.infra.client;

import br.com.bluesburguer.orderingsystem.domain.event.OrderStatusUpdated;

public interface EventPublisher {

	void publish(OrderStatusUpdated event);
}
