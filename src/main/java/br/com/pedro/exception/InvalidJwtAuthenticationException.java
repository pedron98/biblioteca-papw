package br.com.pedro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.JwtException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJwtAuthenticationException extends JwtException {

	private static final long serialVersionUID = 1L;
	
	public InvalidJwtAuthenticationException(String message) {
		super(message);
	}
}
