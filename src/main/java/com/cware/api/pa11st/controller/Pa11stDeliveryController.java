package com.cware.api.pa11st.controller;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Pa11storderlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.delivery.service.Pa11stDeliveryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/delivery", description="주문/발송/지연")
@Controller("com.cware.api.pa11st.Pa11stDeliveryController")
@RequestMapping(value="/pa11st/delivery")
public class Pa11stDeliveryController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.delivery.pa11stDeliveryService")
	private Pa11stDeliveryService pa11stDeliveryService;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stOrderController")
	private Pa11stOrderController pa11stOrderController;
	
	
	/**
//	 * 발주대상목록 조회 (결제완료 목록조회) IF_PA11STAPI_03_001
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "발주대상목록 조회 (결제완료 목록조회)", notes = "발주대상목록 조회 (결제완료 목록조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderList(HttpServletRequest request,
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
		ResponseEntity responseEntity = null;
		
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String msg = "";
		int successCnt = 0;
		int totalCnt = 0;
		log.info("===== 발주대상목록 조회 (결제완료 목록조회) API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_001";
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
			    startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT)+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
			}
			
			String parameter = "/"+startDate+"/"+endDate;
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
				}
				
				log.info("03.발주대상목록 조회 (결제완료 목록조회) API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter,ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
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
			
					for(int i=0; i<responseList.size(); i++){
						Pa11storderlist pa11storderlist = new Pa11storderlist();
						
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdSeq(responseList.get(i).getLong("ordPrdSeq"));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						pa11storderlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						
						log.info("04.발주확인처리 (배송준비중 처리) IF_PA11STAPI_03_002 호출");
						
						//발주확인처리 API 호출
						try{
							responseEntity = orderConfirmProc(pa11storderlist.getOrdNo(),pa11storderlist.getOrdPrdSeq(),pa11storderlist.getDlvNo(),pa11storderlist.getPrdNo(),procPaCode,request);
							if(PropertyUtils.describe(responseEntity.getBody()).get("code").equals("200")){
								successCnt++;
							}
						}catch (Exception e) {
							//= 오류발생 시 로그 처리 방안 추가고려 
							continue;
						}
					}
					totalCnt = totalCnt + responseList.size();
					
					
				} else if (paramMap.getString("ns2:result_code").equals("0")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				}else {
					//11ST API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "orderList" })+paramMap.getString("ns2:result_text"));
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
				msg = "총 건수:" + totalCnt + ", 성공건수:" + successCnt;
				paramMap.put("message",msg);
				if(totalCnt != successCnt){
					paramMap.put("code","405");
					paramMap.put("message","처리건수와 성공건수가 서로 상이합니다. 처리건수:"+totalCnt+" / 성공건수:"+successCnt);
				}else{
					paramMap.put("code","200");
					paramMap.put("message","처리건수와 성공건수가 서로 동일합니다");
				}
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.발주확인할 내역 (결제완료 목록조회) API END");
			
			//=IF_PA11STAPI_03_003 발주확인목록 조회 호출 ( 발송처리할 내역 (배송준비중 목록조회) )
			orderConfirmList(request, "", "");
		}
	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
		
	}

	/**
	 * 발주확인처리 IF_PA11STAPI_03_002
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "발주확인처리", notes = "발주확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderConfirmProc(
			@ApiParam(name = "ordNo", value = "11번가 주문번호", defaultValue = "") @RequestParam(value = "ordNo",		required = true) String ordNo,
			@ApiParam(name = "ordPrdSeq", value = "주문순번", defaultValue = "")  	@RequestParam(value = "ordPrdSeq",	required = true) long ordPrdSeq,
			@ApiParam(name = "dlvNo", value = "배송번호", defaultValue = "") 		@RequestParam(value = "dlvNo",		required = true) String dlvNo,
			@ApiParam(name = "prdNo", value = "11번가 상품번호", defaultValue = "") @RequestParam(value = "prdNo",		required = true) long prdNo,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value = "paCode",		required = true) String paCode,
			@ApiParam(name = "request", value = "request", defaultValue = "") HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		Document doc = null;
		NodeList descNodes = null;
		
		log.info("===== 발주확인처리 (배송준비중 처리) API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String parameter = "";
		String requestType = "GET";
		String prg_id = "IF_PA11STAPI_03_002";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		parameter = "/"+ordNo+"/"+ComUtil.objToStr(ordPrdSeq)+"/N/null/"+dlvNo;
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			log.info("02.발주확인처리 대상 체크");
			
			paramMap.put("ordNo",ordNo);
			paramMap.put("ordPrdSeq",ordPrdSeq);
			paramMap.put("dlvNo",dlvNo);
			paramMap.put("prdNo",prdNo);
			
			int checkCnt = pa11stDeliveryService.selectOrderConfirmProcExists(paramMap);
			
			if(checkCnt > 0){
				paramMap.put("code","200");
				paramMap.put("message","OK");
				rtnMsg = pa11stDeliveryService.saveOrderConfirmProcTx(paramMap);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}else {
				
				log.info("03.발주확인처리 (배송준비중 처리) API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo, paCode, requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
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
					log.info("04.발주확인처리 (배송준비중 처리) 결과 처리");
					paramMap.put("code","200");
					paramMap.put("message","OK");
					
					rtnMsg = pa11stDeliveryService.saveOrderConfirmProcTx(paramMap);
					
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
				} else {
					//11ST API 연결 실패
					paramMap.put("code","304");
					paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq + ", dlvNo : " + dlvNo);
					log.info(paramMap.getString("message"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.NOT_MODIFIED);
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
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 발송처리 (배송중 처리) IF_PA11STAPI_03_005
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "발송처리 (배송중 처리)", notes = "발송처리 (배송중 처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> slipMap = null;
		String dateTime = "";
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		ParamMap resultMap = null;
		log.info("===== 발송처리 (배송중 처리) API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_005";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("01.발송처리할 대상 조회");
			List<Object> slipProcList = pa11stDeliveryService.selectPa11stSlipProcList(); 
			
			if(slipProcList != null){
				if(slipProcList.size() > 0){
					for (int i = 0; i < slipProcList.size(); i++) {
						slipMap = (HashMap<String, Object>) slipProcList.get(i);
						targetCount = targetCount + Integer.parseInt(slipMap.get("TARGET_CNT").toString());
						try {
							//= 발송처리, 부분발송처리 API 호출
							resultMap = pa11stDeliveryService.saveSlipOutProcTx(slipMap);
							if(resultMap.getString("rtnMsg").equals("000000") || resultMap.getString("rtnMsg").equals("0")){
								procCount = procCount + resultMap.getInt("resultCnt");
							}else {
								log.info(slipMap.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " + "|");
								sb.append(slipMap.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " + "|");
							}
						} catch (Exception e) {
							log.info(slipMap.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " +e.getMessage() + "|");
							sb.append(slipMap.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " +e.getMessage() + "|");
							continue;
						}
					}
				}
			}
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + " | ";
					
					//대상건수 모두 성공하였을 경우
					if(targetCount == procCount){
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
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 판매완료 내역  (구매확정 목록조회) IF_PA11STAPI_03_006
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매완료 내역  (구매확정 목록조회)", notes = "판매완료 내역  (구매확정 목록조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delivery-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deliveryCompleteList(HttpServletRequest request,
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
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		Paorderm paorderm = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		String msg = "";
		log.info("===== 판매완료 내역  (구매확정 목록조회) API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_006";
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
				
				log.info("03.판매완료 내역  (구매확정 목록조회) API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter,ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Paorderm> arrPaordermlist = new ArrayList<Paorderm>();
			
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
			
					for(int i=0; i<responseList.size(); i++){
						paorderm = new Paorderm();
						paorderm.setPaCode(paCode);
						paorderm.setPaOrderNo(responseList.get(i).getString("ordNo"));
						paorderm.setPaOrderSeq(responseList.get(i).getString("ordPrdSeq"));
						paorderm.setProcDate(DateUtil.toTimestamp(responseList.get(i).getString("pocnfrmDt"), "yyyy-MM-dd HH:mm:ss"));
						paorderm.setApiResultCode("0");
						paorderm.setApiResultMessage("구매확정일자 success");
						paorderm.setPaDoFlag("90");
						paorderm.setPaCode(paCode);
						
						arrPaordermlist.add(paorderm);
						
					}
					log.info("04.판매완료 내역  (구매확정 목록조회) 호출결과 처리");
					rtnMsg = pa11stDeliveryService.saveDeliveryCompleteListTx(arrPaordermlist);
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					paramMap.put("code","200");
					paramMap.put("message","OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("0")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				}else {
					//11ST API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "deliveryCompleteList" }));
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
		    log.error(paramMap.getString("message")
			    , e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("message",msg);
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.판매완료 내역  (구매확정 목록조회) API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 발주확인목록 조회 IF_PA11STAPI_03_003
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "발주확인목록 조회", notes = "발주확인목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-confirm-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderConfirmList(HttpServletRequest request,
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
		Pa11storderlistVO pa11storderlist = null;
		
		log.info("===== 발주확인목록 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_003";
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
			    //-1일
			    //startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT)+DateUtil.getCurrentHourAsString()+DateUtil.getCurrentMinuteAsString();
			    //-2시간
			    startDate = DateUtil.toString(DateUtil.addHour(new Date(), -2), "").substring(0, 12);
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
				
				log.info("03.발주확인목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter,ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11storderlistVO> arrPa11storderlist = new ArrayList<Pa11storderlistVO>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
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
						pa11storderlist = new Pa11storderlistVO();
						
						String note = ComUtil.NVL(responseList.get(i).getString("ordDlvReqCont"));
						int len = note.getBytes("UTF-8").length;
						//배송시 요청사항
						if( len > 280) {
							pa11storderlist.setOrdDlvReqCont(ComUtil.subStringUTFBytes(note, 280) + "…(내용잘림)");
						} else {
							pa11storderlist.setOrdDlvReqCont(responseList.get(i).getString("ordDlvReqCont"));
						}
						
						pa11storderlist.setPaCode(paCode);
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdSeq(responseList.get(i).getLong("ordPrdSeq"));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						pa11storderlist.setOrdPrdCnSeq(responseList.get(i).getLong("ordPrdCnSeq"));
						pa11storderlist.setPaOrderGb("10");
						pa11storderlist.setProcFlag("10");
						pa11storderlist.setAddPrdNo(responseList.get(i).getString("addPrdNo"));
						pa11storderlist.setAddPrdYn(responseList.get(i).getString("addPrdYn"));
						pa11storderlist.setBndlDlvSeq(responseList.get(i).getLong("bndlDlvSeq"));
						pa11storderlist.setBndlDlvYn(responseList.get(i).getString("bndlDlvYN"));
						pa11storderlist.setCustGrdNm(responseList.get(i).getString("custGrdNm"));
						pa11storderlist.setDlvCst(responseList.get(i).getLong("dlvCst"));
						pa11storderlist.setDlvCstType(responseList.get(i).getString("dlvCstType"));
						pa11storderlist.setBmDlvCst(responseList.get(i).getLong("bmDlvCst"));
						pa11storderlist.setBmDlvCstType(responseList.get(i).getString("bmDlvCstType"));
						pa11storderlist.setGblDlvYn(responseList.get(i).getString("gblDlvYn"));
						pa11storderlist.setGiftCd(responseList.get(i).getString("giftCd"));
						pa11storderlist.setMemId(responseList.get(i).getString("memID"));
						pa11storderlist.setMemNo(responseList.get(i).getLong("memNo"));
						pa11storderlist.setOrdAmt(responseList.get(i).getLong("ordAmt"));
						pa11storderlist.setOrdBaseAddr(responseList.get(i).getString("ordBaseAddr"));
						pa11storderlist.setOrdDt(DateUtil.toTimestamp(responseList.get(i).getString("ordDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setOrdDtlsAddr(responseList.get(i).getString("ordDtlsAddr"));
						pa11storderlist.setOrdMailNo(responseList.get(i).getString("ordMailNo"));
						pa11storderlist.setOrdNm(responseList.get(i).getString("ordNm"));
						pa11storderlist.setOrdOptWonStl(responseList.get(i).getLong("ordOptWonStl"));
						pa11storderlist.setOrdPayAmt(responseList.get(i).getLong("ordPayAmt"));
						pa11storderlist.setOrdPrtblTel(responseList.get(i).getString("ordPrtblTel"));
						pa11storderlist.setOrdQty(responseList.get(i).getLong("ordQty"));
						pa11storderlist.setOrdStlEndDt(DateUtil.toTimestamp(responseList.get(i).getString("ordStlEndDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setOrdTlphnNo(responseList.get(i).getString("ordTlphnNo"));
						pa11storderlist.setPlcodrCnfDt(DateUtil.toTimestamp(responseList.get(i).getString("plcodrCnfDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setPrdNm(responseList.get(i).getString("prdNm"));
						pa11storderlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11storderlist.setPrdStckNo(responseList.get(i).getLong("prdStckNo"));
						pa11storderlist.setRcvrBaseAddr(responseList.get(i).getString("rcvrBaseAddr"));
						pa11storderlist.setRcvrDtlsAddr(responseList.get(i).getString("rcvrDtlsAddr"));
						pa11storderlist.setRcvrMailNo(responseList.get(i).getString("rcvrMailNo"));
						pa11storderlist.setRcvrMailNoSeq(responseList.get(i).getString("rcvrMailNoSeq"));
						pa11storderlist.setRcvrNm(responseList.get(i).getString("rcvrNm"));
						pa11storderlist.setRcvrPrtblNo(responseList.get(i).getString("rcvrPrtblNo"));
						pa11storderlist.setRcvrTlphn(responseList.get(i).getString("rcvrTlphn"));
						pa11storderlist.setSelPrc(responseList.get(i).getLong("selPrc"));
						pa11storderlist.setSellerDscPrc(responseList.get(i).getLong("sellerDscPrc"));
						pa11storderlist.setSellerPrdCd(responseList.get(i).getString("sellerPrdCd"));
						pa11storderlist.setSlctPrdOptNm(responseList.get(i).getString("slctPrdOptNm"));
						pa11storderlist.setTmallDscPrc(responseList.get(i).getLong("tmallDscPrc"));
						pa11storderlist.setTypeAdd(responseList.get(i).getString("typeAdd"));
						pa11storderlist.setTypeBilNo(responseList.get(i).getString("typeBilNo"));
						pa11storderlist.setLstTmallDscPrc(responseList.get(i).getLong("lstTmallDscPrc"));
						pa11storderlist.setLstSellerDscPrc(responseList.get(i).getLong("lstSellerDscPrc"));
						pa11storderlist.setReferSeq(responseList.get(i).getLong("referSeq"));
						pa11storderlist.setSellerStockCd(responseList.get(i).getString("sellerStockCd"));
						pa11storderlist.setAppmtDdDlvDy(responseList.get(i).getString("appmtDdDlvDy"));
						pa11storderlist.setAppmtEltRefuseYn(responseList.get(i).getString("appmtEltRefuseYn"));
						pa11storderlist.setAppmtselStockCd(responseList.get(i).getString("appmtselStockCd"));
						pa11storderlist.setCreateDt(DateUtil.toTimestamp(responseList.get(i).getString("createDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setOrdCnDtlsRsn(responseList.get(i).getString("ordCnDtlsRsn"));
						pa11storderlist.setOrdCnQty(responseList.get(i).getLong("ordCnQty"));
						pa11storderlist.setOrdCnMnbdCd(responseList.get(i).getString("ordCnMnbdCd"));
						pa11storderlist.setOrdCnRsnCd(responseList.get(i).getString("ordCnRsnCd"));
						pa11storderlist.setOrdCnStatCd(responseList.get(i).getString("ordCnStatCd"));
						pa11storderlist.setInsertDate(sysdateTime);
						pa11storderlist.setModifyDate(sysdateTime);
						
						arrPa11storderlist.add(pa11storderlist);
					}
					
					pa11stDeliveryService.saveOrderConfirmListTx(arrPa11storderlist);
					paramMap.put("code","200");
					paramMap.put("message", "OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("0")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				}else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "orderConfirmList" }));
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
			
			log.info("04.발주확인목록 조회 API END");
		}
		
		pa11stOrderController.orderInputMain(request);
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	/**
	 * 오늘발송 요청내역 (배송준비중 목록조회) IF_PA11STAPI_03_023
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "오늘발송 요청내역 (배송준비중 목록조회)", notes = "오늘발송 요청내역 (배송준비중 목록조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/today-delivery-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> todayDeliveryList(HttpServletRequest request) throws Exception{
	       
	    
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
		Pa11storderlistVO pa11storderlist = null;
		
		log.info("===== 오늘발송 요청내역 API Start=====");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_023";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info(" 01. 오늘발송 요청내역 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info(" 02.오늘발송 요청내역 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,"",ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11storderlistVO> arrPa11storderlist = new ArrayList<Pa11storderlistVO>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
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
						pa11storderlist = new Pa11storderlistVO();
						
						pa11storderlist.setPaCode(paCode);
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdSeq(responseList.get(i).getLong("ordPrdSeq"));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						//발송예정일 추가
						pa11storderlist.setDlvSndDue(DateUtil.toTimestamp(responseList.get(i).getString("dlvSndDue"), "yyyy-MM-dd"));
						
						arrPa11storderlist.add(pa11storderlist);
					}
					
					//오늘발송 요청내역 일자 update
					pa11stDeliveryService.updateTodayDeliveryListTx(arrPa11storderlist); //list로 보내기
					
					paramMap.put("code","200");
					paramMap.put("message", "OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("0")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				}else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "todayDeliveryList" }));
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
			
			log.info("03.오늘발송 요청내역 API END");
		}
		
		//=IF_PA11STAPI_03_024 발송지연안내 처리 호출
		deliveryDelayGuide(request);
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 발송지연안내 처리 IF_PA11STAPI_03_024
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "발송지연안내 처리", notes = "발송지연안내 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delivery-delay-list", method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	@ResponseBody
	public ResponseEntity<?> deliveryDelayGuide(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> delayMap = null;
		String dateTime = "";
		String startTime = "";
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		int executeCnt = 0;
		
		//결과XML
		Document doc = null;
		NodeList descNodes = null;
		
		log.info("===== 배송지연안내 처리 API Start=====");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_024";
		String request_type = "POST";
		paramMap.put("apiCode", prg_id);
		
		startTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", startTime);
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");

			
			//오늘 날짜 변수 setting 
			dateTime = systemService.getSysdateToString();
			List<Object> delayList = pa11stDeliveryService.selectPa11stDelayList(dateTime); // 쿼리 내에서 예정일 산정
			targetCount = delayList.size();
			
			if(delayList != null){
				if(delayList.size() > 0){
					for (int k = 0; k < delayList.size(); k++) {
						
						delayMap = (HashMap<String, Object>) delayList.get(k);
						
						if(delayMap.get("PA_CODE").equals(Constants.PA_11ST_BROAD_CODE)){
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"",ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
						} else {
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"",ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
						}
						
						//전문 만들기
						reqXml = new StringBuilder();
						reqXml.append("<DeliveryDelayGuideRequest>");
						reqXml.append("<ordNo>");
						reqXml.append(delayMap.get("PA_ORDER_NO").toString());
						reqXml.append("</ordNo>");
						
						reqXml.append("<dlvNo>");
						reqXml.append(delayMap.get("DLV_NO").toString());
						reqXml.append("</dlvNo>");
						
						reqXml.append("<delaySendDt>");
						reqXml.append(delayMap.get("DELY_DATE").toString());
						reqXml.append("</delaySendDt>");
						
						reqXml.append("<delaySendRsnCd>");
						reqXml.append(delayMap.get("REASON_TYPE").toString());
						reqXml.append("</delaySendRsnCd>");
						
						reqXml.append("<delaySendRsn>");
						reqXml.append("<![CDATA["+delayMap.get("REASON_DETAIL").toString()+"]]>");
						reqXml.append("</delaySendRsn>");
						
						reqXml.append("</DeliveryDelayGuideRequest>");
						
						out = new OutputStreamWriter(conn.getOutputStream()); //write
						out.write(String.valueOf(reqXml)); 
						out.flush();
						
						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						
						// RESPONSE XML 			
						doc = ComUtil.parseXML(conn.getInputStream()); //read
					    descNodes = doc.getElementsByTagName("ResultOrder");
					
					    conn.disconnect();
					    
					    
					    for(int j=0; j<descNodes.getLength();j++){
					        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
				        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					        }
						}
					    
					    if( paramMap.getString("result_code").equals("0") ){
							log.info("배송지연 처리 성공");
							paramMap.put("code","200");
							paramMap.put("message","OK"+"["+delayMap.get("PA_ORDER_NO").toString()+"]");
							paramMap.put("resultText", "배송지연처리 성공");
							
							procCount++;
							
							delayMap.put("apiResultCode", "000000");
							delayMap.put("apiResultMessage", "발송예정일 연기");
							executeCnt = pa11stDeliveryService.updateTpaordermDelaySendDt(delayMap);
							if(executeCnt < 1){
								throw processException("msg.cannot_save", new String[] { "TPAORDERM(11st) REMARK1 UPDATE. 제휴 주문번호: "+ delayMap.get("PA_ORDER_NO") });
							}
						} else {
							//11번가 API 연결 실패
							paramMap.put("code","500");
							paramMap.put("message", paramMap.getString("result_text") + ":" + 
										"ordNo : " + delayMap.get("PA_ORDER_NO").toString() 
									+ ", dlvNo : " + delayMap.get("DLV_NO").toString()
									+ ", ordPrdSeq : " + delayMap.get("PA_ORDER_SEQ").toString() );
							log.info(paramMap.getString("message"));
						}
					}
			} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
			}
		 }
			
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount;
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount;
					
					//대상건수 모두 성공하였을 경우
					if(targetCount == procCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					log.info(msg);
					paramMap.put("message", msg );
				}
				systemService.insertApiTrackingTx(request, paramMap);
				
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
		
}
