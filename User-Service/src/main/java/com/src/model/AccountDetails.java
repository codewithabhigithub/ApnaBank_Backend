package com.src.model;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "accountDetails")
public class AccountDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "accountNumber_generator")
	@GenericGenerator(name = "accountNumber_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "accountNumber_sequence"),
			@Parameter(name = "initial_value", value = "111111"), @Parameter(name = "increment_size", value = "1") })
	private long accountNumber;
	private long cif;
//	@OneToOne(mappedBy = "accountDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
//	private CustomerDetails customerDetails;
	private String accountType;
	private boolean status;
	private LocalDate dateOfOpeningAccount;
	private double minimumbalanceRequired;
	private double balance;
	private String branchCode;
	private String ifscCode;
	private String branchName;

}
