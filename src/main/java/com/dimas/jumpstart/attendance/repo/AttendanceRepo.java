package com.dimas.jumpstart.attendance.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long>{
	
	@Query(value = "SELECT a FROM Attendance a WHERE a.user IN :userId"
			+ " ORDER BY a.attendanceId DESC")
	public List<Attendance> listAttendancesFromUser(@Param("userId") Users userId);
	
	@Query(value = "SELECT a"
			+ " FROM Attendance a INNER JOIN Users u ON a.user = u.userId"
			+ " WHERE u.store IN :storeId"
			+ " ORDER BY a.attendanceId DESC")
	public List<Attendance> listAttendancesFromStore(@Param("storeId") Stores storeId);
	
	@Query(value = "SELECT COUNT(a) FROM Attendance a WHERE a.user IN :userId AND a.isLate IS TRUE")
	public long countByIsLateTrueAndUser(@Param("userId") Users userId);
	
	@Query(value = "SELECT COUNT(a) FROM Attendance a WHERE a.user IN :userId AND a.isFullDay IS TRUE")
	public long countByIsFullDayTrueAndUser(@Param("userId") Users userId);
	
	@Query(value = "SELECT COUNT(a) FROM Attendance a WHERE a.user IN :userId")
	public long countAttendanceFromUser(@Param("userId") Users userId);
	
	@Query(value = "SELECT a FROM Attendance a WHERE a.user IN :userId"
			+ " AND a.checkIn BETWEEN :startDate AND :endDate"
			+ " ORDER BY a.attendanceId DESC")
	public List<Attendance> listAttendancesFromUserBetweenDates(@Param("userId") Users userId,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

}
