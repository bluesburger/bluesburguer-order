package br.com.bluesburguer.order.application.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

import br.com.bluesburguer.order.application.controller.utils.ResponseUtils;
import br.com.bluesburguer.order.application.dto.item.OrderItemRequest;
import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderMapper;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.domain.exception.OrderFailToCreateException;
import br.com.bluesburguer.order.domain.exception.OrderNotFoundException;
import br.com.bluesburguer.order.infra.database.OrderAdapter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderAdapter orderAdapter;
	
	private final OrderMapper orderMapper;
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "List of orders", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(allOf = OrderDto.class)) })
	})
	@GetMapping
	public List<OrderDto> getAll() {
		return orderAdapter.getAll().stream()
			.map(orderMapper::toOrderDto)
			.toList();
	}

	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Order found with id", 
	    content = {@Content(
	    		mediaType = "application/json", 
	    		schema = @Schema(implementation = OrderDto.class))}),
	  @ApiResponse(responseCode = "404", description = "Order not found",
	  	content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/{orderId}")
	public OrderDto getById(@PathVariable UUID orderId) {
		return orderAdapter.getById(orderId)
			.map(orderMapper::toOrderDto)
			.orElseThrow(OrderNotFoundException::new);
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "List of orders with step", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = OrderDto.class)) })
	})
	@GetMapping("/step/{step}")
	public List<OrderDto> getByStep(@PathVariable OrderStep step, 
			@RequestParam(required = false) List<OrderFase> fase) {
		return orderAdapter.getAllByStep(step, fase).stream()
				.map(orderMapper::toOrderDto)
				.toList();
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "201", description = "URI of created order", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = URI.class)) }),
	  @ApiResponse(responseCode = "400", description = "Fail to create order", 
	    content = { @Content(mediaType = "application/json")})
	})
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<URI> createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
		return orderAdapter.createNewOrder(orderRequest)
				.map(orderMapper::toOrderDto)
				.map(dto -> {
					var orderId = String.valueOf(dto.getId());
					URI location = URI.create(orderId);
					return ResponseUtils.<URI>created(location);
				})
				.orElseThrow(OrderFailToCreateException::new);
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Items of order updated", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = OrderDto.class)) }),
	  @ApiResponse(responseCode = "404", description = "Order not found",
	  	content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(value = "/{orderId}")
	public OrderDto updateOrderItems(@PathVariable UUID orderId, @Valid @RequestBody List<OrderItemRequest> orderItems) {
		return orderAdapter.updateOrderItems(orderId, orderItems)
				.map(orderMapper::toOrderDto)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Step/Fase where updated", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = OrderDto.class)) }),
	  @ApiResponse(responseCode = "404", description = "Order not found",
	  	content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(value = "/{orderId}/{step}/{fase}")
	public OrderDto updateStepAndFase(@PathVariable UUID orderId, @PathVariable OrderStep step, @PathVariable OrderFase fase) {
		return orderAdapter.updateStepAndFase(orderId, step, fase)
				.map(orderMapper::toOrderDto)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Fase of order was updated", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = OrderDto.class)) }),
	  @ApiResponse(responseCode = "404", description = "Order not found",
	  	content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(value = "/{orderId}/{fase}")
	public OrderDto updateFase(@PathVariable UUID orderId, @PathVariable OrderFase fase) {
		return orderAdapter.updateFase(orderId, fase)
				.map(orderMapper::toOrderDto)
				.orElseThrow(OrderNotFoundException::new);
	}
	
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "204", description = "Order were deleted", 
	    content = { @Content(mediaType = "application/json", 
	      schema = @Schema(implementation = OrderDto.class)) })
	})
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteById(@PathVariable UUID orderId) {
		orderAdapter.deleteById(orderId);
		return ResponseEntity.noContent().build();
	}
}
