package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.src.model.Locker;
@Repository
public interface LockerRepository extends JpaRepository<Locker, Long> {
	
	public Locker findByCif(long cif);
//	 @Query("SELECT l FROM Locker l WHERE l.cif = :?1")
//	    Locker findByCif(long cif);
	
	@Query(value="SELECT * FROM Locker l WHERE l.status = 'Empty'", nativeQuery = true)
	public List<Locker> findAllEmptyLocker();
	
	@Query(value="SELECT * FROM Locker l WHERE l.status = 'assigned'", nativeQuery = true)
	public List<Locker> findAllAssignedLocker();
}
