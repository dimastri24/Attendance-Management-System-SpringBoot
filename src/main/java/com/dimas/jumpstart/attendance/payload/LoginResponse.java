package com.dimas.jumpstart.attendance.payload;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LoginResponse {
	
	private String accessToken;
	private String tokenType = "Bearer";
	private List<Long> roles;
	
	public LoginResponse(String accessToken, List<Long> roles) {
		this.accessToken = accessToken;
		this.setRoles(roles);
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public List<Long> getRoles() {
		return roles;
	}
	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}



//	private Set<Roles> roles = new HashSet<>();
//	
//	public LoginResponse(String accessToken, Set<Roles> roles) {
//		this.accessToken = accessToken;
//		this.roles = roles;
//	}
//	public String getAccessToken() {
//		return accessToken;
//	}
//	public void setAccessToken(String accessToken) {
//		this.accessToken = accessToken;
//	}
//	public String getTokenType() {
//		return tokenType;
//	}
//	public void setTokenType(String tokenType) {
//		this.tokenType = tokenType;
//	}
//	public Set<Roles> getRoles() {
//		return roles;
//	}
//	public void setRoles(Set<Roles> roles) {
//		this.roles = roles;
//	}

}
