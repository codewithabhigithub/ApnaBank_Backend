package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.LoanAccount;
import com.src.model.LoanApplication;
import com.src.service.ILoanService;

@RestController
@RequestMapping("/managerLoan")
public class ManagerController {

	@Autowired
	private ILoanService service;
	@GetMapping("/allVerified")
	public ResponseEntity<List<LoanApplication>> getAllVerifiedApplication() {
		return new ResponseEntity<List<LoanApplication>>(service.findAllVerifiedApplication(),HttpStatus.OK);
	}
	@GetMapping("/sanction/{applicationId}/{sanctionedAmount}/{accountNumber}")
	public ResponseEntity<?> sanctionApplication(@PathVariable("applicationId") long applicationId,@PathVariable("sanctionedAmount") double amount, @PathVariable("accountNumber") long accountNumber) {

		try {
			LoanAccount account=service.sanctionLoan(service.findApplicationById(applicationId),amount,accountNumber);
			if(account!=null)
			return new ResponseEntity<LoanAccount>(account,HttpStatus.OK);
			else
				throw new Exception("some error occurred");
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/reject/{id}")
	public ResponseEntity<?> reject(@PathVariable("id") long id) {
		LoanApplication application=service.findApplicationById(id);
		service.rejectLoan(application);
		if(application!=null)
		return new ResponseEntity<String>("REJECTED",HttpStatus.OK);
		else
			return new ResponseEntity<String>("NO RECORD",HttpStatus.BAD_REQUEST);
	}
}
