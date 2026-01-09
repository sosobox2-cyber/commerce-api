package com.cware.netshopping.passg.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.passg.v2.domain.PaSsgShipCost;

@Mapper
public interface PaSsgShipCostMapper {

	/**
	 * 이관 배송비정책 조회
	 * 
	 * @param shipCost
	 * @return
	 */
	@Select(value = "select c.* "
			+ " from tpassgshipcost c "
			+ "  where c.pa_code = #{paCode} "
			+ "    and c.shppcst_apl_unit_cd = #{shppcstAplUnitCd} "
			+ "    and c.shppcst_plcy_div_cd = #{shppcstPlcyDivCd} "
			+ "    and c.collect_yn = #{collectYn} "
			+ "    and c.ship_cost_base_amt = #{shipCostBaseAmt} "
			+ "    and c.ship_cost = #{shipCost} "
			+ "    and c.pa_ship_policy_no is null "
			)
	PaSsgShipCost getTransTarget(PaSsgShipCost shipCost);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpassgshipcost c "
    		+ " set c.pa_ship_policy_no = #{paShipPolicyNo} "
			+ "  where c.pa_code = #{paCode} "
			+ "    and c.shppcst_apl_unit_cd = #{shppcstAplUnitCd} "
			+ "    and c.shppcst_plcy_div_cd = #{shppcstPlcyDivCd} "
			+ "    and c.collect_yn = #{collectYn} "
			+ "    and c.ship_cost_base_amt = #{shipCostBaseAmt} "
			+ "    and c.ship_cost = #{shipCost} "
			)
	int updateCompleteTrans(PaSsgShipCost shipCost);

}
