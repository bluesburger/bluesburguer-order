package br.com.bluesburguer.order.adapters.in.order.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemDto;
import br.com.bluesburguer.order.adapters.in.user.dto.UserDto;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OrderDto {

	private Long id;
	
	private OrderStep step;
	
	@NotNull
	private OrderFase fase;
	
	private List<OrderItemDto> items = new ArrayList<>();
	
	private UserDto user;
}
