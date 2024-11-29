package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.LockerApplication;
@Repository
public interface LockerApplicationRepository extends JpaRepository<LockerApplication, Long> {
	@Query(value="SELECT * FROM LockerApplication l WHERE l.status = 'pending'", nativeQuery = true)
	public List<LockerApplication> findAllAppliedApplication();
	
	@Query(value="SELECT * FROM LockerApplication l WHERE l.status = 'approved'", nativeQuery = true)
	public List<LockerApplication> findAllVerifiedApplication();
	
	public LockerApplication findByCif(long cif);
}
