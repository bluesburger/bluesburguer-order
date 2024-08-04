package br.com.bluesburguer.order.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.application.dto.user.UserRequest;
import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.OrderAdapter;
import br.com.bluesburguer.order.infra.database.OrderItemRepository;
import br.com.bluesburguer.order.infra.database.OrderRepository;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.OrderMocks;

public class PanelControllerIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderAdapter orderService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	
	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}
	
	@Nested
	class GetAll {
		
		@Test
		void givenExistantOrder_WhenGetAllByPanel_ThenReturnPanelDto() throws Exception {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase);
			
			mockMvc
				.perform(get("/api/panel"))
				.andDo(print())
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$.orders").exists())
			    .andExpect(jsonPath("$.orders").isMap());
		}
		
		@Test
		void givenNoOrders_WhenGetAllByPanel_ThenReturnPanelDtoWithEmptyList() throws Exception {
			
			mockMvc
				.perform(get("/api/panel"))
				.andDo(print())
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$.orders").exists())
			    .andExpect(jsonPath("$.orders").isMap());
		}
	}
	
	@Nested
	class GetByStep {
		@Test
		void givenExistantOrder_WhenGetAllByPanel_ThenReturnPanelDto() throws Exception {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase);
			
			mockMvc
				.perform(get("/api/panel/{step}", step))
				.andDo(print())
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$.length()", is(1)))
			    .andExpect(jsonPath("$").isArray());
		}

		@Test
		void givenNoOrders_WhenGetByStep_ThenReturnEmptyList() throws Exception {
			var step = OrderStep.KITCHEN;
			mockMvc
				.perform(get("/api/panel/{step}", step))
				.andDo(print())
				.andExpect(status().isOk())
			    .andExpect(jsonPath("$").isArray())
			    .andExpect(jsonPath("$").isEmpty());
		}
	}
	
	private Optional<OrderEntity> createNewOrder(String cpf, String email) {
		var items = List.of(new OrderItemRequest(1L, 1));
		var user = new UserRequest(null, new Cpf(cpf), new Email(email));
		
		return orderService
				.createNewOrder(new OrderRequest(items, user));
	}
	
	private void validatePersistedOrder(Optional<OrderEntity> optionalOrder,
			OrderStep step, OrderFase fase) {
		assertThat(optionalOrder)
			.isPresent()
			.get()
			.hasFieldOrProperty("id")
			.hasFieldOrPropertyWithValue("step", step)
			.hasFieldOrPropertyWithValue("fase", fase)
			.hasFieldOrProperty("user")
			.hasFieldOrProperty("user.id")
			.hasFieldOrProperty("user.email")
			.hasFieldOrProperty("user.cpf")
			.hasFieldOrProperty("user.creationDateTime");
	}
}
