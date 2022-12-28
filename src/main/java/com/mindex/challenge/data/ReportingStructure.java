package com.mindex.challenge.data;

public class ReportingStructure {
	
	private Employee employee;
	private Integer numberOfReports;
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public Employee getEmployee() {
		return this.employee;
	}
	
	public void setNumberOfReports(Integer numberOfReports) {
		this.numberOfReports = numberOfReports;
	}
	
	public Integer getNumberOfReports() {
		return this.numberOfReports;
	}

}
