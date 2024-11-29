package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.CreditCard;
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	public CreditCard findCreditCardByCif(long cif);
}
