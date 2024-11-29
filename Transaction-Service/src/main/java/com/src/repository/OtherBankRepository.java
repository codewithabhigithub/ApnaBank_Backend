package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.OtherBank;
@Repository
public interface OtherBankRepository extends JpaRepository<OtherBank, Long> {

	
}
