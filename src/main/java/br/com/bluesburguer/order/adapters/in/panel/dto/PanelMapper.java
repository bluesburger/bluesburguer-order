package br.com.bluesburguer.order.adapters.in.panel.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.core.domain.OrderStep;

@Component
public class PanelMapper {

	public PanelDto to(List<OrderDto> orderList) {
		Map<OrderStep, List<OrderDto>> mapOrdersByStep = orderList.stream()
				.collect(Collectors.groupingBy(OrderDto::getStep, Collectors.toCollection(ArrayList<OrderDto>::new)));

		return new PanelDto(mapOrdersByStep);
	}
}
