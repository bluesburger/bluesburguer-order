package br.com.bluesburguer.order.domain.entity;

public enum OrderStep {
	ORDER, // PEDIDO
	CHARGE, // COBRANÇA
	KITCHEN, // COZINHA
	INVOICE, // NF
	DELIVERY; // ENTREGA
}
