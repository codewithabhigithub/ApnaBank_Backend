package com.src.service;

import java.util.List;

import com.src.model.AccountDetails;
import com.src.model.LoanAccount;
import com.src.model.LoanApplication;
import com.src.model.LoanTransaction;

public interface ILoanService {
	public LoanApplication findApplicationById(long id);
	public AccountDetails findAccountDetailsByAccountNumber(long accountNumber);
	public LoanAccount findLoanAccountById(long accountNumber);
	public long findCifByMobileNumber(String mobileNumber);
	public List<LoanApplication> findAllAppiledApplication();
	public List<LoanApplication> findAllVerifiedApplication();

	public LoanApplication applyLoan(LoanApplication application);
	public LoanApplication verifyApplication(LoanApplication application);
	public LoanAccount sanctionLoan(LoanApplication application,double amount,long accountNumber);
	public LoanApplication rejectLoan(LoanApplication application);
	public LoanTransaction emiPayment(LoanAccount account,AccountDetails accountDetails);
}
