package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesburguer.order.adapters.out.persistence.entities.Order;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;

public interface OrderRepository extends JpaRepository<Order, String> {

	List<Order> findAllByStepAndFaseInOrderByCreatedTime(OrderStep step, List<OrderFase> fases);
	
	@Modifying
	@Transactional
	@Query(value = "delete from Order o where o.id = ?1")
	void deleteById(String id);
}
