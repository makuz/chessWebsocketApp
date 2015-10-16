package com.chessApp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LogInValidationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public LogInValidationException(String msg) {
		super(msg);
	}

}
