package com.cware.partner.cdc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaCdcGoodsSnapshot;

@Repository
public interface PaCdcGoodsSnapshotRepository extends JpaRepository<PaCdcGoodsSnapshot, String> {

}
