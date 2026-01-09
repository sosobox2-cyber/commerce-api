package com.cware.netshopping.paintp.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaIntpGoods;

@Mapper
public interface PaIntpGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpaintpgoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaIntpGoods getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpaintpgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status = '15' ")
	int updateClearProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);
    
    
    /**
     * 상품 전송 완료
     * 
     * @param intpGoods
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_sale_yn = case when p.pa_sale_gb = '30' then p.trans_sale_yn else '0' end "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.prd_no = #{prdNo} "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaIntpGoods intpGoods);
    

    /**
     * 상품 판매상태 전송 완료
     * 
     * @param intpGoods
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.trans_sale_yn = '0' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetSaleTrans(PaIntpGoods intpGoods);

    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpaintpgoods p "
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
     * 수기중단
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @param transSaleYn
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.pa_status = '90' "
    		+ " , p.pa_sale_gb = '30' "
    		+ " , p.trans_sale_yn = #{transSaleYn} "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status >= '30' ")
	int stopTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId,
			@Param("returnNote") String returnNote, @Param("transSaleYn") String transSaleYn);

    /**
     * 상품 입점 재요청
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.pa_status =  case when p.pa_sale_gb = '30' then '20' else '10' end "
    		+ " , p.prd_no = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);
    
    /**
     * 금칙어 타겟 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.prohibitid_yn = '1' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateProhibitidYn(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);
    
    /**
     * 금칙어 타겟 리셋
     * 
     * @param intpGoods
     * @return
     */
    @Update(value = "update tpaintpgoods p "
    		+ " set p.prohibitid_yn = '0' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int resetProhibitidYn(PaIntpGoods intpGoodse);

}
