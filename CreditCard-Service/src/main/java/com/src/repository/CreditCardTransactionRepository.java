package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.CreditCardTransaction;
@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long>{
	public List<CreditCardTransaction> findCreditCardTransactionByCreditCardNumber(long creditCardNumber);
}
