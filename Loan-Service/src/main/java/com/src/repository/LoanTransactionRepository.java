package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.LoanTransaction;
@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long> {

}
