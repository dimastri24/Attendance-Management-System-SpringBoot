package com.dimas.jumpstart.attendance.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long>{
	
	// for login user detail
	Optional<Users> findByEmail(String email);
	
	// to check duplicate email
	Boolean existsByEmail(String email);
	
	@Query(value = "SELECT u FROM Users u WHERE u.store IN:storeId")
	public List<Users> ListUsersFromStore(@Param("storeId") Stores store);

}
