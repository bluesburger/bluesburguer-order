package br.com.bluesburguer.order.core.domain.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStatus;
import br.com.bluesburguer.order.core.domain.OrderStep;

class OrderStatusUpdatedUnitTests {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();    

	@Test
	void shouldInstance_WithDefaultDateTime() {
		var id = UUID.randomUUID();
		var status = new OrderStatus(OrderStep.KITCHEN, OrderFase.IN_PROGRESS);
		var orderStatus = new OrderStatusUpdated(id, status);
		
		assertThat(orderStatus).isNotNull()
			.hasFieldOrPropertyWithValue("newStatus", status)
			.hasNoNullFieldsOrProperties();
	}
	
	@Test
	void shouldSerialize() throws JsonProcessingException {
		var id = UUID.randomUUID();
		var status = new OrderStatus(OrderStep.KITCHEN, OrderFase.IN_PROGRESS);
		var orderStatus = new OrderStatusUpdated(id, status);
		
		var json = OBJECT_MAPPER.writeValueAsString(orderStatus);
		
		assertThat(json)
			.isNotNull()
			.isNotEmpty();
	}
}
