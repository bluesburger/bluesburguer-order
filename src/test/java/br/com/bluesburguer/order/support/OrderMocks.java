package br.com.bluesburguer.order.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemDto;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
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
	public static Order order() {
		var orderId = UUID.fromString("b4d3f760-761f-4e07-8610-ebe389684e6d");
		return order(orderId);
	}
	
	public static Order order(UUID orderId) {
		var user = new OrderUser(1L, OrderMocks.mockCpf(), OrderMocks.mockEmail(), LocalDateTime.now(), List.of());
		return order(orderId, user);
	}
	
	public static Order order(UUID orderId, OrderUser user) {
		var step = OrderStep.KITCHEN;
		var fase = OrderFase.PENDING;
		
		var createdTime = LocalDateTime.now();
		var updatedTime = LocalDateTime.now();
		List<OrderItem> items = new ArrayList<>();
		return new Order(orderId.toString(), createdTime, updatedTime, step, fase, items, user);
	}
	
	public static OrderItem orderItem(long orderItem, Order order) {
		return new OrderItem(1L, 1L, order, 1, LocalDateTime.now(), LocalDateTime.now());
	}

	public static OrderDto orderDto(UUID orderId) {
		var step = OrderStep.KITCHEN;
		var fase = OrderFase.PENDING;
		var items = List.of(new OrderItemDto(1L, 1));
		var user = UserMocks.userDto();
		return new OrderDto(orderId, step, fase, items, user);
	}
}
