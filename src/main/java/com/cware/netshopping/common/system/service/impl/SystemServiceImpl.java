package com.cware.netshopping.common.system.service.impl;
/**
 * System			: OPEN-API
 * FileName			: SystemServiceImpl
 * Desctiption		: 시스템관련 ServiceImpl Class
 * History
 * company		author	date        Description
 * commerceware	webzest	2011.08.03     신규생성
 */
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.CodeVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaRequestMap;

@Service("common.system.systemService")
public class SystemServiceImpl extends AbstractService implements SystemService{

	@Resource(name = "common.system.systemProcess")
	private SystemProcess systemProcess;

	@Override
	public Config getConfig(String item) throws Exception{
		return systemProcess.getConfig(item);
	}

	@Override
	public String getVal(String item) throws Exception{
		return systemProcess.getVal(item);
	}

	@Override
	public HashMap<String, Object> getConfig() throws Exception{
		return systemProcess.getConfig();
	}

	@Override
	public List<Config> getConfigList() throws Exception{
		return systemProcess.getConfigList();
	}

	@Override
	public List<CodeVO> getCodeList() throws Exception{
		return systemProcess.getCodeList();
	}

	@Override
	public CodeVO getCode(String codeLgroup, String codeMgroup) throws Exception{
		return systemProcess.getCode(codeLgroup, codeMgroup);
	}

