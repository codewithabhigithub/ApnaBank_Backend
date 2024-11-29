package com.src.model;

import java.util.Date;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
	private Date validUpTo;
	private boolean isEnabled;
	
}
