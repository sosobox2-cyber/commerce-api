package com.cware.netshopping.common.system.process;
/*
 * System 		: OPEN-API
 * FileName 	: SysmteProcess 
 * Description 	: 시스템 관련  Process Interface
 * History
 * company		author	date        Description
 * commerceware	webzest 2011.08.203   신규생성
*/
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CodeVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaRequestMap;

public interface SystemProcess {
	
	public void selectConfigList() throws Exception;
	public List<Config> selectConfigCardList() throws Exception;
	public Config getConfig(String item) throws Exception;
	public String getVal(String item) throws Exception;
	public HashMap<String, Object> getConfig() throws Exception;	
	public List<Config> getConfigList() throws Exception;
	
	public void selectCodeList() throws Exception;	
	public List<CodeVO> getCodeList() throws Exception;
	public CodeVO getCode(String codeLgroup, String codeMgroup) throws Exception;
	public CodeVO getCode(String codeLgroup, String codeMgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception;
	public CodeVO getAvailCode(String codeLgroup, String codeMgroup) throws Exception;
	public CodeVO getAvailTermCode(String codeLgroup, String codeMgroup, String codeGreater, String codeLess) throws Exception;	
	public HashMap<String, Object> getAvailCodeAsMap(String codeLgroup, String codeMgroup) throws Exception;
	public CodeVO[] getCodeList(String codeLgroup) throws Exception;	
	public CodeVO[] getCodeList(String codeLgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception;	
	public CodeVO[] getAvailCodeList(String codeLgroup) throws Exception;	
	public CodeVO[] getAvailTermCodeList(String codeLgroup, String codeGreater, String codeLess) throws Exception;
	public HashMap<String, Object>[] getAvailCodeListAsMap(String codeLgroup, String codeMgroup) throws Exception;	
	
	public Timestamp getSysdate() throws Exception;	 
	public Timestamp getSysdatetime() throws Exception; 
	public String getSysdateToString() throws Exception;
	public String getRemitDate() throws Exception;
	public String getSysdatetimeToString() throws Exception;
	public String getSysday() throws Exception;
	public Date getDate() throws Exception;	
	
	public String getSequenceNo(String type) throws Exception;	
	public String getMaxNo(String tableName, String columnName, String modString, int seqFormat) throws Exception;	
	public String getSeqNo(String tableName, String columnName, Timestamp argDate, int seqFormat) throws Exception;
	public String getSeqNo(String tableName, String columnName, String modString, int seqFormat) throws Exception;
	
	/**
	 * Partner - ApiTracking 저장
	 * @param request
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int insertApiTracking(HttpServletRequest request, ParamMap paramMap) throws Exception;	
	public int insertPaApiTracking(HttpServletRequest request, ParamMap paramMap) throws Exception;	
	public int insertPaApiTracking(ParamMap paramMap) throws Exception;	

	public HashMap<String, String> selectSsgApiInfo(String apiCode) throws Exception;
	
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 중복실행 체크
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String checkCloseHistory(String flag, String prg_id) throws Exception;
	/**
	 * tconfig 조회
	 * 
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectTconfig(ParamMap paramMap) throws Exception;
	
	public ParamMap selectStdAddress(ParamMap paramMap) throws Exception;
	
	public int selectPaprocCheck(ParamMap paramMap) throws Exception;
	
	public HashMap<String, String> selectPaApiInfo4Url(String apiCode) throws Exception;
	
	public String insertPaRequestMap(PaRequestMap paRequestMap) throws Exception;
	public HashMap<String, String> selectCloseHistory(String apiCode) throws Exception;
	public String selectTcodeVal(String codeLGroup, String codeMGroup) throws Exception;
	public String getValRealTime(String item) throws Exception;
}
