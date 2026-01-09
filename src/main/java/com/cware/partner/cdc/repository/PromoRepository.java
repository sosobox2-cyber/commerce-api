package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PromoM;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PromoRepository extends JpaRepository<PromoM, String> {

	// 프로모션적용
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PromoApply pa "
			+ " inner join PpromoTarget g on pa.promoNo = g.promoNo "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
//			+ " where not exists (select 1 from PaPromoTarget ppt "
//			+ "           where g.promoNo = ppt.promoNo and g.goodsCode = ppt.goodsCode and gt.paCode = ppt.paCode "
//			+ "             and ppt.procGb != 'D' and rownum = 1) "
			+ " where (pa.promoBdate >= :lastCdcDate or g.modifyDate >= :lastCdcDate ) "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPromoApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


	// 프로모션종료
	@Query(value = "select ppt.goodsCode as goodsCode, max(ppt.modifyId) as modifyId "
			+ " from PromoM pm "
			+ " inner join PaPromoTarget ppt on pm.promoNo = ppt.promoNo "
			+ " inner join PaGoodsTarget gt on ppt.goodsCode = gt.goodsCode and ppt.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on ppt.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where pm.promoEdate <= current_date and pm.promoEdate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by ppt.goodsCode ")
	Slice<GoodsTarget> findPromoEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


	// 프로모션상태변경
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PromoM pm "
			+ " inner join PaPromoTarget g on pm.promoNo = g.promoNo "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and g.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where pm.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPromoStatusTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 프로모션제외
	// 운영의 log테이블 현재 50억 데이터로 사용 불가능
//	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
//			+ " from PromoApply pa "
//			+ " inner join PaPromoTarget g on pa.promoNo = g.promoNo "
//			+ " inner join PpromoTargetLog pg on g.promoNo = pg.promoNo and g.goodsCode = pg.goodsCode "
//			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
//			+ " where pg.logDate >= :lastCdcDate "
//			+ " and q.goodsCode is null "
//			+ " group by g.goodsCode "
//			)
//	Slice<GoodsTarget> findPromoExceptTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest);

	// tpapromotarget기준으로 존재유무 체크하여 타겟팅 해야하는데, 한번 타겟팅 한 건은 누적하지 않도록 큐를 체크하도록 변경
	@Query(value = " select p.goods_code as goodsCode, max(p.modify_id) as modifyId "
			+ "			 from  tpromom m "
			+ "       inner join tpapromotarget p on m.promo_no = p.promo_no "
			+ "       inner join tpagoodstarget gt on p.goods_code = gt.goods_code and p.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
			+ "       inner join tgoods g on g.goods_code = p.goods_code and g.sale_gb = '00'  and g.broad_sale_yn = '0' "
			+ "                            and (g.order_media_all_yn = '1' or instr(g.order_media, '61') > 0 or instr(g.order_media, '62') > 0) "
			+ "                            and sysdate between g.sale_start_date and nvl(g.sale_end_date, sysdate) "
			+ "       left outer join tppromotarget pt on p.promo_no = pt.promo_no and p.goods_code = pt.goods_code "
			+ "       left outer join tpacdcgoods q on p.goods_code = q.goods_code "
			+ "       where m.coupon_yn = '1' "
			+ "         and m.do_type = '30' "
			+ "         and m.immediately_yn = '1' "
			+ "         and sysdate between m.promo_bdate and m.promo_edate "
			+ "         and m.alcout_promo_yn = '1' "
			+ "         and pt.goods_code is null "
			+ "         and q.goods_code is null "
			+ "         and (p.promo_no, p.pa_code, p.goods_code,  p.seq) =  "
			+ "       (select /*+index_desc(pp pk_tpapromotarget)*/  "
			+ "               pp.promo_no, pp.pa_code, pp.goods_code, pp.seq  "
			+ "          from tpapromotarget pp  "
			+ "         where pp.pa_code = p.pa_code  "
			+ "           and pp.goods_code = p.goods_code  "
			+ "           and pp.promo_no = p.promo_no "
			+ "           and rownum = 1 ) "
			+ "           and p.proc_gb != 'D' "
			+ " and not exists (select 1 from tpatargetexcept ge where ge.target_gb = '00' and ge.target_code = g.goods_code and ge.use_yn = '1' "
			+ "                      and (ge.pa_Group_Code_All_Yn = '1' or instr(ge.pa_Group_Code, gt.pa_Group_Code) > 0)) "
			+ " and not exists (select 1 from tpaexceptentp ee "
			+ "                         left outer join tpaexceptbrand be on be.except_Seq = ee.except_Seq and be.entp_code = ee.entp_code and be.use_yn = '1' "
			+ "                        where ee.entp_code = g.entp_code and ee.use_yn = '1' "
			+ "                          and (ee.pa_group_code_all_yn = '1' or instr(ee.pa_group_code, gt.pa_group_code) > 0) "
			+ "                          and (ee.all_brand_yn = '1' or be.brand_code = g.brand_code)"
			+ "                          and (ee.sourcing_media = '00' or instr(ee.sourcing_media, g.sourcing_media) > 0)) "
			+ " and not exists (select 1 "
			+ "					  from tpagoodsfilterlog gf "
			+ " 				 where gf.goods_code = g.goods_code "
			+ "					   and (gf.pa_group_code is null or gf.pa_group_code = gt.pa_group_code) "			
			+ " 				   and gf.insert_date >= :lastCdcDate) "
			+ " group by p.goods_code "
			, nativeQuery = true
			)
	Slice<GoodsTarget> findPromoExceptTargetList(Timestamp lastCdcDate,List<String> paGroupCodes);
	
	
	// 프로모션제휴제외 - 상품 단위
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PaPromoTargetExcept pte "
			+ " inner join PaPromoTarget g on pte.targetCode = g.goodsCode "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and g.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where (pte.modifyDate >= :lastCdcDate or pte.paExceptBdate >= :lastCdcDate) "
			+ " and pte.paExceptBdate < current_date "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPaPromoExceptTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	
	// 프로모션제휴제외 - 업체단위
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PaPromoExceptEntp pee "
			+ " inner join Goods g on g.entpCode = pee.entpCode "
			+ " inner join PaPromoTarget p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsTarget gt on p.goodsCode = gt.goodsCode and p.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on q.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where (pee.modifyDate >= :lastCdcDate or pee.paExceptBdate >= :lastCdcDate) "
			+ " and pee.paExceptBdate < current_date "
			+ " and pee.allBrandYn  =  '1' "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPaPromoExceptEntpList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 프로모션제휴제외 - 브랜드 단위
	@Query(value = "select /*+ LEADING(pee,g,peb) INDEX (g IDX_TGOODS_03) */  g.goods_Code as goods_Code, max(g.modify_Id) as modifyId "
			+ " from TPaPromoExceptEntp pee "
			+ " inner join TGoods g on g.entp_Code = pee.entp_Code "
			+ " inner join TPaPromoTarget p on g.goods_Code = p.goods_Code "
			+ " inner join TPaGoodsTarget gt on p.goods_Code = gt.goods_Code and p.pa_Code = gt.pa_Code and gt.pa_Group_Code in :paGroupCodes "
			+ " inner join TPaPromoExceptBrand peb on peb.entp_Code = pee.entp_Code and peb.pa_Except_Bdate < current_date  and  peb.brand_Code = g.brand_Code "
			+ " left outer join TPaCdcGoodsSnapshot q on g.goods_Code = q.goods_Code and q.cdc_Snapshot_No = :cdcSnapshotNo  "
			+ " where (peb.modify_Date >= :lastCdcDate or pee.pa_Except_Bdate >= :lastCdcDate) "
			+ " and pee.pa_Except_Bdate < current_date "
			+ " and pee.all_Brand_Yn  =  '0' "
			+ " and q.goods_Code is null "
			+ " group by g.goods_Code "
			, nativeQuery = true)
	Slice<GoodsTarget> findPaPromoExceptBrandList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 프로모션제휴제외종료 - 상품 단위
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PaPromoTargetExcept pte "
			+ " inner join PaPromoTarget g on pte.targetCode = g.goodsCode "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and g.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where pte.paExceptEdate <= current_date and  pte.paExceptEdate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPaPromoExceptEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 프로모션제휴제외종료 - 업체단위
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PaPromoExceptEntp pee "
			+ " inner join Goods g on g.entpCode = pee.entpCode "
			+ " inner join PaPromoTarget p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsTarget gt on p.goodsCode = gt.goodsCode and p.paCode = gt.paCode and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on p.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where pee.paExceptEdate <= current_date and  pee.paExceptEdate >= :lastCdcDate "
			+ " and pee.allBrandYn  =  '1' "
			+ " and pee.useYn		=  '1' "	
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findPaPromoExceptEndEntpList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	// 프로모션제휴제외종료 - 브랜드 단위
	@Query(value = "select /*+ LEADING(pee, g) USE_NL(pee) index(g IDX_TGOODS_03) */ g.goods_Code as goodsCode, max(g.modify_Id) as modifyId "
			+ " from TPaPromoExceptEntp pee "
			+ " inner join TGoods g on g.entp_Code = pee.entp_Code "
			+ " inner join TPaPromoTarget p on g.goods_Code = p.goods_Code "
			+ " inner join TPaGoodsTarget gt on p.goods_Code = gt.goods_Code and p.pa_Code = gt.pa_Code and gt.pa_Group_Code in :paGroupCodes "
			+ " inner join TPaPromoExceptBrand peb on peb.entp_Code = pee.entp_Code and peb.pa_Except_Bdate < current_date  and  peb.brand_Code = g.brand_Code and peb.use_Yn = '1'  "
			+ " left outer join TPaCdcGoodsSnapshot q on g.goods_Code = q.goods_Code and q.cdc_Snapshot_No = :cdcSnapshotNo  "
			+ " where peb.pa_Except_Edate <= current_date and  peb.pa_Except_Edate >= :lastCdcDate  "
			+ " and pee.all_Brand_Yn  =  '0' "
			+ " and q.goods_Code is null "
			+ " group by g.goods_Code fetch first :pageSize rows only "
			, nativeQuery = true)
	Slice<GoodsTarget> findPaPromoExceptEndBrandList(Timestamp lastCdcDate, long cdcSnapshotNo, int pageSize, List<String> paGroupCodes);

	// 프로모션최소마진행사적용
	@Query(value = "select /*+ LEADING(atm pp g) USE_HASH(pp) */ g.goods_code as goodsCode , max(pma.modify_id) as modifyId "
			+ " from tgoods g "
			+ " inner join tpagoodspriceapply pp on g.goods_code = pp.goods_code "
			+ "                                  and pp.margin_rate is not null and pp.coupon_promo_no is not null and pp.pa_group_code in :paGroupCodes "
			+ "                                  and (pp.apply_date, pp.price_apply_seq) = (select /*+ index_desc (pp2 pk_tpagoodspriceapply)*/ "
			+ "                                                                                   pp2.apply_date, pp2.price_apply_seq "
			+ "                                                                               from tpagoodspriceapply pp2 "
			+ "                                                                              where pp2.goods_code = pp.goods_code "
			+ "                                                                                and pp2.pa_group_code = pp.pa_group_code "
			+ "                                                                                and pp2.pa_code = pp.pa_code "
			+ "                                                                                and pp2.apply_date <= sysdate "
			+ "                                                                                and rownum = 1) "
			+ " inner join (select atm.event_no "
			+ "                  , atm.event_name "
			+ "                  , atm.media_code "
			+ "                  , atm.goods_kinds_all_yn "
			+ "                  , atm.sourcing_media "
			+ "                  , atm.except_margin_rate "
			+ "                  , (case when atm.media_code = 'EX01' then '01' "
			+ "                          when atm.media_code = 'EX02' then '02' "
			+ "                          when atm.media_code = 'EX03' then '03' "
			+ "                          when atm.media_code = 'EX04' then '04' "
			+ "                          when atm.media_code = 'EX05' then '05' "
			+ "                          when atm.media_code = 'EX06' then '06' "
			+ "                          when atm.media_code = 'EX08' then '07' "
			+ "                          when atm.media_code = 'EX09' then '08' "
			+ "                          when atm.media_code = 'EX10' then '09' "
			+ "                          when atm.media_code = 'EX11' then '10' "
			+ "                          when atm.media_code = 'EX12' then '11' "
			+ "                          when atm.media_code = 'EX13' then '12' "
			+ "                          when atm.media_code = 'EX15' then '13' "
			+ "                          when atm.media_code = 'EX16' then '14' "
			+ "                          when atm.media_code = 'EX17' then '15' "
			+ "                      end) as pa_group_code "
			+ "                    , atm.modify_date "
			+ "                    , atm.modify_id "
			+ "                 from tpapromomarginautom atm "
			+ "                where atm.use_code = '00' "
			+ "                  and sysdate between atm.event_bdate and atm.event_edate "
			+ "                  and atm.event_bdate >= :lastCdcDate ) pma "
			+ "             on pp.pa_group_code = pma.pa_group_code "
			+ "            and pp.margin_rate < pma.except_margin_rate "
			+ "            and pp.pa_code like '%' || decode(pma.sourcing_media, '01', '1', '61', '2') "
			+ " left outer join tpapromomarginautogrp grp on pma.event_no = grp.event_no"
			+ " left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " where q.goods_code is null "
			+ " and g.sale_gb = '00'  and g.broad_sale_yn = '0' "
			+ " and (g.order_media_all_yn = '1' or instr(g.order_media, '61') > 0 or instr(g.order_media, '62') > 0) "
			+ " and sysdate between g.sale_start_date and nvl(g.sale_end_date, sysdate) "
			+ " and ((pma.goods_kinds_all_yn = '1') or (g.lmsd_code = grp.target_code and grp.target_gb = '40' and grp.use_code = '00')) "
			+ " group by g.goods_code"
			, nativeQuery = true)
	Slice<GoodsTarget> findPaPromoMarginAutoApplyList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageable, List<String> paGroupCodes);

	// 프로모션최소마진행사종료
	@Query(value = "select /*+ LEADING(atm pp g) USE_HASH(pp) */ g.goods_code as goodsCode , max(pma.modify_id) as modifyId "
			+ " from tgoods g "
			+ " inner join tpagoodspriceapply pp on g.goods_code = pp.goods_code "
			+ "                                 and pp.pa_group_code in :paGroupCodes "
			+ " inner join (select atm.event_no "
			+ "                  , atm.event_name "
			+ "                  , atm.media_code "
			+ "                  , atm.goods_kinds_all_yn "
			+ "                  , atm.sourcing_media "
			+ "                  , atm.except_margin_rate "
			+ "					 , atm.event_bdate "	
			+ "                  , (case when atm.media_code = 'EX01' then '01' "
			+ "                          when atm.media_code = 'EX02' then '02' "
			+ "                          when atm.media_code = 'EX03' then '03' "
			+ "                          when atm.media_code = 'EX04' then '04' "
			+ "                          when atm.media_code = 'EX05' then '05' "
			+ "                          when atm.media_code = 'EX06' then '06' "
			+ "                          when atm.media_code = 'EX08' then '07' "
			+ "                          when atm.media_code = 'EX09' then '08' "
			+ "                          when atm.media_code = 'EX10' then '09' "
			+ "                          when atm.media_code = 'EX11' then '10' "
			+ "                          when atm.media_code = 'EX12' then '11' "
			+ "                          when atm.media_code = 'EX13' then '12' "
			+ "                          when atm.media_code = 'EX15' then '13' "
			+ "                          when atm.media_code = 'EX16' then '14' "
			+ "                          when atm.media_code = 'EX17' then '15' "
			+ "                      end) as pa_group_code "
			+ "                    , atm.modify_date "
			+ "                    , atm.modify_id "
			+ "                 from tpapromomarginautom atm "
			+ "                where atm.use_code = '00' "
			+ "                  and atm.event_edate <= current_date and atm.event_edate >= :lastCdcDate) pma "
			+ "             on pp.pa_group_code = pma.pa_group_code "
			+ " 		   and (pp.apply_date, pp.price_apply_seq) = (select /*+ index_desc (pp2 pk_tpagoodspriceapply)*/ " 
			+ "      												 		  pp2.apply_date, pp2.price_apply_seq "
            + "    														from tpagoodspriceapply pp2 "
            + "  												       where pp2.goods_code = pp.goods_code "
            + "     													 and pp2.pa_group_code = pp.pa_group_code "
            + "    														 and pp2.pa_code = pp.pa_code "
            + "    													 	 and pp2.insert_Date <= pma.event_bdate " //행사시작 직전 가격 기준 
            + "    													 	 and rownum = 1) "
            + "			   and pp.margin_rate < pma.except_margin_rate "
            + " 		   and pp.coupon_promo_no is not null "
            + " 		   and pp.margin_rate is not null "
			+ "            and pp.pa_code like '%' || decode(pma.sourcing_media, '01', '1', '61', '2') "
			+ " left outer join tpapromomarginautogrp grp on pma.event_no = grp.event_no"
			+ " left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " where q.goods_code is null "
			+ " and g.sale_gb = '00' "
			+ " and ((pma.goods_kinds_all_yn = '1') or (g.lmsd_code = grp.target_code and grp.target_gb = '40' and grp.use_code = '00')) "
			+ " group by g.goods_code"
			, nativeQuery = true)
	Slice<GoodsTarget> findPaPromoMarginAutoEndList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageable, List<String> paGroupCodes);
	
	// 프로모션최소마진행사상태변경(중단)
	@Query(value = "select /*+ LEADING(atm pp g) USE_HASH(pp) */ g.goods_code as goodsCode , max(pma.modify_id) as modifyId "
			+ " from tgoods g "
			+ " inner join tpagoodspriceapply pp on g.goods_code = pp.goods_code "
			+ "                                  and pp.pa_group_code in :paGroupCodes "
			+ " inner join (select atm.event_no "
			+ "                  , atm.event_name "
			+ "                  , atm.media_code "
			+ "                  , atm.goods_kinds_all_yn "
			+ "                  , atm.sourcing_media "
			+ "                  , atm.except_margin_rate "
			+ "					 , atm.event_bdate "	
			+ "                  , (case when atm.media_code = 'EX01' then '01' "
			+ "                          when atm.media_code = 'EX02' then '02' "
			+ "                          when atm.media_code = 'EX03' then '03' "
			+ "                          when atm.media_code = 'EX04' then '04' "
			+ "                          when atm.media_code = 'EX05' then '05' "
			+ "                          when atm.media_code = 'EX06' then '06' "
			+ "                          when atm.media_code = 'EX08' then '07' "
			+ "                          when atm.media_code = 'EX09' then '08' "
			+ "                          when atm.media_code = 'EX10' then '09' "
			+ "                          when atm.media_code = 'EX11' then '10' "
			+ "                          when atm.media_code = 'EX12' then '11' "
			+ "                          when atm.media_code = 'EX13' then '12' "
			+ "                          when atm.media_code = 'EX15' then '13' "
			+ "                          when atm.media_code = 'EX16' then '14' "
			+ "                          when atm.media_code = 'EX17' then '15' "
			+ "                      end) as pa_group_code "
			+ "                    , atm.modify_date "
			+ "                    , atm.modify_id "
			+ "                 from tpapromomarginautom atm "
			+ "                where atm.use_code = '12' "
			+ "                  and atm.event_bdate < atm.modify_date " //시작전에 중단된건 캡쳐안함
			+ "					 and atm.modify_date >= :lastCdcDate) pma "
			+ "             on pp.pa_group_code = pma.pa_group_code "
			+ " 		   and (pp.apply_date, pp.price_apply_seq) = (select /*+ index_desc (pp2 pk_tpagoodspriceapply)*/ " 
			+ "      												 		  pp2.apply_date, pp2.price_apply_seq "
            + "    														from tpagoodspriceapply pp2 "
            + "  												       where pp2.goods_code = pp.goods_code "
            + "     													 and pp2.pa_group_code = pp.pa_group_code "
            + "    														 and pp2.pa_code = pp.pa_code "
            + "    													 	 and pp2.insert_Date <= pma.event_bdate " //행사시작 직전 가격 기준 
            + "    													 	 and rownum = 1) "
            + "			   and pp.margin_rate < pma.except_margin_rate "
            + " 		   and pp.coupon_promo_no is not null "
            + " 		   and pp.margin_rate is not null "
			+ "            and pp.pa_code like '%' || decode(pma.sourcing_media, '01', '1', '61', '2') "
			+ " left outer join tpapromomarginautogrp grp on pma.event_no = grp.event_no"
			+ " left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " where q.goods_code is null "
			+ " and ((pma.goods_kinds_all_yn = '1') or (g.lmsd_code = grp.target_code and grp.target_gb = '40' and grp.use_code = '00')) "
			+ " and g.sale_gb = '00' "
			+ " group by g.goods_code"
			, nativeQuery = true)
	Slice<GoodsTarget> findPaPromoMarginAutoStatusList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageable, List<String> paGroupCodes);
	
	// 프로모션제휴제외 - 브랜드 단위
	//@Query(value = "select /*+ monitor */ g.goods_Code as goods_Code, max(g.modify_Id) as modifyId "
	//		+ " from TPaPromoExceptEntp pee "
	//		+ " inner join TGoods g on g.entp_Code = pee.entp_Code "
	//		+ " inner join TPaPromoTarget p on g.goods_Code = p.goods_Code "
	//		+ " inner join TPaGoodsTarget gt on p.goods_Code = gt.goods_Code and p.pa_Code = gt.pa_Code and gt.pa_Group_Code in ('02') "
	//		+ " inner join TPaPromoExceptBrand peb on peb.entp_Code = pee.entp_Code and peb.pa_Except_Bdate < current_date  and  peb.brand_Code = g.brand_Code and peb.use_Yn = '1'  "
	//		+ " left outer join TPaCdcGoodsSnapshot q on g.goods_Code = q.goods_Code and q.cdc_Snapshot_No = '123456'  "
	//		+ " where peb.pa_Except_Edate <= current_date and  peb.pa_Except_Edate >= sysdate - 1/24  "
	//		+ " and pee.all_Brand_Yn  =  '0' "
	//		+ " and q.goods_Code is null "
	//		+ " group by g.goods_Code "
	//		, nativeQuery = true)
	//Slice<GoodsTarget> findPaPromoExceptEndBrandListTest( );
	
	
	/* Reason 추가시 Union all을 사용하지 말고 entity 단위로 쪼개서 등록필요..
	// 프로모션제휴제외  - 대상 등록, 수정(값 변경 또는 use_yn, endDate 변경 시 감지)
	@Query(value = "select xx.goods_code as goodsCode, max(xx.modify_id) as modifyId "
			+ "		  from (" 
			+ " 			  select g.goods_code , max(g.modify_id) as modify_id "	 //상품단위 프로모션 연동 예외	
			+ " 				from tpapromotargetexcept pte "
			+ " 			   inner join tpapromotarget g on pte.target_code = g.goods_code "
			+ " 			   inner join tpagoodstarget gt on g.goods_code = gt.goods_code and g.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
			+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " 			   where pte.modify_date  		>= :lastCdcDate  "
			+ " 				 and pte.pa_except_bdate 	< current_date "			
			+ " 				 and q.goods_code is null "
			+ " 			   group by g.goods_Code  "
			+ "		 		   union	"
			+ " 			  select g.goods_code , max(pee.modify_id)  "	 //업체 단위 프로모션 연동 예외
			+ "				    from tpapromoexceptentp pee "
			+ "				   inner join tgoods g on pee.entp_code = g.entp_code "		
			+ "				   inner join tpapromotarget p on  g.goods_code = p.goods_code " 
			+ "				   inner join tpagoodstarget gt on p.goods_code = gt.goods_code and p.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
			+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " 			   where pee.modify_date 		>= :lastCdcDate  "
			+ "					 and pee.pa_except_bdate  	< current_date "
			+ " 				 and pee.all_brand_yn  		 = '1' "		
			+ " 				 and q.goods_code is null "
			+ " 				 group by g.goods_Code  "				
			+ "		 		   union	"
			+ " 			  select g.goods_code, max(pee.modify_id)  "	 //브랜드단위 프로모션 연동 예외
			+ "				    from tpapromoexceptentp pee "
			+ "				   inner join tgoods g on pee.entp_code = g.entp_code "		
			+ "				   inner join tpapromotarget p on  g.goods_code = p.goods_code " 
			+ "				   inner join tpagoodstarget gt on p.goods_code = gt.goods_code and p.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
			+ "				   inner join tpapromoexceptbrand peb on peb.entp_code = pee.entp_code and peb.pa_except_bdate < current_date  and  peb.brand_code = g.brand_code "
			+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
			+ " 			   where peb.modify_date 		>= :lastCdcDate "
			+ " 				 and peb.pa_except_bdate  	< current_date "
			+ " 				 and pee.all_brand_yn  		 = '0' "
			+ " 				 and q.goods_code is null "
			+ " 			   group by g.goods_code  )xx "
			+ " 	   group by xx.goods_code " , nativeQuery = true)
	Slice<GoodsTarget> findPaPromoExceptTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
	
	
	//프로모션제휴제외 종료
	@Query(value = "select xx.goods_code as goodsCode, max(xx.modify_id) as modifyId "
				+ "		  from (" 
				+ " 			  select g.goods_code , max(g.modify_id) as modify_id "	 //상품단위 프로모션 연동 예외	
				+ " 				from tpapromotargetexcept pte "
				+ " 			   inner join tpapromotarget g on pte.target_code = g.goods_code "
				+ " 			   inner join tpagoodstarget gt on g.goods_code = gt.goods_code and g.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
				+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
				+ " 			   where pte.pa_except_edate <= current_date and  pte.pa_except_edate >= :lastCdcDate "
				+ " 				 and q.goods_code is null "
				+ " 			   group by g.goods_Code  "
				+ "		 		   union	"
				+ " 			  select g.goods_code , max(pee.modify_id)  "	 //업체 단위 프로모션 연동 예외
				+ "				    from tpapromoexceptentp pee "
				+ "				   inner join tgoods g on pee.entp_code = g.entp_code "		
				+ "				   inner join tpapromotarget p on  g.goods_code = p.goods_code " 
				+ "				   inner join tpagoodstarget gt on p.goods_code = gt.goods_code and p.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
				+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
				+ " 			   where pee.pa_except_edate <= current_date and  pee.pa_except_edate >= :lastCdcDate "
				+ "					 and pee.all_brand_yn  = '1' "			
				+ " 				 and q.goods_code is null "
				+ " 				 group by g.goods_Code  "				
				+ "		 		   union	"
				+ " 			  select g.goods_code, max(pee.modify_id)  "	 //업체 브랜드단위 프로모션 연동 예외
				+ "				    from tpapromoexceptentp pee "
				+ "				   inner join tgoods g on pee.entp_code = g.entp_code "		
				+ "				   inner join tpapromotarget p on  g.goods_code = p.goods_code " 
				+ "				   inner join tpagoodstarget gt on p.goods_code = gt.goods_code and p.pa_code = gt.pa_code and gt.pa_group_code in :paGroupCodes "
				+ "				   inner join tpapromoexceptbrand peb on peb.entp_code = pee.entp_code and peb.brand_code = g.brand_code and peb.use_yn = '1' "
				+ " 			   left outer join tpacdcgoodssnapshot q on g.goods_code = q.goods_code and q.cdc_snapshot_no = :cdcSnapshotNo  "
				+ " 			   where peb.pa_except_edate <= current_date and  peb.pa_except_edate >= :lastCdcDate "			 
				+ " 				 and pee.all_brand_yn  = '0' "
				+ " 				 and q.goods_code is null "
				+ " 			   group by g.goods_code  )xx "
				+ " 	   group by xx.goods_code " , nativeQuery = true)
		Slice<GoodsTarget> findPaPromoExceptEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);
		*/
}
