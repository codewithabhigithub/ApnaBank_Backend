package com.src.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.src.model.CardPayment;
import com.src.model.CreditCard;
import com.src.model.CreditCardCustomerDetails;
import com.src.model.CreditCardTransaction;

public interface ICreditCardService {
	//employee-
	public List<CreditCardCustomerDetails> findAllAppliedCustomers();
	public CreditCard preApproval(CreditCardCustomerDetails cardCustomerDetails);
	public CreditCardCustomerDetails findCardCustomerDetailsByCif(long cif);
	//manager-
	public List<CreditCardCustomerDetails> findAllUderProcessCustomers();
	public CreditCard approveCreditCard(CreditCard creditCard);
	public CreditCard blockCreditCard(CreditCard creditCard);
	public CreditCard unBlockCreditCard(CreditCard creditCard);
	public boolean removeCreditCard(CreditCard creditCard);
	//customer-
	public CreditCardCustomerDetails applyCreditCard(String mobileNumber);
	public CreditCardCustomerDetails findCardCustomerDetailsByMobileNumber(String mobileNumber);
	public CreditCard setPin(int pin,CreditCard creditCard);
	public CreditCardTransaction payment(CreditCard creditCard, CreditCardTransaction transaction);
	public ResponseEntity<?>  billRepayment(long cif,long accountNumber) ;
	public CreditCard findCreditCardByCif(long cif);
	public List<CreditCardTransaction> findCreditCardTransactionHystory(CreditCard creditCard);
	//user
	public long findCifByMobileNumber(String mobileNumber);
	public CreditCard findCreditCardByMobileNumber(String mobileNumber);
	public CardPayment findPayment(long cardNumber);
	
	public boolean deleteCreditCardCustomerDetails(long cif);
}
