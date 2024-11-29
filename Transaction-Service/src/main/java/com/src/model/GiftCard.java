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

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Data
@Entity
@Table(name = "giftCard")
public class GiftCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String recname;
	private String giftCardName;
	private double amount;
//	private double discountAmount;
	private boolean redeemed;
	private long cif;
	
}
