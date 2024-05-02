package br.com.bluesburguer.orderingsystem.order.interfaces.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.orderingsystem.domain.Fase;
import br.com.bluesburguer.orderingsystem.domain.Step;
import br.com.bluesburguer.orderingsystem.order.domain.service.OrderService;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderAssembler;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderDto;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderItemRequest;
import br.com.bluesburguer.orderingsystem.order.interfaces.api.dto.OrderRequest;
import jakarta.validation.Valid;
import kotlin.NotImplementedError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestResource {
	
	private final OrderService orderService;
	
	private final OrderAssembler orderAssembler;
	
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
	public List<OrderDto> getByStep(@PathVariable Step step, @RequestParam List<Fase> fases) {
		log.info("Searching all orders with step {} and fases {}", step, fases);
		throw new NotImplementedError();
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public OrderDto createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
		return Optional.ofNullable(orderService.createNewOrder(orderRequest))
				.map(orderAssembler::to)
				.orElseThrow(() -> new IllegalArgumentException("Impossível salvar novo pedido")); // FIXME: adicionar exceção específica
	}
	
	@PutMapping(value = "/{orderId}")
	public OrderDto updateOrderItems(@PathVariable Long orderId, @Valid @RequestBody List<OrderItemRequest> orderItems) {
		return Optional.ofNullable(orderService.update(orderId, orderItems))
				.map(orderAssembler::to)
				.orElseThrow(() -> new IllegalArgumentException("Impossível salvar novo pedido")); // FIXME: adicionar exceção específica
	}
	
	@PutMapping(value = "/{orderId}/{fase}")
	public OrderDto updateFase(@PathVariable Long orderId, @PathVariable Fase fase) {
		return Optional.ofNullable(orderService.updateFase(orderId, fase))
				.map(orderAssembler::to)
				.orElseThrow(() -> new IllegalArgumentException("Impossível salvar novo pedido")); // FIXME: adicionar exceção específica
	}
	
	@DeleteMapping("/{orderId}")
	public void deleteById(@PathVariable Long orderId) {
		orderService.delete(orderId);
	}
}
