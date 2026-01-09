package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.GoodsDt;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface GoodsDtRepository extends JpaRepository<GoodsDt, String> {

	// 단품정보변경
	@Query(value = "select gd.goodsCode as goodsCode, max(gd.modifyId) as modifyId "
			+ " from GoodsDt gd "
			+ " inner join PaGoods p on gd.goodsCode = p.goodsCode "
			+ " inner join PaGoodsTarget gt on gd.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on gd.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where gd.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null"
			+ " group by gd.goodsCode ")
	Slice<GoodsTarget> findGoodsDtModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
