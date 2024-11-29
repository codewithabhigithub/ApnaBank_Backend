package com.src.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
@Entity
@Table(name = "CardPayment")
public class CardPayment {
	
	@Id
	private long cardNumber;
	//private int pin;
	private double bill;
	private LocalDate billGenerationDate;
	private LocalDate billExpiryDate;
	private double due;
}
