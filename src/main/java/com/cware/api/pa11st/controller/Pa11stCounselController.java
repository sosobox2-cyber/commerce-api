package com.cware.api.pa11st.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pa11st.counsel.service.Pa11stCounselService;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/counsel", description="상품Q&A/긴급메세지")
@Controller("com.cware.api.pa11st.Pa11stCounselController")
@RequestMapping(value="/pa11st/counsel")
public class Pa11stCounselController extends AbstractController {
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.counsel.pa11stCounselService")
	private Pa11stCounselService pa11stCounselService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	/**
	 * 상품Q&A 조회 IF_PA11STAPI_02_001
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A 조회", notes = "상품Q&A 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			@ApiParam(name = "flag", value = "00 : 전체조회, 01 : 답변완료조회, 02 : 미답변조회", defaultValue = "") @RequestParam(value="flag", required=false, defaultValue = "") String flag ) 
			throws Exception{
	       
	    /** 
	     *  11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2)
	     *  기간입력
	     *  Default D-2 ~ D
	     *  
	     *  flag - 00 : 전체조회
	     *         01 : 답변완료조회
	     *         02 : 미답변조회
	     *  
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
		String msgGb = "00"; //일반상담
		Paqnamoment paqnamoment = null;
		String line = "";
		StringBuffer response = null;
		BufferedReader rd = null;
		InputStream is  = null;
		
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		log.info("===== 상품Q&A 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_02_001";
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
			apiInfo.put("contentType", "text/xml;charset=utf-8");

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(Default D-2 ~ D)
			else{
				endDate   = DateUtil.getCurrentDateAsString();
				startDate = DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
			}
			
			
			if(flag.isEmpty() || flag.equals("")){
				flag = "00";
			}
			
			String parameter = "/"+startDate+"/"+endDate+"/"+flag;
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.상품Q&A 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML 
				response = new StringBuffer();
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "euc-kr"));
				
				
				while((line = rd.readLine()) != null) {
					line = line.replaceAll("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFF]+", "");
					response.append(line);
					response.append('\r');
				}
				rd.close();
				
				is = new ByteArrayInputStream( response.toString().getBytes("euc-kr") );
				
				doc = ComUtil.parseXML(is);
			    descNodes = doc.getElementsByTagName("ns2:productQnas");
			    
			    conn.disconnect();
			    
			    
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
			        	if(node.getNodeName().trim().equals("ns2:productQna")){
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
						paqnamoment = new Paqnamoment();
						
						paqnamoment.setPaGroupCode("01");
						paqnamoment.setPaCode(paCode);
						paqnamoment.setPaCounselNo(responseList.get(i).getString("brdInfoNo"));
						paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).getString("createDt"), "yyyy-MM-dd HH:mm:ss"));
						paqnamoment.setTitle(responseList.get(i).getString("brdInfoSbjct"));
						
						//내용제한
						paqnamoment.setQuestComment( ComUtil.subStringBytes( responseList.get(i).getString("brdInfoCont"), 2000 ));
						
						paqnamoment.setCounselGb(responseList.get(i).getString("qnaDtlsCd"));
						paqnamoment.setPaGoodsCode(responseList.get(i).getString("brdInfoClfNo"));
						paqnamoment.setPaGoodsDtCode("");
						paqnamoment.setPaOrderNo(responseList.get(i).getString("ordNoDe"));
						paqnamoment.setOrderYn(responseList.get(i).getString("buyYn").equals("Y")?"1":"0");
						paqnamoment.setDisplayYn(responseList.get(i).getString("dispYn").equals("Y")?"1":"0");
						paqnamoment.setPaCustNo(responseList.get(i).getString("memID"));
						paqnamoment.setCustTel("");
						paqnamoment.setReceiptDate(DateUtil.toTimestamp(responseList.get(i).getString("ordStlEndDt"), "yyyy-MM-dd HH:mm:ss"));
						paqnamoment.setMsgGb("00"); //일반상담 Q&A
						paqnamoment.setInsertId("PA11");
						paqnamoment.setModifyId("PA11");
						paqnamoment.setInsertDate(sysdateTime);
						paqnamoment.setModifyDate(sysdateTime);
						
						paqnamomentList.add(paqnamoment);
					}
					
					rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}else {
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					
				} else if (paramMap.getString("ns2:result_code").equals("500")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_message"));
					log.info(paramMap.getString("ns2:result_message"));
					
				} else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "orderConfirmList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			urgentCounselList(request,"","","");
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
			
			log.info("04.상품Q&A 조회 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 상품Q&A답변처리 IF_PA11STAPI_02_002
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A답변처리", notes = "상품Q&A 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselProc(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String request_type = "PUT";
		StringBuilder reqXml = null;
		PaqnamVO paQna = null;
		int respCode = 0;
		String respMsg = null;
		Document doc = null;
		NodeList descNodes = null;
		String rtnMsg = "";
		
		
		log.info("===== 상품Q&A답변처리 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_02_002";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("msgGb", "00"); //일반메세지
		
		urgentcounselproc(request);//긴급알리미 답변처리
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("01.상품Q&A답변처리 대상 조회");
			List<PaqnamVO> ansList = pa11stCounselService.selectPa11stAnsQna(paramMap);
			
			if(ansList.size() > 0){
				try {
					targetCount = ansList.size();
					apiInfo = systemService.selectPaApiInfo(paramMap);
					apiInfo.put("contentType", "text/xml");
					
					for(int i=0; i<targetCount; i++){
						
						paQna = ansList.get(i);
						
						log.info("03.상품Q&A 조회 API 호출");
						conn = ComUtil.pa11stConnectionSetting(apiInfo, paQna.getPaApiKey(), request_type, "/" + paQna.getPaCounselNo() + "/" + paQna.getPaGoodsCode());
						
						reqXml = new StringBuilder();
						reqXml.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>");
						reqXml.append("<ProductQna>");
						reqXml.append("<answerCont>");
						reqXml.append("<![CDATA[");
						reqXml.append(paQna.getProcNote());
						reqXml.append("]]>");
						reqXml.append("</answerCont>");
						reqXml.append("</ProductQna>");
						
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();
						
						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						//log.info(" connect respCode : "+respCode);
						//log.info(" connect respMsg  : "+respMsg);
						
						if(respCode == 200){
							// RESPONSE XML
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ClientMessage");
							conn.disconnect();
							
							for(int j=0; j<descNodes.getLength();j++){
								for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
									paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
								}
							}
							log.info("result message : "+paramMap.getString("message"));
						} else {
							conn.disconnect();
							continue;
						}
						
						if( paramMap.getString("resultCode").equals("200")){
							paramMap.put("code","200");
							paramMap.put("message","OK");
							
							paQna.setModifyId("PA11");
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40"); //= 완료
							paQna.setTitle("");
							
							rtnMsg = pa11stCounselService.savePa11stQnaTransTx(paQna);
							if(rtnMsg == Constants.SAVE_SUCCESS){
								procCount++;
							}
						}else if(paramMap.getString("message").indexOf("해당문의건이 존재하지 않습니다.") > -1){ 
							paramMap.put("code","200");
							paramMap.put("message","OK");
							
							paQna.setModifyId("PA11");
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40"); //= 완료
							paQna.setTitle("");
							
							rtnMsg = pa11stCounselService.savePa11stQnaTransTx(paQna);
							if(rtnMsg == Constants.SAVE_SUCCESS){
								procCount++;
							}
							log.info("===원본 글이 삭제된 QNA 완료처리 process===");
							pa11stCounselService.saveQnaMonitering(paQna);
							//신규테이블 insert
						}
							
						else {
							//API 연결 실패
							sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : 처리실패" + "|");
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			 } else {
				 paramMap.put("code","200");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			 }
			
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try{
				if (conn != null) conn.disconnect();
				
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
	 * 긴급메세지 조회 IF_PA11STAPI_02_003
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "긴급메세지 조회", notes = "긴급메세지 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/urgent-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentCounselList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			@ApiParam(name = "flag", value = "01 : 미확인, 02 : 답변대기, 04 : 재답변요청", defaultValue = "") @RequestParam(value="flag", required=false, defaultValue = "") String flag ) 
			throws Exception{
	       
	    /** 
	     *  11번가 날짜 전달 파라미터 날짜포맷 : YYYYMMDD
	     *  기간입력
	     *  Default D-2 ~ D
	     *  
	     *  flag - 01 : 미확인
	     *         02 : 답변대기
	     *         04:  재답변요청
	     *  
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
		String msgGb = "10"; //일반상담 
		Paqnamoment paqnamoment = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		log.info("===== 긴급메세지 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_02_003";
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
			apiInfo.put("contentType", "text/xml;charset=utf-8");

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(Default D-2 ~ D)
			else{
				endDate   = DateUtil.getCurrentDateAsString();
				startDate = DateUtil.addDay(endDate, -5, DateUtil.GENERAL_DATE_FORMAT);
			}
			
			String parameter = "";
			
			if(flag.isEmpty() || flag.equals("")){				
				parameter = "/"+startDate+"/"+endDate;
			}else{
			    parameter = "/"+startDate+"/"+endDate+"/"+flag;
			}
						
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.긴급메세지 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:alimi");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
			
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
			        	if(node.getNodeName().trim().equals("ns2:alimListInfo")){
			        	    
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
					    if(responseList.get(i).getString("emerTypeCd").trim().equals("01")){ //01: 답변요청 - 02 : 공지
						if(responseList.get(i).getString("emerNtceCrntCd").trim().equals("01") //미확인상태 
							|| responseList.get(i).getString("emerNtceCrntCd").trim().equals("02")  //답변대기상태
							|| responseList.get(i).getString("emerNtceCrntCd").trim().equals("04")){ //재답변요청상태						    
						    
						    
						  //재답변 요청시 원 상담건도 emerNtceCrntCd(04) 상태 변경되기 때문에 이미 답변된 내용은 skip 
						    if(responseList.get(i).getString("emerReplyList").trim().equals("") || responseList.get(i).getString("emerReplyList").trim() == null){
						    
						    //내용제한
						    String emerCtnt = ComUtil.subStringBytes( responseList.get(i).getString("emerCtnt"), 2000 );
						    
						    ParamMap parm = new ParamMap();
						    parm.put("emerCtnt", emerCtnt);
						    parm.put("emerNtceSeq", responseList.get(i).getString("emerNtceSeq"));
						    
						    String strChk = pa11stCounselService.selectCheckPacounsel(parm);
						    
							if(strChk.equals("0")){ //기존 긴급메세지 존재하지 않음. 
							    //max_seq 채번							
							    int maxSeq = Integer.parseInt(pa11stCounselService.selectGetMaxPacounselSeq(responseList.get(i)));
							    maxSeq++;

							    paqnamoment = new Paqnamoment();
							    paqnamoment.setPaGroupCode("01");
							    paqnamoment.setPaCode(paCode); 
							    //paqnamoment.setPaCounselNo(responseList.get(i).getString("emerNtceSeq"));
							    paqnamoment.setPaCounselNo(responseList.get(i).getString("emerNtceSeq")+ComUtil.lpad(Long.toString(maxSeq), 3, "0"));
							    paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).getString("createDt"), "yyyyMMdd"));
							    paqnamoment.setTitle(responseList.get(i).getString("emerNtceSubject"));
							    
							    paqnamoment.setQuestComment(emerCtnt);
							    
							    //paqnamoment.setCounselGb("11번가 긴급메세지"+responseList.get(i).getString("emerNtceDtlCd"));
							    paqnamoment.setPaGoodsCode("");
							    paqnamoment.setCounselGb("07"); //긴급메세지
							    paqnamoment.setPaGoodsDtCode("");
							    paqnamoment.setPaOrderNo(responseList.get(i).getString("ordNo"));
							    paqnamoment.setOrderYn("");
							    paqnamoment.setDisplayYn("");
							    paqnamoment.setPaCustNo(responseList.get(i).getString("memID"));
							    paqnamoment.setCustTel("");	
							    paqnamoment.setMsgGb("10"); //긴급메세지
							    paqnamoment.setInsertId("PA11");
							    paqnamoment.setModifyId("PA11");

							    paqnamoment.setInsertDate(sysdateTime);
							    paqnamoment.setModifyDate(sysdateTime);

							    paqnamomentList.add(paqnamoment);

							}else{ //기존메세지 존재 

							}
						    }
						}
					    }
					}

					//저장할 데이터가 존재하면
					if(paqnamomentList.size()> 0){
					    rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					}
					
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}else {
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					
				} else if (paramMap.getString("ns2:result_code").equals("500")) {
					paramMap.put("code","200");
					paramMap.put("message",paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
					
				} else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "urgentCounselList" }));
//					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
			
			log.info("04.긴급메세지 조회 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 긴급메세지 답변처리 IF_PA11STAPI_02_004
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "긴급메세지 답변처리", notes = "긴급메세지 답변처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/urgent-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentcounselproc(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		HttpURLConnection conn = null;		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String request_type = "PUT";		
		PaqnamVO paQna = null;
		int respCode = 0;
		String respMsg = null;
		Document doc = null;
		NodeList descNodes = null;
		String rtnMsg = "";
		
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		double timeS = 0;
		double timeE = 0;
		String responseTime = null;
		ParamMap asyncMap = null;
		
		log.info("===== 긴급메세지 답변처리 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_02_004";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("msgGb", "10"); //긴급메세지
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("01.긴급메세지답변처리 대상 조회");

			List<PaqnamVO> ansList = pa11stCounselService.selectPa11stAnsQna(paramMap);
			
			if(ansList.size() > 0){
				try {
					targetCount = ansList.size();
					apiInfo = systemService.selectPaApiInfo(paramMap);
					apiInfo.put("contentType", "text/xml");
					
					for(int i=0; i<targetCount; i++){
						
						paQna = ansList.get(i);
						
						//'%','/', '?', ';' 특수문자 에러 처리
						String procNote = paQna.getProcNote().replaceAll("\\%", "％").replaceAll("\\/", "／").replaceAll("\\?", "？").replaceAll("\\;", "；");
                        
						// 스페이스 처리
						String endprocNote = URLEncoder.encode(procNote,"UTF-8").replaceAll("\\+", "%20");
						
						//log.info("URLEncoder         ============================================ > " + endprocNote);
						log.info("encodeURIComponent ============================================ > " + procNote);
						log.info("03.긴급메세지 답변 API 호출");						
						

						reqXml = new StringBuilder();
						reqXml.append("<request>");
						reqXml.append("<confirmYn>" + "Y" + "</confirmYn>");
						reqXml.append("<emerNtceSeq>" + paQna.getOriginPaCounselNo() + "</emerNtceSeq>");
						reqXml.append("<answerCtnt>" + paQna.getProcNote() + "</answerCtnt>");
						reqXml.append("</request>");
						
						conn = ComUtil.pa11stConnectionSetting(apiInfo, paQna.getPaApiKey(), request_type, "");
						
						timeS = System.nanoTime();
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();
						
						// RESPONSE XML
						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						
						timeE = System.nanoTime();
						responseTime = Double.toString((timeE - timeS) / 1000000000);
						
						asyncMap = new ParamMap();
						asyncMap.put("paCode", "PA_BROAD".equals(paQna.getPaApiKey()) ? Constants.PA_11ST_BROAD_CODE : Constants.PA_11ST_ONLINE_CODE);
						asyncMap.put("apiCode", prg_id);
						asyncMap.put("url", ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
						asyncMap.put("header", "text/xml;charset=utf-8");
						asyncMap.put("body", String.valueOf(reqXml));
						asyncMap.put("responseTime", responseTime);
						asyncMap.put("result", respMsg);
						
						insertPaRequestMap(asyncMap);
						
						log.info(" connect respCode : "+respCode);
						log.info(" connect respMsg  : "+respMsg);
						
						if(respCode == 200){
							// RESPONSE XML
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("alimiResult");
							conn.disconnect();
							
							for(int j=0; j<descNodes.getLength();j++){
								for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
									paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
								}
							}
							log.info("result message : "+paramMap.getString("result_code") + "/"+ paramMap.getString("result_text"));
						} else {
							conn.disconnect();
							continue;
						}
						//성공 또는 이미 처리된 알리미
						if( paramMap.getString("result_code").equals("200") || paramMap.getString("result_code").equals("10005") ){
							paramMap.put("code","200");
							paramMap.put("message","OK");
							
							paQna.setModifyId("PA11");
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40"); //= 완료
							paQna.setTitle("");
							
							rtnMsg = pa11stCounselService.savePa11stQnaTransTx(paQna);
							if(rtnMsg == Constants.SAVE_SUCCESS){
								procCount++;
							}
						} else {
							//API 연결 실패
							sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : 처리실패" + "|");
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			 } else {
				 paramMap.put("code","200");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			 }
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try{
				if (conn != null) conn.disconnect();
				
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