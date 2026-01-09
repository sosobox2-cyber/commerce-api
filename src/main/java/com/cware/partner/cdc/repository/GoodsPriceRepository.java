package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.GoodsImage;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface GoodsPriceRepository extends JpaRepository<GoodsImage, String> {

	// 가격적용
	@Query(value = "select gp.goodsCode as goodsCode, max(gp.insertId) as modifyId "
			+ " from GoodsPrice gp "
			+ " inner join PaGoodsTarget gt on gp.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on gp.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where not exists (select 1 from PaGoodsPrice pgp where gp.goodsCode = pgp.goodsCode and gt.paCode = pgp.paCode and gp.applyDate = pgp.applyDate and rownum = 1) "
			+ " and gp.applyDate >= :lastCdcDate and gp.applyDate <= current_date "
			+ " and q.goodsCode is null"
			+ " group by gp.goodsCode "
			)
	Slice<GoodsTarget> findPriceApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
