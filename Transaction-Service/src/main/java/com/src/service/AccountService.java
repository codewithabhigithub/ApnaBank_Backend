package com.src.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.src.model.AccountDetails;
import com.src.model.GiftCard;
import com.src.model.GiftCardsOffer;
import com.src.model.OtherBank;
import com.src.model.Transaction;
import com.src.model.User;
import com.src.repository.AccountDetailsRepository;
import com.src.repository.GiftCardRepository;
import com.src.repository.GiftCardsOfferRepository;
import com.src.repository.OtherBankRepository;
import com.src.repository.TransactionRepository;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private AccountDetailsRepository accountDetailsRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private OtherBankRepository otherBankRepository;
	@Autowired
	private GiftCardRepository giftCardRepository;
	@Autowired
	private IUserService userService;
	@Autowired
	private GiftCardsOfferRepository giftCardsOfferRepository;
	Transaction transaction = null;
	AccountDetails accountDetails = null;
	GiftCard giftCard=null;
	
	@Override
	public Transaction withdrawAmount(AccountDetails account, double amounts) {

		@SuppressWarnings("unused")
		boolean bool = false;
		if ((account.getBalance() - account.getMinimumbalanceRequired()) > amounts && amounts > 0) {
			account.setBalance(account.getBalance() - amounts);
			accountDetailsRepository.save(account);
			bool = true;
		} else {
			bool = false;
		}
		account = accountDetailsRepository.findById(account.getAccountNumber()).get();

		transaction = Transaction.builder()
				.senderAccountNumber(account.getAccountNumber())
				.amount(amounts)
				.transactionType("debit")
				.dateOfTransaction(LocalDate.now())
				.description("Bank Withdrawl")
				.remainingBalance(account.getBalance())
				.build();
		if(bool) {
			transaction.setStatus("approved");
		}
		else {
			transaction.setStatus("failure");
		}
		return transactionRepository.save(transaction);
	}

	@Override
	public Transaction depositAmount(AccountDetails account, double amounts) {
		
			account.setBalance(account.getBalance() + amounts);
			accountDetailsRepository.save(account);
			
			account = accountDetailsRepository.findById(account.getAccountNumber()).get();
			transaction = Transaction.builder()
					.senderAccountNumber(account.getAccountNumber())
					.amount(amounts)
					.transactionType("credit")
					.dateOfTransaction(LocalDate.now())
					.description("Bank Deposit")
					.status("approved")
					.remainingBalance(account.getBalance())
					.build();
			return transactionRepository.save(transaction);
	}

	@Override
	public Transaction transferAmountToOtherBank(AccountDetails account, OtherBank otherBank, double amounts) {
		if(amounts<50000) {	
			account.setBalance(account.getBalance() - amounts);
			accountDetailsRepository.save(account);
			Optional<OtherBank> optionalOtherBank = otherBankRepository.findById(otherBank.getAccountNumber());

			if(!optionalOtherBank.isPresent()) {
				otherBank.setBalance(otherBank.getBalance()+amounts);
				OtherBank other=OtherBank.builder()
						.accountNumber(otherBank.getAccountNumber())
						.balance(amounts)
						.bankName(otherBank.getBankName())
						.branchName(otherBank.getBranchName())
						.ifsc(otherBank.getIfsc())
						.build();
				otherBankRepository.save(other);	
			}else {
				OtherBank otherBankDetails = optionalOtherBank.get();
				otherBankDetails.setBalance(otherBankDetails.getBalance()+amounts);
				System.out.println("from service:" +otherBank);
				otherBankRepository.save(otherBankDetails);
			}
				
		account = accountDetailsRepository.findById(account.getAccountNumber()).get();
		transaction = Transaction.builder()
				.senderAccountNumber(account.getAccountNumber())
				.receiverAccountNumber(otherBank.getAccountNumber())
				.receiverBankName(otherBank.getBankName())
				.amount(amounts)
				.dateOfTransaction(LocalDate.now())
				.description("Transfer to "+otherBank.getAccountNumber())
				.status("approved")
				.transactionType("debit")
				.remainingBalance(account.getBalance())
				.build();
		return transactionRepository.save(transaction);
		}
		else {
			account.setBalance(account.getBalance() - amounts);
			accountDetailsRepository.save(account);
			account=accountDetailsRepository.findById(account.getAccountNumber()).get();
			transaction = Transaction.builder()
					.senderAccountNumber(account.getAccountNumber())
					.receiverAccountNumber(otherBank.getAccountNumber())
					.receiverBankName(otherBank.getBankName())
					.amount(amounts)
					.transactionType("debit")
					.dateOfTransaction(LocalDate.now())
					.description("Transfer to "+otherBank.getAccountNumber())
					.status("pending")
					.remainingBalance(account.getBalance())
					.build();
			return transactionRepository.save(transaction);
		}
			
	}

	@Override
	public Transaction approveTransaction(long transactionId) {
		transaction=transactionRepository.findById(transactionId).get();
		if(transaction.getReceiverBankName().equalsIgnoreCase("AXIS BANK")) {
			accountDetails = accountDetailsRepository.findById(transaction.getReceiverAccountNumber()).get();
			
				accountDetails.setBalance(accountDetails.getBalance()+transaction.getAmount());
				accountDetailsRepository.save(accountDetails);		
				transaction.setStatus("approved");
				return transactionRepository.save(transaction);
						
		}
		else {
			OtherBank otherBank=otherBankRepository.findById(transaction.getReceiverAccountNumber()).get();
			otherBank.setBalance(otherBank.getBalance()+transaction.getAmount());
			transaction.setStatus("approved");
			return transactionRepository.save(transaction);			
		}		
	}

	@Override
	public Transaction rejectTransaction(long transactionId) {
		transaction=transactionRepository.findById(transactionId).get();
		accountDetails=accountDetailsRepository.findById(transaction.getSenderAccountNumber()).get();
		accountDetails.setBalance(accountDetails.getBalance()+transaction.getAmount());
		accountDetailsRepository.save(accountDetails);
		transaction.setStatus("rejected");
		transactionRepository.save(transaction);
		
		accountDetails=accountDetailsRepository.findById(accountDetails.getAccountNumber()).get();
	
	Transaction repayment = Transaction.builder()
			.senderAccountNumber(accountDetails.getAccountNumber())
			.receiverAccountNumber(accountDetails.getAccountNumber())
			.receiverBankName("AXIS BANK")
			.amount(transaction.getAmount())
			.transactionType("credit")
			.dateOfTransaction(LocalDate.now())
			.description("Refund")
			.status("Refund")
			.remainingBalance(accountDetails.getBalance())
			.build();
	return transactionRepository.save(repayment);
	}

	@Override
	public Transaction transferAmountToAxisBank(AccountDetails senderAccount, AccountDetails receiverAccount,
			double amounts) {
		if(amounts<50000) {	
			senderAccount.setBalance(senderAccount.getBalance() - amounts);
			accountDetailsRepository.save(senderAccount);
			receiverAccount.setBalance(receiverAccount.getBalance()+amounts);
			accountDetailsRepository.save(receiverAccount);		
			senderAccount = accountDetailsRepository.findById(senderAccount.getAccountNumber()).get();
			
//			transaction = Transaction.builder()
//					.senderAccountNumber(senderAccount.getAccountNumber())
//					.receiverAccountNumber(receiverAccount.getAccountNumber())
//					.receiverBankName("AXIS BANK")
//					.amount(amounts)
//					.dateOfTransaction(LocalDate.now())
//					.description("Received From "+senderAccount.getAccountNumber())
//					.status("approved")
//					.transactionType("credit")
//					.remainingBalance(senderAccount.getBalance())
//					.build();
//			transactionRepository.save(transaction);
			
			transaction = Transaction.builder()
				.senderAccountNumber(senderAccount.getAccountNumber())
				.receiverAccountNumber(receiverAccount.getAccountNumber())
				.receiverBankName("AXIS BANK")
				.amount(amounts)
				.dateOfTransaction(LocalDate.now())
				.description("Transfer to "+receiverAccount.getAccountNumber())
				.status("approved")
				.transactionType("debit")
				.remainingBalance(senderAccount.getBalance())
				.build();
		return transactionRepository.save(transaction);
		}
		else {
			senderAccount.setBalance(senderAccount.getBalance() - amounts);
			accountDetailsRepository.save(senderAccount);
			senderAccount=accountDetailsRepository.findById(senderAccount.getAccountNumber()).get();
			transaction = Transaction.builder()
					.senderAccountNumber(senderAccount.getAccountNumber())
					.receiverAccountNumber(receiverAccount.getAccountNumber())
					.receiverBankName("AXIS BANK")
					.amount(amounts)
					.transactionType("debit")
					.dateOfTransaction(LocalDate.now())
					.description("Transfer to "+receiverAccount.getAccountNumber())
					.status("pending")
					.remainingBalance(senderAccount.getBalance())
					.build();
			return transactionRepository.save(transaction);
		}			
	}

	@Override
	public AccountDetails findAccountDetailsByAccountNumber(long accountNumber) {
		
		try{
			return accountDetailsRepository.findById(accountNumber).get();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public AccountDetails findAccountDetailsByMobileNumber(String mobileNumer) {
		
		User user=userService.findByMobileNumber(mobileNumer);
		AccountDetails accountDetails=accountDetailsRepository.findAccoundDetailsByCif(user.getUserId());
		
		return accountDetails;
	}

	@Override
	public List<Transaction> findAllPendingTransaction() {
		
		return transactionRepository.findTransactionByStatus();
	}

	@Override
	public List<Transaction> findAllTranasction() {
		
		return transactionRepository.findAll();
	}

	@Override
	public List<Transaction> findTransactionBySenderAccountNumber(long accountNumber) {
		// TODO Auto-generated method stub
		return transactionRepository.findTransactionBySenderAccountNumber(accountNumber);
	}

	@Override
	public List<Transaction> findAllTransactionOfDateOfTransaction(LocalDate date) {
		
		return transactionRepository.findAllTransactionByDateOfTransaction(date);
	}

	@Override
	public GiftCard createGiftCard(GiftCard card , long accountNumber,double discount) {
		double discountAmount=((card.getAmount()*discount)/100);
		accountDetails=accountDetailsRepository.findById(accountNumber).get();
		System.out.println("Account "+accountDetails+"\n " +card);
		if((accountDetails.getBalance()-(card.getAmount())-discountAmount)>accountDetails.getMinimumbalanceRequired() && card.getAmount()<=10000) {
			accountDetails.setBalance(accountDetails.getBalance()-(card.getAmount()-discountAmount));
			System.out.println(accountDetails.getBalance());
			accountDetails= accountDetailsRepository.save(accountDetails);
			System.out.println(("datattaxa"));
			giftCard=GiftCard.builder()
					.recname(card.getRecname())
					.amount(card.getAmount())
					.cif(accountDetails.getCif())
					.giftCardName(card.getGiftCardName())
					.redeemed(false)
					.build();
		 giftCard =	giftCardRepository.save(giftCard);
//		 giftCard=redeemGiftCard(giftCard.getId());
			
			transaction = Transaction.builder()
					.senderAccountNumber(accountDetails.getAccountNumber())
					.receiverAccountNumber(giftCard.getId())
					.receiverBankName("AXIS BANK")
					.amount(card.getAmount()-discountAmount)
					.transactionType("debit")
					.dateOfTransaction(LocalDate.now())
					.description(card.getGiftCardName()+" Gift Card Amount")
					.status("approved")
					.remainingBalance(accountDetails.getBalance())
					.build();
			transactionRepository.save(transaction);
			return giftCard;
		}else
		return null;
	}

	@Override
	public GiftCard redeemGiftCard(long giftCardId) {
		giftCard=giftCardRepository.findById(giftCardId).get();
		if(giftCard.isRedeemed()) {
			return null;
		}else {
			giftCard.setRedeemed(true);
			return giftCardRepository.save(giftCard);		
			}
	}

	@Override
	public List<AccountDetails> findAccountByMobileNumber(String mobileNumber) {
		User user=userService.findByMobileNumber(mobileNumber);
		return accountDetailsRepository.findAllByCif(user.getUserId());
	}

	@Override
	public List<AccountDetails> findAccoundsByCif(long cif) {
		// TODO Auto-generated method stub
		return accountDetailsRepository.findAllByCif(cif);
	}

	@Override
	public OtherBank findOtherBankByAccountNumber(long accountNumber) {
		try {
			return otherBankRepository.findById(accountNumber).get();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<GiftCard> findAllGiftCard(String mobileNumber) {
		
		// TODO Auto-generated method stub
		return giftCardRepository.findAllByCif(userService.findByMobileNumber(mobileNumber).getUserId());
	}

	@Override
	public GiftCardsOffer createGiftCardsOffer(GiftCardsOffer giftCardsOffer) {
		// TODO Auto-generated method stub
		return giftCardsOfferRepository.save(giftCardsOffer);
	}

	@Override
	public GiftCardsOffer updateGiftCardsOffer(GiftCardsOffer cardsOffer) {
		// TODO Auto-generated method stub
		return giftCardsOfferRepository.save(cardsOffer);
	}

	@Override
	public List<GiftCardsOffer> findAllGiftCardsOffer() {
		// TODO Auto-generated method stub
		return giftCardsOfferRepository.findAll();
	}

	@Override
	public void deleteGiftCardsOffer(long id) {
		giftCardsOfferRepository.deleteById(id);
		
	}
	
	}

