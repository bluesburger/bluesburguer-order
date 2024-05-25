package br.com.bluesburguer.order.application.usecase;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.domain.usecase.CancelarPedidoUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelarPedido implements CancelarPedidoUseCase {

	@Override
	public void executar(String codigoPedido) {
		throw new NotImplementedException();
	}

}
