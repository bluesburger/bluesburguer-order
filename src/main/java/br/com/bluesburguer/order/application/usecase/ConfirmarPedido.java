package br.com.bluesburguer.order.application.usecase;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.domain.usecase.ConfirmarPedidoUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmarPedido implements ConfirmarPedidoUseCase {

	@Override
	public void executar(String codigoPedido) {
		throw new NotImplementedException();
	}

}
