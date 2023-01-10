package com.dimas.jumpstart.attendance.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;

@Service
@Transactional
public interface UsersService {
	
	UserDetails loadUserById(long userId);
	Users addUser(Users user);
	Boolean getByEmail(String email);
	void updateUser(long userId, Users user);
	List<Users> listAllUsersFromStore(Stores store);
	void deleteUser(long userId);
	Users findById(long userId);
	void becomeManager(long userId);
	void demoteEmp(long userId);
}
