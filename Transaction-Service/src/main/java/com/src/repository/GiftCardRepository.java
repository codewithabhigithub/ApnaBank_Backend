package com.src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.GiftCard;
@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

	public List<GiftCard> findAllByCif(long cif);
}
