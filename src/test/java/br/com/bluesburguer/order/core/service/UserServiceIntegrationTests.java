package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.repository.UserRepository;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.UserMocks;

class UserServiceIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}
	
	@Nested
	class FindAll {
	
		@Test
		void givenExistantUsers_WhenFindAll_ThenShouldReturnListOfAllUsers() {
			Cpf cpf = UserMocks.cpf();
			Email email = UserMocks.email();
			assertThat(userService.saveIfNotExist(cpf, email)).isNotNull();
			
			assertThat(userService.findAll())
				.hasSize(1)
				.first()
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
	}
	
	@Nested
	class FindById {
		
		@Test
		void givenExistantUser_WhenFindById_ThenShouldReturnOptionalOfIt() {
			Cpf cpf = UserMocks.cpf();
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
			assertThat(userService.findById(99L))
				.isNotPresent();
		}
	}
	
	@Nested
	class SaveIfNotExists {
		
		@Test
		void givenNewUserRequestWithCpf_WhenSaveIfNotExists_ThenShouldCreate() {
			Cpf cpf = UserMocks.cpf();
			Email email = null;
			var savedUser = userService.saveIfNotExist(new UserRequest(null, cpf, email));
			
			assertThat(savedUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenNewUserRequest_WhenHaveEmail_ThenShouldCreate() {
			Cpf cpf = null;
			Email email = UserMocks.email();
			var savedUser = userService.saveIfNotExist(new UserRequest(null, cpf, email));
			
			assertThat(savedUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenNewUserRequest_WhenHaveNotCpfOrEmail_ThenShouldCreateAnonimousUser() {
			Cpf cpf = null;
			Email email = null;
			var savedUser = userService.saveIfNotExist(new UserRequest(null, cpf, email));
			
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
			Email email = null;
			
			assertThat(userService.saveIfNotExist(new UserRequest(null, cpf, email)))
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
			
			var existantUser = userService.saveIfNotExist(new UserRequest(null, cpf, email));
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
	
	@Nested
	class CreateUser {
		
		@Test
		void givenOnlyCpf_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithCpf() {
			Cpf cpf = UserMocks.cpf();
			Email email = null;
			
			var createdUser = userService.createUser(cpf, email);
			
			assertThat(createdUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenOnlyEmail_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithEmail() {
			Cpf cpf = null;
			Email email = UserMocks.email();
			
			var createdUser = userService.createUser(cpf, email);
			
			assertThat(createdUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenCpfAndEmail_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithCpfAndEmail() {
			Cpf cpf = UserMocks.cpf();
			Email email = UserMocks.email();
			
			var createdUser = userService.createUser(cpf, email);
			
			assertThat(createdUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
		
		@Test
		void givenNullParams_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithNullCpfAndNullEmail() {
			Cpf cpf = null;
			Email email = null;
			
			var createdUser = userService.createUser(cpf, email);
			
			assertThat(createdUser)
				.hasFieldOrProperty("id")
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("creationDateTime")
				.hasFieldOrProperty("orders");
		}
	}
}
