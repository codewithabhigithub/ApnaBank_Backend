package com.src.exception;

public class EmployeeAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeAlreadyExistsException(String massege) {
		super(massege);
	}
}
