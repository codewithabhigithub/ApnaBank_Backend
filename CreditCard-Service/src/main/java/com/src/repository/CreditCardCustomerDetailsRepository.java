package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.CreditCardCustomerDetails;
@Repository
public interface CreditCardCustomerDetailsRepository extends JpaRepository<CreditCardCustomerDetails, Long> {
	
	public CreditCardCustomerDetails findByCif(long cif);
	
	public CreditCardCustomerDetails findByMobileNumber(String mobileNumber);
	
	@Query(value="SELECT * FROM CreditCardCustomerDetails c WHERE c.status = 'Applied'", nativeQuery = true)
	public List<CreditCardCustomerDetails> findAllAppliedCreditCardCustomerDetails();
	
	@Query(value="SELECT * FROM CreditCardCustomerDetails c WHERE c.status = 'Under_Process'", nativeQuery = true)
	public List<CreditCardCustomerDetails> findAllUnderProcessCreditCardCustomerDetails();
}
