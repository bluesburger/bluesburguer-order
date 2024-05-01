package br.com.bluesburguer.orderingsystem.order.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.orderingsystem.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
