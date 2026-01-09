package com.cware.netshopping.pafaple.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.pafaple.domain.model.PaFapleShipCost;
import com.cware.netshopping.pafaple.domain.AddrVO;
import com.cware.netshopping.pafaple.domain.SBrandVO;

@Mapper
public interface PaFapleCommonMapper {
    
	 /**
     * 패플 실적브랜드 업데이트
     * 
     * @param PaFapleShipCost
     * @return
     */  
    @Update("update tpafapleshipcost set "
    		+ "   modify_date = systimestamp"
    		+ " , trans_target_yn = '0' "
    		+ " , brand_id = #{brandId} "
    		+ ", sender_id = #{senderId} "
    		+ " where pa_code = #{paCode} "
    		+ "   and entp_code = #{entpCode} "
    		+ "   and ship_cost_code = #{shipCostCode} "
    		+ "   and brand_code = #{brandCode} "
    		+ "   and ord_cost = #{ordCost} "
    		+ "   and return_cost = #{returnCost} "
    		+ "   and island_cost = #{islandCost} "
    		+ "   and island_return_cost = #{islandReturnCost} "
    		+ "   and trans_target_yn = '1' "
    		+ "	  and brand_id is null "
    		)
	int updateFapleShipCost(PaFapleShipCost paFapleShipCost);
    /**
     * 패플 아이디 조회
     * 
     * @param paCode
     * @return
     */
    @Select(value =  "    select a.remark1 "
				 + "    from tcode a "
				 + " 	where code_lgroup = 'B713'"
				 + "	and code_mgroup = #{paCode} "
		  		 )
    String selectFapleId(Map<String, Object> map);
	
    /**
     * 패플 업데이트브랜드 대상 조회
     * 
     * @return
     */
    @Select(value =  "    select a.sale_brand_name "
				 + "    from tpafapleshipcost a "
				 + " 	where trans_target_yn = '1'"
				 + "      and pa_code = #{paCode} "
		  		 )
    List<Map<String, Object>> selectbrandUpdateTargetList(String paCode);
    
    /**
     * 패션플러스 배송처 등록 대상 조회
     * @param map
     * @return
     */
    List<AddrVO> selectAddrInsertTargetList(Map<String, Object> map);
    
    /**
     * 패션플러스 배송처 수정 대상 조회
     * @param map
     * @return
     */
    List<AddrVO> selectAddrUpdateTargetList(Map<String, Object> map);
        
    /**
     * 패플 실적브랜드 등록 대상 조회
     * @param map
     * @return
     */
    List<SBrandVO> selectbrandInsertTargetList(Map<String, Object> map);
    
	 /**
     * 패플 배송처 ID 업데이트
     * 
     * @param PaFapleShipCost
     * @return
     */  
    @Update("update tpafapleshipcost set "
    		+ "   modify_date = systimestamp"
    		+ " , sender_id = #{senderId} "
    		+ " where pa_code = #{paCode} "
    		+ "   and entp_code = #{entpCode} "
    		+ "   and ship_cost_code = #{shipCostCode} "
    		+ "   and brand_code = #{brandCode} "
    		+ "   and ord_cost = #{ordCost} "
    		+ "   and return_cost = #{returnCost} "
    		+ "   and island_cost = #{islandCost} "
    		+ "   and island_return_cost = #{islandReturnCost} "
    		+ "   and trans_target_yn = '1' "
    		+ "	  and brand_id is null "
    		+ "	  and sender_id is null "
    		)
	int updateFapleSenderId(PaFapleShipCost paFapleShipCost);
}
