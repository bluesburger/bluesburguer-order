package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByStepAndFaseIn(OrderStep step, List<OrderFase> fases);
	
}
