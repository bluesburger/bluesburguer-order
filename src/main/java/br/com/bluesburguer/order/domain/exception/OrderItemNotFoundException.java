package br.com.bluesburguer.order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Item do pedido não encontrado")
@NoArgsConstructor
public class OrderItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4350600952610627028L;
}
