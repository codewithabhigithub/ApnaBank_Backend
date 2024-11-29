package com.src.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.src.model.AccountDetails;
import com.src.model.Locker;
import com.src.model.LockerApplication;
import com.src.model.LockerHistory;
import com.src.model.Transaction;
import com.src.repository.AccountDetailsRepository;
import com.src.repository.LockerApplicationRepository;
import com.src.repository.LockerHistoryRepository;
import com.src.repository.LockerRepository;
import com.src.repository.TransactionRepository;
import com.src.repository.UserRepository;

import lombok.NoArgsConstructor;


@Service
public class LockerService implements ILockerService {

	@Autowired
	private LockerRepository lockerRepository;
	@Autowired
	private LockerHistoryRepository historyRepository;
	@Autowired
	private LockerApplicationRepository applicationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountDetailsRepository accountDetailsRepository;
	@Autowired
	private TransactionRepository transactionRepository;
//	LockerService lk=new LockerService();
	
	Locker locker=null;
	LockerHistory lockerHistory=null;
	LockerApplication lockerApplication=null;
	AccountDetails accountDetails=null;
	Transaction transaction=null;
	
	@Override
	public LockerApplication apply(int duration, long cif) {
		lockerApplication=applicationRepository.findByCif(cif);
		if(lockerApplication!=null)
			return null;
		lockerApplication=LockerApplication.builder()
				.appliedDate(LocalDate.now())
				.cif(cif)
				.duration(duration)
				.status("pending")
				.build();
		int charges=0;
		if(duration<=5)
			charges=5000;
		else if(duration<=10)
			charges=10000;
		else if(duration<=15)
			charges=13500;
		lockerApplication.setCharges(charges);
		return applicationRepository.save(lockerApplication);
			}

	@Override
	public LockerApplication status(long cif) {
		try{
			return	applicationRepository.findByCif(cif);
		}catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public boolean payment(double amount,long cif) {
		List<AccountDetails> list=accountDetailsRepository.findByCif(cif);
	
		lockerApplication=applicationRepository.findByCif(cif);
		accountDetails=accountDetailsRepository.findByCif(cif).get(0);
		System.out.println(accountDetails.getBalance() +" "+ accountDetails.getMinimumbalanceRequired());
		if((accountDetails.getBalance()-amount)>accountDetails.getMinimumbalanceRequired() && lockerApplication.getStatus().equalsIgnoreCase("pending")) {
			accountDetails.setBalance(accountDetails.getBalance()-amount);
		accountDetails= accountDetailsRepository.save(accountDetails);
		transaction=Transaction.builder()
				.amount(amount)
				.dateOfTransaction(LocalDate.now())
				.description("Locker Charges Payment")
				.remainingBalance(accountDetails.getBalance())
				.senderAccountNumber(accountDetails.getAccountNumber())
				.status("approved")
				.transactionType("debit")
				.build();
		transactionRepository.save(transaction);
		
		//lockerApplication.setPaymentStatus(true);
		//lockerApplication.setStatus("verified and payment done");
		return true;		
		}
		return false;
	}

	@Override
	public List<LockerApplication> findAllAppliedApplication() {
		// TODO Auto-generated method stub
		return applicationRepository.findAllAppliedApplication();
	}

	@Override
	public LockerApplication verify(long cif) {
		lockerApplication=applicationRepository.findByCif(cif);
		if(lockerApplication!=null) {
			lockerApplication.setStatus("verified and pending for payment");
			return applicationRepository.save(lockerApplication);
		}else
		return null;
	}

	@Override
	public void reject(long id) {
	
			 applicationRepository.deleteById(id);
	
	}

	@Override
	public List<LockerApplication> findAllVerifiedApplication() {
		// TODO Auto-generated method stub
		return applicationRepository.findAllVerifiedApplication();
	}

	@Override
	public List<Locker> findAllEmptyLockers() {
		// TODO Auto-generated method stub
		return lockerRepository.findAllEmptyLocker();
	}

	@Override
	public List<Locker> findAllAssignedLockers() {
		// TODO Auto-generated method stub
		return lockerRepository.findAllAssignedLocker();
	}
	
	
	@Override
	public LockerHistory assignLocker(LockerApplication application) {
		//if(application.getStatus().equalsIgnoreCase("verified and payment done") && application.isPaymentStatus()) {
//		    lk=new LockerService();
			List<Locker> list=lockerRepository.findAll();
			System.out.println(list.size());
			for (Locker locker : list) {
				System.out.println(locker);
				if(!locker.isOccupied()) {
					System.out.println("mohit");
//					verify(application.getCif());
					boolean d=payment(application.getCharges(), application.getCif());
//					if(d)
//					{
					System.out.println("value of da is "+d);
					application.setStatus("approved");
					application.setPaymentStatus(true);
					applicationRepository.save(application);
					
					
					locker.setCif(application.getCif());
					locker.setDuration(application.getDuration());
					locker.setOccupied(true);
					locker.setStatus("assigned");
					System.out.println(locker.getStatus());
					lockerRepository.save(locker);
					
					
					lockerHistory=LockerHistory.builder()
							.charges(application.getCharges())
							.cif(application.getCif())
							.issuedDate(LocalDate.now())
							.endDate(LocalDate.now().plusYears(application.getDuration()))
							.lockerId(locker.getLockerId())
							.build();
					return historyRepository.save(lockerHistory);
					}
//				}
			}
			return null;
	
				
	}
	@Override
	public Locker removeLocker(long lockerId,long cif) {
		locker=lockerRepository.findByCif(cif);
		if(locker!=null && locker.getLockerId()==lockerId && locker.getStatus().equalsIgnoreCase("assigned")) {
			locker.setCif(0);
			locker.setDuration(0);
			locker.setOccupied(false);
			locker.setStatus("Empty");
			
			lockerHistory=historyRepository.findByCif(cif);
			lockerHistory.setEndDate(LocalDate.now());
			historyRepository.save(lockerHistory);
			
			return lockerRepository.save(locker);
			
		}else
		return null;
	}

	@Override
	public List<Locker> createLockers(int count) {
		List<Locker> list=new ArrayList<>();
			for(int i=1;i<=count;i++) {
				String name="Locker:No_"+i;
				locker=Locker.builder()
						.lockerNumber(name)
						.status("Empty")
						.build();
				list.add(lockerRepository.save(locker));	
			}
			return list;
		
	}

	@Override
	public Locker findLockerByCif(long cif) {
		// TODO Auto-generated method stub
		return lockerRepository.findByCif(cif);
	}

	@Override
	public long findCifByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return userRepository.findByMobileNumber(mobileNumber).getUserId();
	}

	@Override
	public LockerApplication findApplicationByCif(long cif) {
		// TODO Auto-generated method stub
		return applicationRepository.findByCif(cif);
	}

	@Override
	public void deleteLocker(long id) {
		// TODO Auto-generated method stub
	 lockerRepository.deleteById(id);
	}

}