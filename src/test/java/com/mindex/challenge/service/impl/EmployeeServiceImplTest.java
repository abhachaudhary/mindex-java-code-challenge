package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportStructureUrl;
    private String employeeCompensationUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private static String EMP_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReportStructureUrl = "http://localhost:" + port + "/employee/reporting";
        employeeCompensationUrl = "http://localhost:" + port + "/employee/compensation";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }
    
    @Test
	public void testCreateCompensation() {
    	LocalDate effDate = LocalDate.now();
    	double salary = 30789.67;
		Compensation testComp = new Compensation();
		
		testComp.setEmployeeId(EMP_ID);
		testComp.setEffectiveDate(effDate);
		testComp.setSalary(salary);
		
		HttpStatus status = restTemplate.postForEntity(employeeCompensationUrl, testComp, Compensation.class).getStatusCode();
		assertEquals(status, HttpStatus.OK);
				
		Compensation readComp = restTemplate.getForEntity(employeeCompensationUrl + "?employeeId=" + EMP_ID, Compensation.class).getBody();
		assertEquals(EMP_ID, readComp.getEmployeeId());
		assertEquals(effDate, readComp.getEffectiveDate());
		assertEquals(salary, readComp.getSalary(), 0.01);
		
		
    	
    }
    
    @Test
    public void testGetReportingStructure() {
    	
    	ReportingStructure reportStructure = restTemplate.getForEntity(employeeReportStructureUrl + "?employeeId=" + EMP_ID,
				ReportingStructure.class).getBody();
		assertEquals(Integer.valueOf(4), reportStructure.getNumberOfReports());
    }
    

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
