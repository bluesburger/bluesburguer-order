package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	Optional<OrderItem> findByOrderItemId(long orderItemId);

	@Modifying
	@Transactional
	@Query(value = "delete from OrderItem oi where oi.order.id = ?1")
	void deleteAllByOrderId(String orderId);
}
