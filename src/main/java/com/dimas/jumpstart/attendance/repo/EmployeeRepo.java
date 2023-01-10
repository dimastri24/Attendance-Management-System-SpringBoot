package com.dimas.jumpstart.attendance.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dimas.jumpstart.attendance.dao.Employee;
import com.dimas.jumpstart.attendance.dao.Stores;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long>{
	
	Boolean existsByEmail(String email);
	
	Optional<Employee> findByEmail(String email);
	
	@Query(value = "SELECT e FROM Employee e WHERE e.store IN :sid")
	public List<Employee> listEmployeeFromStore(@Param("sid") Stores sid);

}
