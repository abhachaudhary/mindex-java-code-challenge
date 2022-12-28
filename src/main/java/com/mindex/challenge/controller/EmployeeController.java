package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
    
    @GetMapping("/employee/reporting")
    public ReportingStructure getReportingStructure(@RequestParam String employeeId) {
        LOG.debug("Received reporting structure read request for id [{}]", employeeId);

        return employeeService.getReportingStructure(employeeId);
    }
    
    @GetMapping("/employee/compensation")
    public Compensation getCompensation(@RequestParam String employeeId) {
        LOG.debug("Getting compensation for employee [{}]", employeeId);

        return employeeService.getCompensation(employeeId);
    }
    
    @PostMapping("/employee/compensation")
    public void createCompensation(@RequestBody Compensation comp) {
        LOG.debug("Creating compensation for employee id [{}]", comp.getEmployeeId());

        employeeService.createCompensation(comp);
        LOG.debug("Compensation created successfully for employee id [{}]", comp.getEmployeeId());
    }
}
