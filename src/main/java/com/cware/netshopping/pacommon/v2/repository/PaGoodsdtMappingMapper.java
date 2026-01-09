package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaGoodsdtMapping;

@Mapper
public interface PaGoodsdtMappingMapper {
   
    
    /**
     * 옵션 전송 결과(이베이용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_option_code = p.goods_code || p.goodsdt_code "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTransForEbay(PaGoodsdtMapping goodsdt);

    /**
     * 옵션 전송 결과(티몬)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = decode(p.pa_option_code, null, '0', p.trans_stock_yn) "
    		+ " , p.trans_order_able_qty =  decode(p.pa_option_code, null, #{transOrderAbleQty}, p.trans_order_able_qty) "
    		+ " , p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTransForTmon(PaGoodsdtMapping goodsdt);
    

    /**
     * 옵션 전송 결과(카카오용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTransForKakao(PaGoodsdtMapping goodsdt);
    
    /**
     * 옵션 전송 결과(네이버용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
    int updateCompleteTransForV3Naver(PaGoodsdtMapping goodsdt);
    
    /**
     * 옵션 전송 결과(티딜용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTransForTdeal(PaGoodsdtMapping goodsdt);
 
    /**
     * 옵션 전송 결과 - 재고수량 업데이트 (티딜용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
    int updateTransOrderAbleQtyForTdeal(PaGoodsdtMapping goodsdt);    
    
    /**
     * 옵션 전송 결과(패션플러스용)
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpafaplegoodsdtmapping p "
    		+ " set p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.goodsdt_seq = #{goodsdtSeq} "
    		)
	int updateCompleteTransForFaple(PaGoodsdtMapping goodsdt);
    
    /**
     * 옵션 전송 결과
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTrans(PaGoodsdtMapping goodsdt);

    /**
     * 전송비대상 옵션 리셋
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = 0 "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and (p.trans_target_yn = '1' or p.trans_stock_yn = '1') "
    		)
	int updateResetTrans(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    /**
     * 옵션코드 클린징(리텐션 적용)
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.pa_option_code = null "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_option_code is not null "
    		)
	int clearOptionCode(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);
    

	/**
	 * 옵션 전송대상 존재여부 체크
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select count(1) "
			+ " from tpagoodsdtmapping p "
			+ "  where p.goods_code = #{goodsCode} "
			+ "    and p.pa_code = #{paCode} "
			+ "    and (p.trans_target_yn = '1' or p.trans_stock_yn = '1') "
			)
	int existsTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 옵션코드매핑
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateOptionCode(@Param("goodsCode") String goodsCode, @Param("goodsdtCode") String goodsdtCode,
			@Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("paOptionCode") String paOptionCode);
    
    /**
     * 옵션코드매핑(네이버용)
     * 
     * @param goodsCode
     * @param goodsdtCode
     * @param paCode
     * @param modifyId
     * @param paOptionCode
     * @return
     */
    @Update(value = "update tpagoodsdtmapping p "
    		+ " set p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.trans_target_yn = '0' "
    		+ " , p.trans_stock_yn  = '0' "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.pa_code = #{paCode} "
    		)
    int updateOptionCodeForV3Naver(@Param("goodsCode") String goodsCode, @Param("goodsdtCode") String goodsdtCode,
    		@Param("paCode") String paCode,
    		@Param("modifyId") String modifyId, @Param("paOptionCode") String paOptionCode);
    
}
