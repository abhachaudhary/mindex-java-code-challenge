package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CompensationRepository compRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

	@Override
	public ReportingStructure getReportingStructure(String employeeId) {
		LOG.debug("Getting reporting structure for employee with id [{}]", employeeId);

		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		
		Integer numberOfReports =  calculateTotalReportees(employeeId);
		
		ReportingStructure reportingStructure = new ReportingStructure();
		reportingStructure.setEmployee(employee);
		reportingStructure.setNumberOfReports(numberOfReports);
		
		return reportingStructure;
	}
	
	@Override
	public void createCompensation(Compensation comp) {
		
		if(Objects.isNull(comp.getEmployeeId()) || comp.getSalary() == 0.0 || Objects.isNull(comp.getEffectiveDate()))
			throw new RuntimeException("Request has missing parameters");
		
		String empId = comp.getEmployeeId();
		
		if (Objects.isNull(employeeRepository.findByEmployeeId(empId))) {
            throw new EmployeeNotFoundException("Employee does not exists");
        }
		
		if(Objects.nonNull(compRepository.findByEmployeeId(empId))) {
			throw new RuntimeException("Compensation has already created for employee: "+empId);
		}
		
		compRepository.insert(comp);
	}

	@Override
	public Compensation getCompensation(String employeeId) {
		
		LOG.debug("Getting compensation details for employee with id [{}]", employeeId);
		
		Compensation comp = compRepository.findByEmployeeId(employeeId);
		if(comp == null)
			throw new EmployeeNotFoundException("Employee does not exist");
		
		return comp;
	}
	
	private Integer calculateTotalReportees(String employeeId) {
		
		int reportCounts = 0;
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		List<Employee> directReports = employee.getDirectReports();
		
		if(directReports == null)
			return 0;
		
		for(Employee reportee: directReports) {
			reportCounts = reportCounts + calculateTotalReportees(reportee.getEmployeeId());
		}
		
		return reportCounts + directReports.size();
	}
	
	private Integer calculateReporteeBFS(String employeeId) {
		
		int count = 0;
		
		Queue<String> queue = new LinkedList<>();
		queue.offer(employeeId); //parent employee id
		
		while(!queue.isEmpty()) {
			int size = queue.size();
			for(int i=0; i<size; i++) {
				String empId = queue.poll();
				Employee employee = employeeRepository.findByEmployeeId(empId);
				List<Employee> directReports = employee.getDirectReports();
				if(directReports != null) {
					for(Employee emp: directReports) {
						queue.offer(emp.getEmployeeId());
						count++;
					}
				}
			}
		}
		
		return count;	
	}

}
