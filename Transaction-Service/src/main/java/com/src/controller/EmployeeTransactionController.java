package com.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.AccountDetails;
import com.src.model.Transaction;
import com.src.service.IAccountService;
@RestController
@RequestMapping("/transactionByEmployee")
public class EmployeeTransactionController {
	@Autowired
	private IAccountService service;
	
	
	@GetMapping("/withdrawl/{accountNumber}/{amount}")
	public ResponseEntity<?> withdrawl(@PathVariable("amount") double amount,
			@PathVariable("accountNumber") long accountNumber) {
		AccountDetails account = service.findAccountDetailsByAccountNumber(accountNumber);
		if (account == null) {
			return new ResponseEntity<String>("Account Number does not exist", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Transaction>(service.withdrawAmount(account, amount), HttpStatus.OK);
		}
	}
	// deposite to self via cash
	@GetMapping("/deposit/{accountNumber}/{amount}")
	public ResponseEntity<?> deposite(@PathVariable("amount") double amount,
			@PathVariable("accountNumber") long accountNumber) {
		try {
			AccountDetails account = service.findAccountDetailsByAccountNumber(accountNumber);
			return new ResponseEntity<Transaction>(service.depositAmount(account, amount), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Account Number does not exist", HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/find/{accountNumber}")
	public ResponseEntity<?> name(@PathVariable("accountNumber") long accountNumber) {
		AccountDetails accountDetails=service.findAccountDetailsByAccountNumber(accountNumber);
		if(accountDetails!=null) {
			return new ResponseEntity<AccountDetails>(accountDetails,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("not found",HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/accountDetails/findAll/{customerId}")
	public ResponseEntity<?> details(@PathVariable("customerId") long customerId) {
		AccountDetails acc=service.findAccoundsByCif(customerId).get(0);
		System.out.println();
		if(acc!=null) {
			return new ResponseEntity<AccountDetails>(acc,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("not found",HttpStatus.NOT_FOUND);
		}
		
	}
	

}
