package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	List<Order> findAllByStepAndFaseInOrderByCreatedTime(OrderStep step, List<OrderFase> fases);
	
}
