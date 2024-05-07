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
	
	private final OrderMapper orderAssembler;
	
	@GetMapping
	public List<OrderDto> getAll() {
		return orderService.getAll().stream()
			.map(orderAssembler::to)
			.toList();
	}

	@GetMapping("/{orderId}")
	public OrderDto getById(@PathVariable Long orderId) {
		return orderService.getById(orderId)
			.map(orderAssembler::to)
			.orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
	}
	
	@GetMapping("/step/{step}")
	public List<OrderDto> getByStep(@PathVariable OrderStep step, 
			@RequestParam(required = false) List<OrderFase> fase) {
		return orderService.getAllByStep(step, fase).stream()
				.map(orderAssembler::to)
				.toList();
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<URI> createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
		return orderService.createNewOrder(orderRequest)
				.map(orderAssembler::to)
				.map(dto -> {
					var orderId = String.valueOf(dto.getId());
					URI location = URI.create(orderId);
					ResponseEntity<URI> build = ResponseEntity.created(location).build();
					return build;
				})
				.orElseThrow(() -> new OrderNotFoundException("salvar"));
	}
	
	@PutMapping(value = "/{orderId}")
	public ResponseEntity<OrderDto> updateOrderItems(@PathVariable Long orderId, @Valid @RequestBody List<OrderItemRequest> orderItems) {
		return orderService.update(orderId, orderItems)
				.map(orderAssembler::to)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new OrderNotFoundException("salvar"));
	}
	
	@PutMapping(value = "/{orderId}/{step}/{fase}")
	public OrderDto updateStepAndFase(@PathVariable Long orderId, @PathVariable OrderStep step, @PathVariable OrderFase fase) {
		return orderService.updateStepAndFase(orderId, step, fase)
				.map(orderAssembler::to)
				.orElseThrow(() -> new OrderNotFoundException("alterar"));
	}
	
	@PutMapping(value = "/{orderId}/{fase}")
	public OrderDto updateFase(@PathVariable Long orderId, @PathVariable OrderFase fase) {
		return orderService.updateFase(orderId, fase)
				.map(orderAssembler::to)
				.orElseThrow(() -> new OrderNotFoundException("alterar"));
	}
	
	@DeleteMapping("/{orderId}")
	public void deleteById(@PathVariable Long orderId) {
		orderService.delete(orderId);
	}
}
