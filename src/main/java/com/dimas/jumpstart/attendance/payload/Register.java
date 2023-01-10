package com.dimas.jumpstart.attendance.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Register {
	
	@NotBlank
	private String fullName;
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
