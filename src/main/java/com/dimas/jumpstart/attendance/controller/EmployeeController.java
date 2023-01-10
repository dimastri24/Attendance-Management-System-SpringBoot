package com.dimas.jumpstart.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.RoleName;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.service.EmployeeService;
import com.dimas.jumpstart.attendance.service.StoresService;
import com.dimas.jumpstart.attendance.service.UsersPrincipal;
import com.dimas.jumpstart.attendance.service.UsersService;

@RestController
@RequestMapping(value="/jmpstart/emp")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private StoresService storesService;
	
	@PostMapping(value = "/{sid}")
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee, @PathVariable Long sid){
		
		if(employeeService.getByEmail(employee.getEmail())==true) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"This User already register as an Employee!");
		}
		
		//Users user =  usersService.findById(usersPrincipal.getUserId());
		
		Stores store = storesService.getById(sid);
		
//		if (usersPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//
//		}
		
		//employee.setStore(sid != null ? store : user.getStore());
		employee.setStore(store);
		employeeService.addEmployee(employee);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("Employee has successfully registered!");
	}
	
	@PostMapping(value = "/admin")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> addAdmin(@RequestBody Employee employee){
		
		if(employeeService.getByEmail(employee.getEmail())==true) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"This User already register as an Employee!");
		}
		
		employeeService.addEmployee(employee);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("Employee has successfully registered!");
	}
	
	
	@GetMapping(value = "/list/{sid}")
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> listEmployee(@PathVariable Long sid){
		
		//Users user =  usersService.findById(usersPrincipal.getUserId());
		
		Stores store = storesService.getById(sid); 
    	
    	List<Employee> listEmployee = employeeService.listEmployeesFromStore(store);
		
		return ResponseEntity.ok(listEmployee);
	}
	
	@DeleteMapping(value = "/{eid}")
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> deleteEmployee(@PathVariable Long eid){
		employeeService.deleteEmployee(eid);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Employee successfully deleted!");
	}
	
	@GetMapping(value = "/{eid}")
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> getEmployee(@PathVariable Long eid) {
		Employee employee = employeeService.getById(eid);
		
		return ResponseEntity.ok(employee);
		
	}
	
	@PutMapping(value = "/{eid}")
	@Secured({"ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> editEmployee(@PathVariable Long eid, @RequestBody Employee employee) {
		employeeService.updateEmployee(eid, employee);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Employee successfully updated!");
	}

}
