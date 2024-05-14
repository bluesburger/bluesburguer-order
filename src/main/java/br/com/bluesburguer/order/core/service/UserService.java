package br.com.bluesburguer.order.core.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.adapters.out.persistence.repository.UserRepository;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.ports.UserPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class UserService implements UserPort {

	private final UserRepository userRepository;
	
	@Override
	public List<OrderUser> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public Optional<OrderUser> findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Override
	public OrderUser saveIfNotExist(UserRequest userRequest) {
		return saveIfNotExist(userRequest.getCpf(), userRequest.getEmail());
	}
	
	@Override
	public OrderUser saveIfNotExist(Cpf cpf, Email email) {
		
		if (Objects.nonNull(cpf)) {
			var userOptional = userRepository.findFirstByCpfOrderByIdAsc(cpf.getValue());
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		
		if (Objects.nonNull(email)) {
			var userOptional = userRepository.findFirstByEmailOrderByIdAsc(email.getValue());
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		return createUser(cpf, email);
	}
	
	private OrderUser createUser(Cpf cpf, Email email) {
		var newUser = new OrderUser();
		newUser.setCpf(Optional.ofNullable(cpf).map(Cpf::getValue).orElse(null));
		newUser.setEmail(Optional.ofNullable(email).map(Email::getValue).orElse(null));
		return userRepository.save(newUser);
	}
}
