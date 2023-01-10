package com.dimas.jumpstart.attendance.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Stores {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long storeId;
	
	@Column(nullable = false)
	private String storeOwner;
	
	@Column(nullable = false)
	private String contactPhone;
	
	@Column(nullable = false)
	private String storeAddress;
	
	private String locationCountry;
	
	private String LocationCity;
	
	//@OneToMany(fetch = FetchType.LAZY)
//	@OneToMany(mappedBy = "store", orphanRemoval = true)
//	private Set<Users> users = new HashSet<Users>();
	
	//@OneToMany(fetch = FetchType.LAZY)
//	@OneToMany(mappedBy = "store", orphanRemoval = true)
//	private Set<Employee> employees = new HashSet<Employee>();
	
	@Temporal(TemporalType.TIME)
	private Date ClockEntry;
	
	@Temporal(TemporalType.TIME)
	private Date ClockOut;

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreOwner() {
		return storeOwner;
	}

	public void setStoreOwner(String storeOwner) {
		this.storeOwner = storeOwner;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getLocationCountry() {
		return locationCountry;
	}

	public void setLocationCountry(String locationCountry) {
		this.locationCountry = locationCountry;
	}

	public String getLocationCity() {
		return LocationCity;
	}

	public void setLocationCity(String locationCity) {
		LocationCity = locationCity;
	}

//	public Set<Users> getUsers() {
//		return users;
//	}
//	
//	public void setUsers(Set<Users> users) {
//		this.users = users;
//	}
	
//	public Set<Employee> getEmployees() {
//		return employees;
//	}
//	
//	public void setEmployees(Set<Employee> employees) {
//		this.employees = employees;
//	}

	public Date getClockEntry() {
		return ClockEntry;
	}

	public void setClockEntry(Date clockEntry) {
		ClockEntry = clockEntry;
	}

	public Date getClockOut() {
		return ClockOut;
	}

	public void setClockOut(Date clockOut) {
		ClockOut = clockOut;
	}


}
