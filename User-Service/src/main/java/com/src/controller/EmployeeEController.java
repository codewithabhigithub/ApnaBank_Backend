package com.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.CustomerDetails;
import com.src.model.EmployeeDetails;
import com.src.model.User;
import com.src.service.ICustomerService;
import com.src.service.IEmployeeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeEController {
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IEmployeeService employeeService;
	CustomerDetails customerDetails=null;
	@PostMapping("/register/customer")
	public ResponseEntity<?> saveCustomer(@RequestBody CustomerDetails customer,HttpServletRequest request) {
		if (customerService.findCustumerDetailsAadhar(customer.getAadharNumber())==null && 
				customerService.findCustumerDetailsByMobile(customer.getMobileNumber())==null &&
				customerService.findCustumerDetailsByEmail(customer.getEmail())==null
				) {
			customer.setVerificationStatus(false);
			return new ResponseEntity<CustomerDetails>( customerService.directSave(customer),HttpStatus.OK);
			
		}else {
			 EmployeeDetails employee=employeeService.findEmployeeByMobileNumber(request.getAttribute("username").toString());
				return customerService.saveCustomer(customer, employee);	
		}

			
		
	}

	@PutMapping("/update/customer")
	public ResponseEntity<?> updateCustomer(@RequestBody CustomerDetails customer) {
		try {
			customerService.updateCustomer(customer);
			return new ResponseEntity<String>("UPDATED", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/customer/{cif}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("cif") long cif) {
		try {
			customerService.deleteCustomer(cif);
			return new ResponseEntity<String>("DELETED", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/customer/{mobileNumer}")
	public ResponseEntity<?> findCustomerByMobileNumber(@PathVariable("mobileNumer") String mobileNumber) {
		try {
			User user = customerService.findUserByMobileNumber(mobileNumber);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}

	}
	@GetMapping("/profile")
	public ResponseEntity<?> profile(HttpServletRequest request) {
		EmployeeDetails details=employeeService.findEmployeeByMobileNumber(request.getAttribute("username").toString());
			return new ResponseEntity<EmployeeDetails>(employeeService.findById(details.getEmployeeId()),HttpStatus.OK);
	}
	@GetMapping("/customerByCif/{cif}")
	public ResponseEntity<?> findCustomerByMobileNumber(@PathVariable("cif") long cif) {
		try {
		CustomerDetails user=customerService.findCustumerDetailsByCif(cif);
			return new ResponseEntity<CustomerDetails>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}

	}
}
