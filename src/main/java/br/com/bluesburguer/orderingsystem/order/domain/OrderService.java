package br.com.bluesburguer.orderingsystem.order.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.orderingsystem.domain.Fase;
import br.com.bluesburguer.orderingsystem.order.infra.OrderItemRepository;
import br.com.bluesburguer.orderingsystem.order.infra.OrderRepository;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * 
 * FIXME: trocar IllegalArgumentException para uma exceção específica, que retorne 404
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class OrderService {

	private final OrderItemRepository orderItemRepository;

	private final OrderRepository orderRepository;

	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> getById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	public Optional<OrderItem> getItemById(Long orderItemId) {
		try {
			return Optional.of(orderItemRepository.getReferenceById(orderItemId));
		} catch (EntityNotFoundException e) {
			log.error("", e);
			return Optional.empty();
		}
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order save(OrderRequest command) {
		var newOrder = new Order();
		newOrder.setFase(Fase.PENDING);
		return orderRepository.save(newOrder);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order updateFase(Long orderId, Fase fase) {
		return getById(orderId)
			.map(order -> {
				order.setFase(fase);
				return orderRepository.save(order);
			}).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public Order update(Long orderId, List<Long> orderItems) {
		return getById(orderId)
				.map(order -> {
					orderItemRepository.deleteAllByOrderId(order.getId());
					orderItems.stream()
						.map(id -> saveItem(id, order))
						.forEach(order::add);
					return order;
				}).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
	}
	
	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem saveItem(Long id, Order order) {
		var item = new OrderItem();
		item.setId(id);
		item.setOrder(order);
		return orderItemRepository.save(item);
	}
	
	private void removeItem(Long id) {
		orderItemRepository.deleteById(id);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public void delete(Long orderId) {
		var order = getById(orderId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
		orderRepository.delete(order);
	}

	@Transactional(
			rollbackOn = IllegalArgumentException.class, 
			dontRollbackOn = EntityExistsException.class)
	public OrderItem addItem(OrderItem item) {
		if (Objects.isNull(item.getOrder())) {
			throw new IllegalArgumentException("Item necessita de uma ordem");
		}
		return orderItemRepository.save(item);
	}
}
