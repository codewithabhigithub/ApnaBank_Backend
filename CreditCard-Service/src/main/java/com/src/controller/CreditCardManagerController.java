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
@RequestMapping("/creditCard/manager")
public class CreditCardManagerController {

	@Autowired
	private ICreditCardService cardService;

	CreditCard creditCard = null;

	@GetMapping("/approve/{cif}")
	public ResponseEntity<?> approveCreditCard(@PathVariable("cif") long cif) {
		try {
			creditCard = cardService.findCreditCardByCif(cif);
			System.out.println("get card"+ creditCard);
			if (creditCard != null && (creditCard.isStatus() == false)) {
				System.out.println("in if");
					return new ResponseEntity<CreditCard>(cardService.approveCreditCard(creditCard), HttpStatus.OK);	
			}
			throw new Exception("NOT FOUND");
		} catch (Exception e) {
			return new ResponseEntity<String>("Customer is Active or Card Not found", HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/uderProcess")
	public ResponseEntity<?> findAllUderProcessCustomers() {
		return new ResponseEntity<List<CreditCardCustomerDetails>>(cardService.findAllUderProcessCustomers(),HttpStatus.OK);
	}
	
	@PutMapping("/block/{cif}")
	public ResponseEntity<?> blockCard(@PathVariable("cif") long cif) {
		CreditCard card=cardService.findCreditCardByCif(cif);
		if(card.isStatus())
			return new ResponseEntity<CreditCard>(cardService.blockCreditCard(card),HttpStatus.OK);
		else
			return new ResponseEntity<String>("Card Already BLocked",HttpStatus.BAD_REQUEST);
	}
	@PutMapping("/unBlock/{cif}")
	public ResponseEntity<?> unBlockCard(@PathVariable("cif") long cif) {
		CreditCard card=cardService.findCreditCardByCif(cif);
			return new ResponseEntity<CreditCard>(cardService.unBlockCreditCard(card),HttpStatus.OK);
		
	}
	@DeleteMapping("/remove/{cif}")
	public ResponseEntity<?> rejectCreditCard(@PathVariable("cif") long cif) {
		CreditCard card = cardService.findCreditCardByCif(cif);
		if (card.isStatus() == false && card.getRemainingLimit()==0) {
			boolean bool = cardService.removeCreditCard(card);
			if (bool)
				return new ResponseEntity<CreditCard>(cardService.findCreditCardByCif(cif), HttpStatus.OK);
			else
				return new ResponseEntity<String>("NOT FOUND", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>("Cant Remove Becouse Card is Active or Bill RePayment is Pending",
					HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/find/{cif}")
	public ResponseEntity<?> findCreditCard(@PathVariable("cif") long cif) {
		return new ResponseEntity<CreditCard>(cardService.findCreditCardByCif(cif),HttpStatus.OK);
	}
}
