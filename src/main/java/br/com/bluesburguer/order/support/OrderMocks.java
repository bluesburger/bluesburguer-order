package br.com.bluesburguer.order.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMocks {
	
	public static String mockCpf() {
		return "997.307.080-10";
	}

	public static String mockEmail() {
		return "email.usuario@server.com";
	}
}
