package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderUser;

public interface UserRepository extends JpaRepository<OrderUser, Long> {

	Optional<OrderUser> findByCpf(String cpf);
	
	Optional<OrderUser> findByEmail(String email);

	Optional<OrderUser> findByCpfEqualsOrEmailEquals(String cpf, String email);
}
