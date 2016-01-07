package com.chakannom.demo.exception.exception;

public class DemoExceptionHandlerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DemoExceptionHandlerException(String message) {
		super(message + "not available");
	}

}
