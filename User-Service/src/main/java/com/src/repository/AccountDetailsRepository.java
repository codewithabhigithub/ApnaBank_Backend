package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.AccountDetails;
@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {
public AccountDetails findByCif(long cif);
public List<AccountDetails> findAllByCif(long cif);
}
