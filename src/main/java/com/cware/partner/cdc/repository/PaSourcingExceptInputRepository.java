package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaSourcingExceptInput;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PaSourcingExceptInputRepository extends JpaRepository<PaSourcingExceptInput, String> {

	// 제외소싱상품 예외입점
	@Query(value = "select distinct pe.goodsCode as goodsCode, pe.modifyId as modifyId  "
			+ " from PaSourcingExceptInput pe "
			+ " inner join PaGoodsTarget gt on pe.goodsCode = gt.goodsCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on pe.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where pe.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null ")
	Slice<GoodsTarget> findPaExceptStartGoodsList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

}
