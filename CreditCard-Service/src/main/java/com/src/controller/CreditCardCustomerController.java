package com.src.controller;



import java.util.List;

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

import com.src.model.CardPayment;
import com.src.model.CreditCard;
import com.src.model.CreditCardCustomerDetails;
import com.src.model.CreditCardTransaction;
import com.src.model.User;
import com.src.service.ICreditCardService;
import com.src.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/creditCard/customer")
public class CreditCardCustomerController {

	@Autowired
	private ICreditCardService cardService;
	@Autowired
	private IUserService userService;

	@GetMapping("/apply/creditCard")
	public ResponseEntity<?> applyCreditCard(HttpServletRequest request) {
		
		CreditCardCustomerDetails details=cardService.findCardCustomerDetailsByMobileNumber(request.getAttribute("username").toString());
		if(details==null)
			return new ResponseEntity<CreditCardCustomerDetails>(cardService.applyCreditCard(request.getAttribute("username").toString()),HttpStatus.OK);
		else
			return new ResponseEntity<String>("Already Applied",HttpStatus.CONFLICT);
	}
	@GetMapping("/setPin/{pin}")
	public ResponseEntity<?> setPin(@PathVariable("pin") int pin, HttpServletRequest request) {
		//CreditCard card=cardService.findCreditCardByCif(cardService.getCifByMobileNumber("9867761414"));
		CreditCard card=cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString());
		if(String.valueOf(pin).length()==4)
		return new ResponseEntity<CreditCard>(cardService.setPin(pin, card),HttpStatus.OK);
		else
			return new ResponseEntity<String>("Pin must be four digit",HttpStatus.OK);

	}
	
	@GetMapping("/cif")
	public ResponseEntity<?> getCif(HttpServletRequest request) {
		return new ResponseEntity<Long>(cardService.findCifByMobileNumber(request.getAttribute("username").toString()), HttpStatus.OK);
	}

	@PostMapping("/cardPayment/{pin}")
	public ResponseEntity<?> payment(@PathVariable("pin") int pin,
			@RequestBody CreditCardTransaction transaction, HttpServletRequest request) {
		CreditCard card=cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString());
		System.out.println("FROM CNT "+card);
		if(transaction.getAmount()>0 && transaction.getAmount()<=card.getRemainingLimit()) {
		if (card.isStatus() == true) {
			if(card.getPin()==pin) {
				CreditCardTransaction currentTransaction=cardService.payment(card, transaction);
				if(currentTransaction!=null) {
					return new ResponseEntity<CreditCardTransaction>(currentTransaction,HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("Insufficient fund or Limit expired",HttpStatus.NOT_FOUND);
				}
			}else {
				return new ResponseEntity<String>("Pin is incorrect",HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("Card is blocked", HttpStatus.NOT_FOUND);
		}
		}else {
			return new ResponseEntity<String>("Amount should greater than zero or Less than available limit", HttpStatus.NOT_FOUND);

		}
	}

	@GetMapping("/bill/cardPayment/{accountNumber}")
	public ResponseEntity<?> billRepayment(@PathVariable("accountNumber") long accountNumber,HttpServletRequest request) {
		CreditCard card = cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString());
		if (card != null) {
			ResponseEntity<?> response=cardService.billRepayment(card.getCif(),accountNumber);
			return response;
		} else {
			return new ResponseEntity<String>("Card Not Found", HttpStatus.NOT_FOUND);
		}
	}
//
//	@GetMapping("/creditCard")
//	public ResponseEntity<?> viewCreditCard(HttpServletRequest request) {	
//		return new ResponseEntity<CreditCard>(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()), HttpStatus.OK);
//	}
//	@GetMapping("/bill")
//	public ResponseEntity<?> viewCreditCardBill(HttpServletRequest request) {
//		CardPayment payment=cardService.findPayment(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()).getCreditCardNumber());
//		return new ResponseEntity<Double>(payment.getBill(),HttpStatus.OK);
//	}
//	@GetMapping("/balence")
//	public ResponseEntity<?> viewCreditCardBalence(HttpServletRequest request) {
//		return new ResponseEntity<Double>(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()).getRemainingLimit(),HttpStatus.OK);
//	}
//	@GetMapping("/allTransaction")
//	public ResponseEntity<List<CreditCardTransaction>> creditCardAllTransaction(HttpServletRequest request) {
//
//		return new ResponseEntity<List<CreditCardTransaction>>
//		(cardService.findCreditCardTransactionHystory(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString())),HttpStatus.OK);
//	}
	@GetMapping("/creditCard")
	public ResponseEntity<?> viewCreditCard(HttpServletRequest request) {	
		return new ResponseEntity<CreditCard>(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()), HttpStatus.OK);
	}
	@GetMapping("/bill")
	public ResponseEntity<?> viewCreditCardBill(HttpServletRequest request) {
		CardPayment payment=cardService.findPayment(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()).getCreditCardNumber());
		return new ResponseEntity<Double>(payment.getBill(),HttpStatus.OK);
	}
	@GetMapping("/balence")
	public ResponseEntity<?> viewCreditCardBalence(HttpServletRequest request) {
		return new ResponseEntity<Double>(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString()).getRemainingLimit(),HttpStatus.OK);
	}
	@GetMapping("/allTransaction")
	public ResponseEntity<List<CreditCardTransaction>> creditCardAllTransaction(HttpServletRequest request) {

		return new ResponseEntity<List<CreditCardTransaction>>
		(cardService.findCreditCardTransactionHystory(cardService.findCreditCardByMobileNumber(request.getAttribute("username").toString())),HttpStatus.OK);
	}
	@GetMapping("/creditCardApplication")
	public ResponseEntity<?> viewCreditCardApplication(HttpServletRequest request) {	
		try{
			return new ResponseEntity<CreditCardCustomerDetails>(cardService.findCardCustomerDetailsByMobileNumber(request.getAttribute("username").toString()), HttpStatus.OK);
		}catch (Exception e) {
			return null;
		}
	}
	
}
