package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaTdealEvent;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PaTdealEventRepository extends JpaRepository<PaTdealEvent, String> {

	// 티딜 행사 적용
	@Query(value = "select te.goodsCode as goodsCode, max(te.modifyId) as modifyId "
			+ " from PaTdealEvent te "
			+ " inner join PaGoodsTarget gt on te.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode = '13' and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on te.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where te.useYn ='1' "
			+ " and (current_date between te.eventBdate and te.eventEdate) "
			+ " and te.eventBdate >= :lastCdcDate " 
			+ " and q.goodsCode is null"
			+ " group by te.goodsCode ")
	Slice<GoodsTarget> findTdeaEventApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 티딜 행사 종료
	@Query(value = "select te.goodsCode as goodsCode, max(te.modifyId) as modifyId "
			+ " from PaTdealEvent te "
			+ " inner join PaGoodsTarget gt on te.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode = '13' and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on te.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where ((te.eventEdate <= current_date and te.eventEdate >= :lastCdcDate) or (te.useYn = '0' and te.modifyDate >= :lastCdcDate)) "
			+ " and q.goodsCode is null"
			+ " group by te.goodsCode ")
	Slice<GoodsTarget> findTdeaEventEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

}
