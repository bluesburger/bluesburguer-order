package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.support.OrderMocks;
import jakarta.persistence.EntityNotFoundException;

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
			doReturn(orderItem)
				.when(orderItemRepository).getReferenceById(orderItemId);
			
			assertThat(orderService.getItemById(orderItemId))
				.isPresent()
				.isEqualTo(Optional.of(orderItem));
			
			verify(orderItemRepository).getReferenceById(orderItemId);
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_AndServerReturnsNull_ThenShouldReturnEmpty() {
			long orderItemId = 3L;
			doReturn(null)
				.when(orderItemRepository).getReferenceById(orderItemId);
			
			assertThat(orderService.getItemById(orderItemId))
				.isNotPresent();
			verify(orderItemRepository).getReferenceById(orderItemId);
		}
		
		@Test
		void givenOneUnexistantOrder_WhenGetById_AndServerThrowsEntityNotFoundException_ThenShouldReturnEmpty() {
			long orderItemId = 3L;
			doThrow(new EntityNotFoundException("Key not found"))
				.when(orderItemRepository).getReferenceById(orderItemId);
			
			assertThat(orderService.getItemById(orderItemId))
				.isNotPresent();
			verify(orderItemRepository).getReferenceById(orderItemId);
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
		
	}
	
	@Nested
	class UpdateFase {
		
	}
	
	@Nested
	class UpdateOrderItems {
		
	}
	
	@Nested
	class SaveItem {
		
	}
	
	@Nested
	class DeleteById {
		
	}
	
	@Nested
	class AddOrderItem {
		
	}
}
