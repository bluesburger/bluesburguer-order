package br.com.bluesburguer.order.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.bluesburguer.order.application.dto.item.OrderItemDto;
import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMocks {
	
	private static final String CPF = "997.307.080-10";
	private static final String EMAIL = "email.usuario@server.com";
	
	public static String mockCpf() {
		return CPF;
	}

	public static String mockEmail() {
		return EMAIL;
	}
	
	public static String jsonRequestOrder() {
		return """
				{
				"items": [
					{
						"id": 1,
						"quantity": 1
					}
				],
				"user": {
					"id": 1,
					"cpf": "997.307.080-10",
					"email": "email.usuario@server.com"
				}
			}
		""";
	}
	
	public static String jsonRequestOrderItems() {
		return """
				[
					{
						"id": 1,
						"quantity": 1
					}
				]
				""";
	}
	public static OrderEntity order() {
		var orderId = UUID.fromString("b4d3f760-761f-4e07-8610-ebe389684e6d");
		return orderEntity(orderId);
	}
	
	public static OrderEntity orderEntity(UUID orderId) {
		var user = new OrderUserEntity(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail(), LocalDateTime.now(), List.of());
		return order(orderId, user);
	}
	
	public static OrderEntity order(UUID orderId, OrderUserEntity user) {
		var step = OrderStep.ORDER;
		var fase = OrderFase.REGISTERED;
		
		var createdTime = LocalDateTime.now();
		var updatedTime = LocalDateTime.now();
		List<OrderItemEntity> items = new ArrayList<>();
		return new OrderEntity(orderId.toString(), createdTime, updatedTime, step, fase, items, user);
	}
	
	public static OrderItemEntity orderItem(long orderItem, OrderEntity orderEntity) {
		return new OrderItemEntity(1L, orderItem, orderEntity, 1, LocalDateTime.now(), LocalDateTime.now());
	}

	public static OrderDto orderDto(UUID orderId) {
		var step = OrderStep.ORDER;
		var fase = OrderFase.REGISTERED;
		var items = List.of(new OrderItemDto(1L, 1));
		var user = UserMocks.userDto();
		return new OrderDto(orderId, step, fase, items, user);
	}
}
