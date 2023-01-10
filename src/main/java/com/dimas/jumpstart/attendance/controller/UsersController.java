package com.dimas.jumpstart.attendance.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.payload.Profile;
import com.dimas.jumpstart.attendance.repo.UsersRepo;
import com.dimas.jumpstart.attendance.service.AttendanceService;
import com.dimas.jumpstart.attendance.service.UsersPrincipal;
import com.dimas.jumpstart.attendance.service.UsersService;

@RestController
@RequestMapping(value="/jmpstart/users")
public class UsersController {
	
	@Autowired
	private UsersRepo usersRepo;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private AttendanceService attendanceService;
	
	//Profile API <<Get Current User Profile>>
    //@PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN') or hasRole('MANAGER')")
//	@GetMapping("/profile")
//    @Secured({"ROLE_EMPLOYEE","ROLE_MANAGER","ROLE_ADMIN"})
//    public Users getUser(@CurrentUser UsersPrincipal usersPrincipal) {
//    	
//    	return usersRepo.findById(usersPrincipal.getUserId()) 
//                .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", usersPrincipal.getUserId()));
//    }
	
	@GetMapping("/profile")
    @Secured({"ROLE_EMPLOYEE","ROLE_MANAGER","ROLE_ADMIN"})
    public ResponseEntity<?> getUser(@CurrentUser UsersPrincipal usersPrincipal) {
		
		Users user = usersRepo.findById(usersPrincipal.getUserId()) 
                .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", usersPrincipal.getUserId()));
		
		List<Attendance> listAt = attendanceService.listAllAttendanceFromUser(user);
		
		Long countLate = attendanceService.countIsLate(user);
		Long countFull = attendanceService.countIsFullDay(user);
		Long countAt = attendanceService.countAttendance(user);
		
		Profile profile = new Profile();
		profile.setUsers(user);
		
		if (listAt != null) {
			profile.setList(listAt);
			profile.setCountAttendance(countAt);
			profile.setCountFullDay(countFull);
			profile.setCountLate(countLate);
		}
		
//		List<Object> listObject	=  new ArrayList<Object>();
//		listObject.add(user);
//		
//		if(countLate != null || countFull != null) {
//			//listObject.add(listAt);
//			listObject.add(countLate);
//			listObject.add(countFull);			
//		}
    	
    	//return ResponseEntity.ok(new Profile(user, listAt, countLate, countFull));
		//return ResponseEntity.status(HttpStatus.OK).body(listObject);
		return ResponseEntity.status(HttpStatus.OK).body(profile);
    }
    
    @PutMapping("/profile/{uid}")
    @Secured({"ROLE_EMPLOYEE","ROLE_MANAGER","ROLE_ADMIN"})
    public ResponseEntity<?> editProfile(@RequestBody Users users, @PathVariable Long uid){
    	usersService.updateUser(uid, users);
    	return ResponseEntity.status(HttpStatus.OK).body("User successfully update.");
    }
    
//    @PutMapping("/{uid}")
//    @Secured({"ROLE_EMPLOYEE","ROLE_MANAGER","ROLE_ADMIN"})
//    public ResponseEntity<?> editUser(@RequestBody Users users, @PathVariable Long uid){
//    	usersService.updateUser(uid, users);
//    	return ResponseEntity.status(HttpStatus.OK).body("User successfully updated.");
//    }
    
    @GetMapping
    @Secured({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
    public ResponseEntity<?> listUsersFromStore(@CurrentUser UsersPrincipal usersPrincipal){
    	
    	Users user =  usersService.findById(usersPrincipal.getUserId());
    	
    	List<Users> listUsers = usersService.listAllUsersFromStore(user.getStore());
    	
    	return ResponseEntity.status(HttpStatus.OK).body(listUsers);
    }
    
    @DeleteMapping("/{uid}")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> deleteUser(@PathVariable Long uid){
    	usersService.deleteUser(uid);
    	return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted.");
    }
    
    @GetMapping(value = "promote/{uid}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<?> promoteUser(@PathVariable Long uid) {
    	usersService.becomeManager(uid);
    	return ResponseEntity.status(HttpStatus.OK).body("User successfully promote the role.");
    }
    
    @GetMapping(value = "demote/{uid}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<?> demoteUser(@PathVariable Long uid) {
    	usersService.demoteEmp(uid);
    	return ResponseEntity.status(HttpStatus.OK).body("User successfully demote the role.");
    }

}
