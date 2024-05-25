package br.com.bluesburguer.order.infra.database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.infra.database.entity.OrderUserEntity;

public interface UserRepository extends JpaRepository<OrderUserEntity, Long> {

	Optional<OrderUserEntity> findFirstByCpfOrderByIdAsc(String cpf);
	
	Optional<OrderUserEntity> findFirstByEmailOrderByIdAsc(String email);

	Optional<OrderUserEntity> findFirstByCpfEqualsOrEmailEquals(String cpf, String email);
}
