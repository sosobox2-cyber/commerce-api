package com.cware.netshopping.paqeen.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.PaQeenShipCostVO;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.paqeen.domain.CategoryOffer;

@Mapper
public interface PaQeenCommonMapper {
    
	 /**
     * 퀸잇 브랜드 등록
     * 
     * @param PaBrand
     * @return
     */  
    @Insert("insert into tpabrand ( "
    		+ "   PA_GROUP_CODE, PA_BRAND_NO, PA_BRAND_NAME, USE_YN, INSERT_ID, INSERT_DATE "
    		+ " , MODIFY_ID, MODIFY_DATE, REMARK1 "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, #{paBrandNo}, #{paBrandName}, #{useYn}, #{insertId}, #{insertDate} "
    		+ " , #{modifyId}, #{modifyDate}, #{paCode}"
    		+ ") ")
    int insertPaBrand(PaBrand paBrand);
    /**
     * 퀸잇 브랜드 조회
     * 
     * @param paCode
     * @return
     */
    @Select(value =  "    select count(*) "
				 + "    from tpabrand a "
				 + " 	where pa_brand_no = #{paBrandNo}"
				 + "	and pa_group_code = '15' "
		  		 )
    int selectPaBrand(PaBrand paBrand);
    
    /**
     * 퀸잇 브랜드 등록
     * 
     * @param PaBrand
     * @return
     */  
    @Insert("insert into tpabrand ( "
    		+ "   PA_GROUP_CODE, PA_BRAND_NO, PA_BRAND_NAME, USE_YN, INSERT_ID, INSERT_DATE "
    		+ " , MODIFY_ID, MODIFY_DATE "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, #{paBrandNo}, #{paBrandName}, #{useYn}, #{insertId}, #{insertDate} "
    		+ " , #{modifyId}, #{modifyDate} "
    		+ ") ")
    int insertPaCategory(PaBrand paBrand);
    
    /**
     * 퀸잇 브랜드 조회
     * 
     * @param paCode
     * @return
     */
    @Select(value =  "    select count(*) "
				 + "    from tpabrand a "
				 + " 	where pa_brand_no = #{paBrandNo}"
				 + "	and pa_group_code = '15' "
		  		 )
    int selectPaCategory(PaBrand paBrand);
    
    /**
     * 퀸잇 토큰조회
     * 
     * @param paCode
     * @return
     */
    @Select(value =  "SELECT CODE_MGROUP AS TOKEN_TYPE,REMARK AS TOKEN_VALUE ,MODIFY_DATE AS TOKEN_CREATE_TIME,  "
    		+ "        CASE WHEN (CODE_MGROUP = #{paCode} || 'RFT'  AND MODIFY_DATE < SYSDATE - (22 / 24) ) THEN"
    		+ "          'Y'"
    		+ "         WHEN (CODE_MGROUP = #{paCode} || 'ACT'  AND MODIFY_DATE < SYSDATE - (1.5 / 24) ) THEN"
    		+ "          'Y'"
    		+ "         ELSE 'N'"
    		+ "       END FLAG "
    		+ "FROM TCODE WHERE CODE_LGROUP = 'B713' "
    		+ "AND CODE_MGROUP IN (#{paCode}||'ACT', #{paCode}||'RFT')"
    		+ " ORDER BY CODE_MGROUP"
	  		 )
    List<HashMap<String, String>> selectPaQeenToken(String paCode);
    
    /**
     * 퀸잇 토큰 업데이트
     * 
     * @param PaBrand
     * @return
     */  
    @Update("update tcode set "
    		+ "   remark = #{token} ,"
    		+ "   modify_date = sysdate"
    		+ " where code_sname = #{codeSname} "
    		+ "   and code_lgroup= 'B713'"
    		)
    int updatePaQeenToken(@Param("token") String token, @Param("codeSname")String codeSname);
    
    /**
     * 퀸잇 배송정책 등록/수정 대상 조회
     * 
     * @param shipCost
     * @return
     */
    PaQeenShipCostVO selectQeenShipCost(PaQeenShipCostVO shipCost);
    
    
    /**
     * 퀸잇 배송정책 등록/수정 저장
     * 
     * @param shipCost
     * @return
     */
	int updatePaQeenShipCost(PaQeenShipCostVO shipCost);
    
	/**
     * 퀸잇 표준카테고리 MOMENT INSERT
     * 
     * @param paGroupCode
     * @return
     */
    @Insert("insert into tpagoodskindsmoment ( "
    		+ "   pa_group_code, pa_lmsd_key, pa_lgroup, pa_mgroup"
    		+ " , pa_lgroup_name, pa_mgroup_name"
    		+ " , remark "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, substr(cware_enc_dec(#{paLmsdKey},'H'),0,20), substr(cware_enc_dec(#{paLgroup} ,'H'),0,20), substr(cware_enc_dec(#{paMgroup},'H'),0,20)"
    		+ " , #{paLgroupName}, #{paMgroupName}"
    		+ " , #{remark} "
    		+ ") ")
    int insertPaGoodsKindsMoment(PaGoodsKinds paGoodsKinds);
    
    /**
     * 제휴 표준카테고리 INSERT
     * 
     * @param paGroupCode
     * @return
     */
    @Insert("insert into tpaqeengoodskindsenc ( "
    		+ "   pa_lmsd_key_enc, pa_lgroup_enc, pa_mgroup_enc "
    		+ " , pa_lmsd_key_dec, pa_lgroup_dec, pa_mgroup_dec "
    		+ " , insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   substr(cware_enc_dec(#{paLmsdKey},'H'),0,20), substr(cware_enc_dec(#{paLgroup} ,'H'),0,20), substr(cware_enc_dec(#{paMgroup},'H'),0,20)"
    		+ " , #{paLmsdKey}, #{paLgroup}, #{paMgroup}"
    		+ " , #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate}"
    		+ ") ")
    int insertPaQeenGoodsKindsEnc(PaGoodsKinds paGoodsKinds);
    
    @Select(value = " select count(*) from tpaqeengoodskindsenc pb "
    		+ " where pb.pa_lmsd_key_dec = #{paLmsdKey} "
    		)
    int selectPaQeenGoodsKindsEnc(PaGoodsKinds paGoodsKinds);
    
    /**
     * 제휴 표준카테고리 INSERT
     * 
     * @param paGroupCode
     * @return
     */
    @Insert("insert into tpaqeengoodskindsoffer  ( "
    		+ "   pa_lmsd_key, category_id, announcement_type"
    		+ " ) values ( "
    		+ "  substr(cware_enc_dec(#{categoryId},'H'),0,20), #{categoryId}, #{announcementType}"
    		+ ") ")
    int insertQeenCategoryOffer(CategoryOffer qeenCategoryOffer);
	
    @Select(value = " select count(*) from tpaqeengoodskindsoffer pb "
    		+ " where pb.category_id = #{categoryId} "
    		)
    int selectQeenCategoryOffer(CategoryOffer qeenCategoryOffer);
    
}
