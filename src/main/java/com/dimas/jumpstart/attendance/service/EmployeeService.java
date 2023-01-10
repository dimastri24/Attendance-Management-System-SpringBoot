package com.dimas.jumpstart.attendance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.Stores;

@Service
@Transactional
public interface EmployeeService {
	
	Boolean getByEmail(String email);
	Employee findByEmail(String email);
	Employee addEmployee(Employee employee);
	List<Employee> listEmployeesFromStore(Stores store);
	void deleteEmployee(long eid);
	Employee getById(long eid);
	void updateEmployee(long eid, Employee employee);

}