	@Override
	public CodeVO getCode(String codeLgroup, String codeMgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception{
		return systemProcess.getCode(codeLgroup, codeMgroup, isAvail, codeGreater, codeLess);
	}

	@Override
	public CodeVO getAvailCode(String codeLgroup, String codeMgroup) throws Exception{
		return systemProcess.getAvailCode(codeLgroup, codeMgroup);
	}

	@Override
	public CodeVO getAvailTermCode(String codeLgroup, String codeMgroup, String codeGreater, String codeLess) throws Exception{
		return systemProcess.getAvailTermCode(codeLgroup, codeMgroup, codeGreater, codeLess);
	}

	@Override
	public HashMap<String, Object> getAvailCodeAsMap(String codeLgroup, String codeMgroup) throws Exception{
		return systemProcess.getAvailCodeAsMap(codeLgroup, codeMgroup);
	}

	@Override
	public CodeVO[] getCodeList(String codeLgroup) throws Exception{
		return systemProcess.getCodeList(codeLgroup);
	}

	@Override
	public CodeVO[] getCodeList(String codeLgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception{
		return systemProcess.getCodeList(codeLgroup, isAvail, codeGreater, codeLess);
	}

	@Override
	public CodeVO[] getAvailCodeList(String codeLgroup) throws Exception{
		return systemProcess.getAvailCodeList(codeLgroup);
	}

	@Override
	public CodeVO[] getAvailTermCodeList(String codeLgroup, String codeGreater, String codeLess) throws Exception{
		return systemProcess.getAvailTermCodeList(codeLgroup, codeGreater, codeLess);
	}

	@Override
	public HashMap<String, Object>[] getAvailCodeListAsMap(String codeLgroup, String codeMgroup) throws Exception{
		return systemProcess.getAvailCodeListAsMap(codeLgroup, codeMgroup);
	}

	@Override
	public Timestamp getSysdate() throws Exception{
		return systemProcess.getSysdate();
	}

	@Override
	public Timestamp getSysdatetime() throws Exception{
		return systemProcess.getSysdatetime();
	}
	
	@Override
	public String getRemitDate() throws Exception{
		return systemProcess.getRemitDate();
	}

	@Override
	public String getSysdateToString() throws Exception{
		return systemProcess.getSysdateToString();
	}

	@Override
	public String getSysdatetimeToString() throws Exception{
		return systemProcess.getSysdatetimeToString();
	}
	
	@Override
	public String getSysday() throws Exception{
		return systemProcess.getSysday();
	}

	@Override
	public Date getDate() throws Exception{
		return systemProcess.getDate();
	}

	@Override
	public String getSequenceNo(String type) throws Exception{
		return systemProcess.getSequenceNo(type);
	}

	@Override
	public String getMaxNo(String tableName, String columnName, String modString, int seqFormat) throws Exception{
		return systemProcess.getMaxNo(tableName, columnName, modString, seqFormat);
	}

	@Override
	public String getSeqNo(String tableName, String columnName, Timestamp argDate, int seqFormat) throws Exception{
		return systemProcess.getSeqNo(tableName, columnName, argDate, seqFormat);
	}

	@Override
	public List<Config> selectConfigCardList() throws Exception {
		return systemProcess.selectConfigCardList();
	}

	@Override
	public int insertApiTrackingTx(HttpServletRequest request,ParamMap paramMap) throws Exception {
		return systemProcess.insertPaApiTracking(request,paramMap);
	}
	
	@Override
	public int insertPaApiTrackingTx(HttpServletRequest request,ParamMap paramMap) throws Exception {
		return systemProcess.insertPaApiTracking(request,paramMap);
	}
	
	@Override
	public int insertPaApiTrackingTx(ParamMap paramMap) throws Exception {
		return systemProcess.insertPaApiTracking(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectSsgApiInfo(String apiCode) throws Exception {
		return systemProcess.selectSsgApiInfo(apiCode);
	}
	
	@Override
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception {
		return systemProcess.selectPaApiInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectPaApiInfo4Url(String apiCode) throws Exception {
		return systemProcess.selectPaApiInfo4Url(apiCode);
	}
	
	@Override
	public String checkCloseHistoryTx(String flag, String prg_id) throws Exception{
		return systemProcess.checkCloseHistory(flag, prg_id);
	}
	/**
	 * tconfig 조회
	 * 
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<HashMap<String, Object>> selectTconfig(ParamMap paramMap) throws Exception {
		return systemProcess.selectTconfig(paramMap);
	}
	
	@Override
	public int selectPaprocCheck(ParamMap paramMap) throws Exception {
		return systemProcess.selectPaprocCheck(paramMap);
	}

	@Override
	public String insertPaRequestMapTx(PaRequestMap paRequestMap) throws Exception {
		return systemProcess.insertPaRequestMap(paRequestMap);
	}

	public void insertPassingErrorToApitracking(ParamMap paramMap, Exception e) throws Exception {
		try{
			ParamMap pParamMap = new ParamMap();
			//pParamMap = paramMap // Map은 변수이름이 메모리 주소값이 아니라 메모리 주소값을 가리키는 포인터 였다.
			String apiCode   = paramMap.getString("apiCode");
			String claimType = paramMap.getString("claimType");
			String msg = "";
			
			pParamMap.put("startDate"	, paramMap.getString("startDate"));
			pParamMap.put("siteGb"		, paramMap.getString("siteGb"));
			
			
			switch(apiCode){
			
				case "IF_PAGMKTAPI_V2_02_001": //주문
					pParamMap.put("apiCode", "GMKT-PRE-ORDER");
				break;
			
				case "IF_PAGMKTAPI_V2_02_002": //주문
					pParamMap.put("apiCode", "GMKT-ORDER");
				break;
				
				case "IF_PAGMKTAPI_V2_03_001": //취소
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-CANCEL");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-CANCEL-REJECT");
					}else if(claimType.equals("Claimwithdrawn")){
						pParamMap.put("apiCode", "GMKT-CANCEL-REJECT");
					}else if(claimType.equals("ClaimDone")){
						pParamMap.put("apiCode", "GMKT-CANCEL-DONE");
					}else if(claimType.equals("ClaimEnd")){
						pParamMap.put("apiCode", "GMKT-CANCEL-END");
					}else{
						pParamMap.put("apiCode", "GMKT-CANCEL-UNKNOWN");
					}
				break;
				
				case "IF_PAGMKTAPI_V2_03_005": //반품
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-RETURN");
					}else if(claimType.equals("ClaimIng")){
						pParamMap.put("apiCode", "GMKT-RETURNNING");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-RETURN-REJECT");
					}else if(claimType.equals("ClaimDone")){
						pParamMap.put("apiCode", "GMKT-RETURN-DONE");
					}else{
						pParamMap.put("apiCode", "GMKT-RETURN-UNKNOWN");
					}
					
				break;
				
				case "IF_PAGMKTAPI_V2_03_010": //교환
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-EXCHANGE");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-EXCHANGE-REJECT");
					}else{
						pParamMap.put("apiCode", "GMKT-EXCHANGE-UNKNOWN");
					}
					
				break;
				
				case "IF_PAGMKTAPI_V2_05_003": //상담 답변
					msg = "messgeNo: " + paramMap.get("messageNo");
				break;
				
				default : 
					pParamMap.put("apiCode", "GMKT-UNKNOWN");
			}
			
			pParamMap.put("code","444");
			
			if(e.getMessage() != null){
				pParamMap.put("message", msg + e.getMessage());
			}else{
				pParamMap.put("message", e.toString());
			}
			systemProcess.insertPaApiTracking(pParamMap);
	
		}catch(Exception ee){
			log.error(e.getMessage());
		}
	}
	
	@Override
	public void insertPassingErrorToApitracking(HttpServletRequest request, ParamMap paramMap, Exception e) throws Exception {
		try{
			ParamMap pParamMap = new ParamMap();
			//pParamMap = paramMap // Map은 변수이름이 메모리 주소값이 아니라 메모리 주소값을 가리키는 포인터 였다.
			String apiCode   = paramMap.getString("apiCode");
			String claimType = paramMap.getString("claimType");

			pParamMap.put("startDate"	, paramMap.getString("startDate"));
			pParamMap.put("siteGb"		, paramMap.getString("siteGb"));
			
			
			switch(apiCode){
			
				case "IF_PAGMKTAPI_V2_02_001": //주문
					pParamMap.put("apiCode", "GMKT-PRE-ORDER");
				break;
			
				case "IF_PAGMKTAPI_V2_02_002": //주문
					pParamMap.put("apiCode", "GMKT-ORDER");
				break;
				
				case "IF_PAGMKTAPI_V2_03_001": //취소
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-CANCEL");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-CANCEL-REJECT");
					}else if(claimType.equals("Claimwithdrawn")){
						pParamMap.put("apiCode", "GMKT-CANCEL-REJECT");
					}else if(claimType.equals("ClaimDone")){
						pParamMap.put("apiCode", "GMKT-CANCEL-DONE");
					}else if(claimType.equals("ClaimEnd")){
						pParamMap.put("apiCode", "GMKT-CANCEL-END");
					}else{
						pParamMap.put("apiCode", "GMKT-CANCEL-UNKNOWN");
					}
				break;
				
				case "IF_PAGMKTAPI_V2_03_005": //반품
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-RETURN");
					}else if(claimType.equals("ClaimIng")){
						pParamMap.put("apiCode", "GMKT-RETURNNING");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-RETURN-REJECT");
					}else if(claimType.equals("ClaimDone")){
						pParamMap.put("apiCode", "GMKT-RETURN-DONE");
					}else{
						pParamMap.put("apiCode", "GMKT-RETURN-UNKNOWN");
					}
					
				break;
				
				case "IF_PAGMKTAPI_V2_03_010": //교환
					if(claimType.equals("ClaimReady")){
						pParamMap.put("apiCode", "GMKT-EXCHANGE");
					}else if(claimType.equals("ClaimReject")){
						pParamMap.put("apiCode", "GMKT-EXCHANGE-REJECT");
					}else{
						pParamMap.put("apiCode", "GMKT-EXCHANGE-UNKNOWN");
					}
					
				break;
				
				default : 
					pParamMap.put("apiCode", "GMKT-UNKNOWN");
			}
			
			pParamMap.put("code","444");
			
			if(e.getMessage() != null){
				pParamMap.put("message", e.getMessage());
			}else{
				pParamMap.put("message", e.toString());
			}
			systemProcess.insertPaApiTracking(request, pParamMap);
	
		}catch(Exception ee){
			log.error(e.getMessage());
		}
	}

	@Override
	public HashMap<String, String> selectCloseHistory(String apiCode) throws Exception {
		return systemProcess.selectCloseHistory(apiCode);
	}

	@Override
	public String selectTcodeVal(String codeLGroup, String codeMGroup) throws Exception {
		return systemProcess.selectTcodeVal(codeLGroup, codeMGroup);
	}

	@Override
	public String getValRealTime(String item) throws Exception {
		return systemProcess.getValRealTime(item);
	}
}
