package com.dimas.jumpstart.attendance.payload;

import java.util.List;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Users;

public class Profile {
	
	private Users users;
	private long countLate;
	private long countFullDay;
	private long countAttendance;
	private List<Attendance> list;
	
	public Profile() {}
	
	public Profile(Users users, List<Attendance> list, long countLate, long countFullDay, long countAttendance) {
		this.users = users;
		this.list = list;
		this.countLate = countLate;
		this.countFullDay = countFullDay;
		this.countAttendance = countAttendance;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public List<Attendance> getList() {
		return list;
	}

	public void setList(List<Attendance> list) {
		this.list = list;
	}

	public long getCountLate() {
		return countLate;
	}

	public void setCountLate(long countLate) {
		this.countLate = countLate;
	}

	public long getCountFullDay() {
		return countFullDay;
	}

	public void setCountFullDay(long countFullDay) {
		this.countFullDay = countFullDay;
	}

	public long getCountAttendance() {
		return countAttendance;
	}

	public void setCountAttendance(long countAttendance) {
		this.countAttendance = countAttendance;
	}
	
	

}
