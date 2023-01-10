package com.dimas.jumpstart.attendance.config;

public class Auth {
	
	private String tokenSecret;
	private long tokenExpireMsec;
	private long refreshExpirationDateInMs;
	
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	public long getTokenExpireMsec() {
		return tokenExpireMsec;
	}
	public void setTokenExpireMsec(long tokenExpireMsec) {
		this.tokenExpireMsec = tokenExpireMsec;
	}
	public long getRefreshExpirationDateInMs() {
		return refreshExpirationDateInMs;
	}
	public void setRefreshExpirationDateInMs(long refreshExpirationDateInMs) {
		this.refreshExpirationDateInMs = refreshExpirationDateInMs;
	}

}
