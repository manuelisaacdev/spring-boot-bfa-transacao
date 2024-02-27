package com.bfa.transacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class JWTServiceException extends RuntimeException {
	public JWTServiceException(String message) {
		super(message);
	}
	
	public JWTServiceException(Throwable throwable) {
		super(throwable);
	}
}
