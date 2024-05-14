package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;

public interface UserRepository extends JpaRepository<OrderUser, Long> {

	Optional<OrderUser> findFirstByCpf(String cpf);
	
	Optional<OrderUser> findFirstByEmail(String email);

	Optional<OrderUser> findFirstByCpfEqualsOrEmailEquals(String cpf, String email);
}
