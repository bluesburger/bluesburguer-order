package br.com.bluesburguer.order.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.domain.exception.UserNotFoundException;
import br.com.bluesburguer.order.infra.database.UserAdapter;
import br.com.bluesburguer.order.infra.database.UserRepository;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import br.com.bluesburguer.order.support.ApplicationIntegrationSupport;
import br.com.bluesburguer.order.support.UserMocks;

class UserControllerIntegrationTests extends ApplicationIntegrationSupport {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAdapter userService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}
	
	@Nested
	class GetAll {
		
		@Test
		void givenExistantUser_WhenGetAll_ThenShouldReturnIt() throws Exception {
			var savedUser = saveNewUser(UserMocks.cpf(), UserMocks.email());
			
			mockMvc
				.perform(get("/api/user"))
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$").isArray())
			    .andExpect(jsonPath("$.[0].id", is(savedUser.getId().intValue())))
			    .andExpect(jsonPath("$.[0].cpf", is(savedUser.getCpf())))
			    .andExpect(jsonPath("$.[0].email", is(savedUser.getEmail())));
		}

		@Test
		void givenNoUsers_WhenGetAll_ThenShouldReturnEmptyList() throws Exception {
			mockMvc
				.perform(get("/api/user"))
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$").isArray())
			    .andExpect(jsonPath("$").isEmpty());
		}
	}
	
	@Nested
	class GetById {
		@Test
		void givenExistantUser_WhenGetById_ThenShouldReturnIt() throws Exception {
			var savedUser = saveNewUser(UserMocks.cpf(), UserMocks.email());
			
			mockMvc
				.perform(get("/api/user/{id}", savedUser.getId()))
			    .andExpect(status().isOk())
			    .andExpect(jsonPath("$.id", is(savedUser.getId().intValue())))
			    .andExpect(jsonPath("$.cpf", is(savedUser.getCpf())))
			    .andExpect(jsonPath("$.email", is(savedUser.getEmail())));
		}

		@Test
		void givenNoUsers_WhenGetById_ThenShouldReturnEmptyList() throws Exception {
			mockMvc
				.perform(get("/api/user/{id}", 99))
			    .andExpect(status().isNotFound())
			    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserNotFoundException.class))
			    .andExpect(status().reason("Usuário não encontrado"));
		}
	}
	
	private OrderUserEntity saveNewUser(Cpf cpf, Email email) {
		var newUser = userService.saveIfNotExist(cpf, email);
		
		assertThat(newUser)
			.hasFieldOrProperty("id")
			.hasFieldOrPropertyWithValue("cpf", cpf.getValue())
			.hasFieldOrPropertyWithValue("email", email.getValue())
			.hasFieldOrProperty("creationDateTime")
			.hasFieldOrProperty("orders");
		
		return userRepository.save(newUser);
	}
}
