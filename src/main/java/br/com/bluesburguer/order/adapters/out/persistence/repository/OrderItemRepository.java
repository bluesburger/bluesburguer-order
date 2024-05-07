package br.com.bluesburguer.order.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.order.adapters.out.persistence.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	void deleteAllByOrderId(Long orderId);
}
