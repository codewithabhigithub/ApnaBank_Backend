package com.src.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.src.model.AccountDetails;
import com.src.model.LoanAccount;
import com.src.model.LoanApplication;
import com.src.model.LoanTransaction;
import com.src.model.Transaction;
import com.src.repository.AccountDetailsRepository;
import com.src.repository.LoanAccountRepository;
import com.src.repository.LoanApplicationRepository;
import com.src.repository.LoanTransactionRepository;
import com.src.repository.TransactionRepository;
import com.src.repository.UserRepository;

@Service
public class LoanService implements ILoanService {
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	@Autowired
	private LoanAccountRepository loanAccountRepository;
	@Autowired
	private LoanTransactionRepository loanTransactionRepository;
	@Autowired
	private AccountDetailsRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserRepository userRepository;
	
	LoanApplication loanApplication=null;
	LoanAccount loanAccount =null;
	LoanTransaction loanTransaction=null;
	AccountDetails accountDetails=null;
	Transaction transaction=null;
	@Override
	public LoanApplication applyLoan(LoanApplication application) {
		List<LoanApplication> loans=loanApplicationRepository.findByCif(application.getCif());
		for (LoanApplication loanApplication : loans) {
			if(loanApplication.getLoanType().equalsIgnoreCase(application.getLoanType()))
				return null;
		}
			application.setStatus("pending");
			application.setVerificationStatus(false);
			application.setApplyDate(LocalDate.now());
			return loanApplicationRepository.save(application);
	}

	@Override
	public LoanApplication verifyApplication(LoanApplication application) {
		List<LoanApplication> loanApplications=loanApplicationRepository.findByCif(application.getCif());
		if(loanApplications.size()>=1)
		for (LoanApplication loans : loanApplications) {
			if(loans.isVerificationStatus())
				if(loans.getLoanType().equalsIgnoreCase(application.getLoanType()))
						return null;
		}
		application.setVerificationStatus(true);
		return loanApplicationRepository.save(application);
	}

	@Override
	public LoanAccount sanctionLoan(LoanApplication application,double amount, long accountNumber) {
		
		if(application.isVerificationStatus()) {
			double emiCalculater=(amount+((amount*13)/100))/application.getDuration();
			double pendingAmountCalculater=(amount+((amount*13)/100));
		application.setStatus("approved");
		loanAccount=LoanAccount.builder()
				.cif(application.getCif())
				.duration(application.getDuration())
				.emiStartDate(LocalDate.now())
				.emiEndDate(LocalDate.now().plusDays(10))
				.intrestRate(13)
				.sanctionedAmount(amount)
				.emi(emiCalculater)
				.startDate(LocalDate.now())
				.endEnd(LocalDate.now().plusMonths(application.getDuration()))
				.pendingAmount(pendingAmountCalculater)
				.loanType(application.getLoanType())
				.build();
		
		loanApplicationRepository.save(application);
		
		accountDetails=accountRepository.findById(accountNumber).get();
		if(!accountDetails.isStatus() && accountDetails.getCif()==application.getCif())
			return null;
		accountDetails.setBalance(accountDetails.getBalance()+amount);
				
		accountDetails= accountRepository.save(accountDetails);		
		transaction=Transaction.builder()
				.amount(amount)
				.dateOfTransaction(LocalDate.now())
				.description("Loan Balance credited")
				.remainingBalance(accountDetails.getBalance())
				.senderAccountNumber(accountDetails.getAccountNumber())
				.status("approved")
				.transactionType("credit")
				.build();
		transactionRepository.save(transaction);
		
		return loanAccountRepository.save(loanAccount);
		}
		else {
		return null;
		}
	}

	@Override
	public LoanApplication rejectLoan(LoanApplication application) {
		application.setStatus("rejected");
		return loanApplicationRepository.save(application);
	}

	@Override
	public LoanTransaction emiPayment(LoanAccount account, AccountDetails bankAccount) {
		if((bankAccount.getBalance()-account.getEmi())>bankAccount.getMinimumbalanceRequired()) {
			bankAccount.setBalance(bankAccount.getBalance()-account.getEmi());
		accountDetails= accountRepository.save(bankAccount);
		transaction=Transaction.builder()
				.amount(account.getEmi())
				.dateOfTransaction(LocalDate.now())
				.description("Loan EMI Payment")
				.remainingBalance(accountDetails.getBalance())
				.senderAccountNumber(accountDetails.getAccountNumber())
				.receiverAccountNumber(account.getAccountNumber())
				.status("success")
				.transactionType("debit")
				.build();
		transactionRepository.save(transaction);
		
		account.setPendingAmount(account.getPendingAmount()-account.getEmi());
		account.setEmiStartDate(account.getEmiStartDate().plusMonths(1));
		account.setEmiEndDate(account.getEmiStartDate().plusMonths(1).plusDays(10));
		loanAccountRepository.save(account);
		
		loanTransaction=LoanTransaction.builder()
				.amount(account.getEmi())
				.LoanAccountNumber(account.getAccountNumber())
				.dateOfTransaction(LocalDate.now())
				.loanType(account.getLoanType())
				.status("success")
				.build();
		System.out.println(loanTransaction);
		return loanTransactionRepository.save(loanTransaction);
		}
		else {
			return null;
		}
	}

	@Override
	public LoanApplication findApplicationById(long id) {
		
		return loanApplicationRepository.findById(id).get();
	}

	@Override
	public AccountDetails findAccountDetailsByAccountNumber(long accountNumber) {
		
		try{
			return accountRepository.findById(accountNumber).get();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public long findCifByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		 return userRepository.findByMobileNumber(mobileNumber).getUserId();
	}

	@Override
	public List<LoanApplication> findAllAppiledApplication() {
		// TODO Auto-generated method stub
		return loanApplicationRepository.findAllAppliedApplication();
	}

	@Override
	public List<LoanApplication> findAllVerifiedApplication() {
		// TODO Auto-generated method stub
		return loanApplicationRepository.findAllVerifiedApplication();
	}

	@Override
	public LoanAccount findLoanAccountById(long accountNumber) {
		// TODO Auto-generated method stub
		try{
			return loanAccountRepository.findById(accountNumber).get();
		}catch (Exception e) {
			return null;
		}
	}


}