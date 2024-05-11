package br.com.bluesburguer.order.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.adapters.out.persistence.repository.UserRepository;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.support.UserMocks;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Nested
	class FindAll {
	
		@Test
		void givenExistantUsers_WhenFindAll_ThenShouldReturnListOfAllUsers() {
			
			var existantUsers = List.of(UserMocks.user(1L), UserMocks.user(2L));
			
			doReturn(existantUsers)
				.when(userRepository).findAll()	;
			
			assertThat(userService.findAll())
				.hasSize(existantUsers.size())
				.containsAll(existantUsers);
			
			verify(userRepository).findAll();
		}
	}
	
	@Nested
	class FindById {
		
		@Test
		void givenExistantUser_WhenFindById_ThenShouldReturnOptionalOfIt() {
			long userId = 1L;
			var existantUser = UserMocks.user(userId);
			
			doReturn(Optional.of(existantUser))
				.when(userRepository).findById(userId);
			
			assertThat(userService.findById(userId))
				.isPresent()
				.get()
				.isEqualTo(existantUser);
			
			verify(userRepository).findById(userId);
		}
		
		@Test
		void givenUnexistantUser_WhenFindById_ThenShouldReturnOptionalEmpty() {
			long userId = 2L;
			
			doReturn(Optional.empty())
				.when(userRepository).findById(userId);
			
			assertThat(userService.findById(userId))
				.isNotPresent();
			
			verify(userRepository).findById(userId);
		}
	}
	
	@Nested
	class SaveIfNotExists {
		
		@Test
		void givenNewUserRequestWithCpf_WhenSaveIfNotExists_ThenShouldCreate() {
			long userId = 1L;
			Cpf cpf = UserMocks.cpf();
			Email email = null;
			
			doReturn(Optional.empty())
				.when(userRepository).findByCpf(cpf.getValue());
			
			var savedUser = new OrderUser(userId, cpf.getValue(), null, LocalDateTime.now(), new ArrayList<>());
			
			OrderUser newUser = new OrderUser();
			newUser.setCpf(cpf.getValue());
			newUser.setEmail(null);
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			var optionalUser = userService.saveIfNotExist(cpf, email);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", email)
				.hasFieldOrProperty("orders");
			
			verify(userRepository).findByCpf(cpf.getValue());
			verify(userRepository, never()).findByEmail(anyString());
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenNewUserRequest_WhenHaveEmail_ThenShouldCreate() {
			long userId = 1L;
			Cpf cpf = null;
			Email email = UserMocks.email();
			
			doReturn(Optional.empty())
				.when(userRepository).findByEmail(email.getValue());
			
			var savedUser = new OrderUser(userId, null, email.getValue(), LocalDateTime.now(), new ArrayList<>());
			
			OrderUser newUser = new OrderUser();
			newUser.setCpf(null);
			newUser.setEmail(email.getValue());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			var optionalUser = userService.saveIfNotExist(cpf, email);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("orders");
			
			verify(userRepository, never()).findByCpf(anyString());
			verify(userRepository).findByEmail(email.getValue());
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenNewUserRequest_WhenHaveNotCpfOrEmail_ThenShouldCreateAnonimousUser() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = null;
			
			var savedUser = new OrderUser(userId, null, null, LocalDateTime.now(), new ArrayList<>());
			
			OrderUser newUser = new OrderUser();
			newUser.setCpf(null);
			newUser.setEmail(null);
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("orders");
			
			verify(userRepository, never()).findByCpf(anyString());
			verify(userRepository, never()).findByEmail(anyString());
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameCpf_ThenShouldReturnIt() {
			long userId = 1L;
			Cpf cpfParam = UserMocks.cpf();
			Email emailParam = null;
			
			var existantUser = UserMocks.user(userId);
			
			doReturn(Optional.of(existantUser))
				.when(userRepository).findByCpf(cpfParam.getValue());
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.isEqualTo(existantUser);
			
			verify(userRepository).findByCpf(cpfParam.getValue());
			verify(userRepository, never()).findByEmail(anyString());
			verify(userRepository, never()).save(any(OrderUser.class));
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameEmail_ThenShouldReturnIt() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = UserMocks.email();
			
			var existantUser = UserMocks.user(userId);
			
			doReturn(Optional.of(existantUser))
				.when(userRepository).findByEmail(emailParam.getValue());
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.isEqualTo(existantUser);
			
			verify(userRepository, never()).findByCpf(anyString());
			verify(userRepository).findByEmail(emailParam.getValue());
			verify(userRepository, never()).save(any(OrderUser.class));
		}
	}
	
	@Nested
	class CreateUser {
		
		@Test
		void givenOnlyCpf_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithCpf() {
			long userId = 1L;
			Cpf cpfParam = UserMocks.cpf();
			Email emailParam = null;
			
			var savedUser = new OrderUser(userId, cpfParam.getValue(), null, LocalDateTime.now(), new ArrayList<>());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			assertThat(userService.createUser(cpfParam, emailParam))
				.isEqualTo(savedUser);
			
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenOnlyEmail_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithEmail() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = UserMocks.email();
			
			var savedUser = new OrderUser(userId, null, emailParam.getValue(), LocalDateTime.now(), new ArrayList<>());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			assertThat(userService.createUser(cpfParam, emailParam))
				.isEqualTo(savedUser);
			
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenCpfAndEmail_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithCpfAndEmail() {
			long userId = 1L;
			Cpf cpfParam = UserMocks.cpf();
			Email emailParam = UserMocks.email();
			
			var savedUser = new OrderUser(userId, cpfParam.getValue(), emailParam.getValue(), LocalDateTime.now(), new ArrayList<>());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			assertThat(userService.createUser(cpfParam, emailParam))
				.isEqualTo(savedUser);
			
			verify(userRepository).save(any(OrderUser.class));
		}
		
		@Test
		void givenNullParams_WhenCreateIdentifiedUser_ThenShouldSaveNewUserWithNullCpfAndNullEmail() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = null;
			
			var savedUser = new OrderUser(userId, null, null, LocalDateTime.now(), new ArrayList<>());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUser.class));
			
			assertThat(userService.createUser(cpfParam, emailParam))
				.isEqualTo(savedUser);
			
			verify(userRepository).save(any(OrderUser.class));
		}
	}
}
