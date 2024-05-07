package br.com.bluesburguer.order.adapters.in.panel.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.core.domain.OrderStep;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PanelDto {

	private Map<OrderStep, List<OrderDto>> orders;
}
