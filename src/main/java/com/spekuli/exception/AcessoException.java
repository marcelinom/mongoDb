package com.spekuli.exception;

public class AcessoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AcessoException(String errorCode) {
		super(errorCode);
	}
}
