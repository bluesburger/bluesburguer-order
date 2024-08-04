package br.com.bluesburguer.order.infra.database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesburguer.order.infra.database.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
	
	Optional<OrderItemEntity> findByOrderItemId(long orderItemId);

	@Modifying
	@Transactional
	@Query(value = "delete from OrderItemEntity oi where oi.order.id = ?1")
	void deleteAllByOrderId(String orderId);
}
