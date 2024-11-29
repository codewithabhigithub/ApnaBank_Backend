package com.src.model;

import java.time.LocalDate;

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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "creditCard")
public class CreditCard {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "creditCardNumber_generator")
	    @GenericGenerator(name = "creditCardNumber_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	            @Parameter(name = "sequence_name", value = "creditCardNumber_sequence"),
	            @Parameter(name = "initial_value", value = "608023"),
	            @Parameter(name = "increment_size", value = "1") })
	    private long creditCardNumber;

    private long cif;    
    private int cvv;
    private int pin;
//    private String type;
    private double maximumLimit;
    private double remainingLimit;
  //  private double creditCardBill;
  //  private LocalDate billGenerationDate;
  //  private LocalDate billExpiryDate;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private boolean status;
    
   
}
