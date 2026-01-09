package com.cware.netshopping.pafaple.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.PaFapleGoodsVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;

@Mapper
public interface PaFapleGoodsMapper {
	/**
     * 패션플러스 상품상세정보 조회
     * @param map
     * @return
     */
    PaFapleGoodsVO selectPaFapleGoodsInfo(Map<String, Object> map);

    /**
     * 패션플러스 정보고시 조회
     * @param map
     * @return
     */
	List<PaGoodsOffer> selectPaFapleGoodsOfferList(Map<String, Object> map);

    /**
     * 패션플러스 상품 옵션 조회
     * @param map
     * @return
     */
	List<PaGoodsdtMapping> selectPaFapleGoodsdtInfoList(Map<String, Object> map);
	

    /**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpafaplegoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaFapleGoodsVO getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);
	
	 /**
     * 상품 전송 완료
     * 
     * @param tfaplegoods
     * @return
     */
    @Update(value = "update tpafaplegoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_sale_gb = '20' "
    		+ " , p.pa_status = '30' "
    		+ " , p.item_id = #{itemId} "
    		+ " , p.brand_id = #{brandId}"
     		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.brand_change_yn ='0'"
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	void updateResetTrans(PaFapleGoodsVO paFapleGoods);
    
    /**
     * 상품 전송 실패
     * 
     * @param tfaplegoods
     * @return
     */
    @Update(value = "update tpafaplegoods p "
    		+ " set p.pa_status = '20' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status <= '20' ")
	void rejectTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
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
    @Update(value = "update tpafaplegoods p "
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
     * 상품 입점 재요청
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpafaplegoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.item_id = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);
    
    
	 /**
     * 판매상태 변경 대상여부 업데이트(브랜드 변경 신규 등록)
     * 
     * @param tfaplegoods
     * @return
     */
    @Update(value = "update tpafaplegoods p "
    		+ " set p.trans_sale_yn = '0' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	void updateBrandChangeSaleYn(PaFapleGoodsVO paFapleGoods);
    
    /**
     * 패션플러스 브랜드 변경 재입점 대상
     * String paCode
     * @return
     */
	List<PaFapleGoodsVO> selectPaFapleGoodsBrandTarget(String paCode);
	
	
	/**
     * 패션플러스 브랜드 변경 재입점 대상  DELETE
     * @return
     */
    @Delete(value = "delete "
    		+ " from tpafaplegoodsbrandtarget m "
    		+ " where m.goods_code = #{goodsCode} "
    		+ "   and m.pa_code = #{paCode} "
    		+ "   and m.target_yn = '1' ")
	int deleteFapleGoodsBrandTarget(Map<String,Object> map);
    
    /**
     * 패션플러스 브랜드 변경 재입점 대상 테이블 삽입
     * @return
     */
	 void insertBrandReRegisterProduct();
}
