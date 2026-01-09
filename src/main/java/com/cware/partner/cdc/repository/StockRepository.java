package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.OrderStock;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface StockRepository extends JpaRepository<OrderStock, String> {

	// 최대주문가능수량 변경
	@Query(value = "select /*+LEADING(s q) INDEX(gt IDX_TPAGOODSTARGET_02)*/  s.goods_Code as goodsCode, max(s.modify_Id) as modifyId "
			+ " from TInPlanQty s "
			+ " inner join TPaProduct p on s.goods_Code = p.goods_Code "
			+ " inner join TPaGoodsTarget gt on s.goods_Code = gt.goods_Code and gt.pa_Sale_Gb is not null and gt.pa_Group_Code in :paGroupCodes "
			+ " left outer join TPaCdcGoodsSnapshot q on s.goods_Code = q.goods_Code and q.cdc_Snapshot_No = :cdcSnapshotNo "
			+ " where current_date between s.start_Date and s.end_Date "
			+ " and s.start_Date >= :lastCdcDate  "
			+ " and q.goods_Code is null "
			+ " group by s.goods_Code "
			,nativeQuery = true)
	Slice<GoodsTarget> findStockApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


	// 주문재고 변경
	@Query(value = "select /*+LEADING(s q) INDEX(gt IDX_TPAGOODSTARGET_02)*/  s.goods_Code as goodsCode, max(s.modify_Id) as modifyId "
			+ " from TOrderStock s "
			+ " inner join TPaProduct p on s.goods_Code = p.goods_Code "
			+ " inner join TPaGoodsTarget gt on s.goods_Code = gt.goods_Code and gt.pa_Sale_Gb is not null and gt.pa_Group_Code in :paGroupCodes "
			+ " left outer join TPaCdcGoodsSnapshot q on s.goods_Code = q.goods_Code and q.cdc_Snapshot_No = :cdcSnapshotNo "
			+ " where s.modify_Date >= :lastCdcDate "
			+ " and q.goods_Code is null "
			+ " group by s.goods_Code "
			,nativeQuery = true)
	Slice<GoodsTarget> findStockChangeTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


}
