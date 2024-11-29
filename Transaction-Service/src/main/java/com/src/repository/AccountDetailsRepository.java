package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.src.model.AccountDetails;
@Repository
@EnableJpaRepositories
public interface AccountDetailsRepository extends JpaRepository<AccountDetails ,Long> {
	public AccountDetails findAccoundDetailsByCif(long cif);
//	@Query("SELECT a FROM AccountDetails a LEFT JOIN customerDetails c WHERE c.mobileNumber = :mobileNumber")
	public AccountDetails findAccountByCif(long cif);
	
	public List<AccountDetails> findAllByCif(long cif);

	
}
