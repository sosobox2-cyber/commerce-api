package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;

@Mapper
public interface PaGoodsTargetMapper {

	/**
	 * 타겟데이터 존재여부 체크
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param paGroup
	 * @return
	 */
	@Select(value = "select count(1) "
			+ " from tpagoodstarget t "
			+ "  where t.goods_code = #{goodsCode} "
			+ "    and t.pa_code = #{paCode} "
			+ "    and t.pa_group_code = #{paGroupCode} "
			)
	int existsGoodsTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("paGroupCode") String paGroupCode);
	

	@Select(value = "select count(1) "
			+ " from tpagoodstarget t "
			+ "  where t.goods_code = #{goodsCode} "
			+ "    and t.pa_code = #{paCode} "
			)
	int countGoodsTarget(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);

   
    /**
     * 타겟 입점 업데이트
     * 
     * @param goodsTarget
     * @return
     */
    @Update(value = "update tpagoodstarget p "
    		+ " set p.pa_goods_code = #{paGoodsCode} "
    		+ " , p.pa_sale_gb = '20' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} "
    		+ "   and (p.pa_goods_code is null"
    		+ "    or  p.pa_goods_code != #{paGoodsCode}) ")
	int updateGoodsTarget(PaGoodsTarget goodsTarget);

    /**
     * 자동입점 제외
     * 
     * @param goodsCode
     * @param paCode
     * @param paGroupCode
     * @param modifyId
     * @return
     */
    @Update(value = "update tpagoodstarget p "
    		+ " set p.auto_yn = '0' "
    		+ " , p.modify_id = #{modifyId} "
    		+ " , p.modify_date = sysdate "
    		+ " where p.goods_code = #{goodsCode} "
    		+ "   and p.pa_code = #{paCode} "
    		+ "   and p.pa_group_code = #{paGroupCode} " )
	int disableAutoYn(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("paGroupCode") String paGroupCode, @Param("modifyId") String modifyId);

}
