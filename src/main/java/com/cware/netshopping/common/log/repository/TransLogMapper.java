package com.cware.netshopping.common.log.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.domain.PaTransService;

@Mapper
public interface TransLogMapper {
	
    @Insert("insert into tpatransapi ("
    		+ "   trans_code, trans_type, trans_api_no, api_name, api_url, api_note "
    		+ " , request_header, request_date"
    		+ " , trans_service_no, pa_group_code, process_id "
    		+ " , request_payload  "
    		+ " ) values ( "
    		+ "   #{transCode}, #{transType}, #{transApiNo}, #{apiName}, #{apiUrl}, #{apiNote}"
    		+ " , #{requestHeader}, systimestamp "
    		+ " , #{transServiceNo}, #{paGroupCode}, #{processId} "
    		+ " , #{requestPayload}"
    		+ ") ")
    @SelectKey(statement = "select seq_trans_api_no.nextval from dual", keyColumn = "trans_api_no", keyProperty = "transApiNo", before = true, resultType = Long.class)
	int insertPaTransApiReq(PaTransApi apiLog);
    
    @Update("update tpatransapi set "
    		+ "   response_date = systimestamp"
    		+ " , success_yn = #{successYn} "
    		+ " , result_code = #{resultCode} "
    		+ " , result_msg = #{resultMsg} "
    		+ " , response_payload = #{responsePayload} "
    		+ " where trans_code = #{transCode} "
    		+ "   and trans_type = #{transType} "
    		+ "   and trans_api_no = #{transApiNo} ")
	int updatePaTransApiRes(PaTransApi apiLog);
    

    @Select("select request_payload "
    		+ " from tpatransapi "
    		+ " where trans_code = #{transCode} "
    		+ "   and trans_api_no = #{transApiNo} "
    		+ "   and response_date is null "
    		)
	String selectRequestPayload(@Param("transCode") String transCode, @Param("transApiNo") Long transApiNo);

    @Insert("insert into tpatransservice ("
    		+ "   trans_code, trans_type, trans_service_no, service_name, service_note "
    		+ " , start_date, trans_batch_no, pa_group_code, process_id "
    		+ " ) values ( "
    		+ "   #{transCode}, #{transType}, #{transServiceNo}, #{serviceName}, #{serviceNote}"
    		+ " , systimestamp, #{transBatchNo}, #{paGroupCode}, #{processId} "
    		+ ") ")
    @SelectKey(statement = "select seq_trans_service_no.nextval from dual", keyColumn = "trans_service_no", keyProperty = "transServiceNo", before = true, resultType = Long.class)
	int insertPaTransServiceStart(PaTransService serviceLog);

    @Update("update tpatransservice set "
    		+ "  end_date = systimestamp"
    		+ " , success_yn = #{successYn} "
    		+ " , result_code = #{resultCode} "
    		+ " , result_msg = #{resultMsg} "
    		+ " where trans_code = #{transCode} "
    		+ "   and trans_type = #{transType} "
    		+ "   and trans_service_no = #{transServiceNo} ")
	int updatePaTransServiceEnd(PaTransService serviceLog);

    @Select("select seq_trans_api_no.nextval from dual")
	Long createTransApiNo();
    
    @Insert("insert into tpatransapi ("
    		+ "   trans_code, trans_type, trans_api_no, api_name, api_url, api_note "
    		+ " , request_header, request_date"
    		+ " , trans_service_no, pa_group_code, process_id "
    		+ " , request_payload  "
    		+ " ) values ( "
    		+ "   #{transCode}, #{transType}, #{transApiNo}, #{apiName}, #{apiUrl}, #{apiNote}"
    		+ " , #{requestHeader}, systimestamp "
    		+ " , #{transServiceNo}, #{paGroupCode}, #{processId} "
    		+ " , #{requestPayload}"
    		+ ") ")
	int insertPaTransApi(PaTransApi apiLog);

}
