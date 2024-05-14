package br.com.bluesburguer.order.ports;

import java.util.List;
import java.util.Optional;

import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;

public interface UserPort {

	List<OrderUser> findAll();
	
	Optional<OrderUser> findById(Long id);
	
	OrderUser saveIfNotExist(UserRequest userRequest);
	
	OrderUser saveIfNotExist(Cpf cpf, Email email);
}
