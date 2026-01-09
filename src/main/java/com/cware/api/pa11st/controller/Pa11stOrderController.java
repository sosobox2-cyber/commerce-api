package com.cware.api.pa11st.controller;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.pa11st.order.service.Pa11stOrderService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pa11st/order", description="취소/판매불가처리")
@Controller("com.cware.api.pa11st.Pa11stOrderController")
@RequestMapping(value="/pa11st/order")
public class Pa11stOrderController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.order.pa11stOrderService")
	private Pa11stOrderService pa11stOrderService;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stAsycController")
	private Pa11stAsycController asycController;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stClaimController")
	private Pa11stClaimController pa11stClaimController;

	/**
	 * 판매불가처리 (판매거부처리) IF_PA11STAPI_03_004
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매불가처리 (판매거부처리)", notes = "판매불가처리 (판매거부처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-reject-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderRejectProc(
			@ApiParam(name = "ordNo", value = "11번가 주문번호", defaultValue = "")	@RequestParam(value = "ordNo",			required = true) String ordNo,
			@ApiParam(name = "ordPrdSeq", value = "주문순번", defaultValue = "")	@RequestParam(value = "ordPrdSeq",		required = true) String ordPrdSeq,
			@ApiParam(name = "ordCnRsnCd", value = "배송업체", defaultValue = "")	@RequestParam(value = "ordCnRsnCd",		required = true) String ordCnRsnCd,
			@ApiParam(name = "ordCnDtlsRsn", value = "사유(06 : 배송 지연 예상, 07 : 상품/가격 정보 잘못 입력, 08 : 상품 품절(전체옵션), 09 : 옵션 품절(해당옵션), 10 : 고객변심, 99 : 기타", defaultValue = "")@RequestParam(value = "ordCnDtlsRsn",	required = true) String ordCnDtlsRsn,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "")	@RequestParam(value = "paCode",			required = true) String paCode,
			@ApiParam(name = "request", value = "request", defaultValue = "")	HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String dateTime = "";
		String paApiKey = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		NodeList descNodes = null;
		
		log.info("===== 판매불가처리 (판매거부처리) API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String parameter = "";
		String requestType = "GET";
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_004";
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);

		parameter = "/"+ordNo+"/"+ComUtil.objToStr(ordPrdSeq)+"/"+ordCnRsnCd+"/"+ordCnDtlsRsn;
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			log.info("02.판매불가처리 (판매거부처리) API 호출");
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				paApiKey = Constants.PA_11ST_BROAD;
			}else {
				paApiKey = Constants.PA_11ST_ONLINE;
			}
			conn = ComUtil.pa11stConnectionSetting(apiInfo, paApiKey, requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));	
			
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
				log.info("03.판매불가처리 (판매거부처리) 결과 처리");
				paramMap.put("code","200");
				paramMap.put("message","OK"+"["+ordCnDtlsRsn+"]");
				paramMap.put("preCancelReason", "재고부족으로 인한 결품처리");
			} else {
				//11번가 API 연결 실패
				paramMap.put("code","304");
				paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq );
				log.info(paramMap.getString("message"));
			}
			paramMap.put("ordNo",ordNo);
			paramMap.put("ordPrdSeq",ComUtil.objToStr(ordPrdSeq));
			paramMap.put("paCode",paCode);
			paramMap.put("apiResultCode",paramMap.getString("result_code"));
			paramMap.put("apiResultMessage",paramMap.getString("result_text"));
			rtnMsg = pa11stOrderService.saveOrderRejectProcTx(paramMap);
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
	 * 판매불가처리 (판매거부처리) IF_PA11STAPI_03_004_BO
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 * BO - 품절취소반품 대상 처리
	 */
	@ApiOperation(value = "BO - 품절취소반품 대상 처리", notes = "BO - 품절취소반품 대상 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/soldOut-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String dateTime = "";
		String paApiKey = "";
		Document doc = null;
		NodeList descNodes = null;
		String duplicateCheck = "";
		
		log.info("===== 판매불가처리 (판매거부처리) API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String parameter = "";
		String requestType = "GET";
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_004_BO";
		String pa_group_code = "01";
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("paGroupCode", pa_group_code);
		List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
		
		apiInfo = systemService.selectPaApiInfo(paramMap);
		
		String ordNo, ordPrdSeq, paCode, ordCanYn, ordCnRsnCd, ordCnDtlsRsn  = "";
		HashMap<String, String> map = new HashMap<String, String>();
		
		try{
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			if(cancelList.size() > 0) {
				for(int i = 0; i < cancelList.size(); i++) {
					try {
						HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
						ordNo = cancelMap.get("PA_ORDER_NO");
						ordPrdSeq = cancelMap.get("PA_ORDER_SEQ");
						paCode = cancelMap.get("PA_CODE");
						
						ordCanYn = cancelMap.get("ORDER_CANCEL_YN");
						
						if("05".equals(ordCanYn)) {
							ordCnRsnCd = "09"; // 09:옵션품절
							ordCnDtlsRsn = URLEncoder.encode(getMessage("pa.out_of_stock_due_shortage_process"), "UTF-8");
						} else {
							ordCnRsnCd = "99"; // 99:기타
							ordCnDtlsRsn = URLEncoder.encode(getMessage("pa.cannot_deliv"), "UTF-8");
						}
						
						parameter = "/"+ordNo+"/"+ComUtil.objToStr(ordPrdSeq)+"/"+ordCnRsnCd+"/"+ordCnDtlsRsn;
						
						if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
							paApiKey = Constants.PA_11ST_BROAD;
						}else {
							paApiKey = Constants.PA_11ST_ONLINE;
						}
						
						map.put("PA_GROUP_CODE", pa_group_code);
						map.put("ORG_ORD_CAN_YN", ordCanYn);
						map.put("PA_CODE", paCode);
						map.put("SITE_GB", cancelMap.get("SITE_GB"));
						map.put("PA_ORDER_NO", ordNo);
						map.put("PA_ORDER_SEQ", ordPrdSeq);
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						
						conn = ComUtil.pa11stConnectionSetting(apiInfo, paApiKey, requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));	
						
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
							log.info("03.판매불가처리 (판매거부처리) 결과 처리");
							paramMap.put("code","200");
							if("05".equals(ordCanYn)) {
								paramMap.put("message","OK"+"["+"품절취소반품"+"/"+ordNo+"]");
								paramMap.put("preCancelReason", "품절취소반품");
							} else {
								paramMap.put("message","OK"+"["+"일괄취소반품"+"/"+ordNo+"]");
								paramMap.put("preCancelReason", "일괄취소반품");
							}
							map.put("ORDER_CANCEL_YN", "10");
							map.put("RSLT_MESSAGE", "판매취소 성공");
							paorderService.updateOrderCancelYnTx(map);
						} else {
							//11번가 API 연결 실패
							paramMap.put("code","304");
							paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq );
							log.info(paramMap.getString("message"));
							
							map.put("ORDER_CANCEL_YN", "90");
							map.put("RSLT_MESSAGE", "판매취소 실패 " + paramMap.getString("result_text"));
							paorderService.updateOrderCancelYnTx(map);
							
						}
						paramMap.put("ordNo",ordNo);
						paramMap.put("ordPrdSeq",ComUtil.objToStr(ordPrdSeq));
						paramMap.put("paCode",paCode);
						paramMap.put("apiResultCode",paramMap.getString("result_code"));
						paramMap.put("apiResultMessage",paramMap.getString("result_text"));
						
					}catch (Exception e){
						log.error("11st 판매불가처리 실패." + e.getMessage());
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
						paorderService.updateOrderCancelYnTx(map);
						continue;
					} finally {
						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
					}
				}
			}
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
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
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매불가처리 (판매거부처리) API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 취소신청목록조회 (주문취소 요청 목록조회) IF_PA11STAPI_03_007
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "취소신청목록조회 (주문취소 요청 목록조회)", notes = "취소신청목록조회 (주문취소 요청 목록조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelList(HttpServletRequest request,
		@ApiParam(name = "fromDate", value = "검색시작일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
		@ApiParam(name = "toDate", value = "검색종료일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
		throws Exception{
	       
	    /** 
	     *  11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2)
	     *  기간입력
	     *  Default D-2 ~ D
	     * */
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String rtnMsg = "";
		HttpURLConnection conn = null;
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		String msg = "";
		Pa11storderlistVO pa11storderlist = null;
		log.info("===== 취소신청목록조회 (주문취소 요청 목록조회) API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_007";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
	
		//백호선 테스트를 위해 만들어 놓음..
		//asycController.cancelInputAsync4Test( request);

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
				
				log.info("03.취소신청목록조회 (주문취소 요청 목록조회) API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11storderlistVO> arrPa11storderlist = new ArrayList<Pa11storderlistVO>();
				
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
				log.info("04.취소신청목록조회 (주문취소 요청 목록조회) API 호출 결과 처리");
				if( responseList.size() > 0 ){
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for(int i=0; i<responseList.size(); i++){
						pa11storderlist = new Pa11storderlistVO();
						
						pa11storderlist.setPaCode(paCode);
						pa11storderlist.setCreateDt(DateUtil.toTimestamp(responseList.get(i).getString("createDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						pa11storderlist.setOrdCnQty(responseList.get(i).getLong("ordCnQty"));
						pa11storderlist.setOrdCnMnbdCd(responseList.get(i).getString("ordCnMnbdCd"));
						pa11storderlist.setOrdCnRsnCd(responseList.get(i).getString("ordCnRsnCd"));
						pa11storderlist.setOrdCnStatCd(responseList.get(i).getString("ordCnStatCd"));
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdCnSeq(responseList.get(i).getLong("ordPrdCnSeq"));
						pa11storderlist.setOrdPrdSeq(responseList.get(i).getLong("ordPrdSeq"));
						pa11storderlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11storderlist.setSlctPrdOptNm(responseList.get(i).getString("slctPrdOptNm"));
						pa11storderlist.setReferSeq(responseList.get(i).getLong("referSeq"));
						pa11storderlist.setPaOrderGb("20");
						pa11storderlist.setProcFlag("00");
						pa11storderlist.setInsertDate(sysdateTime);
						pa11storderlist.setModifyDate(sysdateTime);
						//pa11storderlist.setCancelProcId("PA11");
						
						String note = ComUtil.NVL(responseList.get(i).getString("ordCnDtlsRsn"));
						int len = note.getBytes("UTF-8").length;
						// 취소사유 길이제한
						if( len > 980) {
							pa11storderlist.setOrdCnDtlsRsn(ComUtil.subStringUTFBytes(note, 980) + "…(내용잘림)");
						} else {
							pa11storderlist.setOrdCnDtlsRsn(responseList.get(i).getString("ordCnDtlsRsn"));
						}
						
						arrPa11storderlist.add(pa11storderlist);
					}
					
					rtnMsg = pa11stOrderService.saveCancelListTx(arrPa11storderlist);
					if(rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","200");
						paramMap.put("message", "OK");
						
						//=IF_PA11STAPI_03_005 취소처리 조회 호출
						cancelConfirmProc(request);
					}
					
				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				}else {
					//11ST API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "cancelList" }));
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
				paramMap.put("message",msg);
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.취소신청목록조회 (주문취소 요청 목록조회) API END");
			
			cancelInputMain(request); //= 취소데이터 생성
		}
		
	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	
	
	}
	
	/**
	 * 주문취소승인 및 주문취소거부처리 IF_PA11STAPI_03_008
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문취소승인 및 주문취소거부처리", notes = "주문취소승인 및 주문취소거부처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(
			   HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> approvalApiInfo = new HashMap<String, String>();
		HashMap<String, String> refusalApiInfo = new HashMap<String, String>();
		String dateTime = "";
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int successCount = 0;
		int failCount = 0;
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> cancelMap = null;
		ParamMap resultMap = null;
		
		log.info("===== 주문취소승인 및 주문취소거부처리 API Start=====");
		
		//= connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_008";
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("01.취소처리할 대상 조회");
			List<Object> cancelList = pa11stOrderService.selectPa11stOrdCancelList();
			
			if(cancelList != null){
				if(cancelList.size() > 0){
					targetCount = cancelList.size();
					ParamMap apiMap = new ParamMap();
					apiMap.put("apiCode", "IF_PA11STAPI_03_008");
					apiMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
					apiMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
					approvalApiInfo = systemService.selectPaApiInfo(apiMap);
					
					apiMap = new ParamMap();
					apiMap.put("apiCode", "IF_PA11STAPI_03_009");
					apiMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
					apiMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
					refusalApiInfo = systemService.selectPaApiInfo(apiMap);
					
					log.info("02.취소승인 및 취소거부처리 API 호출");
					for (int i = 0; i < cancelList.size(); i++) {
						cancelMap = (HashMap<String, Object>) cancelList.get(i);
						cancelMap.put("approvalApiInfo",approvalApiInfo);
						cancelMap.put("refusalApiInfo",refusalApiInfo);
						try {
							//= 취소승인처리, 취소거부처리 API 호출
							resultMap = pa11stOrderService.saveCancelConfirmProcTx(cancelMap);
							if(resultMap.getString("rtnMsg").equals("000000")){
								successCount = successCount + resultMap.getInt("successCnt");
							}else {
								failCount = failCount + resultMap.getInt("failCnt");
								log.info(cancelMap.get("ORD_PRD_CN_SEQ").toString() + ": 취소승인/취소거부 fail - " + "|");
								sb.append(cancelMap.get("ORD_PRD_CN_SEQ").toString() + ": 취소승인/취소거부 fail - " + "|");
							}
						} catch (Exception e) {
							log.info(cancelMap.get("ORD_PRD_CN_SEQ").toString() + ": 취소승인/취소거부 fail - " +e.getMessage() + "|");
							sb.append(cancelMap.get("ORD_PRD_CN_SEQ").toString() + ": 취소승인/취소거부 fail - " +e.getMessage() + "|");
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
			log.info("03.취소승인 및 취소거부처리 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 취소철회 목록조회 IF_PA11STAPI_03_020
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "취소철회 목록조회", notes = "취소철회 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelWithdrawList(HttpServletRequest request,
		@ApiParam(name = "fromDate", value = "검색시작일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
		@ApiParam(name = "toDate", value = "검색종료일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
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
		String rtnMsg = "";
		StringBuffer sb = new StringBuffer();
		Pa11storderlistVO pa11storderlist = null;
		
		log.info("===== 취소철회목록 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_020";
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
				
				log.info("03.취소철회목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:orders");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11storderlistVO> arrPa11storderlist = new ArrayList<Pa11storderlistVO>();
			
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
					//Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for(int i=0; i<responseList.size(); i++){
						pa11storderlist = new Pa11storderlistVO();
						
						pa11storderlist.setPaCode(paCode);
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdSeq(Long.parseLong(responseList.get(i).getString("ordPrdSeq")));
						pa11storderlist.setOrdPrdCnSeq(Long.parseLong(responseList.get(i).getString("ordPrdCnSeq")));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						
						arrPa11storderlist.add(pa11storderlist);
					}
					
					rtnMsg = pa11stOrderService.saveCancelWithdrawListTx(arrPa11storderlist);
					sb.append(procPaCode+ " > " + rtnMsg + "|"); 
					
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
				if(!paramMap.getString("code").equals(200)){
					paramMap.put("message", paramMap.getString("message") + " | " + sb.toString());
				}
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("04.취소철회목록 조회 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 취소완료 목록조회 IF_PA11STAPI_03_021
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "취소완료 목록조회", notes = "취소완료 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelCompleteList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일 년(4)월(2)일(2)시(2)분(2)", defaultValue = "") 	@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
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
		String rtnMsg = "";
		StringBuffer sb = new StringBuffer();
		Pa11storderlistVO pa11storderlist = null;
		
		log.info("===== 취소완료목록 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_021";
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
				
				log.info("03.취소완료목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ns2:orders");
				
				conn.disconnect();
				
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11storderlistVO> arrPa11storderlist = new ArrayList<Pa11storderlistVO>();
				
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
						pa11storderlist = new Pa11storderlistVO();
						
						pa11storderlist.setPaCode(paCode);
						pa11storderlist.setCreateDt(DateUtil.toTimestamp(responseList.get(i).getString("createDt"), "yyyy-MM-dd HH:mm:ss"));
						pa11storderlist.setDlvNo(responseList.get(i).getString("dlvNo"));
						pa11storderlist.setOrdCnQty(responseList.get(i).getLong("ordCnQty"));
						pa11storderlist.setOrdCnMnbdCd(responseList.get(i).getString("ordCnMnbdCd"));
						pa11storderlist.setOrdCnRsnCd(responseList.get(i).getString("ordCnRsnCd"));
						pa11storderlist.setOrdCnStatCd(responseList.get(i).getString("ordCnStatCd"));
						pa11storderlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11storderlist.setOrdPrdCnSeq(responseList.get(i).getLong("ordPrdCnSeq"));
						pa11storderlist.setOrdPrdSeq(responseList.get(i).getLong("ordPrdSeq"));
						pa11storderlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11storderlist.setSlctPrdOptNm(responseList.get(i).getString("slctPrdOptNm"));
						pa11storderlist.setReferSeq(responseList.get(i).getLong("referSeq"));
						pa11storderlist.setPaOrderGb("20");
						pa11storderlist.setProcFlag("10");
						pa11storderlist.setInsertDate(sysdateTime);
						pa11storderlist.setModifyDate(sysdateTime);
						pa11storderlist.setCancelProcId("PA11");
						
						String note = ComUtil.NVL(responseList.get(i).getString("ordCnDtlsRsn"));
						int len = note.getBytes("UTF-8").length;
						// 취소사유 길이제한
						if( len > 980) {
							pa11storderlist.setOrdCnDtlsRsn(ComUtil.subStringUTFBytes(note, 980) + "…(내용잘림)");
						} else {
							pa11storderlist.setOrdCnDtlsRsn(responseList.get(i).getString("ordCnDtlsRsn"));
						}
						
						arrPa11storderlist.add(pa11storderlist);
					}
					
					rtnMsg = pa11stOrderService.saveCancelCompleteListTx(arrPa11storderlist);
					sb.append(procPaCode+ " > " + rtnMsg + "|"); 
					
					paramMap.put("code","200");
					paramMap.put("message", "OK");
					
				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				}else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "cancelCompleteList" }));
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
				if(!paramMap.getString("code").equals(200)){
					paramMap.put("message", paramMap.getString("message") + " | " + sb.toString());
				}
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("04.교환완료목록조회 조회 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 11번가 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "11번가 주문접수 데이터 생성", notes = "11번가 주문접수 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-input", method = RequestMethod.GET)
	@ResponseBody
	public void orderInputMain(HttpServletRequest request) throws Exception{ 
		String duplicateCheck 	= "";
		String prg_id 			= "PA11ST_ORDER_INPUT";
		String isLocalYN 		= null;
		String promoAllowTerm 	= ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );	// 프로모션 연동 종료 건 조회 허용 시간 
		
		log.info("=========================== 11st Order Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderInputTargetList = pa11stOrderService.selectOrderInputTargetList();
			HashMap<String, String> hmSheet = null;
			
			int procCnt = orderInputTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, String>) orderInputTargetList.get(i);
					hmSheet.put("PAPROMO_ALLOW_TERM", promoAllowTerm);
					
					//2018.05.03 백호선 수지넷이 로컬에서 안돌기 때문에, 해당 코드를 넣어줌 (로컬이면 하드코딩, 서버면 수지넷 주소정제...)
					//Asnyc함수 안에서 request.getRemoteHost 함수가 제대로 동작하지 않아서 어쩔수 없이 밖에 심음..
					isLocalYN = getLocalOrNot(request);
					asycController.orderInputAsync(hmSheet, request, isLocalYN);
				
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
			log.info("=========================== 11st Order Create End =========================");
		}
		return;
	}
	
	/**
	 * 11번가 주문취소 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "11번가 주문취소 데이터 생성", notes = "11번가 주문취소 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-input", method = RequestMethod.GET)
	@ResponseBody
	public void cancelInputMain(HttpServletRequest request) throws Exception{ 
		String duplicateCheck = "";
		String prg_id = "PA11ST_CANCEL_INPUT";
		
		log.info("=========================== 11st Cancel Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> cancelInputTargetList = pa11stOrderService.selectCancelInputTargetList();
			
			HashMap<String, Object> hmSheet = null;
			int procCnt = cancelInputTargetList.size();
			//asycController.cancelInputAsync(hmSheet, request);
	
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) cancelInputTargetList.get(i);
					
					asycController.cancelInputAsync(hmSheet, request);
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
			log.info("=========================== 11st Cancel Create End =========================");
		}
		return;
	}
	
	
	/**
	 * 주문취소승인처리 IF_PA11STAPI_03_022
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "주문취소승인처리", notes = "주문취소승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-approval-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProc(
			@ApiParam(name = "ordNo", value = "11번가 주문번호", defaultValue = "")		@RequestParam(value = "ordNo",			required = true) String ordNo,
			@ApiParam(name = "ordPrdSeq", value = "주문순번", defaultValue = "")		@RequestParam(value = "ordPrdSeq",		required = true) String ordPrdSeq,
			@ApiParam(name = "ordPrdCnSeq", value = "클레임번호", defaultValue = "") 	@RequestParam(value = "ordPrdCnSeq",		required = true) String ordPrdCnSeq,
			@ApiParam(name = "request", value = "request", defaultValue = "") 		HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> approvalApiInfo = new HashMap<String, String>();
		String dateTime = "";
		String msg = "";
		HashMap<String, Object> cancelMap = null;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		log.info("===== 주문취소승인처리 API Start=====");
		
		//= connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_022";
		String request_type = "GET";

		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{

			paramMap.put("ordNo", ordNo);
			paramMap.put("ordPrdSeq", ordPrdSeq);
			paramMap.put("ordPrdCnSeq", ordPrdCnSeq);

			log.info("01.취소승인처리할 대상 조회");
			cancelMap = pa11stOrderService.selectPa11stOrdCancelApprovalList(paramMap);
			
			if(cancelMap != null){
				paramMap.put("apiCode", prg_id);
				paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
				paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
				paramMap.put("startDate", dateTime);

				approvalApiInfo = systemService.selectPaApiInfo(paramMap);
				approvalApiInfo.put("contentType", "text/xml;charset=utf-8");
				
				log.info("02.11번가 api호출");
				if(cancelMap.get("PA_CODE").equals(Constants.PA_11ST_BROAD_CODE)){
					conn = ComUtil.pa11stConnectionSetting(approvalApiInfo,Constants.PA_11ST_BROAD,request_type,"/"+cancelMap.get("ORD_PRD_CN_SEQ")+"/"+cancelMap.get("ORD_NO")+"/"+cancelMap.get("ORD_PRD_SEQ"));
				} else {
					conn = ComUtil.pa11stConnectionSetting(approvalApiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+cancelMap.get("ORD_PRD_CN_SEQ")+"/"+cancelMap.get("ORD_NO")+"/"+cancelMap.get("ORD_PRD_SEQ"));
				}
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ResultOrder");
			
				conn.disconnect();
				
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
		        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			        }
				}
				
				log.info("03.취소승인처리 결과");
				if( paramMap.getString("result_code").equals("0") ){
					paramMap.put("code","200");
					paramMap.put("message","OK");
				} else {
					//11번가 API 연결 실패
					paramMap.put("code","304");
					paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq );
					log.info(paramMap.getString("message"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
				
				cancelMap.put("PA_CODE",cancelMap.get("PA_CODE"));
				cancelMap.put("PA_ORDER_NO",cancelMap.get("ORD_NO"));
				cancelMap.put("PA_ORDER_SEQ",cancelMap.get("ORD_PRD_SEQ"));		
				cancelMap.put("PROC_FLAG", "10"); //취소승인
				cancelMap.put("apiResultCode",paramMap.getString("result_code"));
				cancelMap.put("apiResultMessage",paramMap.getString("result_text"));
				rtnMsg = pa11stOrderService.saveCancelApprovalProcTx(cancelMap);
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
		}catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message", e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("03.취소승인 및 취소거부처리 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 판매불가처리 (판매거부처리)_모바일자동취소  IF_PA11STAPI_03_027
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 * 모바일 자동취소 처리
	 */
	@ApiIgnore
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String dateTime = "";
		String paApiKey = "";
		Document doc = null;
		NodeList descNodes = null;
		String duplicateCheck = "";
		
		log.info("===== 판매불가처리 (판매거부처리)_모바일자동취소 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String parameter = "";
		String requestType = "GET";
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_027";
		String pa_group_code = "01";
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("paGroupCode", pa_group_code);
		
		apiInfo = systemService.selectPaApiInfo(paramMap);
		
		String ordNo, ordPrdSeq, paCode, ordCnRsnCd, ordCnDtlsRsn  = "";
		HashMap<String, String> map = new HashMap<String, String>();
		
		try{
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			List<HashMap<String, String>> cancelList = pa11stOrderService.selectPaMobileOrderAutoCancelList(paramMap);

			if(cancelList.size() > 0) {
				for(HashMap<String, String> cancelMap : cancelList) {
					try {
						ordNo = cancelMap.get("PA_ORDER_NO");
						ordPrdSeq = cancelMap.get("PA_ORDER_SEQ");
						paCode = cancelMap.get("PA_CODE");
						
						ordCnRsnCd = "09"; // 09:옵션품절
						ordCnDtlsRsn = URLEncoder.encode(getMessage("pa.out_of_stock_due_shortage_process"), "UTF-8");
						
						parameter = "/"+ordNo+"/"+ComUtil.objToStr(ordPrdSeq)+"/"+ordCnRsnCd+"/"+ordCnDtlsRsn;
						
						if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
							paApiKey = Constants.PA_11ST_BROAD;
						}else {
							paApiKey = Constants.PA_11ST_ONLINE;
						}
						
						map.put("PA_GROUP_CODE", pa_group_code);
						map.put("PA_CODE", paCode);
						map.put("PA_ORDER_NO", ordNo);
						map.put("PA_ORDER_SEQ", ordPrdSeq);
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
						map.put("PROC_ID", "PA11");
						
						conn = ComUtil.pa11stConnectionSetting(apiInfo, paApiKey, requestType, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));	
						
						// RESPONSE XML 			
						doc = ComUtil.parseXML(conn.getInputStream());
					    descNodes = doc.getElementsByTagName("ResultOrder");
					    
					    conn.disconnect();
						
					    ParamMap resultMap = new ParamMap();
					    
						for(int j=0; j<descNodes.getLength();j++){
					        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
				        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        		resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					        }
						}
						
						ParamMap asyncMap = new ParamMap();
						asyncMap.put("paCode", paCode);
						asyncMap.put("apiCode", prg_id);
						asyncMap.put("url", ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL")+parameter);
						asyncMap.put("header", "text/xml;charset=utf-8");
						//asyncMap.put("body", String.valueOf(reqXml));
						//asyncMap.put("responseTime", responseTime);
						asyncMap.put("result", String.valueOf(resultMap));
						
						insertPaRequestMap(asyncMap);
						
						if( paramMap.getString("result_code").equals("0") ){
							log.info("03.판매불가처리 (판매거부처리)_모바일자동취소 결과 처리");
							paramMap.put("code","200");
							paramMap.put("message","OK"+"["+"모바일자동취소"+"/"+ordNo+"]");

							map.put("REMARK3_N", "10");
							map.put("RSLT_MESSAGE", "모바일자동취소 성공");
							paorderService.updateRemark3NTx(map);
							
							//상담생성 & 문자발송 & 상품품절처리
							paCommonService.savePaMobileOrderCancelTx(map);
							
						} else {
							//11번가 API 연결 실패
							paramMap.put("code","304");
							paramMap.put("message", paramMap.getString("result_text") + ":" + "ordNo : " + ordNo + ", ordPrdSeq : " + ordPrdSeq );
							log.info(paramMap.getString("message"));
							
							map.put("REMARK3_N", "90");
							map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + paramMap.getString("result_text"));
							paorderService.updateRemark3NTx(map);
							
						}
						paramMap.put("ordNo",ordNo);
						paramMap.put("ordPrdSeq",ComUtil.objToStr(ordPrdSeq));
						paramMap.put("paCode",paCode);
						paramMap.put("apiResultCode",paramMap.getString("result_code"));
						paramMap.put("apiResultMessage",paramMap.getString("result_text"));
						
					}catch (Exception e){
						log.error("11st 판매불가처리 실패." + e.getMessage());
						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 (api 연동 실패)");
						paramMap.put("code","304");
						paramMap.put("message", e.getMessage());
						paorderService.updateRemark3NTx(map);
						continue;
					}
				}
			}else {
				paramMap.put("code","404");
				paramMap.put("message","조회 대상이 존재하지 않습니다.");
			}
			
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		} finally {
			if (conn != null) conn.disconnect();

			//취소건 BO반영을 위한 취소완료 조회 호출
			cancelCompleteList(request, "", "");
			pa11stClaimController.orderClaimMain(request); // = 반품 데이터 생성.
			
			try{
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매불가처리 (판매거부처리)_모바일자동취소 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	public String getLocalOrNot(HttpServletRequest request){
		
		String isLocalYN = "Y";
		
		if(request.getRemoteHost().equals("0:0:0:0:0:0:0:1")||request.getRemoteHost().equals("127.0.0.1")){
			isLocalYN = "Y";
		}else{
			isLocalYN = "N";
		}
		
		return isLocalYN;		
	}
	
	public void insertPaRequestMap(ParamMap param) throws Exception{
		PaRequestMap paRequestMap = new PaRequestMap();
		paRequestMap.setPaCode(param.getString("paCode"));
		paRequestMap.setReqApiCode(param.getString("apiCode"));
		paRequestMap.setReqUrl(param.getString("url"));
		paRequestMap.setReqHeader(param.getString("header")+"");
		paRequestMap.setRequestMap(param.getString("body"));
		paRequestMap.setResponseMap(param.getString("result"));
		paRequestMap.setRemark(param.getString("responseTime"));
		systemService.insertPaRequestMapTx(paRequestMap);
	}
	
	
}