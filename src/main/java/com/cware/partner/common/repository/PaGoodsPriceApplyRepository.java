package com.cware.partner.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaGoodsPrice;
import com.cware.partner.sync.domain.entity.PaGoodsPriceApply;
import com.cware.partner.sync.domain.entity.PaPromoTarget;
import com.cware.partner.sync.domain.id.PaGoodsPriceApplyId;

@Repository
public interface PaGoodsPriceApplyRepository extends JpaRepository<PaGoodsPriceApply, PaGoodsPriceApplyId> {

	@Query(value = "select gp.* "
			+ " from tpagoodspriceapply gp "
			+ "  where gp.goods_code = :goodsCode "
			+ "    and gp.pa_group_code = :paGroupCode "
			+ "    and gp.pa_code = :paCode "
			+ "    and (gp.apply_date, gp.price_apply_seq) = (select /*+index_desc(sgp pk_tpagoodspriceapply)*/ "
			+ "          sgp.apply_date, sgp.price_apply_seq from tpagoodspriceapply sgp "
			+ "            where sgp.goods_code = gp.goods_code "
			+ "              and sgp.pa_group_code = gp.pa_group_code "
			+ "              and sgp.pa_code = gp.pa_code "
			+ "              and sgp.apply_date <= current_date "
			+ "              and rownum = 1 )"
			+ "   and gp.trans_date is null "
			, nativeQuery = true
			)
	Optional<PaGoodsPriceApply> findGoodsPriceApply(String goodsCode, String paGroupCode, String paCode);


	// 가격적용 전송 업데이트
    @Modifying
    @Query(value = "update PaGoodsPriceApply gp "
    		+ "  set gp.transId = :transId "
    		+ "    , gp.transDate = sysdate "
			+ " where gp.goodsCode = :#{#priceApply.goodsCode} "
			+ "   and gp.paGroupCode = :#{#priceApply.paGroupCode} "
			+ "   and gp.paCode = :#{#priceApply.paCode} "
			+ "   and gp.applyDate = :#{#priceApply.applyDate} "
			+ "   and gp.priceApplySeq = :#{#priceApply.priceApplySeq} "
			+ "   and gp.transDate is null ")
    int updatePriceApplyTrans(PaGoodsPriceApply priceApply, String transId);


    // 상품가격 전송대상
	@Query(value = "select gp "
			+ " from PaGoodsPrice gp "
			+ "where gp.paCode = :#{#priceApply.paCode} "
			+ " and gp.goodsCode = :#{#priceApply.goodsCode} "
			+ " and gp.applyDate = :#{#priceApply.applyDate} "
			+ " and gp.transDate is null"
			)
	PaGoodsPrice findTargetPrice(PaGoodsPriceApply priceApply);


    // 프로모션 전송대상
	@Query(value = "select gp "
			+ " from PaPromoTarget gp "
			+ "where gp.paCode = :#{#priceApply.paCode} "
			+ " and gp.goodsCode = :#{#priceApply.goodsCode} "
			+ " and gp.promoNo = :#{#priceApply.couponPromoNo} "
			+ " and gp.transDate is null "
			+ " and gp.procGb != 'D' "
			+ " and gp.seq = (select max(gpp.seq) from PaPromoTarget gpp "
			+ "                where gpp.paCode = gp.paCode "
			+ "                  and gpp.promoNo = gp.promoNo "
			+ "                  and gpp.goodsCode = gp.goodsCode )"
			)
	PaPromoTarget findTargetPromo(PaGoodsPriceApply priceApply);

    // 프로모션 삭제 전송대상
	@Query(value = "select gp "
			+ " from PaPromoTarget gp "
			+ "where gp.paCode = :#{#priceApply.paCode} "
			+ " and gp.goodsCode = :#{#priceApply.goodsCode} "
			+ " and gp.procGb = 'D' "
			+ " and exists (select 1 from PaPromoTarget pt "
			+ "              where pt.goodsCode = gp.goodsCode and pt.paCode = gp.paCode"
			+ "                and pt.promoNo = gp.promoNo and pt.seq < gp.seq"
			+ "                and pt.procGb != 'D'  "
			+ "                and pt.transDate = ( select max(gpp.transDate) "
			+ "                                       from PaPromoTarget gpp "
			+ "                                      where gpp.paCode = gp.paCode "
			+ "                                        and gpp.goodsCode = gp.goodsCode )) "
			+ " and gp.transDate is null "
			+ " and gp.seq = (select max(gpp.seq) from PaPromoTarget gpp "
			+ "                where gpp.paCode = gp.paCode "
			+ "                  and gpp.promoNo = gp.promoNo "
			+ "                  and gpp.goodsCode = gp.goodsCode )"
			)
	PaPromoTarget findTargetEndPromo(PaGoodsPriceApply priceApply);

    // 프로모션 삭제 전송대상
	@Query(value = "select gp "
			+ " from PaPromoTarget gp "
			+ "where gp.paCode = :#{#priceApply.paCode} "
			+ " and gp.goodsCode = :#{#priceApply.goodsCode} "
			+ " and (:#{#priceApply.couponPromoNo} is null or gp.promoNo != :#{#priceApply.couponPromoNo}) "
			+ " and gp.procGb = 'D' "
			+ " and exists (select 1 from PaPromoTarget pt "
			+ "              where pt.goodsCode = gp.goodsCode and pt.paCode = gp.paCode"
			+ "                and pt.promoNo = gp.promoNo and pt.seq < gp.seq"
			+ "                and pt.procGb != 'D' and pt.transDate is not null) "
			+ " and gp.transDate is null "
			+ " and gp.seq = (select max(gpp.seq) from PaPromoTarget gpp "
			+ "                where gpp.paCode = gp.paCode "
			+ "                  and gpp.promoNo = gp.promoNo "
			+ "                  and gpp.goodsCode = gp.goodsCode )"
			)
	List<PaPromoTarget> findTargetDeletePromo(PaGoodsPriceApply priceApply);

	// 가격 전송 업데이트
    @Modifying
    @Query(value = "update PaGoodsPrice gp "
    		+ "  set gp.transId = :transId "
    		+ "    , gp.transDate = sysdate "
    		+ "    , gp.modifyId = :transId "
    		+ "    , gp.modifyDate = sysdate"
			+ " where gp.goodsCode = :#{#price.goodsCode} "
			+ "   and gp.paCode = :#{#price.paCode} "
			+ "   and gp.applyDate = :#{#price.applyDate} "
			+ "   and gp.transDate is null ")
    int updatePriceTrans(PaGoodsPrice price, String transId);

	// 프로모션 전송 업데이트
    @Modifying
    @Query(value = "update PaPromoTarget gp "
    		+ "  set gp.modifyId = :transId "
    		+ "    , gp.modifyDate = sysdate"
    		+ "    , gp.transDate = sysdate "
    		+ "    , gp.exceptReason = null "
			+ " where gp.goodsCode = :#{#promo.goodsCode} "
			+ "   and gp.paCode = :#{#promo.paCode} "
			+ "   and gp.promoNo = :#{#promo.promoNo} "
			+ "   and gp.seq = :#{#promo.seq} "
			+ "   and gp.transDate is null ")
    int updatePromoTrans(PaPromoTarget promo, String transId);

}
