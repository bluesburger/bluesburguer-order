package br.com.bluesburguer.order.application.controller.utils;

import java.net.URI;

import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {
	public static <T> ResponseEntity<T> created(URI location){
		return ResponseEntity.created(location).build();
	}	
}
