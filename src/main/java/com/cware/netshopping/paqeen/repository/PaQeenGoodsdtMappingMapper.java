package com.cware.netshopping.paqeen.repository;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaQeenGoodsdtMapping;

@Mapper
public interface PaQeenGoodsdtMappingMapper {
	
	
    /**
     * 옵션 전송 결과
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpaqeengoodsdtmapping p "
    		+ " set p.trans_stock_yn = '0' "
    		+ " , p.product_item_proposal_id = #{productItemProposalId}"
    		+ " , p.pa_option_code = #{paOptionCode}"
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
            + "   and p.goodsdt_seq = #{goodsdtSeq} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTrans(PaQeenGoodsdtMapping goodsdt);
    
    /**
     * 옵션 임시 옵션 번호 저장
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpaqeengoodsdtmapping p "
    		+ " set p.product_item_proposal_id = #{productItemProposalId}"
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
    		+ "   and p.goodsdt_seq = #{goodsdtSeq} "
    		+ "   and p.pa_code = #{paCode} "
    		)
    int updateItemProposalId(PaQeenGoodsdtMapping goodsdt);
    
    /**
     * 옵션코드 클린징(리텐션 적용)
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpaqeengoodsdtmapping p "
    		+ " set p.pa_option_code = null "
    		+ " , p.product_item_proposal_id = null "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_option_code is not null "
    		+ "   and p.product_item_proposal_id is not null "
    		)
	int clearOptionCode(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);
    
	
}
