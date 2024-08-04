package br.com.bluesburguer.order.infra.service;

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

import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.infra.database.UserAdapter;
import br.com.bluesburguer.order.infra.database.UserRepository;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import br.com.bluesburguer.order.support.UserMocks;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserAdapter userService;
	
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
				.when(userRepository).findFirstByCpfOrderByIdAsc(cpf.getValue());
			
			var savedUser = new OrderUserEntity(userId, cpf.getValue(), null, LocalDateTime.now(), new ArrayList<>());
			
			OrderUserEntity newUser = new OrderUserEntity();
			newUser.setCpf(cpf.getValue());
			newUser.setEmail(null);
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUserEntity.class));
			
			var optionalUser = userService.saveIfNotExist(cpf, email);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
				.hasFieldOrPropertyWithValue("email", email)
				.hasFieldOrProperty("orders");
			
			verify(userRepository).findFirstByCpfOrderByIdAsc(cpf.getValue());
			verify(userRepository, never()).findFirstByEmailOrderByIdAsc(anyString());
			verify(userRepository).save(any(OrderUserEntity.class));
		}
		
		@Test
		void givenNewUserRequest_WhenHaveEmail_ThenShouldCreate() {
			long userId = 1L;
			Cpf cpf = null;
			Email email = UserMocks.email();
			
			doReturn(Optional.empty())
				.when(userRepository).findFirstByEmailOrderByIdAsc(email.getValue());
			
			var savedUser = new OrderUserEntity(userId, null, email.getValue(), LocalDateTime.now(), new ArrayList<>());
			
			OrderUserEntity newUser = new OrderUserEntity();
			newUser.setCpf(null);
			newUser.setEmail(email.getValue());
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUserEntity.class));
			
			var optionalUser = userService.saveIfNotExist(cpf, email);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", email.getValue())
				.hasFieldOrProperty("orders");
			
			verify(userRepository, never()).findFirstByCpfOrderByIdAsc(anyString());
			verify(userRepository).findFirstByEmailOrderByIdAsc(email.getValue());
			verify(userRepository).save(any(OrderUserEntity.class));
		}
		
		@Test
		void givenNewUserRequest_WhenHaveNotCpfOrEmail_ThenShouldCreateAnonimousUser() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = null;
			
			var savedUser = new OrderUserEntity(userId, null, null, LocalDateTime.now(), new ArrayList<>());
			
			OrderUserEntity newUser = new OrderUserEntity();
			newUser.setCpf(null);
			newUser.setEmail(null);
			doReturn(savedUser)
				.when(userRepository).save(any(OrderUserEntity.class));
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", userId)
				.hasFieldOrPropertyWithValue("cpf", null)
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrProperty("orders");
			
			verify(userRepository, never()).findFirstByCpfOrderByIdAsc(anyString());
			verify(userRepository, never()).findFirstByEmailOrderByIdAsc(anyString());
			verify(userRepository).save(any(OrderUserEntity.class));
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameCpf_ThenShouldReturnIt() {
			long userId = 1L;
			Cpf cpfParam = UserMocks.cpf();
			Email emailParam = null;
			
			var existantUser = UserMocks.user(userId);
			
			doReturn(Optional.of(existantUser))
				.when(userRepository).findFirstByCpfOrderByIdAsc(cpfParam.getValue());
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.isEqualTo(existantUser);
			
			verify(userRepository).findFirstByCpfOrderByIdAsc(cpfParam.getValue());
			verify(userRepository, never()).findFirstByEmailOrderByIdAsc(anyString());
			verify(userRepository, never()).save(any(OrderUserEntity.class));
		}
		
		@Test
		void givenExistantUser_WhenRequestWithSameEmail_ThenShouldReturnIt() {
			long userId = 1L;
			Cpf cpfParam = null;
			Email emailParam = UserMocks.email();
			
			var existantUser = UserMocks.user(userId);
			
			doReturn(Optional.of(existantUser))
				.when(userRepository).findFirstByEmailOrderByIdAsc(emailParam.getValue());
			
			var optionalUser = userService.saveIfNotExist(cpfParam, emailParam);
			assertThat(optionalUser)
				.isNotNull()
				.isEqualTo(existantUser);
			
			verify(userRepository, never()).findFirstByCpfOrderByIdAsc(anyString());
			verify(userRepository).findFirstByEmailOrderByIdAsc(emailParam.getValue());
			verify(userRepository, never()).save(any(OrderUserEntity.class));
		}
	}
}
