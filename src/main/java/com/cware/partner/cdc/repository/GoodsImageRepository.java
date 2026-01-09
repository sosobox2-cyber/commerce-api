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
public interface GoodsImageRepository extends JpaRepository<GoodsImage, String> {

	// 상품이미지
	@Query(value = "select distinct i.goodsCode as goodsCode, i.modifyId as modifyId "
			+ " from GoodsImage i "
			+ " inner join PaGoodsTarget gt on i.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on i.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where i.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null")
	Slice<GoodsTarget> findGoodsImageModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 이베이,쿠팡,티딜 대표이미지/ 티딜 리스트이미지 
	@Query(value = "select distinct i.goodsCode as goodsCode, i.modifyId as modifyId "
			+ " from GoodsInfoImage i "
			+ " inner join PaGoodsTarget gt on i.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in ('02', '03', '05', '13') and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on i.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where ((gt.paGroupCode = '05' and i.infoImageType = '106') "
			+ "         or (gt.paGroupCode = '13' and i.infoImageType in ('201','40','202')) "
			+ "         or (gt.paGroupCode in ('02','03') and i.infoImageType = '200')) "
			+ " and i.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null")
	Slice<GoodsTarget> findGoodsInfoImageModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 티딜 옵션별 이미지
	@Query(value = "select i.goodsCode as goodsCode, max(i.modifyId) as modifyId "
			+ " from PaTdealGoodsdtImage i "
			+ " inner join PaGoodsTarget gt on i.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode = '13' and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on i.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where i.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null"
			+ " group by i.goodsCode ")
	Slice<GoodsTarget> findTdealGoodsdtImageModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

}
