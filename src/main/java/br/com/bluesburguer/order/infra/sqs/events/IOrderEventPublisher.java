package br.com.bluesburguer.order.infra.sqs.events;

import java.util.Optional;

import br.com.bluesburguer.order.application.sqs.events.OrderEvent;

public interface IOrderEventPublisher<T extends OrderEvent> {

	Optional<String> publish(T event);
}
