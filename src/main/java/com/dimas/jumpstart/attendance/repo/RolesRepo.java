package com.dimas.jumpstart.attendance.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dimas.jumpstart.attendance.dao.RoleName;
import com.dimas.jumpstart.attendance.dao.Roles;

public interface RolesRepo extends JpaRepository<Roles, Long>{
	
//	Optional<Roles> findByName(RoleName roleName);
	Set<Roles> findByName(RoleName roleName);
}
