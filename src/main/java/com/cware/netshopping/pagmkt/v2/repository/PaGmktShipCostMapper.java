package com.cware.netshopping.pagmkt.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.pagmkt.v2.domain.EbayShipCost;

@Mapper
public interface PaGmktShipCostMapper {

	/**
	 * 이관 배송비정책 조회
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @return
	 */
	@Select(value = "select c.*, e.pa_addr_seq, m.ship_cost_receipt "
			+ " from tpagmktshipcostdt c "
			+ " inner join tpaentpslip e on e.entp_code = c.entp_code "
			+ "                          and e.entp_man_seq = c.entp_man_seq "
			+ "                          and e.pa_code = '21' and e.pa_addr_seq is not null "
			+ " inner join tshipcostm m on m.entp_code = c.entp_code "
			+ "                               and m.ship_cost_code = c.ship_cost_code "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.entp_man_seq = #{entpManSeq} "
			+ "    and c.ship_cost_code = #{shipCostCode} "
			+ "    and c.trans_target_yn = '1' "
			)
	EbayShipCost getTransTarget(@Param("entpCode") String entpCode, @Param("entpManSeq") String entpManSeq,
			@Param("shipCostCode") String shipCostCode);

    /**
     * 배송비정책 연동 완료
     * 
     * @param shipCost
     * @return
     */
    @Update(value = "update tpagmktshipcostdt c "
    		+ " set c.gmkt_ship_no = #{gmktShipNo} "
    		+ " , c.bundle_no = #{bundleNo} "
    		+ " , c.trans_target_yn = '0' "
    		+ " , c.modify_id = #{modifyId} "
    		+ " , c.modify_date = sysdate "
			+ "  where c.entp_code = #{entpCode} "
			+ "    and c.entp_man_seq = #{entpManSeq} "
			+ "    and c.ship_cost_code = #{shipCostCode} ")
	int updateCompleteTrans(EbayShipCost shipCost);
    

}
