package com.dimas.jumpstart.attendance.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;

@Service
@Transactional
public interface AttendanceService {
	
	List<Attendance> listAllAttendanceFromUser(Users user);
	List<Attendance> listAllAttendanceFromStore(Stores store);
	Attendance getById(long id);
	void updateAttendance(long id, Attendance attendance);
	void deleteAttendance(long id);
	long countIsLate(Users userId);
	long countIsFullDay(Users userId);
	long countAttendance(Users userId);
	List<Attendance> listAllAttendanceFromUserBetweenDate(Users user, String startDate, String endDate);
}
