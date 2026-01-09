package com.cware.netshopping.passg.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;

@Mapper
public interface PaSsgGoodsdtMappingMapper {
   
    /**
     * 옵션 전송 결과
     * 
     * @param goodsdt
     * @return
     */
    @Update(value = "update tpassggoodsdtmapping p "
    		+ " set p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = #{transOrderAbleQty} "
    		+ " , p.pa_option_code = #{paOptionCode} "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.goodsdt_code = #{goodsdtCode} "
            + "   and p.goodsdt_seq = #{goodsdtSeq} "
    		+ "   and p.pa_code = #{paCode} "
    		)
	int updateCompleteTrans(PaSsgGoodsdtMapping goodsdt);

    /**
     * 전송비대상 옵션 리셋
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpassggoodsdtmapping p "
    		+ " set p.trans_stock_yn = '0' "
    		+ " , p.trans_order_able_qty = 0 "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.trans_stock_yn = '1' "
    		)
	int updateResetTrans(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);
}
