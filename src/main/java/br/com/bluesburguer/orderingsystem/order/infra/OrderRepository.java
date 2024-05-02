package br.com.bluesburguer.orderingsystem.order.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.orderingsystem.domain.Fase;
import br.com.bluesburguer.orderingsystem.domain.Step;
import br.com.bluesburguer.orderingsystem.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByStepAndFaseIn(Step step, List<Fase> fases);
	
}
