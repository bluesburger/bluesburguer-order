package br.com.bluesburguer.order.infra.sqs.commands;

import java.util.Optional;

import br.com.bluesburguer.order.application.sqs.commands.OrderCommand;

public interface IOrderCommandPublisher<T extends OrderCommand> {

	Optional<String> publish(T command);
}
