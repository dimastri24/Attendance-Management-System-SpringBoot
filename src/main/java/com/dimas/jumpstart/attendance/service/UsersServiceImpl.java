package com.dimas.jumpstart.attendance.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.RoleName;
import com.dimas.jumpstart.attendance.dao.Roles;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.AppException;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.repo.RolesRepo;
import com.dimas.jumpstart.attendance.repo.UsersRepo;

@Service
@Transactional
public class UsersServiceImpl implements UsersService, UserDetailsService{
	
	@Autowired
	private UsersRepo usersRepo;
	
	@Autowired
	private RolesRepo rolesRepo;
	
//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users users = usersRepo.findByEmail(email)
		.orElseThrow(() -> new UsernameNotFoundException("This email cannot be found" + email));
		return UsersPrincipal.createUser(users);
	}

	@Override
	public UserDetails loadUserById(long userId) {
		Users users = usersRepo.findById(userId)
		.orElseThrow( () -> new ResourceNotFoundException("Users", "userId", userId));
		
		return UsersPrincipal.createUser(users);
	}

//	@Override
//	public Users addUser(Register register) {
//		Users user = new Users();
//		user.setUserName(register.getUserName());
//		user.setEmail(register.getEmail());
//		user.setProvider(AuthProvider.local);
//		//user.setRoles(new HashSet<>(rolesRepo.findBySpecificRoles("USER")));
//		
//		Roles userRole = rolesRepo.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set."));
//
//        user.setRoles(Collections.singleton(userRole));
//		
//        user.setPassword(register.getPassword());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//		//user.setPassword(passwordEncoder.encode(register.getPassword()));
//		
//		return usersRepo.save(user);
//	}
	
//	@Override
//	public Users addUser(Users register) {
//		Users user = new Users();
//		user.setUserName(register.getUserName());
//		user.setEmail(register.getEmail());
//		user.setProvider(AuthProvider.local);
//		//user.setRoles(new HashSet<>(rolesRepo.findBySpecificRoles("USER")));
//		
//		Roles userRole = rolesRepo.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set."));
//
//        user.setRoles(Collections.singleton(userRole));
//		
////        user.setPassword(register.getPassword());
////        user.setPassword(passwordEncoder.encode(user.getPassword()));
//		//user.setPassword(passwordEncoder.encode(register.getPassword()));
//		
//		return usersRepo.save(user);
//	}
	
	@Override
	public Users addUser(Users user) {
		
		return usersRepo.save(user);
	}

	@Override
	public Boolean getByEmail(String email) {
		if(usersRepo.existsByEmail(email)) {
			return true;
		}
		return false;
	}

	@Override
	public void updateUser(long userId, Users user) {
		Users edit = usersRepo.findById(userId).get();
		edit.setFullName(user.getFullName());
		edit.setAddress(user.getAddress());
		edit.setContactPhone(user.getContactPhone());
		edit.setJobTitle(user.getJobTitle());
		usersRepo.save(edit);
		
	}

	@Override
	public List<Users> listAllUsersFromStore(Stores store) {
		return usersRepo.ListUsersFromStore(store);
	}

	@Override
	public void deleteUser(long userId) {
		usersRepo.deleteById(userId);
		
	}

	@Override
	public Users findById(long userId) {
		return usersRepo.findById(userId) 
    	        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", userId));
	}

	@Override
	public void becomeManager(long userId) {
		Users edit = usersRepo.findById(userId) 
    	        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", userId));
		
//		Roles userRole = rolesRepo.findByName(RoleName.ROLE_MANAGER)
//                .orElseThrow(() -> new AppException("User Role not set."));
//		
//		edit.setRoles(Collections.emptySet());
//		usersRepo.save(edit);
//		edit.setRoles(Collections.singleton(userRole));
//		usersRepo.save(edit);
		
		edit.setRoles(new HashSet<>(rolesRepo.findByName(RoleName.ROLE_MANAGER)));
		
	}

	@Override
	public void demoteEmp(long userId) {
		Users edit = usersRepo.findById(userId) 
    	        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", userId));
		edit.setRoles(new HashSet<>(rolesRepo.findByName(RoleName.ROLE_EMPLOYEE)));
		
	}

}
