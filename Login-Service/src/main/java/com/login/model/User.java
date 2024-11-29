package com.login.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name= "user")
public class User {

	@Id
	private String mobileNumber;

	
	private String email;
	private String password;
	private String role;
	private boolean status;
	private Date validUpTo;
	private boolean isEnabled;
//	private String coin;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String mobileNumber, String email, String password, String role, boolean status, Date validUpTo,
			boolean isEnabled) {
		super();
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
		this.validUpTo = validUpTo;
		this.isEnabled = isEnabled;
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
	public Date getValidUpTo() {
		return validUpTo;
	}
	public void setValidUpTo(Date validUpTo) {
		this.validUpTo = validUpTo;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	

	
}
