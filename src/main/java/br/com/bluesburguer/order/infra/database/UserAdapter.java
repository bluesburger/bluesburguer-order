package br.com.bluesburguer.order.infra.database;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.application.dto.user.UserRequest;
import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.domain.service.UserPort;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class UserAdapter implements UserPort {

	private final UserRepository userRepository;
	
	@Override
	public List<OrderUserEntity> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public Optional<OrderUserEntity> findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Override
	public OrderUserEntity saveIfNotExist(UserRequest userRequest) {
		return saveIfNotExist(userRequest.getCpf(), userRequest.getEmail());
	}
	
	@Override
	public OrderUserEntity saveIfNotExist(Cpf cpf, Email email) {
		
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
	
	private OrderUserEntity createUser(Cpf cpf, Email email) {
		var newUser = new OrderUserEntity();
		newUser.setCpf(Optional.ofNullable(cpf).map(Cpf::getValue).orElse(null));
		newUser.setEmail(Optional.ofNullable(email).map(Email::getValue).orElse(null));
		return userRepository.save(newUser);
	}
}
