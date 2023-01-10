package com.dimas.jumpstart.attendance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dimas.jumpstart.attendance.dao.Stores;

@Repository
public interface StoresRepo extends JpaRepository<Stores, Long>{
	
//	@Query(value = "SELECT s FROM Stores s WHERE s.storeName LIKE '%' || :keyword || '%'"
//			+ "OR s.storeDesc LIKE '%' || :keyword || '%'"
//			+ "OR s.locationProvince LIKE '%' || :keyword || '%'"
//			+ "OR s.LocationCity LIKE '%' || :keyword || '%'"
//			+ "OR s.storeDesc LIKE '%' || :keyword || '%'")
//	public List<Stores> searchBykey (@Param("keyword") String keyword);

}
