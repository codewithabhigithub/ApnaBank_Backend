
package com.src.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
//	@OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private CustomerDetails customerDetails;

	private int houseNo;

	private String street;
	
	private String locality;
	
	private String taluka;

	private String district;

	private String state;

	private String country;
	//@Column(nullable = false)
	private int pincode;

	
}
