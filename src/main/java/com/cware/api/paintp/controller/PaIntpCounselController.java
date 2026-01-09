package com.cware.api.paintp.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
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
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.paintp.counsel.service.PaIntpCounselService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/paintp/counsel", description="상담")
@Controller("com.cware.api.paintp.PaIntpCounselController")
@RequestMapping(value="/paintp/counsel")
public class PaIntpCounselController extends AbstractController {
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "paintp.counsel.paIntpCounselService")
	private PaIntpCounselService paIntpCounselService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "common.system.systemProcess")
    private SystemProcess systemProcess;
	
	@Resource(name = "com.cware.netshopping.paintp.util.PaIntpComUtil")
	private PaIntpComUtil paIntpComUtil;
	
	/**
	 * 상품Q&A 조회 IF_PAINTPAPI_02_001
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A 조회", notes = "상품Q&A 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/qna-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getQnaList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate",   required=false, defaultValue = "") String toDate,
			@ApiParam(name ="procId",  value="처리자ID",  defaultValue = "")				   @RequestParam(value = "procId", required=false, defaultValue = "PAINTP") String procId ) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		
		String dateTime = "";
		String startDate = "";
		String endDate = "";
		String duplicateCheck = "";
		String msgGb = "00"; //일반상담 
		
		log.info("===== 상품Q&A 조회 API Start=====");
		log.info("01.상품Q&A 조회 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_02_001";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info("02.상품Q&A 조회 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			Map<String, String> apiParamMap = new HashMap<String, String>();
			
			
			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(조회기간 시작일(YYYYMMDD). 전시 정보 변경일을 기준으로 합니다.)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -3, DateUtil.GENERAL_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
			}
			
			apiParamMap.put("answerYn", "N");
			apiParamMap.put("strDt", startDate);
			apiParamMap.put("endDt", endDate);
			
			for(int q = 0; q < Constants.PA_INTP_CONTRACT_CNT; q++) {
				if(q == 0) {
					apiInfo.put("paCode", Constants.PA_INTP_ONLINE_CODE);
					apiInfo.put("paName", Constants.PA_INTP_ONLINE);
				}
				else {
					apiInfo.put("paCode", Constants.PA_INTP_BROAD_CODE);
					apiInfo.put("paName", Constants.PA_INTP_BROAD);
				}
				
				int locationNo = 0;
				boolean existsCounsel = true;
				
				while(existsCounsel) {
					locationNo++;
					apiParamMap.put("locNo", ComUtil.objToStr(locationNo));
					
					HttpResponse response = ComUtil.paIntpConnectionSetting(apiInfo,apiParamMap);
					
					ParamMap resParam = ComUtil.parseIntpCommonResponse(response);
					
					if("200".equals(resParam.getString("code"))) {
						Map<String, String> map = new HashMap<String, String>();
						doc = (Document) resParam.get("data");
						String errorCheck = "";
						NodeList childeList = doc.getFirstChild().getChildNodes();
						List<ParamMap> responseList = new ArrayList<ParamMap>();
						
						for(int j=0; j<childeList.getLength();j++){
							if("error".equals(childeList.item(j).getNodeName())) {
								errorCheck = "error";
							}
							ParamMap paIntpParamMap = new ParamMap();
							for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
								for(int i=0; i<node.getChildNodes().getLength(); i++){
									Node directionList = node.getChildNodes().item(i);
									paIntpParamMap.put(node.getNodeName().trim(), directionList.getTextContent().trim());
									map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
								}
							}
							if(!paIntpParamMap.get().isEmpty()) responseList.add(paIntpParamMap);
						}
						
						if("error".equals(errorCheck)) {
							if(!map.isEmpty()) {
								String code = map.get("code").replace("|", "");
								code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
								paramMap.put("code", code);
								paramMap.put("message",map.get("explanation"));
							}
							
							existsCounsel = false;
						} else {
							log.info("03.상품Q&A 조회 API후 오픈마켓 상품Q&A(공통) 저장");
							
							List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
							
							if(responseList.size() > 0){
								for(int k=0; k<responseList.size(); k++){
									Paqnamoment paqnamoment = new Paqnamoment();
									paqnamoment.setPaCode(apiInfo.get("paCode"));
									paqnamoment.setPaGroupCode("07"); //인터파크
									paqnamoment.setPaCounselNo(responseList.get(k).getString("qnaNo"));
									paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(k).getString("regDts"), DateUtil.GENERAL_DATE_FORMAT));
									paqnamoment.setTitle(responseList.get(k).getString("subject"));
									paqnamoment.setQuestComment(responseList.get(k).getString("contents"));
									paqnamoment.setCounselGb("01");
									paqnamoment.setPaGoodsCode(responseList.get(k).getString("prdNo"));
									paqnamoment.setPaCustNo("");
									paqnamoment.setCustTel("");
									paqnamoment.setMsgGb(msgGb);
									paqnamoment.setInsertId(procId);
									paqnamoment.setModifyId(procId);
									paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
									
									paqnamomentList.add(paqnamoment);
								}
								
								log.info("04.상품Q&A 저장");
								rtnMsg = paCounselService.savePaQnaTx(paqnamomentList,msgGb);
								
								if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
									paramMap.put("code","200");
									paramMap.put("message","OK");
								}else {
									paramMap.put("code","500");
									paramMap.put("message",rtnMsg);
								}
							}else{
								paramMap.put("code","404");
								paramMap.put("message",getMessage("pa.not_exists_process_list"));
								existsCounsel = false;
							}
						}
					}else{
						paramMap.put("code","500");
						paramMap.put("message",resParam.getString("message"));
						existsCounsel = false;
					}
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
			try{
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}

	/**
	 * 상품Q&A 등록 IF_PAINTPAPI_02_002
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A 등록", notes = "상품Q&A 등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"),  
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/qna-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaInsert(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String request_type = "POST";
		PaqnamVO paQna = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		String dataUrl = "";
		
		log.info("===== 상품Q&A 등록 API Start =====");
		log.info("01.상품Q&A 등록 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_02_002";
		String dateTime = systemService.getSysdatetimeToString();

		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
    	paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("msgGb", "00"); //일반메세지 사용하는지 확인
		
		apiInfo = systemService.selectPaApiInfo(paramMap);
		
		try{
			log.info("02.상품Q&A 등록 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			Map<String, String> apiParamMap = new HashMap<String, String>();			
						
			List<PaqnamVO> ansList = paIntpCounselService.selectPaIntpAnsQna(paramMap);
			
			if(ansList.size() > 0){
				try {
					targetCount = ansList.size();
					
					for(int i=0; i<targetCount; i++){
						paQna = ansList.get(i);
						if(paQna.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
							paramMap.put("paName", Constants.PA_INTP_BROAD);
						} else {
							paramMap.put("paName", Constants.PA_INTP_ONLINE);
						}
						apiInfo.put("paName", paramMap.getString("paName"));
						apiInfo.put("paCode", paQna.getPaCode());
						apiInfo.put("apiInfo", paramMap.getString("apiCode"));
						
						log.info("03.상품Q&A 등록 API 호출");
						
						String internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
						
						if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
							dataUrl = internalXmlUrl + "?paCounselNo=" + paQna.getPaCounselNo();
							apiParamMap.put("dataUrl", dataUrl);
						}
						
						String xmlStr = qnaMakeXmlFile(paQna);
						
						if("".equals(ComUtil.NVL(xmlStr, ""))) {
							paramMap.put("code","404");
							paramMap.put("message","XML 데이터 생성 오류");
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else{
							apiInfo.put("body", xmlStr);
				
							ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo, request_type, apiParamMap);
							
							if("200".equals(resParam.getString("code"))) {
								Map<String, String> map = new HashMap<String, String>();
								doc = (Document) resParam.get("data");
								String errorCheck = "";
								NodeList childeList = doc.getFirstChild().getChildNodes();
								for(int j = 0; j < childeList.getLength(); j++){
									if("error".equals(childeList.item(j).getNodeName())) {
										errorCheck = "error";
									}
									for(Node node = childeList.item(j).getFirstChild(); node != null; node = node.getNextSibling()){
										for(int k = 0; k < node.getChildNodes().getLength(); k++){
					            			Node directionList = node.getChildNodes().item(k);
					            			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
					        			}
									}
								}
								
								if("error".equals(errorCheck)) {
									String code = map.get("code").replace("|", "");
									if(StringUtils.contains(map.get("explanation"), "답변등록이 가능한")) { // 등록 전 고객이 삭제한 문의 건 처리
										paramMap.put("code", "200");
										paramMap.put("message", "OK");

										paQna.setModifyId(Constants.PA_INTP_PROC_ID);
										paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
										paQna.setProcGb("40");
										paQna.setTitle("삭제된 게시물");
										paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

										rtnMsg = paIntpCounselService.savePaIntpQnaTransTx(paQna);
										if (rtnMsg == Constants.SAVE_SUCCESS) {
											procCount++;
										}
									} else {
										code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
										paramMap.put("code", code);
										paramMap.put("message", map.get("explanation"));
									}
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
									
									paQna.setModifyId(Constants.PA_INTP_PROC_ID);
									paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paQna.setProcGb("40");
									paQna.setTitle("");
									paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		
									rtnMsg = paIntpCounselService.savePaIntpQnaTransTx(paQna);
									
									if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
										paramMap.put("code","404");
										paramMap.put("message",getMessage("errors.no.select"));
										return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
									} else {
										paramMap.put("code","200");
										paramMap.put("message","OK");
										procCount++;
									}
								}
							}else{
					        	paramMap.put("code","500");
								paramMap.put("message",resParam.getString("message"));
					        }
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			 } else {
				 paramMap.put("code","404");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
			 }
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
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
	 * 상품Q&A 등록 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/qna_make_update_xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	public String qnaMakeUpdateXml(HttpServletRequest request,
			@RequestParam(value = "paCounselNo"	, required = true, defaultValue = "") String paCounselNo) throws Exception{

		ParamMap paramMap	= new ParamMap();
		String rtnVal = "";
		
		paramMap.put("msgGb", "00");
		paramMap.put("paCounselNo", paCounselNo); 
		
		try {
			List<PaqnamVO> ansList = paIntpCounselService.selectPaIntpAnsQna(paramMap);
			
			for(int i=0; i<ansList.size(); i++){
				PaqnamVO paQna = ansList.get(i);
				rtnVal = qnaMakeXmlFile(paQna);
			}
			
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	public String qnaMakeXmlFile(PaqnamVO paQna) throws Exception
	{
		String rtnVal = null;
		Map<String, String> xmlMap = new HashMap<String, String>();
		
		try {
			xmlMap.put("qnaNo", paQna.getPaCounselNo());
			xmlMap.put("subject", "상품문의 답변");
			xmlMap.put("contents", paQna.getProcNote());
			
			rtnVal = paIntpComUtil.mapToXml(xmlMap);
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	/**
	 * 긴급알리미 조회 IF_PAINTPAPI_02_003
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "긴급알리미 조회", notes = "긴급알리미 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/urgent-notice-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentNoticeList(HttpServletRequest request,
		@ApiParam(name="fromDate", 	required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
		@ApiParam(name="toDate",   	required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate ) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		
		String dateTime = "";
		String startDate = "";
		String endDate = "";
		String duplicateCheck = "";
		Paqnamoment paqnamoment = null;
		String msgGb = "10"; 
		
		log.info("===== 긴급알리미 조회 API Start=====");
		log.info("01.긴급알리미 조회 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_02_003";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info("02.긴급알리미 조회 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			Map<String, String> apiParamMap = new HashMap<String, String>();

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(조회기간 시작일(YYYYMMDD). 전시 정보 변경일을 기준으로 합니다.)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -3, DateUtil.GENERAL_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
			}
			
			apiParamMap.put("strDt", startDate);
			apiParamMap.put("endDt", endDate);
			apiParamMap.put("answerYn", "N");
			
			for(int q = 0; q < Constants.PA_INTP_CONTRACT_CNT; q++) {
				if(q == 0) {
					apiInfo.put("paCode", Constants.PA_INTP_ONLINE_CODE);
					apiInfo.put("paName", Constants.PA_INTP_ONLINE);
				}
				else {
					apiInfo.put("paCode", Constants.PA_INTP_BROAD_CODE);
					apiInfo.put("paName", Constants.PA_INTP_BROAD);
				}
		
				int locationNo = 0;
				boolean existsCounsel = true;
				
				while(existsCounsel){
					locationNo ++ ;
					
					apiParamMap.put("locNo", ComUtil.objToStr(locationNo));
					HttpResponse response = ComUtil.paIntpConnectionSetting(apiInfo,apiParamMap);
					
					ParamMap resParam = ComUtil.parseIntpCommonResponse(response);
				
					if("200".equals(resParam.getString("code")))
					{
						Map<String, String> map = new HashMap<String, String>();
						doc = (Document) resParam.get("data");
						String errorCheck = "";
						NodeList childeList = doc.getFirstChild().getChildNodes();
						List<ParamMap> responseList = new ArrayList<ParamMap>();
						
						for(int j=0; j<childeList.getLength();j++){
							if("error".equals(childeList.item(j).getNodeName())) {
								errorCheck = "error";
							}
							ParamMap paIntpParamMap = new ParamMap();
							for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
								for(int i=0; i<node.getChildNodes().getLength(); i++){
									Node directionList = node.getChildNodes().item(i);
									paIntpParamMap.put(node.getNodeName().trim(), directionList.getTextContent().trim());
									map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
								}
							}
							if(!paIntpParamMap.get().isEmpty()) responseList.add(paIntpParamMap);
						}
						
						if("error".equals(errorCheck)) {
							if(!map.isEmpty()) {
								String code = map.get("code").replace("|", "");
								code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
								paramMap.put("code", code);
								paramMap.put("message",map.get("explanation"));
							}
							existsCounsel = false;
						} else {
							log.info("03.긴급메세지 조회 API후 오픈마켓 (공통) 저장");
							List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
							
							if( responseList.size() > 0 ){
								for(int i=0; i<responseList.size(); i++){
									paqnamoment = new Paqnamoment();
									paqnamoment.setPaCode(apiInfo.get("paCode"));
									paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).getString("rcptDts"), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
									paqnamoment.setTitle("인터파크 긴급알리미");
									paqnamoment.setQuestComment(responseList.get(i).getString("rcptCont"));
									paqnamoment.setCounselGb("02"); //긴급메세지
									paqnamoment.setPaGroupCode("07"); //인터파크
									paqnamoment.setPaGoodsCode(responseList.get(i).getString("prdNo"));
									paqnamoment.setPaOrderNo(responseList.get(i).getString("orderclmNo"));	//	!
									paqnamoment.setPaCustNo(responseList.get(i).getString("ordId"));		//	!
									paqnamoment.setMsgGb(msgGb); //긴급메세지
									paqnamoment.setInsertId(Constants.PA_INTP_PROC_ID);
									paqnamoment.setModifyId(Constants.PA_INTP_PROC_ID);
									paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
									paqnamoment.setPaCounselNo(responseList.get(i).getString("scoreNo"));
									paqnamomentList.add(paqnamoment);
								}
								log.info("04.긴급알리미 저장");
								rtnMsg = paCounselService.savePaQnaTx(paqnamomentList,msgGb);
										
								if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
											paramMap.put("code","200");
											paramMap.put("message","OK");
								}else {
									paramMap.put("code","500");
									paramMap.put("message",rtnMsg);
								}
							}else{
								paramMap.put("code","404");
								paramMap.put("message",getMessage("pa.not_exists_process_list"));
								existsCounsel = false;
							}
						}
					}else{
						paramMap.put("code","500");
						paramMap.put("message",resParam.getString("message"));
						existsCounsel = false;
					}
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
			try{
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e){
					log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
				log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 긴급알리미 답변달기 IF_PAINTPAPI_02_004
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "긴급알리미 답변달기", notes = "긴급알리미 답변달기", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"),  
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/urgent-notice-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentNoticeUpdate(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String request_type = "POST";
		PaqnamVO paQna = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		String dataUrl = "";
		String internalXmlUrl = "";
		
		log.info("===== 긴급알리미 답변달기 API Start=====");
		log.info("01.긴급알리미 답변달기 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_02_004";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
    	paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("msgGb", "10"); 
		
		apiInfo = systemService.selectPaApiInfo(paramMap);
		
		try{
			log.info("02.긴급알리미 답변달기 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			Map<String, String> apiParamMap = new HashMap<String, String>();
			
			List<PaqnamVO> ansList = paIntpCounselService.selectPaIntpAnsQna(paramMap);
			
			if(ansList.size() > 0){
				try {
					targetCount = ansList.size();
				
					for(int i=0; i<targetCount; i++){
						
						paQna = ansList.get(i);
						if(paQna.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
							paramMap.put("paName", Constants.PA_INTP_BROAD);
						} else {
							paramMap.put("paName", Constants.PA_INTP_ONLINE);
						}
						apiInfo.put("paName", paramMap.getString("paName"));
						apiInfo.put("paCode", paQna.getPaCode());
						apiInfo.put("apiInfo", paramMap.getString("apiCode"));
						
						log.info("03.긴급알리미 답변달기 API 호출");
						
						internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
						
						if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
							dataUrl = internalXmlUrl + "?paCounselNo=" + paQna.getPaCounselNo();
							apiParamMap.put("dataUrl", dataUrl);
						}
						
						String xmlStr = urgentMakeXmlFile(paQna);
						
						if("".equals(ComUtil.NVL(xmlStr, ""))) {
							paramMap.put("code","404");
							paramMap.put("message","XML 데이터 생성 오류");
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							apiInfo.put("body", xmlStr);
							
							ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo,request_type,apiParamMap);
							
							if("200".equals(resParam.getString("code"))) {
								Map<String, String> map = new HashMap<String, String>();
								doc = (Document) resParam.get("data");
								String errorCheck = "";
								NodeList childeList = doc.getFirstChild().getChildNodes();
								for(int j=0; j<childeList.getLength();j++){
									if("error".equals(childeList.item(j).getNodeName()))
									{
										errorCheck = "error";
									}
									for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
										for(int k=0; k<node.getChildNodes().getLength(); k++){
					            			Node directionList = node.getChildNodes().item(k);
					            			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
					        			}
									}
								}
								if(map.get("explanation").contains("한번 답변된 내용은 수정이 불가합니다.")) errorCheck = "";
								
								if("error".equals(errorCheck)) {
									String code = map.get("code").replace("|", "");
									code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
									paramMap.put("code", code);
									paramMap.put("message",map.get("explanation"));
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
									
									paQna.setModifyId("PAINTP");
									paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paQna.setProcGb("40"); 
									paQna.setTitle("");
									paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									
									rtnMsg = paIntpCounselService.savePaIntpQnaTransTx(paQna);
									
									if(!rtnMsg.equals("000000")){
										paramMap.put("code","404");
										paramMap.put("message",getMessage("errors.no.select"));
										return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
									} else {
										paramMap.put("code","200");
										paramMap.put("message","OK");
										procCount++;
									}
								}
							} else {
					        	paramMap.put("code","500");
								paramMap.put("message",resParam.getString("message"));
					        }
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			 } else {
				 paramMap.put("code","404");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
			 }
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code","500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
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
	 * 긴급알리미 답변달기 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/urgent_make_update_xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	public String urgentMakeUpdateXml(HttpServletRequest request,
			@RequestParam(value = "paCounselNo"	, required = true, defaultValue = "") String paCounselNo ) throws Exception{
		
		ParamMap paramMap	= new ParamMap();
		String rtnVal = "";
		
		paramMap.put("paCounselNo", paCounselNo);
		paramMap.put("msgGb", "10"); //긴급메세지
		
		try {
			List<PaqnamVO> ansList = paIntpCounselService.selectPaIntpAnsQna(paramMap);
			
				for(int i=0; i<ansList.size(); i++){
					
					PaqnamVO paQna = ansList.get(i);
					log.info("긴급알리미 답변달기 API 호출");
					rtnVal = urgentMakeXmlFile(paQna);
				}
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	public String urgentMakeXmlFile(PaqnamVO paQna) throws Exception {
		String rtnVal = null;
		Map<String, String> xmlMap = new HashMap<String, String>();
		
		try {
			String scoreNo = paQna.getPaCounselNo();
			String answerCont = paQna.getProcNote();

			xmlMap.put("scoreNo", scoreNo);
			xmlMap.put("answerCont", answerCont);
			
			rtnVal = paIntpComUtil.mapToXml(xmlMap);
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}	
}