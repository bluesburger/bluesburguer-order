package br.com.bluesburguer.order.domain.service;

import java.util.List;
import java.util.Optional;

import br.com.bluesburguer.order.application.dto.user.UserRequest;
import br.com.bluesburguer.order.domain.entity.Cpf;
import br.com.bluesburguer.order.domain.entity.Email;
import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;

public interface UserPort {

	List<OrderUserEntity> findAll();
	
	Optional<OrderUserEntity> findById(Long id);
	
	OrderUserEntity saveIfNotExist(UserRequest userRequest);
	
	OrderUserEntity saveIfNotExist(Cpf cpf, Email email);
}
