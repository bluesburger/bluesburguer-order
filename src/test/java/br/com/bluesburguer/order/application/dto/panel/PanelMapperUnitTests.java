package br.com.bluesburguer.order.application.dto.panel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.support.OrderMocks;

@ExtendWith(MockitoExtension.class)
class PanelMapperUnitTests {
	
	@InjectMocks
	private PanelMapper panelMapper;

	@Nested
	class ToListOfOrderDto {
		
		@Test
		void givenOrderList_WhenToOrderDtoList_ThenReturnPanelDto() {
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			var orderDto = OrderMocks.orderDto(orderId);
			var orderList = List.of(orderDto);
			
			var panelDto = panelMapper.to(orderList);
			assertThat(panelDto).isNotNull();
			
			var orders = panelDto.getOrders();
			assertThat(orders)
				.isNotNull()
				.containsKey(orderDto.getStep());
			assertThat(orders.get(orderDto.getStep()))
				.anyMatch(o -> o.equals(orderDto));
		}
	}
}
