package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;

@Mapper
public interface PaCommonMapper {

	/**
     * 제휴 표준카테고리 MOMENT DELETE
     * 
     * @param paGroupCode
     * @return
     */
    @Delete(value = "delete "
    		+ " from tpagoodskindsmoment m "
    		+ " where m.pa_group_code = #{paGroupCode} ")
	int deletePaGoodsKindsMoment(String paGroupCode);
    
    
    /**
     * 제휴 표준카테고리 MOMENT INSERT
     * 
     * @param paGroupCode
     * @return
     */
    @Insert("insert into tpagoodskindsmoment ( "
    		+ "   pa_group_code, pa_lmsd_key, pa_lgroup, pa_mgroup, pa_sgroup, pa_dgroup "
    		+ " , pa_lgroup_name, pa_mgroup_name, pa_sgroup_name, pa_dgroup_name "
    		+ " , remark "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, #{paLmsdKey}, #{paLgroup}, #{paMgroup}, #{paSgroup}, #{paDgroup} "
    		+ " , #{paLgroupName}, #{paMgroupName}, #{paSgroupName}, #{paDgroupName} "
    		+ " , #{remark} "
    		+ ") ")
    int insertPaGoodsKindsMoment(PaGoodsKinds paGoodsKinds);
    
    
    /**
     * 제휴 표준카테고리 INSERT
     * 
     * @param paGroupCode
     * @return
     */
    @Insert("insert into tpagoodskinds ( "
    		+ "   pa_group_code, pa_lmsd_key, pa_lgroup, pa_mgroup, pa_sgroup, pa_dgroup "
    		+ " , pa_lgroup_name, pa_mgroup_name, pa_sgroup_name, pa_dgroup_name "
    		+ " , use_yn, cert_yn, insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, #{paLmsdKey}, #{paLgroup}, #{paMgroup}, #{paSgroup}, #{paDgroup} "
    		+ " , #{paLgroupName}, #{paMgroupName}, #{paSgroupName}, #{paDgroupName} "
    		+ " , '1', '0', #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate} "
    		+ "   ) "
    		)
    int insertPaGoodsKinds(PaGoodsKinds paGoodsKinds);
    
    
    /**
     * TPAENTPSLIP 저장
     * 
     * @param paEntpSlip
     * @return
     */
    @Insert("insert into tpaentpslip ( "
    		+ "   entp_code, entp_man_seq, pa_code, pa_addr_seq, pa_addr_gb, pa_bundle_no "
    		+ "	, pa_gmkt_ship_no, trans_target_yn, last_sync_date "
    		+ " , insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   #{entpCode}, #{entpManSeq}, #{paCode}, #{paAddrSeq}, #{paAddrGb}, #{paBundleNo} "
    		+ " , #{newGmktShipNo}, #{transTargetYn}, #{insertDate} "
    		+ " , #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate} "
    		+ ") ")
    int insertPaEntpSlip(PaEntpSlip paEntpSlip);
    
    
    /**
     * TPAENTPSLIP UPDATE
     * 
     * @param paEntpSlip
     * @return
     */
    @Update(value = "update tpaentpslip pl "
    		+ "   set pl.trans_target_yn = #{transTargetYn} "
    		+ "     , pl.modify_id = #{modifyId} "
    		+ "     , pl.modify_date = #{modifyDate} "
			+ " where pl.entp_code = #{entpCode} "
			+ "   and pl.pa_code = #{paCode} "
			+ "   and pl.pa_addr_seq = #{paAddrSeq} "
			)
    int updatePaEntpSlip(PaEntpSlip paEntpSlip);
    
    
    /**
     * TPAOFFERCODE 저장
     * 
     * @param paOfferCode
     * @return
     */
    @Insert("insert into tpaoffercode ( "
    		+ "   pa_group_code, pa_offer_type, pa_offer_code, pa_offer_type_name, pa_offer_code_name "
    		+ "	, required_yn, sort_seq, use_yn, remark, unit_required_yn, ipt_mthd_cd "
    		+ " , insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ( "
    		+ "   #{paGroupCode}, #{paOfferType}, #{paOfferCode}, #{paOfferTypeName}, #{paOfferCodeName} "
    		+ " , #{requiredYn}, #{sortSeq}, #{useYn}, #{remark}, #{unitRequiredYn}, #{iptMthdCd} "
    		+ " , #{insertId}, #{insertDate}, #{modifyId}, #{modifyDate} "
    		+ ") ")
    int insertPaOfferCode(PaOfferCode paOfferCode);

    
    /**
     * 제휴 브랜드 INSERT
     * @param paBrand
     * @return
     */
    @Insert("insert into tpabrand ( "
    		+ "   pa_group_code, pa_brand_no, pa_brand_name "
    		+ " , use_yn, insert_id, insert_date, modify_id, modify_date "
    		+ " ) values ("
    		+ "   #{paGroupCode}, #{paBrandNo}, #{paBrandName} "
    		+ " , '1', #{insertId}, sysdate , #{modifyId}, sysdate"
    		+ "   ) "
    		)
	int insertPaBrand(PaBrand paBrand);


    @Select(value = " select count(*) from tpabrand pb "
    		+ " where pb.pa_group_code = #{paGroupCode} "
    		+ "   and pb.pa_brand_no   = #{paBrandNo}")
    int selectPaBrand(PaBrand paBrand);



}
