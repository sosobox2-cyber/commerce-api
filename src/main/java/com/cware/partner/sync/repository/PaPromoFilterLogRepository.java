package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaPromoFilterLog;
import com.cware.partner.sync.domain.id.PaPromoFilterId;

@Repository
public interface PaPromoFilterLogRepository extends JpaRepository<PaPromoFilterLog, PaPromoFilterId> {

}

