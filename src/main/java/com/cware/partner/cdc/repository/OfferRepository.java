package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.Offer;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {

	// 상품정보고시
	@Query(value = "select o.goodsCode as goodsCode, max(o.modifyId) as modifyId "
			+ " from Offer o "
			+ " inner join PaGoodsTarget gt on o.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on o.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where o.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by o.goodsCode ")
	Slice<GoodsTarget> findOfferModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


    // 상품정보고시 인덱스 힌트 사용을 위해  nativeQuery로 변환
    @Query(value = "select /*+ index(o idx_toffer_01) */ o.goods_code as goodsCode, max(o.modify_id) as modifyId "
            + " from toffer o "
            + " inner join tpagoodstarget gt on o.goods_code = gt.goods_code and gt.pa_sale_gb is not null and gt.pa_group_code in :paGroupCodes "
            + " left outer join tpacdcgoodssnapshot q on o.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
            + " where o.modify_date >= :lastCdcDate "
            + " and q.goods_code is null "
            + " group by o.goods_code fetch first :pageSize rows only "
            , nativeQuery = true)
    Slice<GoodsTarget> findOfferModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, int pageSize, List<String> paGroupCodes);
}
