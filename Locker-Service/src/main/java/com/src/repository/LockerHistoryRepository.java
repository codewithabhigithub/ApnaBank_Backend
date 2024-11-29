package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.LockerHistory;
@Repository
public interface LockerHistoryRepository extends JpaRepository<LockerHistory, Long> {
	
	public LockerHistory findByCif(long cif);
}
