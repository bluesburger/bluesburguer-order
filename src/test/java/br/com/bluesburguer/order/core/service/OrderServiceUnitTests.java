package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.out.OrderNotFoundException;
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.support.OrderMocks;
import br.com.bluesburguer.order.support.UserMocks;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTests {
	
	private static final UUID EXISTANT_ORDER_ID = UUID.fromString("b9881aef-ca1a-4ce6-843b-0717f0f0b90a");
	private static final UUID UNEXISTANT_ORDER_ID = UUID.fromString("fc48ec45-3b55-497d-8ca7-d3e852e94768");

	@Mock
	UserService userService;
	
	@Mock
	OrderItemRepository orderItemRepository;
	
	@Mock
	OrderRepository orderRepository;
	
	@InjectMocks
	OrderService orderService;
	
	@Nested
	class GetAll {
		@Test
		void givenOneExistantOrder_WhenListAllOrders_ThenShouldReturnListOfOrders() {
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(List.of(order))
				.when(orderRepository).findAll();
			
			assertThat(orderService.getAll())
				.anyMatch(o -> o.equals(order));
		}
	}
	
	@Nested
	class GetAllByStep {
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndFases_ThenShouldReturnListOfOrders() {
			var step = OrderStep.KITCHEN;
			var fases = List.of(OrderFase.IN_PROGRESS);
			
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(List.of(order))
				.when(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, fases);
			
			assertThat(orderService.getAllByStep(step, fases))
				.anyMatch(o -> o.equals(order));
			verify(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, fases);
		}
		
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndNullFases_ThenShouldReturnListOfOrders() {
			var step = OrderStep.KITCHEN;
			List<OrderFase> fases = null;
			var allFases = Stream.of(OrderFase.values()).toList();
			
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(List.of(order))
				.when(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, allFases);
			
			assertThat(orderService.getAllByStep(step, fases))
				.anyMatch(o -> o.equals(order));
			verify(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, allFases);
		}
		
		@Test
		void givenOneExistantOrder_WhenListAllOrdersByStepAndEmptyFases_ThenShouldReturnListOfOrders() {
			var step = OrderStep.KITCHEN;
			List<OrderFase> fases = List.of();
			var allFases = Stream.of(OrderFase.values()).toList();
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			
			doReturn(List.of(order))
				.when(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, allFases);

			assertThat(orderService.getAllByStep(step, fases))
				.anyMatch(o -> o.equals(order));
			verify(orderRepository).findAllByStepAndFaseInOrderByCreatedTime(step, allFases);
		}
	}
	
	@Nested
	class GetById {
		@Test
		void givenOneExistantOrder_WhenGetById_ThenShouldReturnExistantOrder() {
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			
			doReturn(Optional.of(order))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			assertThat(orderService.getById(EXISTANT_ORDER_ID))
				.isPresent()
				.isEqualTo(Optional.of(order));
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_ThenShouldReturnEmpty() {
			doReturn(Optional.empty())
				.when(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			
			assertThat(orderService.getById(UNEXISTANT_ORDER_ID))
				.isNotPresent();
		}
	}
	
	@Nested
	class GetItemByOrderItemId {
		@Test
		void givenOneExistantItem_WhenGetByOrderItemId_ThenShouldReturnExistantItem() {
			long orderItemId = 2L;
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			var orderItem = OrderMocks.orderItem(orderItemId, order);
			doReturn(Optional.of(orderItem))
				.when(orderItemRepository).findByOrderItemId(anyLong());
			
			assertThat(orderService.getItemByOrderItemId(orderItemId))
				.isPresent()
				.isEqualTo(Optional.of(orderItem));
			
			verify(orderItemRepository).findByOrderItemId(orderItemId);
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetByOrderItemId_AndServerReturnsNull_ThenShouldReturnEmpty() {
			long orderItemId = 3L;
			doReturn(Optional.empty())
				.when(orderItemRepository).findByOrderItemId(orderItemId);
			
			assertThat(orderService.getItemByOrderItemId(orderItemId))
				.isNotPresent();
			verify(orderItemRepository).findByOrderItemId(orderItemId);
		}
	}
	
	@Nested
	class GetItemById {
		@Test
		void givenOneExistantItem_WhenGetById_ThenShouldReturnExistantItem() {
			long orderItemId = 2L;
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			var orderItem = OrderMocks.orderItem(orderItemId, order);
			doReturn(Optional.of(orderItem))
				.when(orderItemRepository).findById(anyLong());
			
			assertThat(orderService.getItemById(orderItemId))
				.isPresent()
				.isEqualTo(Optional.of(orderItem));
			
			verify(orderItemRepository).findById(orderItemId);
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_AndServerReturnsNull_ThenShouldReturnEmpty() {
			long orderItemId = 3L;
			doReturn(Optional.empty())
				.when(orderItemRepository).findById(orderItemId);
			
			assertThat(orderService.getItemById(orderItemId))
				.isNotPresent();
			verify(orderItemRepository).findById(orderItemId);
		}
	}
	
	@Nested
	class CreateNewOrder {
		@Test
		void givenNewOrderRequest_WhenSaveNewOrderForExistantUser_ThenShouldReturnSavedOrder() {
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			var userRequest = UserMocks.userRequest();
			
			doReturn(UserMocks.user())
				.when(userService).saveIfNotExist(userRequest);
			
			doReturn(order)
				.when(orderRepository).save(any(Order.class));
			
			var orderItem = OrderMocks.orderItem(1L, order);
			doReturn(orderItem)
				.when(orderItemRepository).save(any(OrderItem.class));
			
			var orderRequest = new OrderRequest(List.of(new OrderItemRequest(1L, 1)), userRequest);
			assertThat(orderService.createNewOrder(orderRequest))
				.isNotNull();
		}
	}
	
	@Nested
	class UpdateStepAndFase {
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFase_ThenShouldReturnUpdatedOrder() {
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			var updatedOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			updatedOrder.setStep(step);
			updatedOrder.setFase(fase);
			
			doReturn(updatedOrder)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateStepAndFase(EXISTANT_ORDER_ID, step, fase);
			assertThat(updatedReturn)
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", EXISTANT_ORDER_ID.toString())
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime")
				.hasSameHashCodeAs(updatedOrder);

			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderRepository).save(any(Order.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateStepAndFase_ThenShouldReturnEmpty() {
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			doReturn(Optional.empty())
				.when(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			
			var updatedOrder = new Order(fase, UserMocks.user());
			updatedOrder.setStep(step);
			
			assertThat(orderService.updateStepAndFase(UNEXISTANT_ORDER_ID, step, fase))
				.isNotPresent();
			
			verify(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			verify(orderRepository, never()).save(updatedOrder);
		}
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFaseWithError_ThenShouldReturnEmpty() {
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			var updatedOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			updatedOrder.setStep(step);
			updatedOrder.setFase(fase);
			
			doReturn(null)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateStepAndFase(EXISTANT_ORDER_ID, step, fase);
			assertThat(updatedReturn)
				.isNotPresent();

			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderRepository).save(any(Order.class));
		}
	}
	
	@Nested
	class UpdateFase {
		@Test
		void givenExistantOrder_WhenUpdateStepAndFase_ThenShouldReturnUpdatedOrder() {
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			var updatedOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			updatedOrder.setFase(fase);
			
			doReturn(updatedOrder)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateFase(EXISTANT_ORDER_ID, fase);
			assertThat(updatedReturn)
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", EXISTANT_ORDER_ID.toString())
				.hasFieldOrPropertyWithValue("step", existantOrder.getStep())
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime")
				.hasSameHashCodeAs(updatedOrder);

			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderRepository).save(any(Order.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateStepAndFase_ThenShouldReturnEmpty() {
			var fase = OrderFase.IN_PROGRESS;
			
			doReturn(Optional.empty())
				.when(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			
			var updatedOrder = new Order(fase, UserMocks.user());
			
			assertThat(orderService.updateFase(UNEXISTANT_ORDER_ID, fase))
				.isNotPresent();
			
			verify(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			verify(orderRepository, never()).save(updatedOrder);
		}
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFaseWithError_ThenShouldReturnEmpty() {
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			var updatedOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			updatedOrder.setFase(fase);
			
			doReturn(null)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateFase(EXISTANT_ORDER_ID, fase);
			assertThat(updatedReturn)
				.isNotPresent();

			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderRepository).save(any(Order.class));
		}
	}
	
	@Nested
	class UpdateOrderItems {
		
		@Test
		void givenExistantOrder_WhenUpdateOrderItems_ThenShouldReturnUpdatedOrder() {
			var existantOrder = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			doReturn(OrderMocks.orderItem(1L, existantOrder))
				.when(orderItemRepository).save(any(OrderItem.class));
			
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			assertThat(orderService.updateOrderItems(EXISTANT_ORDER_ID, orderItems))
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", EXISTANT_ORDER_ID.toString())
				.hasFieldOrPropertyWithValue("step", existantOrder.getStep())
				.hasFieldOrPropertyWithValue("fase", existantOrder.getFase())
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime");
			
			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderItemRepository).deleteAllByOrderId(EXISTANT_ORDER_ID.toString());
			verify(orderItemRepository, times(orderItems.size()))
				.save(any(OrderItem.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateOrderItems_ThenShouldReturnEmpty() {
			doReturn(Optional.empty())
				.when(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			assertThat(orderService.updateOrderItems(UNEXISTANT_ORDER_ID, orderItems))
				.isNotPresent();
			
			verify(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			verify(orderItemRepository, never()).deleteAllByOrderId(UNEXISTANT_ORDER_ID.toString());
			verify(orderItemRepository, never()).save(any(OrderItem.class));
		}
	}
	
	@Nested
	class SaveItem {
		
		@Test
		void givenNewOrder_WhenSaveItem_ThenReturnSavedItem() {
			var itemRequest = new OrderItemRequest(1L, 1);
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			var orderItem = OrderMocks.orderItem(1L, order);
			
			doReturn(orderItem)
				.when(orderItemRepository).save(any(OrderItem.class));
			
			assertThat(orderService.saveItem(itemRequest, order))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", itemRequest.getId())
				.hasFieldOrPropertyWithValue("quantity", itemRequest.getQuantity());
		}
	}
	
	@Nested
	class DeleteById {
		
		@Test
		void givenExistantOrder_WhenDeleteById_ThenShouldReturnNoContent() {
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			doReturn(Optional.of(order))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			orderService.deleteById(EXISTANT_ORDER_ID);
			
			verify(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			verify(orderItemRepository).deleteAllByOrderId(EXISTANT_ORDER_ID.toString());
			verify(orderRepository).deleteById(EXISTANT_ORDER_ID.toString());
		}
		
		@Test
		void givenUnexistantOrder_WhenDeleteById_ThenShouldThrows() {
			doReturn(Optional.empty())
				.when(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			
			assertThrows(OrderNotFoundException.class, () -> orderService.deleteById(UNEXISTANT_ORDER_ID), "Pedido não encontrado");
			
			verify(orderRepository).findById(UNEXISTANT_ORDER_ID.toString());
			verify(orderItemRepository, never()).deleteAllByOrderId(UNEXISTANT_ORDER_ID.toString());
			verify(orderRepository, never()).delete(any(Order.class));
		}
	}
	
	@Nested
	class AddOrderItem {
		
		@Test
		void givenExistantOrder_WhenAddItem_ThenShouldReturnAddedItem() {
			var order = OrderMocks.order(EXISTANT_ORDER_ID);
			var orderItem = OrderMocks.orderItem(1L, order);
			var orderItemSaved = OrderMocks.orderItem(1L, order);
			orderItemSaved.setId(2L);
			
			doReturn(orderItemSaved)
				.when(orderItemRepository).save(any(OrderItem.class));
			
			doReturn(Optional.of(order))
				.when(orderRepository).findById(EXISTANT_ORDER_ID.toString());
			
			var itemRequest = new OrderItemRequest(orderItem.getId(), orderItem.getQuantity());
			assertThat(orderService.addItem(UUID.fromString(order.getId()), itemRequest))
				.hasFieldOrPropertyWithValue("id", orderItemSaved.getId());
			
			verify(orderItemRepository).save(any(OrderItem.class));
		}
	}
}
