package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.CardPayment;
@Repository
public interface PaymentRepository extends JpaRepository<CardPayment, Long> {

}
