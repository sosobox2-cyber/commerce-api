package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.Describe;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface DescribeRepository extends JpaRepository<Describe, String> {

	// 상품기술서
	@Query(value = "select d.goodsCode as goodsCode, max(d.modifyId) as modifyId "
			+ " from Describe d "
			+ " inner join PaGoodsTarget gt on d.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on d.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where d.describeCode in ('200', '998', '999') "
			+ "  and (d.describeExt is not null or d.describeNote is not null)"
			+ "  and d.modifyDate >= :lastCdcDate "
			+ "  and q.goodsCode is null"
			+ " group by d.goodsCode ")
	Slice<GoodsTarget> findDescribeModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


    // 상품기술서 인덱스 힌트 사용을 위해  nativeQuery로 변환
    @Query(value = "select /*+ index(d idx_tdescribe_01) */ d.goods_code as goodsCode, max(d.modify_id) as modifyId "
            + " from tdescribe d "
            + " inner join tpagoodstarget gt on d.goods_code = gt.goods_code and gt.pa_sale_gb is not null and gt.pa_group_code in :paGroupCodes "
            + " left outer join tpacdcgoodssnapshot q on d.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
            + " where d.describe_code in ('200', '998', '999') "
            + "  and (d.describe_ext is not null or d.describe_note is not null)"
            + "  and d.modify_date >= :lastCdcDate "
            + "  and q.goods_code is null"
            + "  and not exists (select 1"
			+ "                    from tgoods tg, temp_entp_except t"
			+ "                   where tg.entp_code = t.entp_code"
			+ "                     and tg.goods_code = d.goods_code)"
            + " group by d.goods_code fetch first :pageSize rows only "
            , nativeQuery = true)
    Slice<GoodsTarget> findDescribeModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, int pageSize, List<String> paGroupCodes);

}
