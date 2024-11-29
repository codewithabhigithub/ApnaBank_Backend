package com.src.service;

import java.time.LocalDate;
import java.util.List;

import com.src.model.AccountDetails;
import com.src.model.GiftCard;
import com.src.model.GiftCardsOffer;
import com.src.model.OtherBank;
import com.src.model.Transaction;

public interface IAccountService {

	public Transaction withdrawAmount(AccountDetails accountDetails, double amounts);
	public Transaction depositAmount(AccountDetails accountDetails, double amounts);
	
	public Transaction transferAmountToAxisBank(AccountDetails senderAccount , AccountDetails receiverAccount, double amount);
	public Transaction transferAmountToOtherBank(AccountDetails accountDetails , OtherBank otherBank, double amount);
	
	public Transaction approveTransaction(long transactionId);
	public Transaction rejectTransaction(long transactionId);
	
	public List<Transaction> findAllPendingTransaction();
	public List<Transaction> findAllTranasction();
	public List<Transaction> findTransactionBySenderAccountNumber(long accountNumber);
	public List<Transaction> findAllTransactionOfDateOfTransaction(LocalDate date);

	public AccountDetails findAccountDetailsByAccountNumber(long accountNumber);
	public AccountDetails findAccountDetailsByMobileNumber(String mobileNumer);
	
	public List<AccountDetails> findAccoundsByCif(long cif);
	public List<AccountDetails> findAccountByMobileNumber(String mobileNumber);
	public GiftCard createGiftCard(GiftCard card, long accountNumber,double discount);
	public GiftCard redeemGiftCard(long giftCardId);
	public GiftCardsOffer createGiftCardsOffer(GiftCardsOffer giftCardsOffer);
	public GiftCardsOffer updateGiftCardsOffer(GiftCardsOffer cardsOffer);
	public List<GiftCardsOffer> findAllGiftCardsOffer();
	public void deleteGiftCardsOffer(long id);
	
	public OtherBank findOtherBankByAccountNumber(long accountNumber);
	
	public List<GiftCard> findAllGiftCard(String mobileNumber);
	
	
}
