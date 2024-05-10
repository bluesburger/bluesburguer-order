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
		
		Optional<OrderUser> userOptional = Optional.empty();
		if (Objects.nonNull(cpf)) {
			userOptional = userRepository.findByCpf(cpf.getValue());
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		
		if (userOptional.isEmpty() && Objects.nonNull(email)) {
			userOptional = userRepository.findByEmail(email.getValue());
			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}
		return createIdentifiedUser(cpf, email);
	}
	
	@Override
	public OrderUser createIdentifiedUser(Cpf cpf, Email email) {
		var newUser = new OrderUser();
		newUser.setCpf(cpf.getValue());
		newUser.setEmail(email.getValue());
		return userRepository.save(newUser);
	}

	@Override
	public OrderUser createAnonymous() {
		var newUser = new OrderUser();
		return userRepository.save(newUser);
	}
}