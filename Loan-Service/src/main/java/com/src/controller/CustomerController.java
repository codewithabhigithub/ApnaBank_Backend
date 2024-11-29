package com.src.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.AccountDetails;
import com.src.model.LoanAccount;
import com.src.model.LoanApplication;
import com.src.model.LoanTransaction;
import com.src.service.ILoanService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customerLoan")
public class CustomerController {

	@Autowired
	private ILoanService service;
	
	@GetMapping("hello")
	public String name() {
		
		return "Hello";
	}
	@PostMapping("/apply")
	public ResponseEntity<?> applyLoanApplication(@RequestBody LoanApplication application,HttpServletRequest request) {
		//application.setCif(service.findCifByMobileNumber(request.getAttribute("username").toString()));
		application.setCif(service.findCifByMobileNumber(request.getAttribute("username").toString()));//remove
		LoanApplication loanApplication=service.applyLoan(application);
		if(loanApplication!=null)
			return new ResponseEntity<LoanApplication>(loanApplication,HttpStatus.OK);
		else
			return new ResponseEntity<String>("Aleady Loan",HttpStatus.BAD_REQUEST);
	}
	@PutMapping("emiPayment/{loanAccountNumber}/{accountNumber}")
	public ResponseEntity<?> emiPayment(@PathVariable("loanAccountNumber") long loanAccountNumber,@PathVariable("accountNumber") long accountNumber , HttpServletRequest request) {
		LoanAccount loanAccount=service.findLoanAccountById(loanAccountNumber);
		if(loanAccount!=null && loanAccount.getPendingAmount()>0) {
			AccountDetails bankAccount=service.findAccountDetailsByAccountNumber(accountNumber);
		if(bankAccount!=null && bankAccount.getCif()==service.findCifByMobileNumber(request.getAttribute("username").toString())) {
			LoanTransaction loanTransaction=service.emiPayment(loanAccount, bankAccount);
			if(loanTransaction!=null) {
				return new ResponseEntity<LoanTransaction>(loanTransaction,HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("Insfficient Fund",HttpStatus.BAD_REQUEST);
			}
		}else {
			return new ResponseEntity<String>("Bank Account not matched ",HttpStatus.BAD_REQUEST);
		}
		}else {
			return new ResponseEntity<String>("Loan Account not matched or no loan",HttpStatus.BAD_REQUEST);
		}
	}
	
}