package br.com.bluesburguer.orderingsystem.order.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.orderingsystem.order.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByCpf(String cpf);
	
	Optional<User> findByEmail(String email);

	Optional<User> findByCpfEqualsOrEmailEquals(String cpf, String email);
}
