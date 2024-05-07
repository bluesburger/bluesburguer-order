package br.com.bluesburguer.order.adapters.in.panel;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.adapters.in.order.dto.OrderMapper;
import br.com.bluesburguer.order.adapters.in.panel.dto.PanelDto;
import br.com.bluesburguer.order.adapters.in.panel.dto.PanelMapper;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import br.com.bluesburguer.order.core.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/panel")
@RequiredArgsConstructor
public class PanelRestResource {

	private final OrderService orderService;

	private final OrderMapper orderMapper;
	
	private final PanelMapper panelMapper;

	@GetMapping
	public PanelDto getAll() {
		var orderList = orderService.getAll().stream()
				.map(orderMapper::to)
				.toList();
		return panelMapper.to(orderList);
	}
	
	@GetMapping("/{step}")
	public List<OrderDto> getByStep(@PathVariable OrderStep step) {
		return orderService.getAllByStep(step, List.of(OrderFase.values())).stream()
				.map(orderMapper::to)
				.toList();
	}
}
