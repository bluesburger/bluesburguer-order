package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTests {

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
			var order = OrderMocks.order(1L);
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
			
			var order = OrderMocks.order(2L);
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
			
			var order = OrderMocks.order(2L);
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
			var order = OrderMocks.order(2L);
			
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
			long orderId = 2L;
			var order = OrderMocks.order(orderId);
			
			doReturn(Optional.of(order))
				.when(orderRepository).findById(orderId);
			
			assertThat(orderService.getById(orderId))
				.isPresent()
				.isEqualTo(Optional.of(order));
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_ThenShouldReturnEmpty() {
			long orderId = 2L;
			doReturn(Optional.empty())
				.when(orderRepository).findById(orderId);
			
			assertThat(orderService.getById(orderId))
				.isNotPresent();
		}
	}
	
	@Nested
	class GetItemById {
		@Test
		void givenOneExistantItem_WhenGetById_ThenShouldReturnExistantItem() {
			long orderItemId = 2L;
			var orderItem = OrderMocks.orderItem(orderItemId, null);
			doReturn(Optional.of(orderItem))
				.when(orderItemRepository).findById(orderItemId);
			
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
			var order = OrderMocks.order(1L);
			var userRequest = OrderMocks.userRequest();
			
			doReturn(OrderMocks.user())
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
			long orderId = 1L;
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(orderId);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = OrderMocks.order(orderId);
			updatedOrder.setStep(step);
			updatedOrder.setFase(fase);
			
			doReturn(updatedOrder)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateStepAndFase(orderId, step, fase);
			assertThat(updatedReturn)
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", orderId)
				.hasFieldOrPropertyWithValue("step", step)
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime")
				.hasSameHashCodeAs(updatedOrder);

			verify(orderRepository).findById(orderId);
			verify(orderRepository).save(any(Order.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateStepAndFase_ThenShouldReturnEmpty() {
			long orderId = 1L;
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			doReturn(Optional.empty())
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = new Order(fase, OrderMocks.user());
			updatedOrder.setStep(step);
			
			assertThat(orderService.updateStepAndFase(orderId, step, fase))
				.isNotPresent();
			
			verify(orderRepository).findById(orderId);
			verify(orderRepository, never()).save(updatedOrder);
		}
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFaseWithError_ThenShouldReturnEmpty() {
			long orderId = 1L;
			var step = OrderStep.ORDER;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(orderId);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = OrderMocks.order(orderId);
			updatedOrder.setStep(step);
			updatedOrder.setFase(fase);
			
			doReturn(null)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateStepAndFase(orderId, step, fase);
			assertThat(updatedReturn)
				.isNotPresent();

			verify(orderRepository).findById(orderId);
			verify(orderRepository).save(any(Order.class));
		}
	}
	
	@Nested
	class UpdateFase {
		@Test
		void givenExistantOrder_WhenUpdateStepAndFase_ThenShouldReturnUpdatedOrder() {
			long orderId = 1L;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(orderId);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = OrderMocks.order(orderId);
			updatedOrder.setFase(fase);
			
			doReturn(updatedOrder)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateFase(orderId, fase);
			assertThat(updatedReturn)
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", orderId)
				.hasFieldOrPropertyWithValue("step", existantOrder.getStep())
				.hasFieldOrPropertyWithValue("fase", fase)
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime")
				.hasSameHashCodeAs(updatedOrder);

			verify(orderRepository).findById(orderId);
			verify(orderRepository).save(any(Order.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateStepAndFase_ThenShouldReturnEmpty() {
			long orderId = 1L;
			var fase = OrderFase.IN_PROGRESS;
			
			doReturn(Optional.empty())
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = new Order(fase, OrderMocks.user());
			
			assertThat(orderService.updateFase(orderId, fase))
				.isNotPresent();
			
			verify(orderRepository).findById(orderId);
			verify(orderRepository, never()).save(updatedOrder);
		}
		
		@Test
		void givenExistantOrder_WhenUpdateStepAndFaseWithError_ThenShouldReturnEmpty() {
			long orderId = 1L;
			var fase = OrderFase.IN_PROGRESS;
			
			var existantOrder = OrderMocks.order(orderId);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(orderId);
			
			var updatedOrder = OrderMocks.order(orderId);
			updatedOrder.setFase(fase);
			
			doReturn(null)
				.when(orderRepository).save(any(Order.class));
			
			var updatedReturn = orderService.updateFase(orderId, fase);
			assertThat(updatedReturn)
				.isNotPresent();

			verify(orderRepository).findById(orderId);
			verify(orderRepository).save(any(Order.class));
		}
	}
	
	@Nested
	class UpdateOrderItems {
		
		@Test
		void givenExistantOrder_WhenUpdateOrderItems_ThenShouldReturnUpdatedOrder() {
			long orderId = 1L;
			var existantOrder = OrderMocks.order(orderId);
			doReturn(Optional.of(existantOrder))
				.when(orderRepository).findById(orderId);
			
			doReturn(OrderMocks.orderItem(1L, existantOrder))
				.when(orderItemRepository).save(any(OrderItem.class));
			
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			assertThat(orderService.updateOrderItems(orderId, orderItems))
				.isPresent()
				.get()
				.hasFieldOrPropertyWithValue("id", orderId)
				.hasFieldOrPropertyWithValue("step", existantOrder.getStep())
				.hasFieldOrPropertyWithValue("fase", existantOrder.getFase())
				.hasFieldOrProperty("user")
				.hasFieldOrPropertyWithValue("user.id", existantOrder.getUser().getId())
				.hasFieldOrPropertyWithValue("user.email", existantOrder.getUser().getEmail())
				.hasFieldOrPropertyWithValue("user.cpf", existantOrder.getUser().getCpf())
				.hasFieldOrProperty("user.creationDateTime");
			
			verify(orderRepository).findById(orderId);
			verify(orderItemRepository).deleteAllByOrderId(orderId);
			verify(orderItemRepository, times(orderItems.size()))
				.save(any(OrderItem.class));
		}
		
		@Test
		void givenUnexistantOrder_WhenUpdateOrderItems_ThenShouldReturnEmpty() {
			long orderId = 1L;;
			doReturn(Optional.empty())
				.when(orderRepository).findById(orderId);
			
			var orderItems = List.of(new OrderItemRequest(1L, 1));
			assertThat(orderService.updateOrderItems(orderId, orderItems))
				.isNotPresent();
			
			verify(orderRepository).findById(orderId);
			verify(orderItemRepository, never()).deleteAllByOrderId(orderId);
			verify(orderItemRepository, never()).save(any(OrderItem.class));
		}
	}
	
	@Nested
	class SaveItem {
		
		@Test
		void givenNewOrder_WhenSaveItem_ThenReturnSavedItem() {
			var itemRequest = new OrderItemRequest(1L, 1);
			var order = OrderMocks.order(1L);
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
			long orderId = 1L;
			
			var order = OrderMocks.order(orderId);
			doReturn(Optional.of(order))
				.when(orderRepository).findById(orderId);
			
			orderService.deleteById(orderId);
			
			verify(orderRepository).findById(orderId);
			verify(orderItemRepository).deleteAllByOrderId(orderId);
			verify(orderRepository).delete(order);
		}
		
		@Test
		void givenUnexistantOrder_WhenDeleteById_ThenShouldThrows() {
			long orderId = 1L;
			
			doReturn(Optional.empty())
				.when(orderRepository).findById(orderId);
			
			assertThrows(OrderNotFoundException.class, () -> orderService.deleteById(orderId), "Pedido não encontrado");
			
			verify(orderRepository).findById(orderId);
			verify(orderItemRepository, never()).deleteAllByOrderId(orderId);
			verify(orderRepository, never()).delete(any(Order.class));
		}
	}
	
	@Nested
	class AddOrderItem {
		
		@Test
		void givenExistantOrder_WhenAddItem_ThenShouldReturnAddedItem() {
			var order = OrderMocks.order(1L);
			var orderItem = OrderMocks.orderItem(1L, order);
			var orderItemSaved = OrderMocks.orderItem(1L, order);
			orderItemSaved.setId(2L);
			
			doReturn(orderItemSaved)
				.when(orderItemRepository).save(orderItem);
			
			assertThat(orderService.addItem(orderItem))
				.hasFieldOrPropertyWithValue("id", orderItemSaved.getId());
			
			verify(orderItemRepository).save(orderItem);
		}
		
		@Test
		void givenUnexistantOrder_WhenAddItem_ThenShouldThrowsHandledException() {
			var orderItem = OrderMocks.orderItem(1L, null);
			
			assertThrows(OrderNotFoundException.class, () -> orderService.addItem(orderItem), "Pedido não encontrado");
			
			verify(orderItemRepository, never()).save(orderItem);
		}
	}
}
