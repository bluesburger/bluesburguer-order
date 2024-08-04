package br.com.bluesburguer.order.infra.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

	List<OrderEntity> findAllByStepAndFaseInOrderByCreatedTime(OrderStep step, List<OrderFase> fases);
	
	@Modifying
	@Transactional
	@Query(value = "delete from OrderEntity o where o.ID = ?1")
	void deleteById(String id);
}
