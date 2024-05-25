package br.com.bluesburguer.order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Usuário não encontrado")
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4039534595214046018L;

}
