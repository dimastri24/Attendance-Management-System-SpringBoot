package com.dimas.jumpstart.attendance.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long empId;
	
	@Column(nullable = false)
	private String fullName;
	
	@Email
	@Column(nullable = false, unique = true)
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Stores store;

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Stores getStore() {
		return store;
	}

	public void setStore(Stores store) {
		this.store = store;
	}

}
