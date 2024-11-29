package com.src.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "LoanApplication")
public class LoanApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long cif;
	private String mobileNumber;
	private LocalDate applyDate;
	private String status;
	private boolean verificationStatus;
	private String email;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private Date dob;
	private String gender;
	private String aadharNumber;
	private String panNumber;
	private String loanType;
	private double amount;
	private int duration;
	
//	private LocalDate sanctionedDate;
//	private LocalDate dueDate;
//	private LocalDate lastPaymentDate;
//	private double installmentAmount;
//	private double outstandingAmount;
//	private double overDue;
}
