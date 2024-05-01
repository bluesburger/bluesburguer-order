package br.com.bluesburguer.orderingsystem.order.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluesburguer.orderingsystem.order.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	void deleteAllByOrderId(Long orderId);
}
