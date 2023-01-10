package com.dimas.jumpstart.attendance.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dimas.jumpstart.attendance.dao.AuthProvider;
import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.RoleName;
import com.dimas.jumpstart.attendance.dao.Roles;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.AppException;
import com.dimas.jumpstart.attendance.exception.BadRequestException;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.jwtsecurity.TokenProvider;
import com.dimas.jumpstart.attendance.payload.AuthenticationResponse;
import com.dimas.jumpstart.attendance.payload.Login;
import com.dimas.jumpstart.attendance.payload.LoginResponse;
import com.dimas.jumpstart.attendance.payload.Register;
import com.dimas.jumpstart.attendance.repo.RolesRepo;
import com.dimas.jumpstart.attendance.repo.UsersRepo;
import com.dimas.jumpstart.attendance.service.EmployeeService;
import com.dimas.jumpstart.attendance.service.UsersPrincipal;
import com.dimas.jumpstart.attendance.service.UsersService;

import io.jsonwebtoken.impl.DefaultClaims;

@RestController
@RequestMapping("/jmpstart/auth")
public class AuthController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RolesRepo rolesRepo;
	
	@Autowired
	private UsersRepo usersRepo;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "/register")
	public ResponseEntity<?> registerUser(@RequestBody Register register){
		if(employeeService.getByEmail(register.getEmail()) == false) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"This User has not been register as an Employee!");
		}
		
		if(usersService.getByEmail(register.getEmail()) == true) {
			//throw new BadRequestException("Email has already registered before so kindly try another email");
			//return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to add the user");
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"This email already register as User!");
		}
		
		Employee employee = employeeService.findByEmail(register.getEmail());
		
		Users user = new Users();
		user.setFullName(register.getFullName());
		user.setEmail(register.getEmail());
		user.setProvider(AuthProvider.local);
		
		
		//user.setRoles(new HashSet<>(rolesRepo.findBySpecificRoles("USER")));
//		if (employee.getStore() == null) {
//			Roles userRole = rolesRepo.findByName(RoleName.ROLE_ADMIN)
//	                .orElseThrow(() -> new AppException("User Role not set."));
//
//	        user.setRoles(Collections.singleton(userRole));
//		} else {
//			user.setStore(employee.getStore());
//			Roles userRole = rolesRepo.findByName(RoleName.ROLE_EMPLOYEE)
//					.orElseThrow(() -> new AppException("User Role not set."));
//			
//			user.setRoles(Collections.singleton(userRole));
//		}
		if (employee.getStore() == null) {
			user.setRoles(new HashSet<>(rolesRepo.findByName(RoleName.ROLE_ADMIN)));
		} else {
			user.setStore(employee.getStore());
			user.setRoles(new HashSet<>(rolesRepo.findByName(RoleName.ROLE_EMPLOYEE)));
		}
		user.setPassword(register.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		usersService.addUser(user);
		
		//Users user = usersService.addUser(register);
		
//		URI location = ServletUriComponentsBuilder
//				.fromCurrentContextPath().path("/users/me")
//				.buildAndExpand(user.getUserId()).toUri();
		
//		return ResponseEntity.created(location)
//				.body(new RegisterResponse(true, "User has successfully registered!!!"));
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("User has successfully registered!");
	}
	
	//Local Login
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Login login/* , @CurrentUser UsersPrincipal usersPrincipal */) {
//
//		//Checking Authentication 
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                		login.getEmail(),
//                		login.getPassword()
//                )
//        );
//        
//        if (authentication == null) {
//        	//return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
//        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to add the user");
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        //If authorized user, create token
//        String token = tokenProvider.createToken(authentication);
//        
//        // retrieve user data to get use role
//        Users user =  usersRepo.findById((usersPrincipal.getUserId())) 
//        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", usersPrincipal.getUserId()));
//        
//        //Return to LoginResponse Payload
//        return ResponseEntity.ok(new LoginResponse(token, user.getRoles()));
//        
		String token = null;
		
		try {
			// Checking Authentication
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			// If authorized user, create token
			token = tokenProvider.createToken(authentication);

		} catch (Exception e) {

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user is Unauthorized", e);
		}
		
		Optional<Users> user = Optional.of(usersRepo.findByEmail(login.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("This email cannot be found" + login.getEmail())));
		
		List<Long> rolesId = new ArrayList<Long>();
		
		for(Roles val : user.get().getRoles()) {
			rolesId.add(val.getId());
		}
		
		// Return to LoginResponse Payload
		return ResponseEntity.ok(new LoginResponse(token, rolesId));
		
    }
	
	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = tokenProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
	
//	@PostMapping(value = "/admin")
//	@Secured("ROLE_ADMIN")
//	public ResponseEntity<?> addAdmin(@RequestBody Register register){
//		if(usersService.getByEmail(register.getEmail()) == true) {
//			throw new ResponseStatusException(HttpStatus.CONFLICT,
//					"This email already register as User!");
//		}
//		
//		Users user = new Users();
//		user.setFullName(register.getFullName());
//		user.setEmail(register.getEmail());
//		user.setProvider(AuthProvider.local);
//		
//		Roles userRole = rolesRepo.findByName(RoleName.ROLE_ADMIN)
//                .orElseThrow(() -> new AppException("User Role not set."));
//
//        user.setRoles(Collections.singleton(userRole));
//		user.setPassword(register.getPassword());
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		usersService.addUser(user);
//		
//		return ResponseEntity
//				.status(HttpStatus.CREATED)
//				.body("Admin has successfully registered!");
//	}

}
