package br.com.bluesburguer.order.adapters.in.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.order.adapters.in.user.dto.UserMapper;
import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;
import br.com.bluesburguer.order.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestResource {
	
	private final UserMapper userAssembler;
	
	private final UserService userService;	

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
			.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
	}
}
