package com.src.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.model.CreditCard;
import com.src.model.CreditCardCustomerDetails;
import com.src.service.ICreditCardService;

@RestController
@RequestMapping("/creditCard/employee")
public class CreditCardEmployeeController {
	@Autowired
	private ICreditCardService cardService;

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}

	@GetMapping("/approve/{cif}")
	public ResponseEntity<?> PreApproveCreditCard(@PathVariable("cif") long cif) {
		try {
			CreditCard creditCard = cardService.findCreditCardByCif(cif);
			CreditCardCustomerDetails cardDetails = cardService.findCardCustomerDetailsByCif(cif);
			if (creditCard == null) {
				System.out.println("in if");
				if (cardDetails != null && cardDetails.getStatus().equalsIgnoreCase("Applied")) {
					return new ResponseEntity<CreditCard>(cardService.preApproval(cardDetails), HttpStatus.OK);
				} else {
					throw new Exception("Credit Card Not Applied");
				}
			} else {
				throw new Exception("Credit Card Already Present");
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/applied")
	public ResponseEntity<?> findAllApplied() {
		return new ResponseEntity<List<CreditCardCustomerDetails>>(cardService.findAllAppliedCustomers(),HttpStatus.OK);
	}
	@DeleteMapping("/remove/{cif}")
	public ResponseEntity<?> rejectCreditCard(@PathVariable("cif") long cif) {
		cardService.deleteCreditCardCustomerDetails(cif);
				return new ResponseEntity<String>("Deleted success", HttpStatus.OK);
	
	}
}
