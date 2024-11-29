package com.src.model;

import java.time.LocalDateTime;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name ="CreditCardTransaction" )
public class CreditCardTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long creditCardTransactionId;
	private long creditCardNumber;
	private String receiverDetails;
	private LocalDateTime date;
	private String description;
	private double amount;
	private double remainingLimit;
	private String status;
}
