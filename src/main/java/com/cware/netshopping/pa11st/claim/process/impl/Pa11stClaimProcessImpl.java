package com.cware.netshopping.pa11st.claim.process.impl;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.claim.process.Pa11stClaimProcess;
import com.cware.netshopping.pa11st.claim.repository.Pa11stClaimDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.claim.pa11stClaimProcess")
public class Pa11stClaimProcessImpl extends AbstractService implements Pa11stClaimProcess{

	@Resource(name = "pa11st.claim.pa11stClaimDAO")
	private Pa11stClaimDAO pa11stClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	
	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PA11STAPI_03_010 : 저장.
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			//= 1. 데이터 중복 CHECK.
			existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
			if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
			
			//= 2. TPA11STCLAIMLIST INSERT
			executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
			}
			
			//= 3. TPAORDERM INSERT
			paorderm = new Paorderm();
			paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
			paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
			paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
			paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
			paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
			paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
			paorderm.setPaDoFlag("20");
			paorderm.setOutBefClaimGb("0");
			paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
			paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
			paorderm.setModifyId("PA11");
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
			
		}
		return rtnMsg;
	}

	/**
	 * 반품완료목록조회 IF_PA11STAPI_03_025 : 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnCompleteList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			try {
				// paorderm setting
				paorderm = new Paorderm();
				paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
				paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
				paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
				paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
				paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
				paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
				paorderm.setPaDoFlag("60"); // 반품완료
				paorderm.setOutBefClaimGb("0");
				paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
				paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
				paorderm.setModifyId("PA11");
				
				//= 1. 데이터 중복 CHECK.
				existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
				if(existsCnt > 0) { 
					// paDoFlag 60 check
					existsCnt = pa11stClaimDAO.selectPa11stClaim60(arrPa11stclaimlist.get(i)); //반품데이터 완료 되었는지 확인
					if(existsCnt < 1) {
						executedRtn = paCommonDAO.updatePaOrderM(paorderm); //= 2. TPAORDERM 60 UPDATE
						if (executedRtn < 0) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM 60 UPDATE" });
						}
					}
				} else {
					executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMDONE LIST INSERT" });//= 2. TPA11STCLAIMLIST INSERT
					}
					
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if (executedRtn < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM 60 INSERT" }); //= 3. TPAORDERM 60 INSERT
					}
				}
				
			} catch(Exception e) {
				continue;
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 반품승인처리 - 회수확정대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnOkList() throws Exception{
		return pa11stClaimDAO.selectReturnOkList();
	}
	
	/**
	 * 11번가 반품승인처리 - 반품승인처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveReturnConfirmProc(HashMap<String, Object> returnMap) throws Exception{
		HttpURLConnection conn = null;
		String requestType = "GET";
		String parameter = "";
		Document doc = null;
		NodeList descNodes = null;
		ParamMap resultMap = new ParamMap();
		int executedRtn = 0;
		int targetCnt = 1;
		int successCnt = 0;
		int failCnt = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		try{
			
			if( returnMap.get("OUT_BEF_CLAIM_GB").equals("1") ) {
				resultMap.put("result_code","0");				
				resultMap.put("paDoFlag","60");
				resultMap.put("result_text","출고전 반품 성공");
				successCnt++;
				
			} else if( returnMap.get("PA_DO_FLAG").equals("90") ) {
				resultMap.put("result_code","0");				
				resultMap.put("paDoFlag","60");
				resultMap.put("result_text","직권취소 성공");
				successCnt++;				
			} else {
				parameter = "/"+returnMap.get("PA_CLAIM_NO")+"/"+returnMap.get("PA_ORDER_NO")+"/"+returnMap.get("PA_ORDER_SEQ");
				
				conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) returnMap.get("apiInfo"), returnMap.get("PA_API_KEY").toString(), requestType, parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ResultOrder");
				
				conn.disconnect();
				
				for(int j=0; j<descNodes.getLength();j++){
					for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
						resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					}
				}
				
				if( resultMap.getString("result_code").equals("0") ){
					resultMap.put("result_text","반품승인처리 성공");
					resultMap.put("paDoFlag","60");
					successCnt++;
				} else {
					//11ST API 연결 실패
					resultMap.put("paDoFlag","");
					failCnt++;
				}
			}
			
		}catch (Exception e) {
			log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_011 fail ["+returnMap.get("MAPPING_SEQ")+"]");
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
			resultMap.put("paDoFlag","");
			failCnt++;
			
		}finally{
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(returnMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaDoFlag(resultMap.getString("paDoFlag"));
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			//= TPAORDERM UPDATE
			executedRtn = pa11stClaimDAO.updatePaOrdermResult(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
			
		//= 대상건수와 성공건수가 다를 경우 TAPITRACKING.API_RESULT_MSG에 저장.
		if(targetCnt == successCnt){
			resultMap.put("rtnMsg",Constants.SAVE_SUCCESS);
			resultMap.put("successCnt",successCnt);
		} else {
			resultMap.put("rtnMsg",Constants.SAVE_FAIL );
			resultMap.put("failCnt",failCnt);
		}
		
		return resultMap;
	}
	
	/**
	 * 반품철회목록조회 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnCancelList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			//= 1. 데이터 중복 CHECK.
			existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
			if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
			
			//= 2. TPA11STCLAIMLIST INSERT
			executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
			}
			
			//= 3. TPAORDERM INSERT
			paorderm = new Paorderm();
			paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
			paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
			paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
			paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
			paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
			paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
			paorderm.setPaDoFlag("20");
			paorderm.setOutBefClaimGb("0");
			paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
			paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
			paorderm.setModifyId("PA11");
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 반품보류처리 - 반품보류대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnHoldList() throws Exception{
		return pa11stClaimDAO.selectReturnHoldList();
	}
	
	/**
	 * 11번가 반품완료보류처리 - 반품완료보류 처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveReturnHoldProc(HashMap<String, Object> returnMap) throws Exception{
		HttpURLConnection conn = null;
		String requestType = "GET";
		String parameter = "";
		Document doc = null;
		NodeList descNodes = null;
		ParamMap resultMap = new ParamMap();
		int executedRtn = 0;
		int targetCnt = 1;
		int successCnt = 0;
		int failCnt = 0;
		
		try{
			
			parameter = "/"+returnMap.get("PA_ORDER_NO")+"/"+returnMap.get("PA_ORDER_SEQ")+"/"+returnMap.get("PA_CLAIM_NO")+"/"+returnMap.get("HOLD_CODE")+"/"+URLEncoder.encode(returnMap.get("HOLD_CONTENT").toString(), "UTF-8").replaceAll("\\+", "%20");
			
			conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) returnMap.get("apiInfo"), returnMap.get("PA_API_KEY").toString(), requestType, parameter);
			
			// RESPONSE XML 			
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ResultOrder");
			
			conn.disconnect();
			
			for(int j=0; j<descNodes.getLength();j++){
				for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				}
			}
			
			if( resultMap.getString("result_code").equals("0") ){
				//= TPAORDERM UPDATE
				resultMap.put("result_text", "반품완료보류처리 성공");
				resultMap.put("pa_hold_yn", "1");
				successCnt++;
			} else {
				//11ST API 연결 실패
				resultMap.put("pa_hold_yn", "0");
				failCnt++;
			}
			
		}catch (Exception e) {
			log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_013 fail ["+returnMap.get("MAPPING_SEQ")+"]");
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
			failCnt++;
			
		}finally{
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(returnMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaHoldYn(resultMap.getString("pa_hold_yn"));
			//= TPAORDERM UPDATE
			executedRtn = pa11stClaimDAO.updatePaOrdermHoldYn(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
			
		//= 대상건수와 성공건수가 다를 경우 TAPITRACKING.API_RESULT_MSG에 저장.
		if(targetCnt == successCnt){
			resultMap.put("rtnMsg",Constants.SAVE_SUCCESS);
			resultMap.put("successCnt",successCnt);
		} else {
			resultMap.put("rtnMsg",Constants.SAVE_FAIL );
			resultMap.put("failCnt",failCnt);
		}
		
		return resultMap;
	}
	
	/**
	 * 수취확인후 직권취소목록조회 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveAuthCancelList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			//= 1. 데이터 중복 CHECK.
			existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
			if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
			
			//= 2. TPA11STCLAIMLIST INSERT
			executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
			}
			
			//= 3. TPAORDERM INSERT
			paorderm = new Paorderm();
			paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
			paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
			paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
			paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
			paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
			paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
			paorderm.setPaDoFlag("20");
			paorderm.setOutBefClaimGb("0");
			paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
			paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
			paorderm.setModifyId("PA11");
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 반품접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return pa11stClaimDAO.selectOrderClaimTargetList();
	}

	/**
	 * 11번가 반품취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return pa11stClaimDAO.selectClaimCancelTargetList();
	}
	
	/**
	 * 11번가 반품접수 대상조회 - 상세(반품접수건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return pa11stClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	/**
	 * 11번가 반품접수 대상조회 - 상세(출하지시 이후 취소건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return pa11stClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	/**
	 * 기존 배송지와 비교
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public String compareAddress(ParamMap param) throws Exception{
		return pa11stClaimDAO.compareAddress(param);
	}
	
	/**
	 * 반품송장입력 - 반품송장등록대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnSlipProcList() throws Exception{
		return pa11stClaimDAO.selectReturnSlipProcList();
	}
	
	/**
	 * 반품송장입력 - 반품송장등록처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveReturnSlipProc(HashMap<String, Object> returnSlipMap) throws Exception{
		HttpURLConnection conn = null;
		String requestType = "GET";
		String parameter = "";
		Document doc = null;
		NodeList descNodes = null;
		ParamMap resultMap = new ParamMap();
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String sendDate = DateUtil.getCurrentDateAsString()+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
		
		try{
			parameter = "/"+sendDate+"/"+returnSlipMap.get("PA_SHIP_NO")+"/"+returnSlipMap.get("PA_DELY_GB")+"/"+returnSlipMap.get("SHEET_NO")+"/01/"+ returnSlipMap.get("PA_CLAIM_NO");
			
			conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) returnSlipMap.get("apiInfo"), returnSlipMap.get("PA_API_KEY").toString(), requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
			
			// RESPONSE XML 			
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ResultOrder");
			
			conn.disconnect();
			
			for(int j=0; j<descNodes.getLength();j++){
				for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				}
			}
			
			if( resultMap.getString("result_code").equals("0") ){
				resultMap.put("result_text","반품송장등록처리 성공");
				resultMap.put("paDoFlag","20");
				resultMap.put("remark",returnSlipMap.get("PA_DELY_GB_NAME") + "[" + returnSlipMap.get("SHEET_NO") + "]");
			} else {
				//11ST API 연결 실패
				resultMap.put("paDoFlag","");
			}
			
		}catch (Exception e) {
			log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_026 fail ["+returnSlipMap.get("MAPPING_SEQ")+"]");
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
			resultMap.put("paDoFlag","");
			
		}finally{
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(returnSlipMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaDoFlag(resultMap.getString("paDoFlag"));
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setRemark1V(resultMap.getString("remark"));
			//= TPAORDERM UPDATE
			executedRtn = pa11stClaimDAO.updatePaOrdermResult(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
		
		return resultMap;
	}
}