package com.cware.netshopping.patdeal.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cware.netshopping.domain.PaTdealShipCostVO;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaTdealDisplayCategory;
import com.cware.netshopping.domain.model.PaTdealShipArea;

@Mapper
public interface PaTdealCommonMapper {
    
	/**
     * 티딜 전시카테고리 INSERT
     * 
     * @param paTdealDispCategory
     * @return
     */
    @Insert("insert into tpatdealdisplaycategory ( "
    		+ "   disp_cat_id, depth1_cat_nm, depth2_cat_nm, depth3_cat_nm, depth4_cat_nm, depth5_cat_nm "
    		+ " , full_cat_nm, disp_order, disp_yn, delete_yn "
    		+ " , insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   #{dispCatId}, #{depth1CatNm}, #{depth2CatNm}, #{depth3CatNm}, #{depth4CatNm}, #{depth5CatNm} "
    		+ " , #{fullCatNm}, #{dispOrder}, #{dispYn}, #{deleteYn} "
    		+ " , #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate} "
    		+ ") ")
    int insertPaTdealDispCategory(PaTdealDisplayCategory paTdealDispCategory);
    
    
    /**
     * 티딜 입출고주소 생성 정보 조회
     * 
     * @param paEntpSlip
     * @return
     */
    @Select(value = "select eu.* "
			+ "  from tentpuser   eu "
			+ "     , tenterprise ep "
			+ " where eu.entp_code = ep.entp_code "
			+ "   and eu.entp_code = #{entpCode} "
			+ "   and eu.entp_man_seq = #{entpManSeq} "
			+ "   and not exists ( select 'X' "
			+ "   					 from tpaentpslip pl "
			+ "						where eu.entp_code = pl.entp_code"
			+ "						  and eu.entp_man_seq = pl.entp_man_seq"
			+ "						  and pl.pa_code = #{paCode} "
			+ "	  ) "
			)
    Map<String, Object> getSlipInsert(PaEntpSlip paEntpSlip);

    /**
     * 티딜 브랜드 생성 대상 조회
     * 
     * @param brandCode
     * @return
     */
	List<Brand> selectRegisterBrandTarget(@Param("brandCode") String brandCode);

    
    /**
     * 티딜 입출고주소 수정 정보 조회
     * 
     * @param paEntpSlip
     * @return
     */
    @Select(value = "select eu.* "
    		+ "			  , pl.pa_addr_seq "
			+ "  from tentpuser   eu "
			+ "     , tenterprise ep "
			+ "     , tpaentpslip pl "
			+ " where eu.entp_code = ep.entp_code "
			+ "   and eu.entp_code = pl.entp_code "
			+ "   and eu.entp_man_seq = pl.entp_man_seq "
			+ "   and pl.pa_code = #{paCode}"
			+ "   and pl.trans_target_yn = '1' "
			+ "   and pl.pa_addr_seq is not null "
			+ "   and pl.entp_code = #{entpCode} "
			+ "   and pl.entp_man_seq = #{entpManSeq} "
			)
    Map<String, Object> getSlipUpdate(PaEntpSlip paEntpSlip);
    
    
    /**
     * 티딜 배송비 지역 INSERT
     * 
     * @param patdealshiparea
     * @return
     */
    @Insert("insert into tpatdealshiparea ( "
    		+ "   area_no, address, country_cd, default_area_yn "
    		+ " , insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   #{areaNo}, #{address}, #{countryCd}, #{defaultAreaYn} "
    		+ " , #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate} "
    		+ ") ")
    int insertPaTdealShipArea(PaTdealShipArea patdealshiparea);
    
    
    /**
     * 티딜 지역별 추가배송비 설정 정보 조회
     * 
     * @param shipCost
     * @return
     */
    @Select(value = " select tc.* "
			+ "  from tpatdealshipcost tc "
			+ " where tc.pa_code = #{paCode} "
			+ "   and tc.entp_code = #{entpCode} "
			+ "   and tc.ship_man_seq = #{shipManSeq} "
			+ "   and tc.return_man_seq = #{returnManSeq} "
			+ "   and tc.ship_cost_code = #{shipCostCode} "
			+ "   and tc.area_fee_no is null "
			+ "   and tc.trans_target_yn = '1' "
			)
    PaTdealShipCostVO getAreaFeesRegisterTarget(PaTdealShipCostVO shipCost);
    
    
    /**
     * 티딜 지역별 추가배송비 설정 정보 조회
     * 
     * @param shipCost
     * @return
     */
    @Select(value = " select sa.* "
			+ " from tpatdealshiparea sa "
			)
    List<PaTdealShipArea> getAreaFees(PaTdealShipCostVO shipCost);
    
    
    /**
     * 티딜 지역별 추가배송비 번호 저장
     * 
     * @param shipCost
     * @return
     */
	int updatePaTdealShipCost(PaTdealShipCostVO shipCost);
	
	
	/**
     * 티딜 배송비 템플릿 그룹 생성 정보 조회
     * 
     * @param shipCost
     * @return
     */
    PaTdealShipCostVO selectShipCostGroupTarget(PaTdealShipCostVO shipCost);
    
    
    /**
     * 티딜 지역별 추가배송비 수정 정보 조회
     * 
     * @param shipCost
     * @return
     */
    @Select(value = " select tc.* "
			+ "  from tpatdealshipcost tc "
			+ " where tc.pa_code = #{paCode} "
			+ "   and tc.entp_code = #{entpCode} "
			+ "   and tc.ship_man_seq = #{shipManSeq} "
			+ "   and tc.return_man_seq = #{returnManSeq} "
			+ "   and tc.ship_cost_code = #{shipCostCode} "
			+ "   and tc.area_fee_no is not null "
			+ "   and tc.trans_target_yn = '1' "
			)
    PaTdealShipCostVO getAreaFeesUpdateTarget(PaTdealShipCostVO shipCost);

}
