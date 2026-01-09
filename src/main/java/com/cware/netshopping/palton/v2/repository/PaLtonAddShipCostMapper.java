package com.cware.netshopping.palton.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaLtonAddShipCost;

@Mapper
public interface PaLtonAddShipCostMapper {

	/**
	 * 이관 추가배송비정책 조회
	 * 
	 * @param islandCost
	 * @param jejuCost
	 * @param paCode
	 * @return
	 */
	@Select(value = "select c.* "
			+ " from tpaltonaddshipcost c "
			+ "  where c.pa_code = #{paCode} "
			+ "    and c.island_cost = #{islandCost} "
			+ "    and c.jeju_cost = #{jejuCost} "
			+ "    and c.dv_cst_pol_no is null "
			)
	PaLtonAddShipCost getTransTarget(@Param("islandCost") Double islandCost, 
			@Param("jejuCost") Double jejuCost, @Param("paCode") String paCode);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpaltonaddshipcost c "
    		+ " set  c.dv_cst_pol_no = #{dvCstPolNo} "
			+ "  where c.pa_code = #{paCode} "
			+ "    and c.island_cost = #{islandCost} "
			+ "    and c.jeju_cost = #{jejuCost} "
			)
	int updateCompleteTrans(PaLtonAddShipCost shipCost);

}
