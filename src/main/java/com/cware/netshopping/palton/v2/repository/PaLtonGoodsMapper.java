package com.cware.netshopping.palton.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.PaLtonGoodsVO;

@Mapper
public interface PaLtonGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpaltongoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaLtonGoodsVO getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpaltongoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpaltongoods p "
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
     * @param ltonGoods
     * @return
     */
    @Update(value = "update tpaltongoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.spd_no = #{spdNo} "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaLtonGoodsVO ltonGoods);

    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpaltongoods p "
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
    @Update(value = "update tpaltongoods p "
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
    @Update(value = "update tpaltongoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int resetTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

    /**
     * 표준카테고리/전시카테고리 매핑
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @param stdCatId 표준카테고리
     * @return
     */
    @Update(value = "update tpaltongoods p "
           + " set p.scat_no = #{stdCatId} "
           + " , p.lf_dcat_no = (select listagg(d.disp_cat_id, ',') from tpaltonstandarddisplist d where d.std_cat_id = #{stdCatId}) "
           + " , p.return_note = #{returnNote} "
           + " , p.modify_id = #{modifyId} "
           + " , p.modify_date = sysdate "
           + " where p.goods_code = #{goodsCode} "
           + "   and p.pa_code = #{paCode} ")
    int mappingCategory(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
            @Param("modifyId") String modifyId,
            @Param("returnNote") String returnNote, @Param("stdCatId") String stdCatId);
    

    /**
     * 매핑 브랜드 리셋
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpaltongoods p "
           + " set p.brd_no = null "
           + " , p.return_note = #{returnNote}"
           + " , p.modify_id = #{modifyId} "
           + " , p.modify_date = sysdate "
           + " where p.goods_code = #{goodsCode} "
           + "   and p.pa_code = #{paCode} ")
   int resetBrand(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
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
    @Update(value = "update tpaltongoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.spd_no = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

}
