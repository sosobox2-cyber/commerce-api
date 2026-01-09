package com.cware.api.pa11st.controller;

import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.exchange.service.Pa11stExchangeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/exchange", description="교환")
@Controller("com.cware.api.pa11st.Pa11stExchangeController")
@RequestMapping(value="/pa11st/exchange")
public class Pa11stExchangeController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.exchange.pa11stExchangeService")
	private Pa11stExchangeService pa11stExchangeService;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stAsycController")
	private Pa11stAsycController asycController;
	
	
	/**
	 * 교환신청목록조회(교환 요청 목록조회) IF_PA11STAPI_03_014
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "교환신청목록조회(교환 요청 목록조회)", notes = "교환신청목록조회(교환 요청 목록조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeList(HttpServletRequest request,
		@RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
		@RequestParam(value="toDate", required=false, defaultValue = "") String toDate ) throws Exception{
	       
	    /** 
	     *  11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2)
	     *  기간입력
	     *  Default D-2 ~ D
	     * */
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;
		
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;
		
		log.info("===== 교환신청목록조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_014";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		
	
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(Default D-2 ~ D)
			else{
			    endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), +1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
			    startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -2, DateUtil.GENERAL_DATE_FORMAT)+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
			}
			
			String parameter = "/"+startDate+"/"+endDate;
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.교환신청목록조회 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
			        	if(node.getNodeName().trim().equals("ns2:order")){
			        		ParamMap pa11stParamMap = new ParamMap();
		        			for(int i=0; i<node.getChildNodes().getLength(); i++){
		            			Node directionList = node.getChildNodes().item(i);
		            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
		        			}
		        			responseList.add(pa11stParamMap);
			        	}else{
			        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			        	}
			        }
			    }
				
				if( responseList.size() > 0 ){
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for(int i=0; i<responseList.size(); i++){
						pa11stclaimlist = new Pa11stclaimlistVO();
						
						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i).getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i).getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("40");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(i).getString("affliateBndlDlvSeq"));
						pa11stclaimlist.setClmReqCont(responseList.get(i).getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i).getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i).getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i).getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i).getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(responseList.get(i).getString("reqDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setOrdNm(responseList.get(i).getString("ordNm"));
						pa11stclaimlist.setOrdTlphnNo(responseList.get(i).getString("ordTlphnNo"));
						pa11stclaimlist.setOrdPrtblTel(responseList.get(i).getString("ordPrtblTel"));
						pa11stclaimlist.setRcvrMailNo(responseList.get(i).getString("rcvrMailNo"));
						pa11stclaimlist.setRcvrMailNoSeq(responseList.get(i).getString("rcvrMailNoSeq"));
						pa11stclaimlist.setRcvrBaseAddr(responseList.get(i).getString("rcvrBaseAddr"));
						pa11stclaimlist.setRcvrDtlsAddr(responseList.get(i).getString("rcvrDtlsAddr"));
						pa11stclaimlist.setRcvrTypeAdd(responseList.get(i).getString("rcvrTypeAdd"));
						pa11stclaimlist.setRcvrTypeBilNo(responseList.get(i).getString("rcvrTypeBilNo"));
						pa11stclaimlist.setTwMthd(responseList.get(i).getString("twMthd"));
						pa11stclaimlist.setExchNm(responseList.get(i).getString("exchNm"));
						pa11stclaimlist.setExchTlphnNo(responseList.get(i).getString("exchTlphnNo"));
						pa11stclaimlist.setExchPrtbTel(responseList.get(i).getString("exchPrtblTel"));
						pa11stclaimlist.setExchMailNo(responseList.get(i).getString("exchMailNo"));
						pa11stclaimlist.setExchMailNoSeq(responseList.get(i).getString("exchMailNoSeq"));
						pa11stclaimlist.setExchBaseAddr(responseList.get(i).getString("exchBaseAddr"));
						pa11stclaimlist.setExchDtlsAddr(responseList.get(i).getString("exchDtlsAddr"));
						pa11stclaimlist.setExchTypeAdd(responseList.get(i).getString("exchTypeAdd"));
						pa11stclaimlist.setExchTypeBilNo(responseList.get(i).getString("exchTypeBilNo"));
						pa11stclaimlist.setClmLstDlvCst(responseList.get(i).getInt("clmLstDlvCst"));
						pa11stclaimlist.setAppmtDlvCst(responseList.get(i).getInt("appmtDlvCst"));
						pa11stclaimlist.setClmDlvCstMthd(responseList.get(i).getString("clmDlvCstMthd"));
						pa11stclaimlist.setTwPrdInvcNo(responseList.get(i).getString("twPrdInvcNo"));
						pa11stclaimlist.setDlvEtprsCd(responseList.get(i).getString("dlvEtprsCd"));
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);
						
						arrPa11stclaimlist.add(pa11stclaimlist);
					}
					
					pa11stExchangeService.saveExchangeListTx(arrPa11stclaimlist);
					
					paramMap.put("code","200");
					paramMap.put("message", "OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				}else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "exchangeList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("04.교환신청목록조회 조회 API END");
			
			orderChangeMain(request); //= 교환접수 데이터 생성
			
		}
		
		
	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 교환승인처리 IF_PA11STAPI_03_015
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "교환승인처리", notes = "교환승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeConfirmProc(
			   HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String msg = "";
		int targetCount = 0;
		int successCount = 0;
		int failCount = 0;
		String duplicateCheck = "";
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> returnMap = null;
		ParamMap resultMap = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		log.info("===== 교환승인처리 Start=====");
		//= connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_015";
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("01.교환출고 처리 건 조회");
			List<Object> returnList = pa11stExchangeService.selectExchangeSlipList();
			
			ParamMap apiMap = new ParamMap();
			apiMap.put("apiCode", prg_id);
			apiMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
			apiMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
			apiInfo = systemService.selectPaApiInfo(apiMap);
			
			log.info("02.교환승인처리 API 호출");
			if(returnList != null){
				if(returnList.size() > 0){
					targetCount = returnList.size();
					for (int i = 0; i < returnList.size(); i++) {
						returnMap = (HashMap<String, Object>) returnList.get(i);
						returnMap.put("apiInfo", apiInfo);
						try {
							//= 교환승인처리 API 호출
							resultMap = pa11stExchangeService.saveExchangeConfirmProcTx(returnMap);
							if(resultMap.getString("rtnMsg").equals(Constants.SAVE_SUCCESS)){
								successCount = successCount + resultMap.getInt("successCnt");
							}else {
								failCount = failCount + resultMap.getInt("failCnt");
								log.info(returnMap.get("MAPPING_SEQ").toString() + ": 교환승인처리 fail - " + "|");
								sb.append(returnMap.get("MAPPING_SEQ").toString() + ": 교환승인처리 fail - " + "|");
							}
						} catch (Exception e) {
							log.info(returnMap.get("MAPPING_SEQ").toString() + ": 교환승인처리 fail - " +e.getMessage() + "|");
							sb.append(returnMap.get("MAPPING_SEQ").toString() + ": 교환승인처리 fail - " +e.getMessage() + "|");
							continue;
						}
					}
				}
			}
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:" + failCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:" + failCount + "|";
					
					//대상건수 모두 성공하였을 경우
					if(targetCount == successCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				systemService.insertApiTrackingTx(request, paramMap);
				
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("03.교환승인처리 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 교환거부처리 IF_PA11STAPI_03_016
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "교환거부처리", notes = "교환거부처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-reject-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeRejectProc(
			@ApiParam(name = "ordNo", value = "11번가 주문번호", defaultValue = "") @RequestParam(value = "ordNo",		required = true) String ordNo,
			@ApiParam(name = "ordPrdSeq", value = "주문순번", defaultValue = "") 	@RequestParam(value = "ordPrdSeq",	required = true) String ordPrdSeq,
			@ApiParam(name = "clmReqSeq", value = "클레임 번호", defaultValue = "")	@RequestParam(value = "clmReqSeq",	required = true) String clmReqSeq,
			@ApiParam(name = "refsRsnCd", value = "사유코드 (201 : 교환 상품 미입고, 202 : 고객 교환신청 철회 대행, 203 : 교환 불가 상품, 204 : 기타)", defaultValue = "") 	@RequestParam(value = "refsRsnCd",	required = true) String refsRsnCd,
			@ApiParam(name = "refsRsn", value = "사유", defaultValue = "") 		@RequestParam(value = "refsRsn",	required = true) String refsRsn,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value = "paCode",		required = true) String paCode,
			@ApiParam(name = "request", value = "request", defaultValue = "") 	HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		NodeList descNodes = null;
		HttpURLConnection conn = null;
		String dateTime = "";
		String parameter = "";
		String paApiKey = "";
		String requestType = "GET";
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		int exchRefusalExists = 0;
		
		log.info("===== 교환거부처리 Start=====");
		log.info("01.API 기본정보 세팅");
		//= connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_016";
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
//		refsRsn = URLDecoder.decode(refsRsn, "UTF-8") ;
		paramMap.put("code","200");
		paramMap.put("message","OK");
		
		parameter = "/"+ordNo+"/"+ordPrdSeq+"/"+clmReqSeq+"/"+refsRsnCd+"/"+refsRsn;
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			ParamMap paramExchMap = new ParamMap();
			paramExchMap.put("ordNo", ordNo);
			paramExchMap.put("ordPrdSeq", ordPrdSeq);
			paramExchMap.put("clmReqSeq", clmReqSeq);
			paramExchMap.put("paCode", paCode);
			exchRefusalExists = pa11stExchangeService.selectExchangeRefusalExists(paramExchMap);
			if(exchRefusalExists > 0){
				log.info("01-1.교환거부 처리전 교환취소건 존재");
				paramMap.put("code","406");
				paramMap.put("message","교환거부 처리전 교환취소건 존재");
			}else {
				
				log.info("02.교환거부처리 API 호출");
				if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
					paApiKey = Constants.PA_11ST_BROAD;
				}else {
					paApiKey = Constants.PA_11ST_ONLINE;
				}
				conn = ComUtil.pa11stConnectionSetting(apiInfo, paApiKey, requestType, parameter);	
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ResultOrder");
				
				conn.disconnect();
				
				for(int j=0; j<descNodes.getLength();j++){
					for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
						paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					}
				}
				
				if( paramMap.getString("result_code").equals("0") ){
					log.info("03.교환거부처리 결과 처리");
					paramMap.put("code","200");
					paramMap.put("message","OK"+"["+clmReqSeq+"]");
					paramMap.put("preCancelReason", "재고부족으로 인한 교환거부처리");
				} else {
					//11번가 API 연결 실패
					paramMap.put("code","304");
					paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq + ", clmReqSeq : " + clmReqSeq);
					log.info(paramMap.getString("message"));
				}
				paramMap.put("ordNo",ordNo);
				paramMap.put("ordPrdSeq",ordPrdSeq);
				paramMap.put("clmReqSeq",clmReqSeq);
				paramMap.put("paCode",paCode);
				paramMap.put("apiResultCode",paramMap.getString("result_code"));
				paramMap.put("apiResultMessage",paramMap.getString("result_text"));
				rtnMsg = pa11stExchangeService.saveExchangeRejectProcTx(paramMap);
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		} finally {
			if (conn != null) conn.disconnect();

			try{
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 교환철회목록조회 IF_PA11STAPI_03_017
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "교환철회목록조회", notes = "교환철회목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-cancel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeCancelList(HttpServletRequest request,
		@ApiParam(name = "fromDate", value = "검색시작일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
		@ApiParam(name = "toDate", value = "검색종료일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
		throws Exception{
	       
	    /** 
	     *  11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2)
	     *  기간입력
	     *  Default D-2 ~ D
	     * */
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;
		
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;
		
		log.info("===== 교환철회목록조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_017";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(Default D-2 ~ D)
			else{
			    endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), +1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
			    startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -2, DateUtil.GENERAL_DATE_FORMAT)+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
			}
			
			String parameter = "/"+startDate+"/"+endDate;
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.교환철회목록조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
			        	if(node.getNodeName().trim().equals("ns2:order")){
			        		ParamMap pa11stParamMap = new ParamMap();
		        			for(int i=0; i<node.getChildNodes().getLength(); i++){
		            			Node directionList = node.getChildNodes().item(i);
		            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
		        			}
		        			responseList.add(pa11stParamMap);
			        	}else{
			        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			        	}
			        }
			    }
				
				if( responseList.size() > 0 ){
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for(int i=0; i<responseList.size(); i++){
						pa11stclaimlist = new Pa11stclaimlistVO();
						
						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i).getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i).getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("41");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(i).getString("affliateBndlDlvSeq"));
						pa11stclaimlist.setClmReqCont(responseList.get(i).getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i).getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i).getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i).getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i).getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(responseList.get(i).getString("reqDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);
						
						arrPa11stclaimlist.add(pa11stclaimlist);
					}
					
					pa11stExchangeService.saveExchangeCancelListTx(arrPa11stclaimlist);
					
					paramMap.put("code","200");
					paramMap.put("message", "OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				}else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "exchangeCancelList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("04.교환철회목록 조회 API END");
			
			changeCancelMain(request); //= 교환취소 데이터 생성
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 11번가 교환접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "11번가 교환접수 데이터 생성", notes = "11번가 교환접수 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-change", method = RequestMethod.GET)
	@ResponseBody
	public void orderChangeMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PA11ST_ORDER_CHANGE";
		HashMap<String, Object> hmSheet = null;
		String isLocalYn = "N";
		int procCnt = 0;
		log.info("=========================== 11st Order Change Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderChangeTargetList = pa11stExchangeService.selectOrderChangeTargetList();
			
			procCnt = orderChangeTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
								
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderChangeTargetList.get(i);
					
					if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) ||("127.0.0.1").equals(request.getRemoteHost())){
						isLocalYn = "Y";
					}else{
						isLocalYn = "N";
					}
										
					asycController.orderChangeAsync(hmSheet, request, isLocalYn);
				} catch (Exception e) {
					continue;
				}
			}
		
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== 11st Order Change End =========================");
		}
		return;
	}
	
		
	/**
	 * 11번가 교환취소 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "11번가 교환취소 데이터 생성 ", notes = "11번가 교환취소 데이터 생성 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/change-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void changeCancelMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PA11ST_CHANGE_CANCEL";
		HashMap<String, Object> hmSheet = null;
		
		log.info("=========================== 11st Change Cancel Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> changeCancelTargetList = pa11stExchangeService.selectChangeCancelTargetList();
			int procCnt = changeCancelTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) changeCancelTargetList.get(i);
					if(RefineCancelTargetList(hmSheet)) continue;
					
					asycController.changeCancelAsync(hmSheet, request);
					
					
				} catch (Exception e) {
					continue;
				}
			}
			
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== 11st Change Cancel End =========================");
		}
		return;
	}
	
	
	private boolean RefineCancelTargetList(HashMap<String, Object> hm){
		// 상담원이 교환건에 대해 상담프로그램으로 처리하지 않고 있는 상태에서 (단일상품은 해당사항 없음)
		// 교환 철회가 들어왔을때 BO나 제휴 데이터 자체는 문제가 없으나
		// 중간테이블 Update를 해주지 않기 때문에 계속 API가 읽어서 에러로그를 만드는 문제가 발생햇습니다.
		// 해당 함수는 그러한 문제를 처리하기 위해 고안한 함수입니다. 
		
		try {
			
			String paOrderNo = "";
			String paClaimNo = "";
			int cnt = 0;
			String sysdate = DateUtil.getCurrentDateTimeAsString();
			
			
			if (hm.get("PA_ORDER_NO") != null){
				paOrderNo = hm.get("PA_ORDER_NO").toString();
			}
			else return false;
			
			if (hm.get("PA_CLAIM_NO") != null){
				paClaimNo = hm.get("PA_CLAIM_NO").toString();
			}
			else return false;
					
		
			cnt = pa11stExchangeService.refindOrderChangeTargetList(paOrderNo);

			if ( cnt> 0){
				/*
				ParamMap paramMap = new ParamMap();
				paramMap.put("preCancelReason", "상담원이 교환처리 하기 전 교환 철회");
				paramMap.put("paOrderNo", paOrderNo);
				paramMap.put("paClaimNo", paClaimNo);*/
				
				Paorderm paorderm = new Paorderm();
				paorderm.setPreCancelReason("상담원이 교환처리 하기 전 교환 철회");
				paorderm.setPaOrderNo(paOrderNo);
				paorderm.setPaClaimNo(paClaimNo);
				paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss")
);
				
				
			
				pa11stExchangeService.updatePaOrderm4preChangeCancle(paorderm);
				return true;
			}
					
		} catch (Exception e) {
			return false;  //일단 데이터상으로는 에러가 나오지 않으니 해당 함수가 에러나도 프로그램은 그냥 돌린다.
		}
		
		return false;
		
	}
	
}
