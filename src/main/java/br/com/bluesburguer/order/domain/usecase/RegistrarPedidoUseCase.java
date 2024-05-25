package br.com.bluesburguer.order.domain.usecase;

import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;

public interface RegistrarPedidoUseCase {

	OrderDto executar(OrderRequest orderRequest);
}
