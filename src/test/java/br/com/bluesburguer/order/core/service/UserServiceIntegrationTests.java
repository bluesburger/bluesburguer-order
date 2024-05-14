package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderItemRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.OrderRepository;
import br.com.bluesburguer.order.adapters.out.persistence.repository.UserRepository;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.UserMocks;

class UserServiceIntegrationTests extends ApplicationIntegrationSupport {
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}
	
	@Nested
	class FindAll {
	
		@Test
		void givenExistantUserWithSameCpf_WhenFindAll_ThenShouldReturnListOfAllUsers() {
			Cpf cpf = UserMocks.cpf();
			Email email = null;
			assertThat(userService.saveIfNotExist(cpf, email)).isNotNull();
			
			var list = userService.findAll();
			assertThat(list)
				.hasSizeGreaterThanOrEqualTo(1)
				.filteredOn(user -> cpf.getValue().equals(user.getCpf()))
				.first()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrProperty("email")
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenExistantUserWithSameEmail_WhenFindAll_ThenShouldReturnListOfAllUsers() {
			Cpf cpf = null;
			Email email = UserMocks.email();
			assertThat(userService.saveIfNotExist(cpf, email)).isNotNull();
			
			var list = userService.findAll();
			assertThat(list)
				.hasSizeGreaterThanOrEqualTo(1)
				.filteredOn(user -> email.getValue().equals(user.getEmail()))
				.first()
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("cpf")
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
	}
	
	@Nested
	class FindById {
		
		@Test
		void givenExistantUser_WhenFindById_ThenShouldReturnOptionalOfIt() {
			Cpf cpf = new Cpf("955.083.240-62");
			Email email = UserMocks.email();
			var savedUser = userService.saveIfNotExist(cpf, email);
			assertThat(savedUser).isNotNull();
			
			assertThat(userService.findById(savedUser.getId()))
				.isPresent()
				.get()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenUnexistantUser_WhenFindById_ThenShouldReturnOptionalEmpty() {
			var id = new Random().nextLong(1612L);
			assertThat(userService.findById(id))
				.isNotPresent();
		}
	}
	
	@Nested
	class SaveIfNotExists {
		
		@Test
		void givenNewUserRequestWithCpf_WhenSaveIfNotExists_ThenShouldCreate() {
			Cpf cpf = UserMocks.cpf();
			var savedUser = userService.saveIfNotExist(new UserRequest(null, cpf, null));
			
			assertThat(savedUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenNewUserRequest_WhenHaveEmail_ThenShouldCreate() {
			Email email = UserMocks.email();
			var savedUser = userService.saveIfNotExist(new UserRequest(null, null, email));
			
			assertThat(savedUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenNewUserRequest_WhenHaveNotCpfOrEmail_ThenShouldCreateAnonimousUser() {
			var savedUser = userService.saveIfNotExist(new UserRequest(null, null, null));
			
			assertThat(savedUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameCpf_ThenShouldReturnIt() {
			Cpf cpf = UserMocks.cpf();
			
			assertThat(userService.saveIfNotExist(new UserRequest(null, cpf, null)))
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
			
			var existantUser = userService.saveIfNotExist(new UserRequest(null, cpf, null));
			assertThat(existantUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameEmail_ThenShouldReturnIt() {
			Cpf cpf = null;
			Email email = UserMocks.email();
			
			assertThat(userService.saveIfNotExist(new UserRequest(null, cpf, email)))
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
			
			var existantUser = userService.saveIfNotExist(new UserRequest(null, cpf, email));
			assertThat(existantUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
	}
}
