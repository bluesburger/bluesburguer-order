package br.com.bluesburguer.order.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.order.application.dto.user.UserDto;
import br.com.bluesburguer.order.application.dto.user.UserMapper;
import br.com.bluesburguer.order.domain.exception.UserNotFoundException;
import br.com.bluesburguer.order.infra.database.UserAdapter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserMapper userAssembler;
	
	private final UserAdapter userService;	

	@GetMapping
	public List<UserDto> getAll() {
		return userService.findAll().stream()
			.map(userAssembler::to)
			.toList();
	}
	
	@GetMapping("/{id}")
	public UserDto getById(@PathVariable Long id) {
		return userService.findById(id)
			.map(userAssembler::to)
			.orElseThrow(UserNotFoundException::new);
	}
}
