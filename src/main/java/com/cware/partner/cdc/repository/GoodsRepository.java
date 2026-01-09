package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.Goods;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, String> {

	// 상품입점
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId  "
			+ "from GoodsStart g left outer join PaCdcGoods q on g.goodsCode = q.goodsCode "
			+ "inner join GoodsAddInfo ga on g.goodsCode = ga.goodsCode "
			+ "inner join GoodsPrice gp on g.goodsCode = gp.goodsCode "
			+ "inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is null and gt.paGroupCode in :paGroupCodes "
			+ "where ga.emGoodsYn = '0'  " // 구성원상품여부는 변경이 가능하기 때문에 쿼리에서 체크, 새벽배송,해외배송은 변경 불가이므로 입점동기화에서 타게팅 제거하도록 함
			+ "and gp.applyDate = (select max(gps.applyDate) from GoodsPrice gps where gps.goodsCode = g.goodsCode and gps.applyDate <= CURRENT_DATE) "
			+ "and gp.salePrice > 0 "
			+ "and q.goodsCode is null "
			+ "and not exists (select 1 from PaTargetExcept ge where ge.targetGb = '00' and ge.targetCode = g.goodsCode and ge.useYn = '1' "
			+ "                      and (ge.paGroupCodeAllYn = '1' or instr(ge.paGroupCode, gt.paGroupCode) > 0)) "
			+ "and not exists (select 1 from PaExceptEntp ee "
			+ "                         left outer join PaExceptBrand be on be.exceptSeq = ee.exceptSeq and be.entpCode = ee.entpCode and be.useYn = '1' "
			+ "                        where ee.entpCode = g.entpCode and ee.useYn = '1' "
			+ "                          and (ee.paGroupCodeAllYn = '1' or instr(ee.paGroupCode, gt.paGroupCode) > 0) "
			+ "                          and (ee.allBrandYn = '1' or be.brandCode = g.brandCode)"
			+ "                          and (ee.sourcingMedia = '00' or instr(ee.sourcingMedia, g.sourcingMedia) > 0)) "
			)
	Slice<GoodsTarget> findGoodsStartTargetList(Pageable pageRequest, List<String> paGroupCodes);

	// 행사입점
	// 행사입점 전에 타겟에서 제거되는 경우 행사 등록 후 타겟에 등록을 다시 해야함.
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId  "
			+ "from GoodsStart g left outer join PaCdcGoods q on g.goodsCode = q.goodsCode "
			+ "inner join GoodsAddInfo ga on g.goodsCode = ga.goodsCode "
			+ "inner join GoodsPrice gp on g.goodsCode = gp.goodsCode "
			+ "inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is null and gt.paGroupCode in :paGroupCodes "
			+ "inner join PaGoodsEvent pe on g.goodsCode = pe.goodsCode "
			+ "where ga.emGoodsYn = '0'  " // 구성원상품여부는 변경이 가능하기 때문에 쿼리에서 체크, 새벽배송,해외배송은 변경 불가이므로 입점동기화에서 타게팅 제거하도록 함
			+ "and gp.applyDate = (select max(gps.applyDate) from GoodsPrice gps where gps.goodsCode = g.goodsCode and gps.applyDate <= CURRENT_DATE) "
			+ "and gp.salePrice > 0 "
			+ "and q.goodsCode is null "
			+ "and pe.useYn = '1' "
			+ "and current_date between pe.startDate and nvl(pe.endDate, current_date) "
			+ "and not exists (select 1 from PaTargetExcept ge where ge.targetGb = '00' and ge.targetCode = g.goodsCode and ge.useYn = '1' "
			+ "                      and (ge.paGroupCodeAllYn = '1' or instr(ge.paGroupCode, gt.paGroupCode) > 0)) "
			+ "and not exists (select 1 from PaExceptEntp ee "
			+ "                         left outer join PaExceptBrand be on be.exceptSeq = ee.exceptSeq and be.entpCode = ee.entpCode and be.useYn = '1' "
			+ "                        where ee.entpCode = g.entpCode and ee.useYn = '1' "
			+ "                          and (ee.paGroupCodeAllYn = '1' or instr(ee.paGroupCode, gt.paGroupCode) > 0) "
			+ "                          and (ee.allBrandYn = '1' or be.brandCode = g.brandCode)"
			+ "                          and (ee.sourcingMedia = '00' or instr(ee.sourcingMedia, g.sourcingMedia) > 0)) "
			)
	Slice<GoodsTarget> findGoodsEventStartTargetList(Pageable pageRequest, List<String> paGroupCodes);

	// 상품정보변경
	@Query(value = "select distinct g.goods_Code as goodsCode, g.modify_Id as modifyId"
			+ "  from tGoods g"
			+ " inner join tPaGoodsTarget gt"
			+ "    on g.goods_Code = gt.goods_Code"
			+ "   and gt.pa_Sale_Gb is not null"
			+ "   and gt.pa_Group_Code in :paGroupCodes"
			+ "  left outer join tPaCdcGoodsSnapshot q"
			+ "    on g.goods_Code = q.goods_Code"
			+ "   and q.cdc_Snapshot_No = :cdcSnapshotNo"
			+ " where g.modify_Date >= :lastCdcDate"
			+ "   and q.goods_Code is null"
			+ "   and not exists (select 1"
			+ "          from tgoods tg, temp_entp_except2 t"
			+ "         where tg.entp_code = t.entp_code"
			+ "           and tg.goods_code = gt.goods_code)"
			, nativeQuery = true)
	Slice<GoodsTarget> findGoodsModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 상품판매종료
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from Goods g "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where g.saleEndDate <= current_date and g.saleEndDate >= :lastCdcDate "
			+ " and q.goodsCode is null ")
	Slice<GoodsTarget> findGoodsEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 행사중단
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from PaGoodsEvent g "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.useYn = '0' and g.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null ")
	Slice<GoodsTarget> findGoodsEventStatusTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 행사종료
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from PaGoodsEvent g "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.endDate <= current_date and g.endDate >= :lastCdcDate "
			+ " and q.goodsCode is null ")
	Slice<GoodsTarget> findGoodsEventEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 사용자 판매재개
	@Query(value = "select distinct g.goodsCode as goodsCode, g.insertId as modifyId "
			+ " from PaSaleNoGoods g "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.paSaleGb = '20' "
			+ "  and g.insertDate >= :lastCdcDate "
			+ "  and g.paGroupCode in :paGroupCodes"
			+ "  and q.goodsCode is null ")
	Slice<GoodsTarget> findManualStartTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 행사재입점
	@Query(value = "select distinct g.goodsCode as goodsCode, g.modifyId as modifyId "
			+ " from PaGoodsEvent g "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo "
			+ " where g.startDate <= current_date and g.startDate >= :lastCdcDate "
			+ " and nvl(g.endDate, current_date) >= current_date "
			+ " and g.useYn = '1' "
			+ " and q.goodsCode is null ")
	Slice<GoodsTarget> findGoodsEventForSaleTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
		
	// 쿠팡 구매옵션재입점 처리 (target 있는경우)
	@Query(value = "select z.goods_code as goodsCode, z.modify_id as modifyId"
			+ " from ( "
			+ " select distinct g.goods_code, g.modify_id"
			+ " from tpacopngoodsuserattri e "
			+ " inner join tpacopngoods f on e.goods_code = f.goods_code and f.pa_sale_gb in ('20', '30') and f.pa_status  in ('10', '20') and f.modify_date >= sysdate - 1/24 "
			+ " inner join tgoods g on f.goods_code = g.goods_code and g.sale_gb = '00' "
			+ " inner join tpagoodstarget gt on e.goods_code = gt.goods_code and gt.pa_sale_gb is not null and gt.pa_goods_code is null and gt.pa_group_code in :paGroupCodes "
			+ " left outer join tpacdcgoodssnapshot q on e.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
			+ " where e.attribute_name_unit is not null "
			+ " and not exists ( "
			+ " select 1 "
			+ " from tpacopngoodsexceptopt d "
			+ " where d.goods_code = e.goods_code ) ) z "
			+ " left join tgoodsdt c on c.goods_code = z.goods_code "
			+ " left join tpacopngoodsuserattri b on b.goods_code   = c.goods_code and b.goodsdt_code = c.goodsdt_code "
			+ " group by z.goods_code, z.modify_id "
			+ " having count(c.goodsdt_code) = count(b.goodsdt_code) ", nativeQuery = true)
	Slice<GoodsTarget> findCopnAttriForSaleList(long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 쿠팡 구매옵션재입점 처리 (target 없는경우)
	@Query(value = "select z.goods_code as goodsCode, z.modify_id as modifyId"
			+ " from ( "
			+ " select distinct g.goods_code, g.modify_id "
			+ " from tpacopngoodsuserattri e "				
			+ " inner join tgoods g on e.goods_code = g.goods_code and g.sale_gb = '00' "				
			+ " left outer join tpacdcgoodssnapshot q on e.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo "
			+ " where e.attribute_name_unit is not null "
			+ " and e.modify_date >= sysdate - 1/24 "
			+ " and not exists ( "
			+ " select 1 "
			+ " from tpacopngoodsexceptopt d "
			+ " where d.goods_code = e.goods_code ) "
			+ " and not exists ( "
			+ " select 1 "
			+ " from tpagoodstarget gt "
			+ " where e.goods_code = gt.goods_code and gt.pa_group_code in :paGroupCodes ) ) z "
			+ " left join tgoodsdt c on c.goods_code = z.goods_code "
			+ " left join tpacopngoodsuserattri b on b.goods_code   = c.goods_code and b.goodsdt_code = c.goodsdt_code "
			+ " group by z.goods_code, z.modify_id "
			+ " having count(c.goodsdt_code) = count(b.goodsdt_code) ", nativeQuery = true)
	Slice<GoodsTarget> findCopnAttriForSaleTargetList(long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
}
