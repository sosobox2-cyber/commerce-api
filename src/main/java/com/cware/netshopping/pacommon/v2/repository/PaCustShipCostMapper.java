package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaCustShipCost;

@Mapper
public interface PaCustShipCostMapper {

	/**
	 * 이관 배송비정책 조회
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select c.* "
			+ " from tpacustshipcost c "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.pa_code = #{paCode} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.trans_target_yn = '1' "
			)
	PaCustShipCost getTransTarget(@Param("entpCode") String entpCode,
			@Param("shipCostCode") String shipCostCode, @Param("paCode") String paCode);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpacustshipcost c "
    		+ " set c.trans_target_yn = '0' "
    		+ " , c.group_code = #{groupCode} "
    		+ " , c.modify_id = #{modifyId} "
    		+ " , c.modify_date = sysdate "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.pa_code = #{paCode} "
			)
	int updateCompleteTrans(PaCustShipCost shipCost);

}
