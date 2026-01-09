package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.ShipCostDt;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface ShipCostDtRepository extends JpaRepository<ShipCostDt, String> {

	// 배송비적용
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from ShipCostDt sc "
			+ " inner join Goods g on sc.entpCode = case when g.delyType = '10' then '100001' else g.entpCode end and sc.shipCostCode = g.shipCostCode "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.saleGb = '00' and sc.applyDate >= :lastCdcDate and sc.applyDate <= current_date  "
			+ " and q.goodsCode is null "
			)
	Slice<GoodsTarget> findShipCostApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
