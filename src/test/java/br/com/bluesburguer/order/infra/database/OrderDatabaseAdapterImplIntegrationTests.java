package br.com.bluesburguer.order.infra.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.OrderMocks;

class OrderDatabaseAdapterImplIntegrationTests extends ApplicationIntegrationSupport {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDatabaseAdapter orderDatabaseAdapter;
	
	OrderEntity orderSaved;
	
	OrderUserEntity orderUserSaved;
	
	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}
	
	@BeforeEach
	void setUp() {
		this.orderSaved = saveNewOrder();
	}
	
	private OrderEntity saveNewOrder(OrderStep step, OrderFase fase) {
		var orderEntity = OrderMocks.order();
		orderEntity.setStep(step);
		orderEntity.setFase(fase);
		return saveNewOrder(orderEntity);
	}
	
	private OrderEntity saveNewOrder() {
		return saveNewOrder(OrderMocks.order());
	}
	
	private OrderEntity saveNewOrder(OrderEntity orderEntity) {
		if (this.orderUserSaved == null) {
			this.orderUserSaved = userRepository.save(orderEntity.getUser());
		}
		orderEntity.setUser(this.orderUserSaved);
		return orderRepository.save(orderEntity);
	}
	
	@Nested
	class GetAll {
		@Test
		void shouldGetAll() {
			assertThat(orderDatabaseAdapter.getAll())
				.isNotEmpty()
				.satisfiesExactlyInAnyOrder(order -> {
					assertThat(order)
						.hasFieldOrPropertyWithValue("id", orderSaved.getId())
						.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
						.hasFieldOrPropertyWithValue("fase", orderSaved.getFase())
						.hasFieldOrPropertyWithValue("user", orderSaved.getUser());
				});
		}
	}
	
	@Nested
	class GetById {
		@Test
		void shouldGetById() {
			assertThat(orderDatabaseAdapter.getById(orderSaved.getId()))
				.hasFieldOrPropertyWithValue("id", orderSaved.getId())
				.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
				.hasFieldOrPropertyWithValue("fase", orderSaved.getFase())
				.hasFieldOrPropertyWithValue("user", orderSaved.getUser());
		}
	}
	
	@Nested
	class GetAllByStep {
		
		@Test
		void givenExistantOrderWithStep_WhenGetAllByStep_ThenShouldReturnExistantOrdersWithSameStep() {
			assertThat(orderDatabaseAdapter.getAllByStep(orderSaved.getStep(), null))
				.isNotEmpty()
				.satisfiesExactlyInAnyOrder(order -> {
					assertThat(order)
						.hasFieldOrPropertyWithValue("id", orderSaved.getId())
						.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
						.hasFieldOrPropertyWithValue("fase", orderSaved.getFase())
						.hasFieldOrPropertyWithValue("user", orderSaved.getUser());
				});
		}
		
		@Test
		void givenExistantOrderWithStepAndFase_WhenGetAllByStep_ThenShouldReturnExistantOrderWithSameStepAndFase() {
			var anotherOrderWithDifferentFase = saveNewOrder(OrderStep.ORDER, OrderFase.FAILED);
			
			assertThat(orderDatabaseAdapter.getAllByStep(orderSaved.getStep(), List.of(orderSaved.getFase())))
				.isNotEmpty()
				.satisfiesExactlyInAnyOrder(order -> {
					assertThat(order)
						.hasFieldOrPropertyWithValue("id", orderSaved.getId())
						.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
						.hasFieldOrPropertyWithValue("fase", orderSaved.getFase())
						.hasFieldOrPropertyWithValue("user", orderSaved.getUser());
				})
				.noneSatisfy(order -> {
					assertThat(order)
						.hasFieldOrPropertyWithValue("id", anotherOrderWithDifferentFase.getId());
				});
		}
	}
	
	@Nested
	class CreateNewOrder {
		@Test
		void givenOrderRequest_WhenCreateNewOrder_ThenShouldReturnOrderDto() {
			var item1 = new OrderItemRequest(1L, 2);
			var item2 = new OrderItemRequest(2L, 5);
			
			var userRequest = new UserRequest(orderUserSaved.getId(), 
					new Cpf(orderUserSaved.getCpf()), 
					new Email(orderUserSaved.getEmail()));
			
			var orderRequest = new OrderRequest(List.of(item1, item2), userRequest);
			assertThat(orderDatabaseAdapter.createNewOrder(orderRequest))
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", OrderStep.ORDER)
				.hasFieldOrPropertyWithValue("fase", OrderFase.REGISTERED)
				.hasFieldOrPropertyWithValue("user.id", userRequest.getId())
				.hasFieldOrPropertyWithValue("user.cpf", userRequest.getCpf().toString())
				.hasFieldOrPropertyWithValue("user.email", userRequest.getEmail().toString());
		}
	}
	
	@Nested
	class UpdateStepAndFase {
		@Test
		void givenExistantOrder_WhenUpdateStepAndFase_ThenShouldReturnUpdatedOrder() {
			var newStep = OrderStep.DELIVERY;
			var newFase = OrderFase.CONFIRMED;
			var orderDto = orderDatabaseAdapter.updateStepAndFase(orderSaved.getId(), newStep, newFase);
			assertThat(orderDto)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", newStep)
				.hasFieldOrPropertyWithValue("fase", newFase)
				.hasFieldOrPropertyWithValue("user.id", orderDto.getUser().getId())
				.hasFieldOrPropertyWithValue("user.cpf", orderDto.getUser().getCpf())
				.hasFieldOrPropertyWithValue("user.email", orderDto.getUser().getEmail());
		}
	}
	
	@Nested
	class UpdateFase {
		@Test
		void givenExistantOrder_WhenUpdateFase_ThenShouldReturnUpdatedOrder() {
			var newFase = OrderFase.CONFIRMED;
			var orderDto = orderDatabaseAdapter.updateFase(orderSaved.getId(), newFase);
			assertThat(orderDto)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
				.hasFieldOrPropertyWithValue("fase", newFase)
				.hasFieldOrPropertyWithValue("user.id", orderDto.getUser().getId())
				.hasFieldOrPropertyWithValue("user.cpf", orderDto.getUser().getCpf())
				.hasFieldOrPropertyWithValue("user.email", orderDto.getUser().getEmail());
		}
	}
	
	@Nested
	class UpdateOrderItems {
		@Test
		void givenExistantOrder_WhenUpdateOrderItems_ThenShouldReturnUpdatedOrder() {
			var newOrderItem1 = new OrderItemRequest(3L, 1);
			var newOrderItem2 = new OrderItemRequest(3L, 2);
			var orderDto = orderDatabaseAdapter
					.updateOrderItems(orderSaved.getId(), List.of(newOrderItem1, newOrderItem2));
			assertThat(orderDto)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
				.hasFieldOrPropertyWithValue("fase", orderSaved.getFase());
			// FIXME: adicionar items ao Order e ao mapper.toOrder
		}
	}
	
	@Nested
	class DeleteAllItemsByOrderId {
		@Test
		void givenExistantOrder_WhenDeleteAllItemsByOrderId_ThenShouldReturnUpdatedOrder() {
			orderDatabaseAdapter.deleteAllItemsByOrderId(orderSaved.getId());
			
			var existantOrder = orderRepository.findById(orderSaved.getId());
			assertThat(existantOrder).isPresent().get()
				.hasFieldOrPropertyWithValue("id", orderSaved.getId())
				.hasFieldOrPropertyWithValue("step", orderSaved.getStep())
				.hasFieldOrPropertyWithValue("fase", orderSaved.getFase());
			// TODO: .hasFieldOrPropertyWithValue("items", ?);
		}
	}
	
	@Nested
	class DeleteById {
		@Test
		void givenExistantOrder_WhenDeleteById_ThenShouldDeleteSuccessfully() {
			orderDatabaseAdapter.deleteById(orderSaved.getId());
			
			assertThat(orderRepository.findById(orderSaved.getId())).isNotPresent();
		}
		
		@Test
		void givenUnexistantOrder_WhenDeleteById_ThenShouldDeleteSuccessfully() {
			String unexistantId = "999L";
			assertThrows(OrderNotFoundException.class, () -> orderDatabaseAdapter.deleteById(unexistantId));
		}
	}
}
