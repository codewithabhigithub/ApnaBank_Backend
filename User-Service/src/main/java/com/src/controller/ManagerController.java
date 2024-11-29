package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
//@PreAuthorize("hasAuthority('ROLE_MANAGER')")
@RequestMapping("/manager")
public class ManagerController {
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private ICustomerService customerService;
	
	@PostMapping("/register/employee")
	public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDetails employee) {
		try {
			employeeService.saveEmployee(employee);
			return new ResponseEntity<String>("ADDED",HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<String>("ERROR",HttpStatus.CONFLICT);
		}
	}
	@PutMapping("/update/employee")
	public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDetails employee) {
		try {
			employeeService.updateEmployee(employee);
			return new ResponseEntity<String>("UPDATED",HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>("ERROR",HttpStatus.NOT_FOUND);
		}

	}
	@DeleteMapping("/delete/employee/{employeeId}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") long employeeId) {
	try {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("DELETED",HttpStatus.OK);
	}catch (Exception e) {
		return new ResponseEntity<String>("ERROR",HttpStatus.NOT_FOUND);
	}
		
		
	}
	@GetMapping("/find/manager/{mobileNumber}")
	public ResponseEntity<?> getEmployeeUserDetailsByMobileNumber(@PathVariable("mobileNumber") String mobileNumber) {
		try {
			User user = employeeService.findUserByMobileNumber(mobileNumber);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/manager/{employeeId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getEmployeeDetailsByEmployeeId(@PathVariable("employeeId") long employeeId) {
		try {
			EmployeeDetails employeeDetails = employeeService.findById(employeeId);
			return new ResponseEntity<EmployeeDetails>(employeeDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/approve/customer/{cif}")
	public ResponseEntity<?> approveCustomerByCif(@PathVariable("cif") long cif,HttpServletRequest request){
		String username=request.getAttribute("username").toString();	
		return new ResponseEntity<List<Object>>(customerService.approveCustomer(cif, employeeService.findEmployeeByMobileNumber(username)),HttpStatus.OK);
		
	}
	@GetMapping("/pending/customer")
	public ResponseEntity<?> getPendingCustomers() {
		return new ResponseEntity<List<CustomerDetails>>(customerService.nonActiveCustomer(),HttpStatus.OK);
	}
	@GetMapping("/customer/{cif}")
	public ResponseEntity<?> findCustomer(@PathVariable("cif") long cif){
		return new ResponseEntity<CustomerDetails>(customerService.findCustumerDetailsByCif(cif),HttpStatus.OK);
	}
	@GetMapping("/reject/customer/{cif}")
	public ResponseEntity<?> name(@PathVariable("cif") long cif) {
		customerService.rejectCustomer(cif);
		return new ResponseEntity<String>("rejected", HttpStatus.OK);
	}
	@GetMapping("/profile")
	public ResponseEntity<?> profile(HttpServletRequest request) {
		EmployeeDetails details=employeeService.findEmployeeByMobileNumber(request.getAttribute("username").toString());
			return new ResponseEntity<EmployeeDetails>(employeeService.findById(details.getEmployeeId()),HttpStatus.OK);
	}
}
	

