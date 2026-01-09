package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaQeenGoodsKindsOffer;

@Repository
public interface PaQeenGoodsKindsOfferRepository extends JpaRepository<PaQeenGoodsKindsOffer, String> {

	
	PaQeenGoodsKindsOffer getByCategoryId(String categoryId);


}