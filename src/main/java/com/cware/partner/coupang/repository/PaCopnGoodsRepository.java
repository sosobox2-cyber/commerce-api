package com.cware.partner.coupang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cware.partner.sync.domain.entity.PaCopnGoods;
import com.cware.partner.sync.domain.id.PaGoodsId;

@Repository
public interface PaCopnGoodsRepository extends JpaRepository<PaCopnGoods, PaGoodsId> {

    // 판매중지처리
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ " set p.transSaleYn = '1' "
    		+ " , p.paSaleGb = '30' "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode like :paCode "
    		+ "   and p.paSaleGb = '20' "
    		+ "   and p.paStatus = '30' ")
    int stopSale(String goodsCode, String paCode, String modifyId);

    // 전송대상여부 활성화
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.transTargetYn = '1'"
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode like :paCode "
    		+ "   and p.transTargetYn = '0' ")
    int enableTransTarget(String goodsCode, String paCode, String modifyId);

    // 신규 입점 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoodsTarget t on g.goodsCode = t.goodsCode and g.paCode = t.paCode and g.paGroupCode = t.paGroupCode "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsPrice pp on g.goodsCode = pp.goodsCode and g.paCode = pp.paCode "
			+ "   and pp.applyDate = ( select max(gp.applyDate) from GoodsPrice gp where gp.goodsCode = pp.goodsCode and gp.applyDate <= current_date) "
			+ " inner join Goods m on g.goodsCode = m.goodsCode  and m.saleGb = '00' "
			+ " where g.paStatus = '10' "
			+ " and g.paSaleGb = '20' "
			+ " and p.insertDate < current_date - 1/24 "
			+ " and g.insertDate > current_date - 7 "
			+ " and exists (select 1 from PaEntpSlip s where s.entpCode = m.shipEntpCode and s.entpManSeq = m.shipManSeq "
			+ "               and s.paCode = g.paCode and s.transTargetYn = '0' )"
			)
	Slice<PaCopnGoods> findTransTargetList(Pageable pageable);

    // 신규 입점 대상 (파일럿테스트용)
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoodsTarget t on g.goodsCode = t.goodsCode and g.paCode = t.paCode and g.paGroupCode = t.paGroupCode "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsPrice pp on g.goodsCode = pp.goodsCode and g.paCode = pp.paCode "
			+ "   and pp.applyDate = ( select max(gp.applyDate) from GoodsPrice gp where gp.goodsCode = pp.goodsCode and gp.applyDate <= current_date) "
			+ " inner join Goods m on g.goodsCode = m.goodsCode and m.saleGb = '00' "
			+ " where g.paStatus in ('10', '20') "
			+ " and g.paSaleGb = '20' "
			+ " and g.goodsCode in :goodsCodes "
			+ " and exists (select 1 from PaEntpSlip s where s.entpCode = m.shipEntpCode and s.entpManSeq = m.shipManSeq "
			+ "               and s.paCode = g.paCode and s.transTargetYn = '0' )"
			)
	Slice<PaCopnGoods> findTransTargetList(Pageable pageable, List<String> goodsCodes);

	// 상품수정 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join Goods m on g.goodsCode = m.goodsCode  and m.saleGb = '00' "
			+ " where g.paStatus = '30' "
			+ " and g.paSaleGb = '20' "
			+ " and g.transTargetYn = '1' "
			+ " and g.transSaleYn = '0' "
			+ " and g.approvalStatus in ('00', '20', '30') "
			+ " and exists (select 1 from PaEntpSlip s where s.entpCode = m.shipEntpCode and s.entpManSeq = m.shipManSeq "
			+ "               and s.paCode = g.paCode and s.transTargetYn = '0' )"
			)
	Slice<PaCopnGoods> findUpdateTargetList(Pageable pageable);

	// 가격변경 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsPriceApply gp on g.goodsCode = gp.goodsCode and g.paGroupCode = gp.paGroupCode and g.paCode = gp.paCode "
			+ " where g.paStatus = '30' "
			+ " and g.paSaleGb = '20' "
			+ " and g.transSaleYn = '0' "
			+ " and gp.transDate is null "
			+ " and (g.modifyDate > sysdate - 14 or gp.insertDate > sysdate - 14)"
			+ " and gp.applyDate = (select max(sgp.applyDate) from PaGoodsPriceApply sgp "
			+ "            where sgp.goodsCode = gp.goodsCode "
			+ "              and sgp.paGroupCode = gp.paGroupCode "
			+ "              and sgp.paCode = gp.paCode )"
			+ " and gp.priceApplySeq = (select max(sgp.priceApplySeq) from PaGoodsPriceApply sgp "
			+ "            where sgp.goodsCode = gp.goodsCode "
			+ "              and sgp.paGroupCode = gp.paGroupCode "
			+ "              and sgp.paCode = gp.paCode "
			+ "              and sgp.applyDate = gp.applyDate) "
			+ " and exists (select 1 from PaGoodsDtMapping m where m.paCode = g.paCode and m.goodsCode = g.goodsCode and m.paOptionCode is not null) "
			)
	Slice<PaCopnGoods> findPriceTargetList(Pageable pageable);

	// 판매재개 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsPrice pp on g.goodsCode = pp.goodsCode and g.paCode = pp.paCode "
			+ "   and pp.applyDate = ( select max(gp.applyDate) from GoodsPrice gp where gp.goodsCode = pp.goodsCode and gp.applyDate <= current_date) "
			+ " inner join Goods t on g.goodsCode = t.goodsCode and t.saleGb = '00' "
			+ " where g.paStatus = '30' "
			+ " and g.paSaleGb = '20' "
			+ " and g.transSaleYn = '1' "
			+ " and g.sellerProductId is not null "
