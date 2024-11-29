package com.src.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Builder
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
	
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private CustomerDetails customerDetails;
//    
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private EmployeeDetails employeeDetails;

    
	
}
