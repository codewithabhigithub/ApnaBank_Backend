package com.src.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity

@Table(name= "user")
public class User {
	@Id
	private long userId;
	private String mobileNumber;
	private String email;
	private String password;
	private String role;
	private boolean status;
	private LocalDate validUpTo;
	private boolean isEnabled;
	
	
	
	public User(long userId, String mobileNumber, String email, String password, String role, boolean status,
			LocalDate validUpTo, boolean isEnabled) {
		super();
		this.userId = userId;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
		this.validUpTo = validUpTo;
		this.isEnabled = isEnabled;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public LocalDate getValidUpTo() {
		return validUpTo;
	}
	public void setValidUpTo(LocalDate validUpTo) {
		this.validUpTo = validUpTo;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", mobileNumber=" + mobileNumber + ", email=" + email + ", password="
				+ password + ", role=" + role + ", status=" + status + ", validUpTo=" + validUpTo + ", isEnabled="
				+ isEnabled + "]";
	}

    
	
}
