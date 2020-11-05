package com.djoker.ars.exceptions;

public class CustomerException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public CustomerException() {
	}

	public CustomerException(String errorMessage) {
		super("Customer not found " + errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
