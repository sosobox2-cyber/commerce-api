package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaTargetExcept;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PaTargetExceptRepository extends JpaRepository<PaTargetExcept, String> {

	// 상품입점제외
	@Query(value = "select distinct pte.targetCode as goodsCode, pte.insertId as modifyId  "
			+ " from PaTargetExcept pte "
			+ " inner join PaGoodsTarget gt on pte.targetCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on pte.targetCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where pte.targetGb = '00' and (pte.paGroupCodeAllYn = '1' or instr(pte.paGroupCode, gt.paGroupCode) > 0) "
			+ "  and pte.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null ")
	Slice<GoodsTarget> findPaTargetExceptGoodsList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 업체입점제외
	@Query(value = "select distinct g.goodsCode as goodsCode, pee.modifyId as modifyId  "
			+ " from PaExceptEntp pee "
			+ " inner join Goods g on g.entpCode = pee.entpCode "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where pee.allBrandYn = '1' and pee.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null ")
	Slice<GoodsTarget> findPaExceptEntpGoodsList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 업체브랜드입점제외
	@Query(value = "select distinct g.goodsCode as goodsCode, peb.modifyId as modifyId  "
			+ " from PaExceptBrand peb "
			+ " inner join Goods g on g.entpCode = peb.entpCode and g.brandCode = peb.brandCode "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where peb.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null ")
	Slice<GoodsTarget> findPaExceptBrandGoodsList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
