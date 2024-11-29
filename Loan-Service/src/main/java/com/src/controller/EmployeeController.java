package com.src.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.LoanApplication;
import com.src.service.ILoanService;

@RestController
@RequestMapping("/employeeLoan")
public class EmployeeController {

	@Autowired
	private ILoanService service;
	@GetMapping("/allApplied")
	public ResponseEntity<List<LoanApplication>> getAllAppliedApplications() {
		return new ResponseEntity<List<LoanApplication>>(service.findAllAppiledApplication(),HttpStatus.OK);
	} 
	
	@GetMapping("/verify/{applicationId}")
	public ResponseEntity<?> verifyApplication(@PathVariable("applicationId") long applicationId) {
		LoanApplication application=service.verifyApplication(service.findApplicationById(applicationId));
		if(application!=null) {
		return new ResponseEntity<LoanApplication>(application,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Customer is having Loan Already or not found",HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/reject/{applicationId}")
	public ResponseEntity<?> reject(@PathVariable("applicationId") long applicationId) {
		LoanApplication application=service.findApplicationById(applicationId);
		if(application!=null)
		return new ResponseEntity<LoanApplication>(service.rejectLoan(application),HttpStatus.OK);
		else
			return new ResponseEntity<String>("not found",HttpStatus.BAD_REQUEST);
	}

}
