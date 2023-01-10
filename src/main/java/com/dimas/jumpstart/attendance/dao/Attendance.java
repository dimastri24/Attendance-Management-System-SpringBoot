package com.dimas.jumpstart.attendance.dao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Attendance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long attendanceId;
	
	//@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkIn;
	
	//@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkOut;
	
	private boolean isLate = false;
	
	private boolean isFullDay = false;
	
	private long timeWorkInMins;
	
	private boolean hasCheckIn = false;
	
	private boolean hasCheckOut = false;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;

	public long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
	}

	public boolean isFullDay() {
		return isFullDay;
	}

	public void setFullDay(boolean isFullDay) {
		this.isFullDay = isFullDay;
	}

	public long getTimeWorkInMins() {
		return timeWorkInMins;
	}

	public void setTimeWorkInMins(long timeWorkInMins) {
		this.timeWorkInMins = timeWorkInMins;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public boolean isHasCheckIn() {
		return hasCheckIn;
	}

	public void setHasCheckIn(boolean hasCheckIn) {
		this.hasCheckIn = hasCheckIn;
	}

	public boolean isHasCheckOut() {
		return hasCheckOut;
	}

	public void setHasCheckOut(boolean hasCheckOut) {
		this.hasCheckOut = hasCheckOut;
	}

}
