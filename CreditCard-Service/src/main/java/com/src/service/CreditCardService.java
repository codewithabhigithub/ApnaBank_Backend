package com.src.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.src.model.AccountDetails;
import com.src.model.CreditCard;
import com.src.model.CreditCardCustomerDetails;
import com.src.model.CreditCardTransaction;
import com.src.model.CustomerDetails;
import com.src.model.CardPayment;
import com.src.model.Transaction;
import com.src.repository.AccountDetailsRepository;
import com.src.repository.CreditCardCustomerDetailsRepository;
import com.src.repository.CreditCardRepository;
import com.src.repository.CreditCardTransactionRepository;
import com.src.repository.CustomerDetailsRepository;
import com.src.repository.PaymentRepository;
import com.src.repository.TransactionRepository;
import com.src.repository.UserRepository;

@Service
public class CreditCardService implements ICreditCardService {

	@Autowired
	private CreditCardRepository cardRepository;
	@Autowired
	private CreditCardTransactionRepository cardTransactionRepository;
	@Autowired
	private AccountDetailsRepository accountDetailsRepository;
	@Autowired
	private TransactionRepository accountTransactionRepository;
	@Autowired
	private CustomerDetailsRepository customerDetailsRepository;
	@Autowired
	private CreditCardCustomerDetailsRepository cardCustomerDetailsRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private UserRepository userRepository;

	CreditCard card = null;
	CardPayment cardPayment = null;
	AccountDetails account = null;
	CreditCardTransaction cardTransaction = null;
	CustomerDetails customerDetails = null;
	CreditCardCustomerDetails cardCustomerDetails = null;

	@Override
	public CreditCardCustomerDetails applyCreditCard(String mobileNumber) {
		customerDetails = customerDetailsRepository.findCustomerDetailsByMobileNumber(mobileNumber);
		cardCustomerDetails = CreditCardCustomerDetails.builder()
				.cif(customerDetails.getCif())
				.mobileNumber(customerDetails.getMobileNumber())
				.email(customerDetails.getEmail())
				.firstName(customerDetails.getFirstName())
				.lastName(customerDetails.getLastName())
				.fatherName(customerDetails.getFatherName())
				.motherName(customerDetails.getMotherName())
				.dob(customerDetails.getDob()).gender(customerDetails.getGender())
				.aadharNumber(customerDetails.getAadharNumber()).
				panNumber(customerDetails.getPanNumber())
				.status("Applied")
				.build();

		return cardCustomerDetailsRepository.save(cardCustomerDetails);
	}

	@Override
	public CreditCard preApproval(CreditCardCustomerDetails cardCustomerDetails) {
		System.out.println( "service: "+cardCustomerDetails);
		card = CreditCard.builder()
				.cif(cardCustomerDetails.getCif())
				.cvv(new Random().nextInt(900) + 100)
				.maximumLimit(150000).issuedDate(LocalDate.now()).expiryDate(LocalDate.now().plusYears(5)).status(false)
				.build();
		cardCustomerDetails.setStatus("Under_Process");
		cardCustomerDetailsRepository.save(cardCustomerDetails);
		return cardRepository.save(card);
	}

