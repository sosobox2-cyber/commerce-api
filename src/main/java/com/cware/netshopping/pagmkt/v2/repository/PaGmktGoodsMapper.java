package com.cware.netshopping.pagmkt.v2.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoods;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoodsDescribe;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoodsOffer;

@Mapper
public interface PaGmktGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpagmktgoods g"
			+ " inner join tpagoodstarget t on t.goods_code = g.goods_code "
			+ "   and t.pa_group_code = g.pa_group_code "
			+ "   and t.pa_code = g.pa_code "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	List<EbayGoods> getGoodsList(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status = '15' ")
	int updateClearProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '20' "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status <= '20' ")
	int rejectTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

    /**
     * 사이트별 입점완료 
     * 
     * @param gmktGoods
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '30' "
    		+ " , p.esm_goods_code = #{esmGoodsCode} "
    		+ " , p.item_no = #{itemNo} "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.return_note = null "
    		+ " , p.trans_target_yn = '0' "
    		+ " , p.trans_complete_date = sysdate "
    		+ " , p.selling_period = trunc(sysdate) + 90 "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} ")
	int updateCompleteTrans(PaGmktGoods gmktGoods);

    /**
     * 사이트별 입점반려
     * 
     * @param gmktGoods
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '20' "
    		+ " , p.pa_sale_gb = #{paSaleGb} "
    		+ " , p.esm_goods_code = #{esmGoodsCode} "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} "
    		+ "   and p.pa_status <= '20' ")
	int updateRejectTrans(PaGmktGoods gmktGoods);

    /**
     * 사이트별 수기중단
     * 
     * @param gmktGoods
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '90' "
    		+ " , p.pa_sale_gb = '30' "
    		+ " , p.trans_sale_yn = '1' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} "
    		+ "   and p.pa_status >= '30' ")
	int updateStopTrans(PaGmktGoods gmktGoods);
    
    /**
     * 사이트별 연동대상리셋 
     * 
     * @param gmktGoods
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_sale_yn = case when p.pa_sale_gb = '30' then p.trans_sale_yn else '0' end "
    		+ " , p.return_note = null "
    		+ " , p.item_no = #{itemNo} "
//    		+ " , p.sale_stop_date = case when p.trans_sale_yn = '1' and p.pa_sale_gb = '30' then sysdate else null end "
    		+ " , p.selling_period = nvl(selling_period, trunc(sysdate)) + 90 "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} ")
	int updateResetTrans(PaGmktGoods gmktGoods);


    /**
     * 상품 입점 재요청
     * 
     * @param gmktGoods
     * @return
     */
    @Update(value = "update tpagmktgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.esm_goods_code = null "
    		+ " , p.item_no = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
			+ "   and p.pa_group_code = #{paGroupCode} ")
	int requestTransTarget(PaGmktGoods gmktGoods);

    
	/**
	 * 연동상품정보
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param isModify
	 * @return
	 */
	List<EbayGoods> selectGoodsTransTaget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("isModify") boolean isModify);

	/**
	 * 정보고시
	 * 
	 * @param goodsCode
	 * @return
	 */
	List<EbayGoodsOffer> selectGoodsOffer(@Param("goodsCode") String goodsCode);

	/**
	 * 상품설명
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	EbayGoodsDescribe selectGoodsDescribe(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

	/**
	 * 예외업체 발송정책 
	 * @param entpCode
	 * @param paCode
	 * @param paGroupCode
	 * @return
	 */
	PaGmktPolicy selectDispatchPolicyExceptEntp(@Param("entpCode") String entpCode, @Param("paCode") String paCode,
			@Param("paGroupCode") String paGroupCode);

	/**
	 * 예외중분류 발송정책
	 * 
	 * @param lmCode 대+중
	 * @param paCode
	 * @param paGroupCode
	 * @return
	 */
	PaGmktPolicy selectDispatchPolicyExceptMgroup(@Param("lmCode") String lmCode, @Param("paCode") String paCode,
			@Param("paGroupCode") String paGroupCode);

	/**
	 * 발송정책
	 * 
	 * @param gmktPolicy
	 * @return
	 */
	PaGmktPolicy selectDispatchPolicy(PaGmktPolicy gmktPolicy);

	/**
	 * 단품정보
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	List<PaGoodsdtMapping> selectGoodsOption(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

	@Select(value = "select code_group "
			+ " from tcode c "
			+ "  where c.code_lgroup = 'O505' "
			+ "    and c.code_mgroup = #{paCode} "
			)
	String getStockRate(@Param("paCode") String paCode);
	
}
