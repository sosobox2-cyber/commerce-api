package com.cware.netshopping.pa11st.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.pa11st.v2.domain.Pa11stCnShipCost;

@Mapper
public interface Pa11stCnShipCostMapper {

	/**
	 * 이관 배송비정책 조회
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select c.*, e.pa_addr_seq "
			+ " from tpa11stcnshipcost c "
			+ " inner join tpaentpslip e on e.entp_code = c.entp_code "
			+ "                          and e.entp_man_seq = c.entp_man_seq "
			+ "                          and e.pa_code = c.pa_code and e.pa_addr_seq is not null "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.entp_man_seq = #{entpManSeq} "
			+ "    and c.pa_code = #{paCode} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.trans_target_yn = '1' "
			)
	Pa11stCnShipCost getTransTarget(@Param("entpCode") String entpCode, @Param("entpManSeq") String entpManSeq,
			@Param("shipCostCode") String shipCostCode, @Param("paCode") String paCode);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpa11stcnshipcost c "
    		+ " set c.pa_addr_seq = #{paAddrSeq} "
    		+ " , c.pa_addr_nm = #{paAddrNm} "
    		+ " , c.modify_id = #{modifyId} "
    		+ " , c.modify_date = sysdate "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.entp_man_seq = #{entpManSeq} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.pa_code = #{paCode} "
			)
	int updateOutAddressTrans(Pa11stCnShipCost shipCost);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpa11stcnshipcost c "
    		+ " set c.trans_target_yn = '0' "
    		+ " , c.modify_id = #{modifyId} "
    		+ " , c.modify_date = sysdate "
    		+ " , c.trans_date = sysdate "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.entp_man_seq = #{entpManSeq} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.pa_code = #{paCode} "
			)
	int updateCompleteTrans(Pa11stCnShipCost shipCost);

	/**
	 * 출고지조건부배송비 순번
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @return
	 */
	@Select(value = "select c.pa_addr_seq "
			+ " from tpa11stcnshipcost c "
			+ " inner join tgoods g on decode(g.dely_type, '10', '100001', g.entp_code) = c.entp_code "
			+ "       and decode(g.dely_type, '10', '003', g.ship_man_seq) = c.entp_man_seq "
			+ "       and g.ship_cost_code = c.ship_cost_code "
			+ "       and g.goods_code = #{goodsCode} "
			+ "  where c.pa_code = #{paCode} "
			+ "    and c.pa_addr_seq is not null "
			)
	String getCnPaAddrSeq(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode);
    

}
