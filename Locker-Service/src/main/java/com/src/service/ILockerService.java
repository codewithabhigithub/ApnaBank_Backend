package com.src.service;

import java.util.List;

import com.src.model.Locker;
import com.src.model.LockerApplication;
import com.src.model.LockerHistory;

public interface ILockerService {
	//user
	public LockerApplication apply(int duration, long cif);
	public LockerApplication status(long cif);
	public boolean payment(double amount,long cif);
	public Locker findLockerByCif(long cif);
	public long findCifByMobileNumber(String mobileNumber);
	//employee
	public List<LockerApplication > findAllAppliedApplication();
	public LockerApplication findApplicationByCif(long cif);
	public LockerApplication verify(long cif);
	public void reject(long id);
	//manager
	public List<LockerApplication> findAllVerifiedApplication();
	public List<Locker> findAllEmptyLockers();
	public List<Locker> findAllAssignedLockers();
	public LockerHistory assignLocker(LockerApplication application);
	public Locker removeLocker(long lockerId,long cif);
	//admin
	public List<Locker> createLockers(int count);
	public void deleteLocker(long id);
}
