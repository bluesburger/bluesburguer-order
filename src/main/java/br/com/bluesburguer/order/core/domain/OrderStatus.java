package br.com.bluesburguer.order.core.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
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
@Embeddable
public class OrderStatus implements Serializable {
	private static final long serialVersionUID = -1837328577857983314L;

	@NonNull
	private OrderStep step;
	
	@NonNull
	private OrderFase fase;
}
