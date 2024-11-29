package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.CustomerDetails;
@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {
	@Query(value="SELECT * FROM customerDetails c WHERE c.mobileNumber = ?1", nativeQuery = true)
    public CustomerDetails findCustomerDetailsByMobileNumber(String mobileNumber);
}
