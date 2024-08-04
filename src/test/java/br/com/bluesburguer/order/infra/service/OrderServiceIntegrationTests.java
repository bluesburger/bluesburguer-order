package br.com.bluesburguer.order.infra.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.application.dto.user.UserRequest;
import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.domain.exception.OrderNotFoundException;
import br.com.bluesburguer.order.infra.database.OrderAdapter;
import br.com.bluesburguer.order.infra.database.OrderItemRepository;
import br.com.bluesburguer.order.infra.database.OrderRepository;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.OrderMocks;

class OrderServiceIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	OrderAdapter orderService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}
	
	@Nested
	class GetAll {
		
		@Test
		void givenOneExistantOrder_WhenListAllOrders_ThenShouldReturnListOfOrders() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			validatePersistedOrder(createNewOrder(cpf, email), step, fase, cpf, email);
			
			assertThat(orderService.getAll())
				.isNotEmpty()
				.first()
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

	@Nested
	class GetAllByStep {
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndFases_ThenShouldReturnListOfOrders() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			validatePersistedOrder(createNewOrder(cpf, email), step, fase, cpf, email);
			
			assertThat(orderService.getAllByStep(step, List.of(fase)))
				.isNotEmpty()
				.first()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrProperty("user.id")
				.hasFieldOrPropertyWithValue("user.email", email)
				.hasFieldOrPropertyWithValue("user.cpf", cpf)
				.hasFieldOrProperty("user.creationDateTime");
		}
		
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndNullFases_ThenShouldReturnListOfOrders() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			validatePersistedOrder(createNewOrder(cpf, email), step, fase, cpf, email);
			
			assertThat(orderService.getAllByStep(step, null))
				.isNotEmpty()
				.first()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrProperty("user.id")
				.hasFieldOrPropertyWithValue("user.email", email)
				.hasFieldOrPropertyWithValue("user.cpf", cpf)
				.hasFieldOrProperty("user.creationDateTime");
		}
		
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndEmptyFases_ThenShouldReturnListOfOrders() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			validatePersistedOrder(createNewOrder(cpf, email), step, fase, cpf, email);
			
			assertThat(orderService.getAllByStep(step, List.of()))
				.isNotEmpty()
				.first()
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
	
	@Nested
	class GetById {
		@Test
		void givenOneExistantOrder_WhenGetById_ThenShouldReturnExistantOrder() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var newOrderOpt = createNewOrder(cpf, email);
			assertThat(newOrderOpt).isPresent();
			
			validatePersistedOrder(newOrderOpt, step, fase, cpf, email);
			
			assertThat(orderService.getById(UUID.fromString(newOrderOpt.get().getId())))
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
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_ThenShouldReturnEmpty() {
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			assertThat(orderService.getById(orderId))
				.isNotPresent();
		}
	}
	
	@Nested
	class GetItemById {
		@Test
		void givenOneExistantItem_WhenGetById_ThenShouldReturnExistantItem() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
			var itemIdOptional = createdOrder.get().getItems().stream().findFirst().map(i -> i.getId());
			assertThat(itemIdOptional).isPresent();
			
			assertThat(orderService.getItemById(itemIdOptional.get()))
				.isPresent()
				.get()
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("order")
				.hasFieldOrPropertyWithValue("quantity", 1)
				.hasFieldOrProperty("createdTime")
				.hasFieldOrProperty("updatedTime");
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_ThenShouldReturnEmpty() {
			assertThat(orderService.getItemById(98L))
				.isNotPresent();
		}
	}
	
	@Nested
	class CreateNewOrder {
		@Test
		void givenNewOrderRequest_WhenSaveNewOrderForExistantUser_ThenShouldReturnSavedOrder() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
		}
	}
	
	@Nested
	class UpdateStepAndFase {
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFase_ThenShouldReturnUpdatedOrder() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			var newStep = OrderStep.INVOICE;
			var newFase = OrderFase.CONFIRMED;
			assertThat(orderService.updateStepAndFase(UUID.fromString(createdOrder.get().getId()), newStep, newFase))
				.isPresent()
				.get()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", newStep)
				.hasFieldOrPropertyWithValue("fase", newFase)
				.hasFieldOrProperty("user")
				.hasFieldOrProperty("user.id")
				.hasFieldOrPropertyWithValue("user.email", email)
				.hasFieldOrPropertyWithValue("user.cpf", cpf)
				.hasFieldOrProperty("user.creationDateTime");
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateStepAndFase_ThenShouldReturnEmpty() {
			var newStep = OrderStep.INVOICE;
			var newFase = OrderFase.CONFIRMED;
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			assertThat(orderService.updateStepAndFase(orderId, newStep, newFase))
				.isNotPresent();
		}
	}
	
	@Nested
	class UpdateFase {
		@Test
		void givenExistantOrder_WhenUpdateFase_ThenShouldReturnUpdatedOrder() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			var newFase = OrderFase.CONFIRMED;
			assertThat(orderService.updateFase(UUID.fromString(createdOrder.get().getId()), newFase))
				.isPresent()
				.get()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", newFase)
				.hasFieldOrProperty("user")
				.hasFieldOrProperty("user.id")
				.hasFieldOrPropertyWithValue("user.email", email)
				.hasFieldOrPropertyWithValue("user.cpf", cpf)
				.hasFieldOrProperty("user.creationDateTime");
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateFase_ThenShouldReturnEmpty() {
			var newFase = OrderFase.CONFIRMED;
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			assertThat(orderService.updateFase(orderId, newFase))
				.isNotPresent();
		}
	}
	
	@Nested
	class UpdateOrderItems {
		
		@Test
		void givenExistantOrder_WhenUpdateOrderItems_ThenShouldReturnUpdatedOrder() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			assertThat(orderService.updateOrderItems(UUID.fromString(createdOrder.get().getId()), orderItems))
				.isPresent()
				.get()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrProperty("user.id")
				.hasFieldOrPropertyWithValue("user.email", email)
				.hasFieldOrPropertyWithValue("user.cpf", cpf)
				.hasFieldOrProperty("user.creationDateTime")
				.hasFieldOrProperty("items");
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateOrderItems_ThenShouldReturnEmpty() {
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			assertThat(orderService.updateOrderItems(orderId, orderItems))
				.isNotPresent();
		}
	}
	
	@Nested
	class SaveItem {
		void givenNewOrder_WhenSaveItem_ThenReturnSavedItem() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
			int quantity = 1;
			var itemRequest = new OrderItemRequest(1L, quantity);
			assertThat(orderService.saveItem(itemRequest, createdOrder.get()))
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("order")
				.hasFieldOrPropertyWithValue("quantity", quantity)
				.hasFieldOrProperty("createdTime")
				.hasFieldOrProperty("updatedTime");
		}
	}
	
	@Nested
	class DeleteById {
		@Test
		void givenExistantOrder_WhenDeleteById_ThenShouldReturnNoContent() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
			orderService.deleteById(UUID.fromString(createdOrder.get().getId()));
		}
		
		@Test
		void givenUnexistantOrder_WhenDeleteById_ThenShouldThrows() {
			var orderId = UUID.fromString("ddedf1ab-0b2f-4766-a9fc-104bedc98492");
			assertThrows(OrderNotFoundException.class, () -> orderService.deleteById(orderId), "Pedido não encontrado");
		}
	}
	
	@Nested
	class AddOrderItem {
		
		@Test
		void givenExistantOrder_WhenAddItem_ThenShouldReturnAddedItem() {
			var cpf = OrderMocks.mockCpf();
			var email = OrderMocks.mockEmail();
			var step = OrderStep.ORDER;
			var fase = OrderFase.REGISTERED;
			int quantity = 1;
			
			var createdOrder = createNewOrder(cpf, email);
			
			validatePersistedOrder(createdOrder, step, fase, cpf, email);
			
			var item = new OrderItemRequest(1L, quantity);
			var orderId = createdOrder.get().getId();
			assertThat(orderService.addItem(UUID.fromString(orderId), item))
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("order")
				.hasFieldOrPropertyWithValue("quantity", quantity)
				.hasFieldOrProperty("createdTime")
				.hasFieldOrProperty("updatedTime");
		}
		
		@Test
		void givenUnexistantOrder_WhenAddItem_ThenShouldThrowsHandledException() {
			var item = new OrderItemRequest(1L, 1);
			assertThrows(OrderNotFoundException.class, () -> orderService.addItem(null, item), "Pedido não encontrado");
		}
	}
	
	private Optional<OrderEntity> createNewOrder(String cpf, String email) {
		var items = List.of(new OrderItemRequest(1L, 1));
		var user = new UserRequest(null, new Cpf(cpf), new Email(email));
		
		return orderService
				.createNewOrder(new OrderRequest(items, user));
	}
	
	private void validatePersistedOrder(Optional<OrderEntity> optionalOrder, 
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