	@Override
	public CreditCard approveCreditCard(CreditCard creditCard) {
		creditCard.setRemainingLimit(creditCard.getMaximumLimit());
		creditCard.setStatus(true);
		card = cardRepository.save(creditCard);
		cardPayment = CardPayment.builder()
				.cardNumber(card.getCreditCardNumber())
				.billGenerationDate(LocalDate.now())
				.billExpiryDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))
				.build();
		paymentRepository.save(cardPayment);
		CreditCardCustomerDetails cardDetails = cardCustomerDetailsRepository.findByCif(card.getCif());
		cardDetails.setStatus("Approved");
		cardCustomerDetailsRepository.save(cardDetails);
		return card;
	}

	@Override
	public CreditCard blockCreditCard(CreditCard creditCard) {
		creditCard.setStatus(false);
		return cardRepository.save(creditCard);
	}

	@Override
	public CreditCard unBlockCreditCard(CreditCard creditCard) {
		creditCard.setStatus(true);
		return cardRepository.save(creditCard);
	}

	@Override
	public boolean removeCreditCard(CreditCard creditCard) {
		cardRepository.delete(creditCard);
		paymentRepository.deleteById(creditCard.getCreditCardNumber());
		cardCustomerDetailsRepository.deleteById(creditCard.getCif());
		return true;
	}

	@Override
	public CreditCardTransaction payment(CreditCard creditCard, CreditCardTransaction transaction) {
		System.out.println( "BEFORE "+creditCard);
		cardPayment = paymentRepository.findById(creditCard.getCreditCardNumber()).get();
		System.out.println("AFTER "+cardPayment);
		double amount = transaction.getAmount();
		cardTransaction = CreditCardTransaction.builder()
				.amount(transaction.getAmount())
				.creditCardNumber(creditCard.getCreditCardNumber())
				.date(LocalDateTime.now())
				.description(transaction.getDescription())
				.receiverDetails(transaction.getReceiverDetails())
				.remainingLimit(creditCard.getRemainingLimit() - amount)
				.build();
		
		if (LocalDate.now().isBefore(cardPayment.getBillExpiryDate())) {
			cardPayment.setBill(cardPayment.getBill() + transaction.getAmount());
			paymentRepository.save(cardPayment);
			creditCard.setRemainingLimit(creditCard.getRemainingLimit() - amount);
			cardRepository.save(creditCard);
			cardTransaction.setStatus("SUCCESS");
		} else {
			cardTransaction.setStatus("FAILED");
		}
		return cardTransactionRepository.save(cardTransaction);

	}

	@Override
	public ResponseEntity<?>  billRepayment(long cif,long accountNumber)  {
		account = accountDetailsRepository.findById(accountNumber).get();
		card=cardRepository.findCreditCardByCif(cif);
		CardPayment payment=paymentRepository.findById(card.getCreditCardNumber()).get();
		
		try {
			if(account.isStatus()) {
				if( (account.getBalance()-account.getMinimumbalanceRequired()) > (payment.getBill()+payment.getDue()) ) {
					if((payment.getBill()+payment.getDue())>0) {
						account.setBalance(account.getBalance()-(payment.getBill()+payment.getDue()));
						account=accountDetailsRepository.save(account);
						
						Transaction transaction=Transaction.builder()
								.senderAccountNumber(account.getAccountNumber())
								.amount(payment.getBill())
								.dateOfTransaction(LocalDate.now())
								.description("Debit For Credit Card Bill")
								.status("approved")
								.transactionType("debit")
								.remainingBalance(account.getBalance())
								.build();
						accountTransactionRepository.save(transaction);
						
						card.setRemainingLimit(card.getMaximumLimit());
						cardRepository.save(card);
						
						cardTransaction=CreditCardTransaction.builder()
						.amount(payment.getBill())
						.creditCardNumber(card.getCreditCardNumber())
						.date(LocalDateTime.now())
						.description("Credit Card Bill Repayment")
						.remainingLimit(card.getMaximumLimit())
						.status("BILL PAID")
						.build();
						cardTransactionRepository.save(cardTransaction);
						
						payment.setBill(0);
						payment.setDue(0);
						paymentRepository.save(payment);
		                return ResponseEntity.ok("Bill Payment Successful. ");

					}else {
						 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You Don't Have Bill To Pay");
					}
					
				}else {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Fund In Account To Make Payment");
				}
			}else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Customer Account is Blocked");
			}
		}catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@Override
	public CreditCard findCreditCardByCif(long cif) {
		// TODO Auto-generated method stub
		return cardRepository.findCreditCardByCif(cif);
	}

	@Override
	public List<CreditCardTransaction> findCreditCardTransactionHystory(CreditCard creditCard) {
	
		return cardTransactionRepository.findCreditCardTransactionByCreditCardNumber(creditCard.getCreditCardNumber());

	}

	@Override
	public CreditCardCustomerDetails findCardCustomerDetailsByCif(long cif) {
		// TODO Auto-generated method stub
		return cardCustomerDetailsRepository.findByCif(cif);
	}

	@Override
	public CreditCardCustomerDetails findCardCustomerDetailsByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return cardCustomerDetailsRepository.findByMobileNumber(mobileNumber);
	}

	@Override
	public CreditCard setPin(int pin, CreditCard creditCard) {
		creditCard.setPin(pin);
		return cardRepository.save(creditCard);
	}

	@Override
	public long findCifByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return userRepository.findByMobileNumber(mobileNumber).getUserId();
	}

	@Override
	public CreditCard findCreditCardByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return cardRepository.findCreditCardByCif(findCifByMobileNumber(mobileNumber));
	}

	@Override
	public CardPayment findPayment(long cardNumber) {
		// TODO Auto-generated method stub
		return paymentRepository.findById(cardNumber).get();
	}

	@Override
	public List<CreditCardCustomerDetails> findAllAppliedCustomers() {
		// TODO Auto-generated method stub
		return cardCustomerDetailsRepository.findAllAppliedCreditCardCustomerDetails();
	}

	@Override
	public List<CreditCardCustomerDetails> findAllUderProcessCustomers() {
		// TODO Auto-generated method stub
		return cardCustomerDetailsRepository.findAllUnderProcessCreditCardCustomerDetails();
	}

	@Override
	public boolean deleteCreditCardCustomerDetails(long cif) {
		
		 cardCustomerDetailsRepository.deleteById(cif);
		 return true;
	}




}
