package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaCopnGoodsKindsFresh;
import com.cware.partner.sync.domain.id.PaCopnGoodsKindsFreshId;

@Repository
public interface PaCopnGoodsKindsFreshRepository extends JpaRepository<PaCopnGoodsKindsFresh, PaCopnGoodsKindsFreshId> {


	
}
