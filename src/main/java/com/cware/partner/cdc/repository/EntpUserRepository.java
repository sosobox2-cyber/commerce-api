package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.EntpUser;
import com.cware.partner.common.domain.GoodsTarget;


@Repository
public interface EntpUserRepository extends JpaRepository<EntpUser, String> {

	// 회수지주소변경
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from EntpUser eu "
			+ " inner join Goods g on eu.entpCode = g.entpCode and eu.entpManSeq = g.returnManSeq  "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.saleGb = '00' and eu.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			)
	Slice<GoodsTarget> findEntpReturnModifyList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
