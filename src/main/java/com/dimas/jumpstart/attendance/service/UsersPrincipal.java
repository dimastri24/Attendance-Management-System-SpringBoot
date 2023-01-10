package com.dimas.jumpstart.attendance.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dimas.jumpstart.attendance.dao.Users;

public class UsersPrincipal implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private long userId;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UsersPrincipal(long userId, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	//createUser Method for Role User
	//It returns usersPrincipal object which is included userId, email, password, ROLE_USER
	public static UsersPrincipal createUser(Users users) {
		//List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		List<GrantedAuthority> authorities = users.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
		UsersPrincipal usersPrincipal = new UsersPrincipal(users.getUserId(), users.getEmail(), users.getPassword(), authorities);
		return usersPrincipal;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {  
        return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersPrincipal that = (UsersPrincipal) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId);
    }

}
