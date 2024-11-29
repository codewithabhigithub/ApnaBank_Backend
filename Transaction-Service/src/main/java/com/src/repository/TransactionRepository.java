package com.src.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.Transaction;
@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, Long>{
	
	@Query(value = "SELECT * FROM transaction WHERE status='pending'", nativeQuery = true)
	public List<Transaction> findTransactionByStatus();
	
	public List<Transaction> findTransactionBySenderAccountNumber(long senderAccountNumber);
	
	public List<Transaction> findAllTransactionByDateOfTransaction(LocalDate date);
}
