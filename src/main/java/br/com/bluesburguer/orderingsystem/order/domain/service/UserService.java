package br.com.bluesburguer.orderingsystem.order.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.orderingsystem.order.domain.User;
import br.com.bluesburguer.orderingsystem.order.infra.UserRepository;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class UserService {

	private final UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
	
	public User saveIfNotExist(UserRequest userRequest) {
		return saveIfNotExist(userRequest.getCpf(), userRequest.getEmail());
	}
	
	public User saveIfNotExist(String cpf, String email) {
		
		Optional<User> userOptional = Optional.empty();
		if (Objects.nonNull(cpf)) {
			userOptional = userRepository.findByCpf(cpf);
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		
		if (userOptional.isEmpty() && Objects.nonNull(email)) {
			userOptional = userRepository.findByEmail(email);
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		return createIdentifiedUser(cpf, email);
	}
	
	private User createIdentifiedUser(String cpf, String email) {
		var newUser = new User();
		newUser.setCpf(cpf);
		newUser.setEmail(email);
		return userRepository.save(newUser);
	}
}
