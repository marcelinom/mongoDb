package com.spekuli.exception;

public class EmailException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EmailException(String errorCode) {
		super(errorCode);
	}

}
