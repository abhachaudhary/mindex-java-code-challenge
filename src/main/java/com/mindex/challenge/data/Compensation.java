package com.mindex.challenge.data;

import java.time.LocalDate;

public class Compensation {
	
	private String employeeId;
	private double salary;
	private LocalDate effectiveDate;
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getEmployeeId() {
		return this.employeeId;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public double getSalary() {
		return this.salary;
	}
	
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public LocalDate getEffectiveDate() {
		return this.effectiveDate;
	}

}
