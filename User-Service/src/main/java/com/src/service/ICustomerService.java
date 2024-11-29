package com.src.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.src.model.CustomerDetails;
import com.src.model.EmployeeDetails;
import com.src.model.User;

public interface ICustomerService {
	
	public ResponseEntity<?> saveCustomer(CustomerDetails customer,EmployeeDetails employee);
	
	public List<CustomerDetails> nonActiveCustomer();
	
	public User updateCustomer(CustomerDetails customer);
	
	public boolean deleteCustomer(long cif);
	
	public User findUserByMobileNumber(String mobileNumber);
	
	public List<Object> approveCustomer(long cif,EmployeeDetails employee);
	
	public void rejectCustomer(long cif);
	
	public CustomerDetails findCustumerDetailsByCif(long cif);
	public CustomerDetails findCustumerDetailsAadhar(String cif);
	public CustomerDetails findCustumerDetailsByMobile(String cif);
	public CustomerDetails findCustumerDetailsByEmail(String cif);
	public CustomerDetails directSave(CustomerDetails customerDetails);
	
}
