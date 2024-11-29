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
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "employeedetails")
public class EmployeeDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "employeeId_generator")
    @GenericGenerator(
            name = "employeeId_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "employeeId_sequence"),
                    @Parameter(name = "initial_value", value = "6001"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long employeeId;
	private String mobileNumber;
	private String email;
	private String position;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private LocalDate dob;
	private String gender;
	private String aadharNumber;
	private String panNumber;
	private String branchName;
	private String branchCode;
	private String ifsc;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;

	
}
