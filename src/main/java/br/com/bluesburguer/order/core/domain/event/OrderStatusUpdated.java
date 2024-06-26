package br.com.bluesburguer.order.core.domain.event;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.bluesburguer.order.core.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class OrderStatusUpdated implements Serializable {

	private static final long serialVersionUID = -2138667769166531317L;
	
	@NonNull
	private UUID id;

	@NonNull
	private OrderStatus newStatus;
}
