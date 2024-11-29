package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.CustomerDetails;

@Repository
public interface CustomerDeatilsRepository extends JpaRepository<CustomerDetails, Long> {

	public CustomerDetails findByAadharNumber(String aadharNumber);
	
	public List<CustomerDetails> findByVerificationStatus(boolean bool);
	
	public CustomerDetails findByMobileNumber(String mobileNumber);
	public CustomerDetails findByEmail(String email);
}
