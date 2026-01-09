package com.cware.netshopping.pa11st.delivery.process.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.impl.SystemProcessImpl;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.delivery.process.Pa11stDeliveryProcess;
import com.cware.netshopping.pa11st.delivery.repository.Pa11stDeliveryDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.delivery.pa11stDeliveryProcess")
public class Pa11stDeliveryProcessImpl extends AbstractService implements Pa11stDeliveryProcess{

	@Resource(name = "pa11st.delivery.pa11stDeliveryDAO")
	private Pa11stDeliveryDAO pa11stDeliveryDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemProcess")
	private SystemProcessImpl systemProcess;
	
	/**
	 * 발주확인처리 - 처리결과 insert
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderConfirmProc(ParamMap paramMap) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			paramMap.put("paOrderGb","10");
			paramMap.put("procFlag","00");
			executedRtn = pa11stDeliveryDAO.insertPa11stOrderList(paramMap); //= tpa11storderlist insert
			
			if(executedRtn == 1){
				log.info("tpa11storderlist insert success");
			} else {
				log.info("tpa11storderlist insert fail");
				log.info("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq") + ", dlvNo : " + paramMap.getString("dlvNo"));
				throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST INSERT" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	/**
	 * 11번가 발주확인 처리대상 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderConfirmProcExists(ParamMap paramMap) throws Exception{
		return pa11stDeliveryDAO.selectOrderConfirmProcExists(paramMap);
	}
	
	/**
	 * 11번가 발송처리 - 출고대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@Override
	public List<Object> selectPa11stSlipProcList() throws Exception{
		return pa11stDeliveryDAO.selectPa11stSlipProcList();
	}
	
	/**
	 * 11번가 발송처리 - 발송처리/부분발송처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveSlipOutProc(HashMap<String, Object> slipMap) throws Exception{
		String paShipNo = slipMap.get("PA_SHIP_NO").toString();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap resultMap 				= new ParamMap();
		int executedRtn 				= 0;
		int targetCnt 					= 0;
		int resultCnt 					= 0;
		
		//부분출고 대상 확인
		//1. 부분출고대상 일 경우 : 부분발송처리 API 호출
		//2. 부분출고 대상이 아닐 경우 : 발송처리  API 호출
		
		int slipNoUsedCnt = pa11stDeliveryDAO.selectSlipNoUsedCnt(paShipNo); 
		List<Object> slipProcList = pa11stDeliveryDAO.selectSlipProcList(paShipNo);
		targetCnt = slipProcList.size();
		if(slipNoUsedCnt == 0 || slipProcList.size() == 0){
			throw processException(paShipNo + "출고 대상 조회 실패");
		}
		
		ParamMap apiMap = new ParamMap();
		apiMap.put("apiCode"	, "IF_PA11STAPI_03_005");
		apiMap.put("broadCode"	, Constants.PA_11ST_BROAD_CODE);
		apiMap.put("onlineCode"	, Constants.PA_11ST_ONLINE_CODE);
		apiInfo = systemDAO.selectPaApiInfo(apiMap);
		
		
		if(slipNoUsedCnt > 1){//=1. 부분발송대상
			//= 부분발송처리(부분배송중)
			for(Object slipProc  :slipProcList ){
				HashMap<String, Object> paramMap  = new HashMap<String, Object>(); 
				paramMap = (HashMap<String, Object>) slipProc;
				
				try{
					resultCnt = resultCnt + sendSlipTo11st(paramMap,resultMap,apiInfo,paramMap.get("SLIP_NO").toString(),"PART",paramMap.get("PA_DELY_GB").toString());

					//동일 운송장 케이스
					if(!resultMap.getString("result_code").equals("0") && !resultMap.getString("result_code").equals("000000")){
						resultCnt = resultCnt + reSendToSlipINo(paramMap, resultMap, apiInfo, "PART");
					}
					
				}catch (Exception e) {
					resultCnt = resultCnt + reSendToSlipINo(paramMap, resultMap, apiInfo, "PART");  ////PA11ST DelyGB Mapping 또는 다른 알려지지 않은 에러 CASE
				}finally{
					//= TPAORDERM UPDATE
					executedRtn = updatePaOrderm4Result(resultMap, paramMap);
					if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}
			}
			
		}else { //=2. 전체발송대상
			//= 발송처리(배송중 처리)
			for(Object slipProc  : slipProcList ){
				HashMap<String, Object> paramMap  = new HashMap<String, Object>(); 
				paramMap  = (HashMap<String, Object>) slipProc;
				targetCnt = slipProcList.size();
				
				try{
					
					//= 발송처리 대상 체크
					int checkCnt = pa11stDeliveryDAO.selectShppDeliveryProcExists(paramMap);
					
					if(checkCnt > 0){
						resultMap.put("paDoFlag"	, "40");
						resultMap.put("result_code"	, "0");
						resultMap.put("result_text"	, "전체 1 건이 발송처리 되었습니다.");
						resultCnt++;
						continue; //TODO FINAL 타는지 확인 필요
					}
						
					resultCnt = resultCnt + sendSlipTo11st(paramMap,resultMap,apiInfo,paramMap.get("SLIP_NO").toString(),"ALL",paramMap.get("PA_DELY_GB").toString());
						
					if(!resultMap.getString("result_code").equals("0") && !resultMap.getString("result_code").equals("000000")){
						resultCnt = resultCnt + reSendToSlipINo(paramMap, resultMap, apiInfo, "ALL");
					}
					//전체발송 체크
					paramMap.put("isAll", "1");
					
				}catch (Exception e) {
					//PA11ST DelyGB Mapping 또는 다른 알려지지 않은 에러 CASE
					try{
						String slipINo = paramMap.get("SLIP_I_NO").toString();
						resultCnt = resultCnt + sendSlipTo11st(paramMap,resultMap,apiInfo,slipINo,"ALL","00099");							
					}catch(Exception ee) {
						resultCnt = resultCnt + reSendToSlipINo(paramMap, resultMap, apiInfo, "ALL");
					}
					
				}finally{
					executedRtn = updatePaOrderm4Result(resultMap, paramMap);
					if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}
			}
		}
		//= 대상건수와 성공건수가 다를 경우 TAPITRACKING.API_RESULT_MSG에 저장.
		if(targetCnt == resultCnt){
			resultMap.put("rtnMsg",Constants.SAVE_SUCCESS);
			resultMap.put("resultCnt",resultCnt);
		} else {
			resultMap.put("rtnMsg",Constants.SAVE_FAIL );
		}
		return resultMap;
	}
	
	
	private int reSendToSlipINo(HashMap<String, Object> paramMap , ParamMap resultMap, HashMap<String, String> apiInfo, String partGb) {
		int resultCnt = 0;
		
		try {
			String slipINo = paramMap.get("SLIP_I_NO").toString();
			resultCnt = resultCnt + sendSlipTo11st(paramMap,resultMap,apiInfo,slipINo,partGb,"00099");
			
		}catch (Exception e) {
			log.error(getMessage("errors.process") + " : IF_PA11STAPI_03_005 fail ["+paramMap.get("PA_ORDER_NO")+"/"+paramMap.get("PA_ORDER_SEQ")+"]");
			resultMap.put("result_code"	, "-9999");
			resultMap.put("result_text"	, e.getMessage());
			resultMap.put("paDoFlag"	, "");				
		}
		return resultCnt;
	}
		
	/**
	 * 11번가 판매완료내역 구매확정일시 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveDeliveryCompleteList(List<Paorderm> arrPaordermlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		for(int i = 0; arrPaordermlist.size() > i; i++){
			
			executedRtn = pa11stDeliveryDAO.updatePaOrdermResult(arrPaordermlist.get(i)); //= tpaorderm update
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			
		}
		
		return rtnMsg;
	}

	/**
	 * 11번가 발주확인목록 저장
	 * @param List<Pa11storderlist>
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderConfirmList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11storderlist.size() > i; i++){
			try{
				// Case 1. 동일한 데이터가(주문번호, 주문번호순번, 배송번호) 없을 경우							> TPA11STORDERLIST INSERT, TPAORDERM INSERT
				// Case 2. 동일한 데이터가(주문번호, 주문번호순번, 배송번호) 있으나 값이 처리상태값이 '00'일 경우	> TPA11STORDERLIST UPDATE, TPAORDERM INSERT
				// Case 3. 동일한 데이터가(주문번호, 주문번호순번, 배송번호) 있으나 값이 처리상태값이 '10'일 경우	> SKIP
				
				//= 1. 데이터 처리여부 CHECK.
				existsCnt = pa11stDeliveryDAO.selectPa11stOrderListExists(arrPa11storderlist.get(i));
				if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
				
				//= 2. TPA11STORDERLIST INSERT OR UPDATE
				executedRtn = pa11stDeliveryDAO.mergePa11stOrderList(arrPa11storderlist.get(i));
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST MERGE" });
				}
				
				//= 3. TPAORDERM INSERT
				paorderm = new Paorderm();
				paorderm.setPaOrderGb("10");
				paorderm.setPaCode(arrPa11storderlist.get(i).getPaCode());
				paorderm.setPaOrderNo(arrPa11storderlist.get(i).getOrdNo());
				paorderm.setPaOrderSeq(Long.toString(arrPa11storderlist.get(i).getOrdPrdSeq()));
				paorderm.setPaShipNo(arrPa11storderlist.get(i).getDlvNo());
				paorderm.setPaProcQty(Long.toString(arrPa11storderlist.get(i).getOrdQty()));
				paorderm.setPaDoFlag("30");
				paorderm.setOutBefClaimGb("0");
				paorderm.setInsertDate(arrPa11storderlist.get(i).getInsertDate());
				paorderm.setModifyDate(arrPa11storderlist.get(i).getModifyDate());
				paorderm.setModifyId("PA11");
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}catch(Exception e) {
				
				ParamMap paramMap = new ParamMap();
				
				paramMap.put("linkCode","Empty");
				paramMap.put("apiCode","IF_PA11STAPI_03_003");
				paramMap.put("entpId","999999");
				paramMap.put("code","004");
				paramMap.put("message","11번가 발주확인목록 저장 실패/제휴주문번호: " + arrPa11storderlist.get(i).getOrdNo());
				paramMap.put("siteGb","PA11");
				
				systemProcess.insertPaApiTracking(paramMap);
				continue;
			}
		}
		return rtnMsg;
	}
	
	
	
	private int sendSlipTo11st(HashMap<String, Object> paramMap, ParamMap resultMap , HashMap<String, String> apiInfo,  String slip_no, String partGB, String delyGB) throws IOException, Exception{
		
		String sendDate = DateUtil.getCurrentDateAsString()+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
		String parameter = "";
		String requestType = "GET";
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		int resultCnt  = 0;
		
		
		switch(partGB){
		
		case "PART":
			parameter = "/"+sendDate+"/01/"+delyGB+"/"+slip_no+"/"+paramMap.get("PA_SHIP_NO")+"/Y/"+paramMap.get("PA_ORDER_NO")+"/"+paramMap.get("PA_ORDER_SEQ");
			log.info("부분발송처리(부분배송중) API 호출");
			break;
			
		case "ALL":	
			parameter = "/"+sendDate+"/01/"+delyGB+"/"+slip_no+"/"+paramMap.get("PA_SHIP_NO");
			log.info("발송처리(배송중 처리) API 호출");		
			break;
		}
		
		
		conn = ComUtil.pa11stConnectionSetting(apiInfo, paramMap.get("PA_API_KEY").toString(), requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
		
		// RESPONSE XML 			
		doc = ComUtil.parseXML(conn.getInputStream());
		descNodes = doc.getElementsByTagName("ResultOrder");
		
		conn.disconnect();
		
		for(int j=0; j<descNodes.getLength();j++){
			for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
				resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			}
		}
		
		log.info("부분발송처리 (부분 배송중 처리) 결과 처리");
		if( resultMap.getString("result_code").equals("0") || resultMap.getString("result_code").equals("000000")  ){
			resultMap.put("paDoFlag","40");
			resultCnt++;
		} else {
			//11ST API 연결 실패
			resultMap.put("paDoFlag","");
		}
		
		return resultCnt;
	}
	
	private int updatePaOrderm4Result(ParamMap resultMap ,HashMap<String, Object> paramMap ){
		
		
		//= TPAORDERM UPDATE
		try {
			
			String sysdate = DateUtil.getCurrentDateTimeAsString();
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(paramMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaDoFlag(resultMap.getString("paDoFlag"));
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setPaOrderNo(paramMap.get("PA_ORDER_NO").toString());
			paorderm.setPaShipNo(paramMap.get("PA_SHIP_NO").toString());
			if( paramMap.containsKey("isAll") ) {
				paorderm.setIsAll(paramMap.get("isAll").toString());
			}
			return pa11stDeliveryDAO.updatePaOrdermResult(paorderm);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0 ;
		
		}
	
	}
	/**
	 * 11번가 오늘발송 요청내역 
	 * @param Pa11storderlist
	 * @return String
	 * @throws Exception
	 */
	public String updateTodayDeliveryList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11storderlist.size() > i; i++){
			//= 1. 데이터 처리여부 CHECK.
			existsCnt = pa11stDeliveryDAO.selectPa11stTodayDeliveryListExists(arrPa11storderlist.get(i));
			if(existsCnt == 0)  {
				//= 2. TPA11STORDERLIST 발송예정일 UPDATE
				executedRtn = pa11stDeliveryDAO.updateTodayDeliveryList(arrPa11storderlist.get(i));
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST UPDATE (dlvSndDue) / paOrderNo : " + arrPa11storderlist.get(i).getOrdNo() });
				}
			}
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 배송지연안내 처리 List
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@Override
	public List<Object> selectPa11stDelayList(String dateTime) throws Exception{
		return pa11stDeliveryDAO.selectPa11stDelayList(dateTime);
	}
	
	/**
	 * 11번가 배송지연안내 처리 성공
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@Override
	public int updateTpaordermDelaySendDt(Map<String, Object> paramMap) throws Exception{
		return pa11stDeliveryDAO.updateTpaordermDelaySendDt(paramMap);
	}
		
}