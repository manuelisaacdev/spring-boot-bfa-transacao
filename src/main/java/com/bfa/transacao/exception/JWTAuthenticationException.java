package com.bfa.transacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JWTAuthenticationException extends AuthenticationException {

	public JWTAuthenticationException(String msg) {
		super(msg);
	}
	
	public JWTAuthenticationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
