package com.cware.netshopping.common.system.repository;
/*
 * System 		: OPEN-API
 * FileName 	: SystemDAO
 * Description 	: 공통  관련  DAO Class
 * History
 * company		author	date        Description
 * commerceware	kst  	2011.02.24     신규생성
*/
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CodeVO;
import com.cware.netshopping.domain.model.ApiTracking;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaApiTracking;
import com.cware.netshopping.domain.model.PaRequestMap;

@SuppressWarnings("unchecked")
@Repository("common.system.systemDAO")
public class SystemDAO extends AbstractDAO {

	public List<CodeVO> selectCodeList(HashMap<String, Object> hashMap) throws Exception{
        return list("common.system.selectCodeList", hashMap);
    }
	public List<Config> selectConfigList(HashMap<String, Object> hashMap) throws Exception{
        return list("common.system.selectConfigList", hashMap);
    }
	public String getSysdate() throws Exception{
        return (String)selectByPk("common.system.getSysdate", null);
    }
	public String getRemitDate() throws Exception{
        return (String)selectByPk("common.system.getRemitDate", null);
    }
	public String getSysdatetime() throws Exception{
        return (String)selectByPk("common.system.getSysdatetime", null);
    }
	public String getSysday() throws Exception{
		return (String)selectByPk("common.system.getSysday", null);
	}
	public Date getDate() throws Exception{
        return (Date)selectByPk("common.system.getDate", null);
    }
	public String selectSequenceNo(HashMap<String, Object> hashMap) throws Exception{
        return (String)selectByPk("common.system.selectSequenceNo", hashMap);
    }
	public String selectSequenceNoCondition(HashMap<String, Object> hashMap) throws Exception{
        return (String)selectByPk("common.system.selectSequenceNoCondition", hashMap);
    }
	public String selectMaxNo(HashMap<String, Object> hashMap) throws Exception {
		return (String)selectByPk("common.system.selectMaxNo", hashMap);
	}
	public String selectSeqNo(HashMap<String, Object> hashMap) throws Exception {
		return (String)selectByPk("common.system.selectSeqNo", hashMap);
	}
	public String selectSeqNoEmpty(HashMap<String, Object> hashMap) throws Exception {
		return (String)selectByPk("common.system.selectSeqNoEmpty", hashMap);
	}
	public String selectSeqNoFormat(HashMap<String, Object> hashMap) throws Exception {
		return (String)selectByPk("common.system.selectSeqNoFormat", hashMap);
	}
	public List<Config> selectConfigCardList() throws Exception {
		return list("common.system.selectConfigCardList", null);
	}
	public String getVal(String item) throws Exception{
        return (String)selectByPk("common.system.getVal", item);
    }
	
	/**
	 * Partner - ApiTracking 저장
	 * @param apiTracking
	 * @return int
	 * @throws Exception
	 */
	public int insertApiTracking(ApiTracking apiTracking) throws Exception{
		return insert("common.system.insertApiTracking",apiTracking);
	}
	
	public int insertPaApiTracking(PaApiTracking apiTracking) throws Exception{
		return insert("common.system.insertPaApiTracking",apiTracking);
	}
	
	/**
	 * SSG.COM - API URL / KEY 정보
	 * @param API_CODE
	 * @return ParamMap
	 * @throws Exception
	 */
	public HashMap<String, String> selectSsgApiInfo(String apiCode) throws Exception{
		return (HashMap<String, String>) selectByPk("common.system.selectSsgApiInfo",apiCode);
	}
	
	/**
	 * PA제휴입점 - API URL / KEY 정보
	 * @param API_CODE
	 * @return ParamMap
	 * @throws Exception
	 */
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("common.system.selectPaApiInfo",paramMap.get());
	}
	
	
	public HashMap<String, String> selectPaApiInfo4Url(String apiCode) throws Exception{
		return (HashMap<String, String>) selectByPk("common.system.selectPaApiInfo4Url", apiCode);
	}
	
	
	/**
	 * 중복실행 Check
	 */
	public String checkCloseHistory(String argString) throws Exception{
        return (String)selectByPk("common.system.checkCloseHistory", argString);
    }
	public int insertCloseHistory(String  prg_id) throws Exception{
		return  insert("common.system.insertCloseHistory", prg_id);
	}
	public int updateCloseHistory(ParamMap paramMap) throws Exception{
		return  update("common.system.updateCloseHistory", paramMap.get());
	}

	/**
	 * tconfig 조회
	 * 
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectTconfig(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("common.system.selectTconfig", paramMap.get());
	}
	public int selectPaprocCheck(ParamMap paramMap) throws Exception {
        return (Integer) selectByPk("common.system.selectPaprocCheck", paramMap.get());
	}
	public String selectCodeRemarkChkForRequestMap(String apiCode) throws Exception {
        return (String) selectByPk("common.system.selectCodeRemarkChkForRequestMap", apiCode);
	}
	public int insertPaRequestMap(PaRequestMap paRequestMap) throws Exception {
        return insert("common.system.insertPaRequestMap", paRequestMap);
	}
	public HashMap<String, String> selectCloseHistory(String apiCode) throws Exception{
		return (HashMap<String, String>) selectByPk("common.system.selectCloseHistory", apiCode);
	}
	public String selectTcodeVal(ParamMap paramMap)  throws Exception{
		return (String) selectByPk("common.system.selectTcodeVal", paramMap.get());
	}
}
