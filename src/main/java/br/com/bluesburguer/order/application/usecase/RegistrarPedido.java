package br.com.bluesburguer.order.application.usecase;

import org.springframework.stereotype.Service;

import br.com.bluesburguer.order.application.dto.order.OrderDto;
import br.com.bluesburguer.order.application.dto.order.OrderRequest;
import br.com.bluesburguer.order.domain.exception.OrderItemNotFoundException;
import br.com.bluesburguer.order.domain.repository.IOrderDatabaseAdapter;
import br.com.bluesburguer.order.domain.repository.IOrderItemDatabaseAdapter;
import br.com.bluesburguer.order.domain.usecase.RegistrarPedidoUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarPedido implements RegistrarPedidoUseCase {
	
	private final IOrderDatabaseAdapter orderDatabaseAdapter;
	private final IOrderItemDatabaseAdapter orderItemDatabaseAdapter;

	@Override
	public OrderDto executar(OrderRequest orderRequest) {
		orderRequest.getItems().stream()
			.forEach(item -> {
				if (orderItemDatabaseAdapter.getItemById(item.getId()) == null) {
					throw new OrderItemNotFoundException();
				}
			});
		
		return orderDatabaseAdapter.createNewOrder(
				new OrderRequest(orderRequest.getItems(), orderRequest.getUser()));
	}
}
