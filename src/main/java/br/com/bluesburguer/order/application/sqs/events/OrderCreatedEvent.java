package br.com.bluesburguer.order.application.sqs.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCreatedEvent extends OrderEvent {
	
	public static final String EVENT_NAME = "ORDER_CREATED_EVENT";

	private static final long serialVersionUID = 7702500048926979660L;
	
	@JsonCreator
	public OrderCreatedEvent(@JsonProperty("orderId") String orderId) {
		super(orderId);
	}
	
	@Override
	public String getEventName() {
		return EVENT_NAME;
	}
}
