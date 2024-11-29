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
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Data
@Builder
@Entity
@Table(name = "LoanAccount")
public class LoanAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "loanNumber_generator")
	@GenericGenerator(name = "loanNumber_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "loanNumber_generator"),
			@Parameter(name = "initial_value", value = "555555"), @Parameter(name = "increment_size", value = "1") })
	private long accountNumber;
	private long cif;
	private double sanctionedAmount;
	private double pendingAmount;
	private double emi;
	private LocalDate emiStartDate;
	private LocalDate emiEndDate;
	private String loanType;
	private double intrestRate;
	private int duration;
	private LocalDate startDate;
	private LocalDate endEnd;
	
	
	
	
}
