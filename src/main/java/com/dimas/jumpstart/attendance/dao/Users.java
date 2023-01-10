package com.dimas.jumpstart.attendance.dao;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users", 
	   uniqueConstraints = {
			   @UniqueConstraint(columnNames = "email")
	   })
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@Column(nullable = false)
	private String fullName;
	
	@Email
	@Column(nullable = false, unique = true)
	private String email;
	
	@JsonIgnore
	private String password;
	
//	private String imgUrl;
	
	@Column(nullable = false)
	private Boolean emailVerified = false;
	
//	private String providerId;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;
	
	private String contactPhone;
	
	private String address;
	
	private String jobTitle;
	
    @ManyToMany
    @JoinTable( name="user_role",
                joinColumns = @JoinColumn(name = "users_id"),
                inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Roles> roles = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "store_id" )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Stores store;
    
//    @OneToMany(mappedBy = "user", orphanRemoval = true)
//    private Set<Attendance> attendances = new HashSet<Attendance>();

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public String getImgUrl() {
//		return imgUrl;
//	}
//
//	public void setImgUrl(String imgUrl) {
//		this.imgUrl = imgUrl;
//	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

//	public String getProviderId() {
//		return providerId;
//	}
//
//	public void setProviderId(String providerId) {
//		this.providerId = providerId;
//	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void setProvider(AuthProvider provider) {
		this.provider = provider;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}
	
	public Stores getStore() {
		return store;
	}

	public void setStore(Stores store) {
		this.store = store;
	}

//	public Set<Attendance> getAttendances() {
//		return attendances;
//	}
//
//	public void setAttendances(Set<Attendance> attendances) {
//		this.attendances = attendances;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(password, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		return Objects.equals(password, other.password) && Objects.equals(email, other.email);
	}

}
