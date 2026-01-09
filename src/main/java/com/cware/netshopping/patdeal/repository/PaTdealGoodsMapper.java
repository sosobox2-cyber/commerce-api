package com.cware.netshopping.patdeal.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.cware.netshopping.domain.PaTdealGoodsVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaTdealEvent;
import com.cware.netshopping.domain.model.PaTdealGoodsDtImage;


@Mapper
public interface PaTdealGoodsMapper {

    
    @Update(value = "update tpatdealgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status = '15' ")
	int updateClearProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);


	/**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpatdealgoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    /**
     * 티딜 상품상세정보 조회
     * @param map
     * @return
     */
    PaTdealGoodsVO selectPaTdealGoodsInfo(Map<String, Object> map);
    
    /**
     * 티딜 정보고시 조회
     * @param map
     * @return
     */
	List<PaGoodsOffer> selectPaTdealGoodsOfferList(Map<String, Object> map);
	
	/**
	 * 티딜 단품 옵션 조회
	 * @param map
	 * @return
	 */
	List<PaGoodsdtMapping> selectPaTdealGoodsdtInfoList(Map<String, Object> map);

	 /**
     * 상품 전송 완료
     * 
     * @param tdealgoods
     * @return
     */
    @Update(value = "update tpatdealgoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.mall_product_no = #{mallProductNo} "
     		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	void updateResetTrans(PaTdealGoodsVO paTdealGoods);

    /**
     * 승인 완료 상태 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpatdealgoods p "
    		+ " set p.confirm_status = '20' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} " )
	void updateConfirmComplete(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    /**
     * 승인 대기 상태 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpatdealgoods p "
    		+ " set p.confirm_status = '10' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} " )
	void updateConfirmWait(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
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
    @Update(value = "update tpatdealgoods p "
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
     * 상품 입점 재요청
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpatdealgoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.mall_product_no = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
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
    @Update(value = "update tpatdealgoods p "
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
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpatdealgoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaTdealGoodsVO getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);


	/**
	 * 티딜 옵션 이미지 조회
	 * @param goodsCode
	 * @return
	 */
	@Select(value = "select ti.* "
			+ " from tpatdealgoodsdtimage ti "
			+ "  where ti.goods_code = #{goodsCode} "
			+ "    and ti.image_file is not null "
			)
	List<PaTdealGoodsDtImage> selectPaTdealGoodsDtImageList(@Param("goodsCode") String goodsCode);


	/**
	 * 티딜 행사 상품 조회
	 * @param goodsCode
	 * @return
	 */
	PaTdealEvent selectPaTdealEvent(String goodsCode);


}
