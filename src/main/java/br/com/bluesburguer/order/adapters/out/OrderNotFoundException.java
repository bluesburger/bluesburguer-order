package br.com.bluesburguer.order.adapters.out;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderNotFoundException extends ResponseStatusException {

	private static final long serialVersionUID = -4350600952610627028L;
	
	public OrderNotFoundException() {
		super(HttpStatus.NOT_FOUND, "Pedido não encontrado");
	}
	
	public OrderNotFoundException(String funcao) {
		super(HttpStatus.NOT_FOUND, String.format("Impossível %s pedido. Pedido não encontrado", funcao));
	}
}
