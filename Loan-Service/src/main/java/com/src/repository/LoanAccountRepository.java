package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.LoanAccount;
@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Long> {

}
