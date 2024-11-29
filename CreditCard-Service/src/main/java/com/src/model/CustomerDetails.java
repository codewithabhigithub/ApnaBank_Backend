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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "customerDetails")
public class CustomerDetails {
	
	@Id
	private long cif;
	
	private String mobileNumber;
	private String email;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private Date dob;
	private String gender;
	private String aadharNumber;
	private String panNumber;



}
