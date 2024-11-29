package com.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.GiftCardsOffer;
@Repository
public interface GiftCardsOfferRepository extends JpaRepository<GiftCardsOffer, Long> {

}
