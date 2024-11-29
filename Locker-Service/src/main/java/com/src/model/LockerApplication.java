package com.src.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
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
@Table(name = "LockerApplication")
public class LockerApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long cif;
	private LocalDate appliedDate;
	@Column(name="years")
	private int duration;//in years
	private String status;
	private double charges;
	private boolean paymentStatus;
	
}
