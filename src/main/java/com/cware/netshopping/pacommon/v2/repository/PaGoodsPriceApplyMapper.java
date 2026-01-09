package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;

@Mapper
public interface PaGoodsPriceApplyMapper {

	@Select(value = "select gp.* "
			+ " from tpagoodspriceapply gp "
			+ "  where gp.goods_code = #{goodsCode} "
			+ "    and gp.pa_group_code = #{paGroupCode} "
			+ "    and gp.pa_code = #{paCode} "
			+ "    and (gp.apply_date, gp.price_apply_seq) = (select /*+index_desc(sgp pk_tpagoodspriceapply)*/ "
			+ "          sgp.apply_date, sgp.price_apply_seq from tpagoodspriceapply sgp "
			+ "            where sgp.goods_code = gp.goods_code "
			+ "              and sgp.pa_group_code = gp.pa_group_code "
			+ "              and sgp.pa_code = gp.pa_code "
			+ "              and sgp.apply_date <= current_date "
			+ "              and rownum = 1 "
			+ " ) "
			)
	PaGoodsPriceApply getGoodsPrice(@Param("goodsCode") String goodsCode,
					@Param("paGroupCode") String paGroupCode, @Param("paCode") String paCode);

    @Update(value = "update tpagoodspriceapply gp "
    		+ "  set gp.trans_id = #{transId} "
    		+ "    , gp.trans_date = sysdate "
			+ " where gp.goods_code = #{goodsCode} "
			+ "   and gp.pa_group_code = #{paGroupCode} "
			+ "   and gp.pa_code = #{paCode} "
			+ "   and gp.apply_date = #{applyDate} "
			+ "   and gp.price_apply_seq = #{priceApplySeq} "
			+ "   and gp.trans_date is null ")
    int updatePriceApplyTrans(PaGoodsPriceApply priceApply);

    /**
     * 레거시 가격연동 
     * 
     * @param priceApply
     * @return
     */
    @Update(value = "update tpagoodsprice gp "
    		+ "  set gp.trans_id = #{transId} "
    		+ "    , gp.trans_date = sysdate "
			+ " where gp.goods_code = #{goodsCode} "
			+ "   and gp.pa_code = #{paCode} "
			+ "   and gp.apply_date = #{applyDate} "
			+ "   and gp.trans_date is null ")
    int updatePriceTrans(PaGoodsPriceApply priceApply);

    /**
     * 레거시 프로모션연동 
     * 
     * @param priceApply
     * @return
     */
    @Update(value = "update tpapromotarget gp "
    		+ "  set gp.modify_id = #{transId} "
    		+ "    , gp.modify_date = sysdate "
    		+ "    , gp.trans_date = sysdate "
    		+ "    , gp.except_reason = null "
			+ " where gp.goods_code = #{goodsCode} "
			+ "   and gp.pa_code = #{paCode} "
			+ "   and gp.pa_group_code = #{paGroupCode} "
			+ "   and gp.promo_no = #{couponPromoNo} "
			+ "   and gp.proc_gb != 'D' "
			+ "   and gp.seq = (select max(gpp.seq) from tpapromotarget gpp "
			+ "                where gpp.pa_code = gp.pa_code "
			+ "                  and gpp.pa_group_code = gp.pa_group_code "
			+ "                  and gpp.promo_no = gp.promo_no "
			+ "                  and gpp.goods_code = gp.goods_code )"
			+ "   and gp.trans_date is null ")
    int updatePromoTrans(PaGoodsPriceApply priceApply);
    

    /**
     * 레거시 프로모션 제외 연동
     * 
     * @param priceApply
     * @return
     */
    @Update(value = " update tpapromotarget gp "
    		+ "  set gp.modify_id = #{transId} "
    		+ "    , gp.modify_date = sysdate "
    		+ "    , gp.trans_date = sysdate "
    		+ "    , gp.except_reason = null "
			+ " where gp.goods_code = #{goodsCode} "
			+ "   and gp.pa_code = #{paCode} "
			+ "   and gp.pa_group_code = #{paGroupCode} "
			+ "   and gp.proc_gb = 'D' "
			+ "   and exists (select 1 from tpapromotarget pt "
			+ "              where pt.goods_code = gp.goods_code "
			+ "                and pt.pa_code = gp.pa_code  and pt.pa_group_code = gp.pa_group_code"
			+ "                and pt.promo_no = gp.promo_no and pt.seq < gp.seq"
			+ "                and pt.proc_gb != 'D' "
			+ "                and pt.trans_date = ( select max(gpp.trans_date) "
			+ "                                       from tpapromotarget gpp "
			+ "                                      where gpp.pa_code = gp.pa_code "
			+ "                                        and gpp.pa_group_code = gp.pa_group_code "
			+ "                                        and gpp.goods_code = gp.goods_code )) "
			+ "   and gp.seq = (select max(gpp.seq) from tpapromotarget gpp "
			+ "                where gpp.pa_code = gp.pa_code "
			+ "                  and gpp.pa_group_code = gp.pa_group_code "
			+ "                  and gpp.promo_no = gp.promo_no "
			+ "                  and gpp.goods_code = gp.goods_code )"
			+ "   and gp.trans_date is null ")
    int updateDelPromoTrans(PaGoodsPriceApply priceApply);

    /**
     * SSG 레거시 프로모션연동
     * 
     * @param priceApply
     * @return
     */
    @Update(value = "update tpapromogoodsprice gp "
    		+ "  set gp.trans_id = #{transId} "
    		+ "    , gp.trans_date = sysdate "
			+ " where gp.goods_code = #{goodsCode} "
			+ "   and gp.pa_code = #{paCode} "
			+ "   and gp.apply_date = #{applyDate} "
			+ "   and gp.promo_seq = (select max(gpp.promo_seq) from tpapromogoodsprice gpp "
			+ "                where gpp.pa_code = gp.pa_code "
			+ "                  and gpp.goods_code = gp.goods_code "
			+ "                  and gpp.apply_date = gp.apply_date )"
			+ "   and gp.trans_date is null ")
    int updatePromoGoodsPriceTrans(PaGoodsPriceApply priceApply);

}
