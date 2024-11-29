package com.src.model;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customerDetails")
public class CustomerDetails {
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cif_generator")
	@GenericGenerator(name = "cif_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "cif_sequence"),
			@Parameter(name = "initial_value", value = "5001"), @Parameter(name = "increment_size", value = "1") })
	private long cif;
	
//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "mobileNumber")
//	private User user;
	private String mobileNumber;
	private String email;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private LocalDate dob;
	private String gender;
	private String aadharNumber;
	private String panNumber;
	private boolean verificationStatus;
	private String accountType;
	
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "account_number")
//	private AccountDetails accountDetails;

}
