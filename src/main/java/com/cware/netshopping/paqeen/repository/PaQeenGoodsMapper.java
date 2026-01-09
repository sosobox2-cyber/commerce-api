package com.cware.netshopping.paqeen.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.PaQeenGoodsVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaQeenGoodsdtMapping;

@Mapper
public interface PaQeenGoodsMapper {
	/**
     * 퀸잇 상품상세정보 조회
     * @param map
     * @return
     */
	PaQeenGoodsVO selectPaQeenGoodsInfo(Map<String, Object> map);
	
    /**
     * 퀸잇 정보고시 조회
     * @param map
     * @return
     */
	List<PaGoodsOffer> selectPaQeenGoodsOfferList(Map<String, Object> map);
	
    /**
     * 퀸잇 상품 옵션 조회
     * @param map
     * @return
     */
	List<PaQeenGoodsdtMapping> selectPaQeenGoodsdtInfoList(Map<String, Object> map);

	
	 /**
     * 상품 전송 완료
     * 
     * @param paQeenGoods
     * @return
     */
    @Update(value = "update tpaqeengoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_sale_gb = '20' "
    		+ " , p.pa_status = '30' "
    		+ " , p.reified_product_id = #{reifiedProductId} "
    		+ " , p.product_proposal_id = #{productProposalId} "
     		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaQeenGoodsVO paQeenGoods);
   
    /**
     * 상품 임시 번호 저장
     * 
     * @param paQeenGoods
     * @return
     */
    @Update(value = "update tpaqeengoods p "
    		+ " set p.product_proposal_id = #{productProposalId} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
    int updateQeenProposal(PaQeenGoodsVO paQeenGoods);
    
    /**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpaqeengoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaQeenGoodsVO getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);
	
	
	
    /**
     * 상품 입점반려
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @param returnNote
     * @return
     */
    @Update(value = "update tpaqeengoods p "
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
    @Update(value = "update tpaqeengoods p "
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
    @Update(value = "update tpaqeengoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.product_proposal_id = null "
    		+ " , p.reified_product_id = null "
    		+ " , p.return_note = #{returnNote}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " , p.insert_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int requestTransTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId, @Param("returnNote") String returnNote);

}
