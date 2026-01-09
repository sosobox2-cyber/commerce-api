package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaGoodsKindsMapping;
import com.cware.partner.cdc.domain.id.PaLmsdId;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PaGoodsKindsMappingRepository extends JpaRepository<PaGoodsKindsMapping, PaLmsdId> {
	
	// 카테고리 매핑 변경(for 11번가, 위메프, 티몬, 카카오)
	@Query(value = "select /*+ LEADING(gm, g, gt) */ g.goods_code as goodsCode, max(gm.modify_id) as modifyId "
			+ " from tgoods g "
			+ " inner join tpagoodstarget gt on g.goods_code = gt.goods_code and gt.pa_sale_gb is not null and gt.pa_goods_code is not null and gt.pa_group_code in :paGroupCodes "
			+ " inner join tpagoodskindsmapping  gm on gt.pa_group_code = gm.pa_group_code and g.lmsd_code = gm.lmsd_code "
			+ " left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
			+ " where gm.modify_date >= :lastCdcDate " 
			+ " and q.goods_code is null "
			+ " group by g.goods_code ", nativeQuery = true)
	Slice<GoodsTarget> findPaGoodsKindsMappingChangeTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	//이베이 카테고리 매핑 변경
	@Query(value="select  /*+ LEADING(gm, g, gt) */ g.goods_code as goodsCode, max(gm.modify_id) as modifyId "
			+ " from tgoods g "
			+ " inner join tpagoodstarget gt on g.goods_code = gt.goods_code and gt.pa_sale_gb is not null and gt.pa_goods_code is not null and gt.pa_group_code in :paGroupCodes "
			+ " inner join tpaesmgoodskindsmapping gm on g.lmsd_code = gm.lmsd_code "
			+ " left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
			+ " where gm.modify_date >= :lastCdcDate "
			+ " and q.goods_code is null "
			+ " group by g.goods_code ", nativeQuery = true)
	Slice<GoodsTarget> findEbayGoodsKindsMappingChangeTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

}
