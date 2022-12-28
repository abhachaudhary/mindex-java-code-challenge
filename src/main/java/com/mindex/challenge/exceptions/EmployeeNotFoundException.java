package com.mindex.challenge.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
	
	public EmployeeNotFoundException(String errorMsg) {
		super(errorMsg);
	}

}
