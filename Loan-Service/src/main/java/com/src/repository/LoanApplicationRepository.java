package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.LoanApplication;
@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
	
	public List<LoanApplication> findByCif(long cif);
	
	@Query(value="SELECT * FROM LoanApplication l WHERE l.verificationStatus = false and l.status='pending'", nativeQuery = true)
	public List<LoanApplication> findAllAppliedApplication();
	
	@Query(value="SELECT * FROM LoanApplication l WHERE l.verificationStatus = true and l.status='pending'", nativeQuery = true)
	public List<LoanApplication> findAllVerifiedApplication();
	
}
