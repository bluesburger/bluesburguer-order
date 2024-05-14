package br.com.bluesburguer.order.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	Optional<OrderItem> findByOrderItemId(long orderItemId);

	void deleteAllByOrderId(String orderId);
}
