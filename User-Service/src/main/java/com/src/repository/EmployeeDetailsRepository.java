package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.EmployeeDetails;
@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

	public EmployeeDetails findByMobileNumber(String mobileNumber);
	public EmployeeDetails findByAadharNumber(String aadharNumber);
}
