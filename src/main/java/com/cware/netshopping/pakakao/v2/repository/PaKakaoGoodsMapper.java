package com.cware.netshopping.pakakao.v2.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaKakaoGoods;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;

@Mapper
public interface PaKakaoGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpakakaogoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaKakaoGoods getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpakakaogoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpakakaogoods p "
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
     * @param kakaoGoods
     * @return
     */
    @Update(value = "update tpakakaogoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.product_id = #{productId} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaKakaoGoods kakaoGoods);

    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpakakaogoods p "
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
    @Update(value = "update tpakakaogoods p "
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
    @Update(value = "update tpakakaogoods p "
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
    @Update(value = "update tpakakaogoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.product_id = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);


	/**
	 * 상품이미지 조회
	 * 
	 * @param goodsCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpakakaogoodsimage g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_group_code = '11' "
			+ "    and g.kakao_image is not null "
			+ "    and g.upload_status = '30' "
			+ "    and not exists (select 1 from tpakakaogoodsimage gi "
			+ "                   where gi.goods_code = g.goods_code"
			+ "                     and gi.pa_group_code = '11' "
			+ "                     and gi.upload_status < '30' )"
			)
	List<PaKakaoGoodsImage> getImageList(@Param("goodsCode") String goodsCode);
	
	/**
     * 톡딜 TRANS_BDATE UPDATE
     * 
     * @param goodsCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpakakaotalkdeal p "
    		+ " set p.trans_bdate = sysdate "
    		+ " , p.modify_date = sysdate "
    		+ " , p.modify_id = #{modifyId} "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.promo_no = #{promoNo} " )
	int updateTalkDealBDate(@Param("goodsCode") String goodsCode, @Param("promoNo") String promoNo, @Param("modifyId") String modifyId);
    
    /**
     * 톡딜 TRANS_EDATE UPDATE
     * 
     * @param goodsCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpakakaotalkdeal p "
    		+ " set p.trans_edate = sysdate "
    		+ " , p.modify_date = sysdate "
    		+ " , p.modify_id = #{modifyId} "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.trans_bdate is not null "
    		+ "   and p.trans_edate is null " )
	int updateTalkDealEDate(@Param("goodsCode") String goodsCode, @Param("modifyId") String modifyId);
    
    /**
     * 톡딜 아이디 UPDATE
     * 
     * @param goodsCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpakakaotalkdeal p "
    		+ " set p.deal_id = #{dealId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.modify_id = #{modifyId} "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.promo_no = #{promoNo} " )
	int updateTalkDealId(@Param("goodsCode") String goodsCode, @Param("dealId") String dealId,
			@Param("promoNo") String promoNo, @Param("modifyId") String modifyId);
	
	/**
     * 톡딜상품 수기중단
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpakakaogoods p "
    		+ " set p.pa_status = '80' "
    		+ " , p.pa_sale_gb = '30' "
    		+ " , p.trans_sale_yn = '0' "
    		+ " , p.return_note = #{returnNote} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int stopTalkDealGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);
	
}
