package com.dimas.jumpstart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.repo.EmployeeRepo;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public Boolean getByEmail(String email) {
		
		if(employeeRepo.existsByEmail(email)) {
			return true;
		}
		
		return false;
	}

	@Override
	public Employee findByEmail(String email) {
		return employeeRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee", "email", email));
	}

	@Override
	public Employee addEmployee(Employee employee) {
		return employeeRepo.save(employee);
	}

	@Override
	public List<Employee> listEmployeesFromStore(Stores store) {
		return employeeRepo.listEmployeeFromStore(store);
	}

	@Override
	public void deleteEmployee(long eid) {
		employeeRepo.deleteById(eid);
	}

	@Override
	public Employee getById(long eid) {
		return employeeRepo.findById(eid)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee", "employeeId", eid));
	}

	@Override
	public void updateEmployee(long eid, Employee employee) {
		Employee edit = employeeRepo.findById(eid).get();
		edit.setFullName(employee.getFullName());
		edit.setEmail(employee.getEmail());
		employeeRepo.save(edit);
	}

}
