package br.com.bluesburguer.order.adapters.in.panel;

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

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.core.service.OrderService;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.OrderMocks;

public class PanelRestResourceIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() throws Exception {
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
			var fase = OrderFase.PENDING;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
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
			var fase = OrderFase.PENDING;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
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
	
	private Optional<Order> createNewOrder(String cpf, String email) {
		var items = List.of(new OrderItemRequest(1L, 1));
		var user = new UserRequest(null, new Cpf(cpf), new Email(email));
		
		return orderService
				.createNewOrder(new OrderRequest(items, user));
	}
	
	private void validatePersistedOrder(Optional<Order> optionalOrder, 
			OrderStep step, OrderFase fase, String cpf, String email) {
		assertThat(optionalOrder)
			.isPresent()
			.get()
			.hasFieldOrProperty("id")
			.hasFieldOrPropertyWithValue("step", step)
			.hasFieldOrPropertyWithValue("fase", fase)
			.hasFieldOrProperty("user")
			.hasFieldOrProperty("user.id")
			.hasFieldOrPropertyWithValue("user.email", email)
			.hasFieldOrPropertyWithValue("user.cpf", cpf)
			.hasFieldOrProperty("user.creationDateTime");
	}
}
