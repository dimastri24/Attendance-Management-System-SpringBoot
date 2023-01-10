package com.dimas.jumpstart.attendance.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.BadRequestException;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.payload.CustomResponse;
import com.dimas.jumpstart.attendance.payload.TwoDatesInString;
import com.dimas.jumpstart.attendance.service.EmployeeService;
import com.dimas.jumpstart.attendance.service.StoresService;
import com.dimas.jumpstart.attendance.service.UsersService;

@RestController
@RequestMapping(value="/jmpstart/stores")
public class StoresController {
	
	@Autowired
	private StoresService storesService;
	
//	@Autowired
//	private EmployeeService employeeService;
	
	@Autowired
	private UsersService usersService;
	
	DateFormat tf = new SimpleDateFormat("HH:mm");
	
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Stores> listAllStores(){
		return storesService.viewAllStores();
	}
	
	@GetMapping(value = "/{sid}")
	@Secured({"ROLE_EMPLOYEE","ROLE_MANAGER","ROLE_ADMIN"})
	public ResponseEntity<?> getStoreById(@PathVariable Long sid) {
		Stores store = storesService.getById(sid);
		List<Users> users = usersService.listAllUsersFromStore(store);
		//List<Employee> employees = employeeService.listEmployeesFromStore(store);
		
//		Param param = new Param();
//		param.setStores(store);
//		param.setEmployees(employees);
		
		ParamGetStore param = new ParamGetStore();
		param.setStores(store);
		param.setUsers(users);
		
		return ResponseEntity.status(HttpStatus.OK).body(param);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> postStore(@RequestBody ParamPostStore param) throws ParseException {
		
		Stores store = param.getStores();
		TwoDatesInString datesInString = param.getTwoDatesInString();
		
		// direct store.getCLockEntry return null
		// System.out.println("ClockEntry: " + store.getClockEntry().toString());
		
		
		Date dateClockEntry = tf.parse(datesInString.getStart());
		Date dateClockOut = tf.parse(datesInString.getEnd());
		
		store.setClockEntry(dateClockEntry);
		store.setClockOut(dateClockOut);
		
		storesService.addStore(store);
		//return new ResponseEntity<>("Store successfully added", HttpStatus.CREATED) ;
		//return ResponseEntity.ok(new CustomResponse("Store successfully added", HttpStatus.CREATED));
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("Store successfully added!");
	}
	
	@DeleteMapping(value = "/{sid}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteStore(@PathVariable Long sid){
		storesService.deleteById(sid);
		//return new ResponseEntity<>("Store successfully deleted", HttpStatus.OK) ;
		//return ResponseEntity.ok(new CustomResponse("Store successfully deleted", HttpStatus.OK));
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Store successfully deleted!");
	}
	
	@PutMapping(value = "/{sid}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	public ResponseEntity<?> editStore(@PathVariable Long sid, @RequestBody ParamPostStore param) throws ParseException{
		
		Stores store = param.getStores();
		TwoDatesInString datesInString = param.getTwoDatesInString();
		
		Date dateClockEntry = tf.parse(datesInString.getStart());
		Date dateClockOut = tf.parse(datesInString.getEnd());
		
		store.setClockEntry(dateClockEntry);
		store.setClockOut(dateClockOut);
		
		storesService.updateStore(sid, store);
		//return new ResponseEntity<>("Store successfully updated", HttpStatus.NO_CONTENT) ;
		//return ResponseEntity.ok(new CustomResponse("Store successfully updated", HttpStatus.OK));
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Store successfully updated!");
	}
	
//	@GetMapping(value = "/search")
//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//	public List<Stores> getListStoresByKey(@RequestParam String key){
//		return storesService.getStoresByKey(key);
//	}

}

class ParamPostStore {
	private Stores stores;
	private TwoDatesInString twoDatesInString;
	public Stores getStores() {
		return stores;
	}
	public void setStores(Stores stores) {
		this.stores = stores;
	}
	public TwoDatesInString getTwoDatesInString() {
		return twoDatesInString;
	}
	public void setTwoDatesInString(TwoDatesInString twoDatesInString) {
		this.twoDatesInString = twoDatesInString;
	}
}

class ParamGetStore {
	private Stores stores;
	private List<Users> users;
	public Stores getStores() {
		return stores;
	}
	public void setStores(Stores stores) {
		this.stores = stores;
	}
	public List<Users> getUsers() {
		return users;
	}
	public void setUsers(List<Users> users) {
		this.users = users;
	}
}