//			+ " and exists (select 1 from PaGoodsDtMapping m where m.paCode = g.paCode and m.goodsCode = g.goodsCode and m.paOptionCode is not null) "
			+ " and exists (select 1 from PaEntpSlip s where s.entpCode = t.shipEntpCode and s.entpManSeq = t.shipManSeq "
			+ "               and s.paCode = g.paCode and s.transTargetYn = '0' )"
			)
	Slice<PaCopnGoods> findResumeSaleTargetList(Pageable pageable);

	// 제휴입점 반려사유
	@Transactional
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.paStatus = '20'"
    		+ " , p.returnNote = :returnNote "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode"
    		+ "   and p.paStatus != '20' ")
    int rejectTransTarget(String goodsCode, String paCode, String modifyId, String returnNote);

	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.paStatus = '30' "
			+ " and g.paSaleGb = '20' "
			+ " and g.approvalStatus = '20' "
			+ " and g.sellerProductId is not null "
			+ " and g.modifyDate < current_date - 14 "
			+ " and not exists (select 1 from PaGoodsDtMapping m where m.paCode = g.paCode and m.goodsCode = g.goodsCode and m.paOptionCode is not null and rownum = 1)"
			)
	Slice<PaCopnGoods> findRejectTargetList(Pageable pageable);

	// 반려상태 업데이트
	@Transactional
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.paStatus = '90' "
    		+ " , p.paSaleGb = '30' "
    		+ " , p.returnNote = :returnNote "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode"
    		+ "   and p.paStatus = '30' "
    		+ "   and p.paSaleGb = '20' "
    		+ "   and p.approvalStatus = '20' ")
    int updateRejectStatus(String goodsCode, String paCode, String modifyId, String returnNote);

	// 상태업데이트 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.approvalStatus < '20' "
			+ " and g.paStatus >= '30' "
			+ " and g.sellerProductId is not null "
			)
	Slice<PaCopnGoods> findStatusTargetList(Pageable pageable);

	// 승인상태 업데이트
	@Transactional
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.approvalStatus = :approvalStatus "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode" )
    int updateApprovalStatus(String goodsCode, String paCode, String modifyId, String approvalStatus);

	// 옵션매핑 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.approvalStatus = '30' "
			+ " and ((g.paSaleGb = '20' "
			+ " and g.paStatus = '30')"
			+ "  or (g.paSaleGb = '30'"
			+ " and g.paStatus >= '30' "
			+ " and g.transSaleYn = '1')) "
			+ " and g.sellerProductId is not null "
			+ " and exists (select 1 from PaGoodsDtMapping m where m.paCode = g.paCode and m.goodsCode = g.goodsCode "
			+ " 			and (m.paOptionCode is null or m.remark1 is null or m.remark2 is null) "
			+ " 			and rownum = 1)"
			)
	Slice<PaCopnGoods> findOptionCodeTargetList(Pageable pageable);

    // 수기중단처리
	@Transactional
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ " set p.transSaleYn = '1' "
    		+ " , p.paSaleGb = '30' "
    		+ " , p.paStatus = '90' "
    		+ " , p.returnNote = :returnNote "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode "
    		+ "   and p.paSaleGb = '20' "
    		+ "   and p.paStatus = '30' ")
    int stopSaleForReject(String goodsCode, String paCode, String modifyId, String returnNote);

	/**
	 * 입점된 상품
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	Optional<PaCopnGoods> findByGoodsCodeAndPaCodeAndSellerProductIdIsNotNull(String goodsCode, String paCode);

	/**
	 * 판매상태변경대상
	 * @param goodsCode
	 * @param paCode
	 * @param paSaleGb
	 * @param paStatus
	 * @param transSaleYn
	 * @return
	 */
	Optional<PaCopnGoods> findByGoodsCodeAndPaCodeAndPaSaleGbAndPaStatusAndTransSaleYn(String goodsCode, String paCode,
			String paSaleGb, String paStatus, String transSaleYn);

	@Transactional
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ " set p.transSaleYn = '0' "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode ")
    int updateTransSaleTarget(String goodsCode, String paCode, String modifyId);

	// 판매중지 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.transSaleYn = '1' "
			+ " and g.paSaleGb = '30' "
			+ " and g.paStatus in ('30', '90') "
			+ " and g.sellerProductId is not null "
			+ " and exists (select 1 from PaGoodsDtMapping m where m.paCode = g.paCode and m.goodsCode = g.goodsCode and m.paOptionCode is not null)"
			)
	Slice<PaCopnGoods> findStopSaleTargetList(Pageable pageable);

    // 판매중지 상품
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.transSaleYn = '1' "
			+ "  and g.paSaleGb = '30' "
			+ "  and g.paStatus in ('30', '90') "
			+ "  and g.goodsCode = :goodsCode "
			+ "  and g.paCode = :paCode "
			)
	Optional<PaCopnGoods> findStopSale(String goodsCode, String paCode);

	// 재고변경 대상
	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " where g.paSaleGb = '20' "
			+ " and g.paStatus = '30' "
			+ " and g.sellerProductId is not null "
			+ " and exists (select 1 from PaGoodsDtMapping m "
			+ "             where m.paCode = g.paCode and m.goodsCode = g.goodsCode and m.paOptionCode is not null "
			+ "               and m.transStockYn = '1')"
			)
	Slice<PaCopnGoods> findUpdateStockTargetList(Pageable pageable);

    //쿠팡 필수 구매옵션 입력 여부
	@Query(value = "select case "
            + "         when bbb = aaa then "
            + "          '등록' "
            + "         else "
            + "          '미등록' "
            + "       end as status "
            + "  from (select (select count(1) "
            + "				   from tpacopngoodsuserattri b, tgoodsdt c "
            + "                 where c.goods_code = z.goods_code "
            + "                   and b.goods_code(+) = c.goods_code "
            + "                   and b.goodsdt_code(+) = c.goodsdt_code) as aaa, "
		    + "               (select count(1) "
		    + "                  from tpacopngoodsuserattri b, tgoodsdt t1 "
		    + "                 where b.goods_code = z.goods_code "
		    + "                   and b.goods_code = t1.goods_code "
		    + "                   and b.goodsdt_code = t1.goodsdt_code) as bbb "
		    + "          from tgoods z "
		    + "         where z.goods_code = :goodsCode) ",
            nativeQuery = true
            )
	String findCopnPurchaseOptionYn(String goodsCode);
	
	@Query(value = "select case "
			+ "         when exists ( "
			+ "				select 1"
			+ "				  from tpacopngoodsexceptopt ce"
			+ " 			 where ce.goods_code = :goodsCode "
			+ "             ) then 'Y' "
			+ "         else 'N' "
			+ "       end as stauts"
			+ "      from dual ",
			nativeQuery = true
			)
	String findCopnOptExceptYn(String goodsCode);
	
	@Query(value = "select case "
			+ "			when count(1) > 0 then 'Y' "
		    + "   		else 'N' "
		    + "   	   end as requiredYn "
		    + "		  from tpacopnctgrunitlist t, tpagoodskindsmapping a, tgoods b "
		    + " 	 where t.required_yn = 'Y' "
		    + " 	   and a.pa_group_code = '05' "
		    + " 	   and a.lmsd_code = b.lmsd_code "
		    + " 	   and t.pa_lmsd_key = a.pa_lmsd_key "
		    + " 	   and b.goods_code = :goodsCode " ,
			nativeQuery = true
			)
	String findCopnOptRequiredYn(String goodsCode);
}
