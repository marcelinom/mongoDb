package com.spekuli.exception;

public class RegraNegocioException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RegraNegocioException(String errorCode) {
		super(errorCode);
	}

}
