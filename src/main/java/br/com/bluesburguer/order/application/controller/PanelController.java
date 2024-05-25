package br.com.bluesburguer.order.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderMapper;
import br.com.bluesburguer.order.application.dto.panel.PanelDto;
import br.com.bluesburguer.order.application.dto.panel.PanelMapper;
import br.com.bluesburguer.order.domain.entity.OrderFase;
import br.com.bluesburguer.order.domain.entity.OrderStep;
import br.com.bluesburguer.order.infra.database.OrderAdapter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/panel")
@RequiredArgsConstructor
public class PanelController {

	private final OrderAdapter orderService;

	private final OrderMapper orderMapper;
	
	private final PanelMapper panelMapper;

	@GetMapping
	public PanelDto getAll() {
		var orderList = orderService.getAll().stream()
				.map(orderMapper::toOrderDto)
				.toList();
		return panelMapper.to(orderList);
	}
	
	@GetMapping("/{step}")
	public List<OrderDto> getByStep(@PathVariable OrderStep step) {
		return orderService.getAllByStep(step, List.of(OrderFase.values())).stream()
				.map(orderMapper::toOrderDto)
				.toList();
	}
}
