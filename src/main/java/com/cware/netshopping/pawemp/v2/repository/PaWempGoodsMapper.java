package com.cware.netshopping.pawemp.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaWempGoods;

@Mapper
public interface PaWempGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpawempgoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaWempGoods getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpawempgoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpawempgoods p "
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
     * @param wempGoods
     * @return
     */
    @Update(value = "update tpawempgoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.product_no = #{productNo} "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_sale_start_date = #{paSaleStartDate, jdbcType=VARCHAR} "
    		+ " , p.pa_sale_end_date = #{paSaleEndDate, jdbcType=VARCHAR} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaWempGoods wempGoods);

    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpawempgoods p "
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
     * @return
     */
    @Update(value = "update tpawempgoods p "
    		+ " set p.pa_status = '90' "
    		+ " , p.pa_sale_gb = '30' "
    		+ " , p.trans_sale_yn = '1' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status >= '30' ")
	int stopTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

    /**
     * 상품 전송상태 리셋
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpawempgoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int resetTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

    
    /**
     * 상품 입점 재요청
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpawempgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.product_no = null "
    		+ " , p.pa_sale_start_date = null "
    		+ " , p.pa_sale_end_date = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

}
