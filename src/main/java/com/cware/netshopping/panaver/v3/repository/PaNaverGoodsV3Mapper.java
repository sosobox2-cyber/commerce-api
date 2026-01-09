package com.cware.netshopping.panaver.v3.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.api.panaver.product.type.PaNaverGoodsVO;

@Mapper
public interface PaNaverGoodsV3Mapper {
    
    /**
     * 상품 전송 완료
     * 
     * @param naverGoods
     * @return
     */
    @Update(value = "update tpanavergoods p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.trans_sale_yn = '0' "
    		+ " , p.return_note = null "
    		+ " , p.pa_status = '30' "
    		+ " , p.product_id = #{productId} "
    		+ " , p.org_product_no = #{orgProductNo} "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} ")
	int updateResetTrans(PaNaverGoodsVO naverGoods);

    /**
     * 상품 원상품코드 업데이트
     * @param channelProductNo
     * @param originProductNo
     * @param modifyId
     * @return
     */
    @Update(value = "update tpanavergoods p "
    		+ " set p.org_product_no = #{originProductNo}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.product_id = #{channelProductNo} "
    		)
	int updateOrgProductNo(@Param("channelProductNo") long channelProductNo, @Param("originProductNo") long originProductNo,
			@Param("modifyId") String modifyId);
    
    /**
     * 상품 modelId 업데이트
     * @param goodsCode
     * @param modelId
     * @param modifyId
     * @return
     */
    @Update(value = "update tpanavergoods p "
    		+ " set p.model_id = #{modelId}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		)
    int updateModelId(@Param("goodsCode") String goodsCode, @Param("modelId") String modelId,
    		@Param("modifyId") String modifyId);


}
