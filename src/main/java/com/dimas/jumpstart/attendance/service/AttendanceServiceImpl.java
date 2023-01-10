package com.dimas.jumpstart.attendance.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.repo.AttendanceRepo;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService{
	
	@Autowired
	private AttendanceRepo attendanceRepo;

	@Override
	public List<Attendance> listAllAttendanceFromUser(Users user) {
		return attendanceRepo.listAttendancesFromUser(user);
	}
	
	@Override
	public List<Attendance> listAllAttendanceFromStore(Stores store) {
		return attendanceRepo.listAttendancesFromStore(store);
	}

	@Override
	public Attendance getById(long id) {
		return attendanceRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Attendance", "attendanceId", id));
	}

	@Override
	public void updateAttendance(long id, Attendance attendance) {
		Attendance edit = attendanceRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Attendance", "attendanceId", id));
		edit.setCheckIn(attendance.getCheckIn());
		edit.setCheckOut(attendance.getCheckOut());
		edit.setFullDay(attendance.isFullDay());
		edit.setLate(attendance.isLate());
		edit.setHasCheckIn(attendance.isHasCheckIn());
		edit.setHasCheckOut(attendance.isHasCheckOut());
		edit.setTimeWorkInMins(attendance.getTimeWorkInMins());
		attendanceRepo.save(edit);
		
	}

	@Override
	public void deleteAttendance(long id) {
		attendanceRepo.deleteById(id);
		
	}

	@Override
	public long countIsLate(Users userId) {
		return attendanceRepo.countByIsLateTrueAndUser(userId);
	}

	@Override
	public long countIsFullDay(Users userId) {
		return attendanceRepo.countByIsFullDayTrueAndUser(userId);
	}

	@Override
	public List<Attendance> listAllAttendanceFromUserBetweenDate(Users user, String startDate, String endDate) {
		return attendanceRepo.listAttendancesFromUserBetweenDates(user, startDate, endDate);
	}

	@Override
	public long countAttendance(Users userId) {
		return attendanceRepo.countAttendanceFromUser(userId);
	}





}
