package br.com.bluesburguer.order.adapters.in.order;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.adapters.in.order.dto.OrderMapper;
import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.in.order.utils.ResponseUtils;
import br.com.bluesburguer.order.adapters.out.OrderFailToCreateException;
import br.com.bluesburguer.order.adapters.out.OrderNotFoundException;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.core.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestResource {
	
	private final OrderService orderService;
	
	private final OrderMapper orderMapper;
	
	@GetMapping
	public List<OrderDto> getAll() {
		return orderService.getAll().stream()
			.map(orderMapper::to)
			.toList();
	}

	@GetMapping("/{orderId}")
	public OrderDto getById(@PathVariable Long orderId) {
		return orderService.getById(orderId)
			.map(orderMapper::to)
			.orElseThrow(OrderNotFoundException::new);
	}
	
	@GetMapping("/step/{step}")
	public List<OrderDto> getByStep(@PathVariable OrderStep step, 
			@RequestParam(required = false) List<OrderFase> fase) {
		return orderService.getAllByStep(step, fase).stream()
				.map(orderMapper::to)
				.toList();
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<URI> createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
		return orderService.createNewOrder(orderRequest)
				.map(orderMapper::to)
				.map(dto -> {
					var orderId = String.valueOf(dto.getId());
					URI location = URI.create(orderId);
					return ResponseUtils.<URI>created(location);
				})
				.orElseThrow(OrderFailToCreateException::new);
	}
	
	@PutMapping(value = "/{orderId}")
	public OrderDto updateOrderItems(@PathVariable Long orderId, @Valid @RequestBody List<OrderItemRequest> orderItems) {
		return orderService.update(orderId, orderItems)
				.map(orderMapper::to)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@PutMapping(value = "/{orderId}/{step}/{fase}")
	public OrderDto updateStepAndFase(@PathVariable Long orderId, @PathVariable OrderStep step, @PathVariable OrderFase fase) {
		return orderService.updateStepAndFase(orderId, step, fase)
				.map(orderMapper::to)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@PutMapping(value = "/{orderId}/{fase}")
	public OrderDto updateFase(@PathVariable Long orderId, @PathVariable OrderFase fase) {
		return orderService.updateFase(orderId, fase)
				.map(orderMapper::to)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteById(@PathVariable Long orderId) {
		orderService.deleteById(orderId);
		return ResponseEntity.noContent().build();
	}
}
