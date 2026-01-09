package com.cware.netshopping.patmon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.patmon.v2.domain.PaTmonShipCost;

@Mapper
public interface PaTmonShipCostMapper {

	/**
	 * 이관 배송템플릿 조회
	 * 
	 * @param shipCost
	 * @return
	 */
	@Select(value = "select c.* "
			+ "           , s.pa_addr_seq as pa_ship_man_seq "
			+ "           , r.pa_addr_seq as pa_return_man_seq "
			+ " from tpatmonshipcost c "
			+ " inner join tpaentpslip s on s.entp_code = c.entp_code and s.entp_man_seq = c.ship_man_seq and s.pa_code = c.pa_code and s.pa_addr_seq is not null "
			+ " inner join tpaentpslip r on r.entp_code = c.entp_code and r.entp_man_seq = c.return_man_seq and r.pa_code = c.pa_code and r.pa_addr_seq is not null "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.pa_code = #{paCode} "
			+ "    and c.ship_man_seq = #{shipManSeq} "
			+ "    and c.return_man_seq = #{returnManSeq} "
			+ "    and c.product_type = #{productType} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.apply_date = #{applyDate} "
			+ "    and c.no_ship_island = #{noShipIsland} "
			+ "    and c.pa_ship_policy_no is null "
			)
	PaTmonShipCost getTransTarget(PaTmonShipCost shipCost);

    /**
     * 배송템플릿 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpatmonshipcost c "
    		+ " set c.pa_ship_policy_no = #{paShipPolicyNo} "
    		+ " , c.modify_id = #{modifyId} "
    		+ " , c.modify_date = sysdate "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.pa_code = #{paCode} "
			+ "    and c.ship_man_seq = #{shipManSeq} "
			+ "    and c.return_man_seq = #{returnManSeq} "
			+ "    and c.product_type = #{productType} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.apply_date = #{applyDate} "
			+ "    and c.no_ship_island = #{noShipIsland} "
			)
	int updateCompleteTrans(PaTmonShipCost shipCost);

}
