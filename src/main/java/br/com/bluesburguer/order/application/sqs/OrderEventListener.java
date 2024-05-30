package br.com.bluesburguer.order.application.sqs;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.application.sqs.commands.CancelOrderCommand;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.domain.service.OrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cloud.aws.sqs.listener.auto-startup", havingValue = "true")
public class OrderEventListener {
	
	private final OrderPort orderPort;

	@SqsListener(value = "${queue.cancel-order-command:queue-cancel-order-command}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void handle(@Payload CancelOrderCommand command, Acknowledgment ack) {
		log.info("Command received: {}", command.toString());
		var uuid = UUID.fromString(command.getOrderId());
		var updated = orderPort.updateStepAndFase(uuid, OrderStep.ORDER, OrderFase.CANCELED)
			.isPresent();
		if (updated) {
			ack.acknowledge();
		}
	}
}
