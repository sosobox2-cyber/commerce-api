package com.cware.netshopping.pacopn.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaCopnGoods;

@Mapper
public interface PaCopnGoodsMapper {

	/**
	 * 이관상품정보 조회
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select g.* "
			+ " from tpacopngoods g "
			+ "  where g.goods_code = #{goodsCode} "
			+ "    and g.pa_code = #{paCode} "
			)
	PaCopnGoods getGoods(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

    /**
     * 입점요청중 업데이트
     * 
     * @param goodsCode
     * @param paCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpacopngoods p "
    		+ " set p.pa_status = '15' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status in ('10', '20') ")
	int updateProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

    @Update(value = "update tpacopngoods p "
    		+ " set p.pa_status = '10' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_status = '15' ")
	int updateClearProceeding(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("modifyId") String modifyId);

}
