package br.com.bluesburguer.order.adapters.in.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.out.OrderFailToCreateException;
import br.com.bluesburguer.order.adapters.out.OrderNotFoundException;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.core.service.OrderService;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.OrderMocks;

class OrderRestResourceIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private OrderService orderService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	
	
	@Nested
	class GetAll {
		@Test
		void givenNoOrders_whenMockMVC_thenReturnsEmptyArrayOfOrders() throws Exception {
			doReturn(List.of())
				.when(orderService)
				.getAll();
			
			mockMvc
					.perform(get("/api/order"))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$").isArray());
		}
	}
	
	@Nested
	class GetById {
	
		@Test
		void givenOneExistantOrderwhenMockMVCGetById_thenReturnExistantOrder() throws Exception {
			var order = OrderMocks.order(1L);
			doReturn(Optional.of(order))
				.when(orderService).getById(order.getId());
			
			mockMvc
					.perform(get("/api/order/{orderId}", order.getId()))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$.id", is(1)))
				    .andExpect(jsonPath("$.step", is("KITCHEN")))
				    .andExpect(jsonPath("$.fase", is("PENDING")))
				    .andExpect(jsonPath("$.items", hasSize(0)))
				    .andExpect(jsonPath("$.user").exists())
				    .andExpect(jsonPath("$.user.id", is(1)))
				    .andExpect(jsonPath("$.user.cpf", is(order.getUser().getCpf())))
				    .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));
		}
		
		@Test
		void givenUnexistantOrder_WhenMockMVCGetById_thenReturnNotFoundStatus() throws Exception {
			long orderId = 2L;
			doReturn(Optional.empty())
				.when(orderService).getById(orderId);
			
			mockMvc
					.perform(get("/api/order/{orderId}", orderId))
	//			    .andDo(print())
				    .andExpect(status().isNotFound())
				    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(OrderNotFoundException.class))
				    .andExpect(status().reason("Pedido não encontrado"));
		}
	}
	
	@Nested
	class GetByStep {
		@Test
		void givenOneExistantOrder_WhenMockMVCGetAllByStep_thenReturnArrayOfOrdersWithIt() throws Exception {
			var step = OrderStep.KITCHEN;
			var fase = OrderFase.PENDING;
			OrderUser user = new OrderUser();
			user.setCpf(OrderMocks.mockCpf());
			user.setEmail(OrderMocks.mockEmail());
			doReturn(List.of(new Order(fase, user)))
				.when(orderService).getAllByStep(step, null);
			
			mockMvc
					.perform(get("/api/order/step/" + step))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$").isArray())
				    .andExpect(jsonPath("$", hasSize(1)));
		}
	}
	
	@Nested
	class CreateNewOrder {
	
		@Test
		void givenNewOrder_WhenMockMVCPost_thenReturnURICreated() throws Exception {
			doReturn(Optional.of(OrderMocks.order(1L)))
				.when(orderService).createNewOrder(any(OrderRequest.class));
			
			mockMvc
					.perform(post("/api/order")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(OrderMocks.jsonRequestOrder())
							.characterEncoding("utf-8"))
				    .andExpect(status().isCreated());
		}
		
		@Test
		void givenNewOrder_WhenMockMVCPostAndThrowsError_thenReturnHandledException() throws Exception {
			doReturn(Optional.empty())
				.when(orderService).createNewOrder(any(OrderRequest.class));
			
			mockMvc
					.perform(post("/api/order")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(OrderMocks.jsonRequestOrder())
							.characterEncoding("utf-8"))
				    .andExpect(status().isBadRequest())
				    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(OrderFailToCreateException.class))
				    .andExpect(status().reason("Erro ao criar pedido"));;
		}
	}
	
	@Nested
	class UpdateOrderItems {
	
		@Test
		void givenExistantOrder_WhenMockMVCPut_thenReturnUpdatedOrder() throws Exception {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.of(order))
				.when(orderService).updateOrderItems(anyLong(), anyList());
			
			mockMvc
					.perform(put("/api/order/{orderId}", orderId)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(OrderMocks.jsonRequestOrderItems())
							.characterEncoding("utf-8"))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$.id", is(1)))
				    .andExpect(jsonPath("$.step", is("KITCHEN")))
				    .andExpect(jsonPath("$.fase", is("PENDING")))
				    .andExpect(jsonPath("$.items", hasSize(0)))
				    .andExpect(jsonPath("$.user").exists())
				    .andExpect(jsonPath("$.user.id", is(1)))
				    .andExpect(jsonPath("$.user.cpf", is(order.getUser().getCpf())))
				    .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));
		}
		
		@Test
		void givenUnexistantOrder_WhenMockMVCPut_thenReturnUpdatedOrder() throws Exception {
			long orderId = 1L;
			doReturn(Optional.empty())
				.when(orderService).updateOrderItems(anyLong(), anyList());
			
			mockMvc
					.perform(put("/api/order/{orderId}", orderId)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(OrderMocks.jsonRequestOrderItems())
							.characterEncoding("utf-8"))
				    .andExpect(status().isNotFound())
				    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(OrderNotFoundException.class))
				    .andExpect(status().reason("Pedido não encontrado"));
		}
	}
	
	@Nested
	class UpdateStepAndFase {
		
		@Test
		void givenExistantOrder_WhenMockMVCPutStepAndFase_thenReturnUpdatedOrder() throws Exception {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.of(order))
				.when(orderService).updateStepAndFase(orderId, order.getStep(), order.getFase());
			
			mockMvc
					.perform(put("/api/order/{orderId}/{step}/{fase}", orderId, order.getStep(), order.getFase())
							.contentType(MediaType.APPLICATION_JSON_VALUE))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$.id", is(1)))
				    .andExpect(jsonPath("$.step", is("KITCHEN")))
				    .andExpect(jsonPath("$.fase", is("PENDING")))
				    .andExpect(jsonPath("$.items", hasSize(0)))
				    .andExpect(jsonPath("$.user").exists())
				    .andExpect(jsonPath("$.user.id", is(1)))
				    .andExpect(jsonPath("$.user.cpf", is(order.getUser().getCpf())))
				    .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));
		}
		
		@Test
		void givenUnexistantOrder_WhenMockMVCPutStepAndFase_thenThrowsHandledError() throws Exception {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.empty())
				.when(orderService).updateStepAndFase(orderId, order.getStep(), order.getFase());
			
			mockMvc
					.perform(put("/api/order/{orderId}/{step}/{fase}", orderId, order.getStep(), order.getFase())
							.contentType(MediaType.APPLICATION_JSON_VALUE))
				    .andExpect(status().isNotFound())
				    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(OrderNotFoundException.class))
				    .andExpect(status().reason("Pedido não encontrado"));
		}
	}
	
	@Nested
	class UpdateFase {
		
		@Test
		void givenExistantOrder_WhenMockMVCPutFase_thenReturnUpdatedOrder() throws Exception {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.of(order))
				.when(orderService).updateFase(orderId, order.getFase());
			
			mockMvc
					.perform(put("/api/order/{orderId}/{fase}", orderId, order.getFase())
							.contentType(MediaType.APPLICATION_JSON_VALUE))
				    .andExpect(status().isOk())
				    .andExpect(jsonPath("$.id", is(1)))
				    .andExpect(jsonPath("$.step", is("KITCHEN")))
				    .andExpect(jsonPath("$.fase", is("PENDING")))
				    .andExpect(jsonPath("$.items", hasSize(0)))
				    .andExpect(jsonPath("$.user").exists())
				    .andExpect(jsonPath("$.user.id", is(1)))
				    .andExpect(jsonPath("$.user.cpf", is(order.getUser().getCpf())))
				    .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));
		}
		
		@Test
		void givenUnexistantOrder_WhenMockMVCPutFase_thenThrowsHandledError() throws Exception {
			long orderId = 1L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.empty())
				.when(orderService).updateFase(orderId, order.getFase());
			
			mockMvc
					.perform(put("/api/order/{orderId}/{fase}", orderId, order.getFase())
							.contentType(MediaType.APPLICATION_JSON_VALUE))
				    .andExpect(status().isNotFound())
				    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(OrderNotFoundException.class))
				    .andExpect(status().reason("Pedido não encontrado"));
		}
	}
	
	@Nested
	class DeleteById {
		@Test
		void givenOrder_WhenMockMVCDeleteById_thenReturnNoContent() throws Exception {
			long orderId = 1L;
			
			mockMvc
					.perform(delete("/api/order/{orderId}", orderId))
				    .andDo(print())
				    .andExpect(status().isNoContent());
		}
	}
}
