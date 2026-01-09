package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PaGoodsImageMapper {
   
    /**
     * 상품이미지 타겟 리셋
     * 
     * @param goodsCode
     * @param paGroupCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpagoodsimage p "
    		+ " set p.trans_target_yn = '0' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} ")
	int updateCompleteTrans(@Param("goodsCode") String goodsCode, @Param("paGroupCode") String paGroupCode,
			@Param("modifyId") String modifyId);

}
