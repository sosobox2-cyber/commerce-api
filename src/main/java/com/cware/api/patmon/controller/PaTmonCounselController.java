package com.cware.api.patmon.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.patmon.counsel.service.PaTmonCounselService;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/counsel", description="상담")
@Controller("com.cware.api.patmon.PaTmonCounselController")
@RequestMapping(value = "/patmon/counsel")
public class PaTmonCounselController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaTmonCounselController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "patmon.counsel.paTmonCounselService")
	private PaTmonCounselService paTmonCounselService;
	
	@Autowired
	PaTmonConnectUtil paTmonConnectUtil;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품문의 조회", notes = "상품문의 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/qna-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> qnaList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate",   required=false, defaultValue = "") String toDate,
			@ApiParam(name ="procId",  value="처리자ID",  defaultValue = "")				   @RequestParam(value = "procId", required=false, defaultValue = "PATMON") String procId ) throws Exception{
		
		List<Map<String, Object>> tmonDataList = new ArrayList<Map<String, Object>>();
		String	dateTime 		= "";
		String	rtnMsg 			= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		String paCode       = "";
		String msgGb = "00"; //일반상담 
		boolean hasNext = true;
		int page = 1;
		
		log.info("===== 상품 문의 조회 API Start =====");
		log.info("01.API 기본정보 세팅");
		String prg_id = "IF_PATMONAPI_02_001";
		dateTime = systemService.getSysdatetimeToString();
		
		try {
			
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "2");
			fromDate  =  retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_TMON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				hasNext = true;
				page = 1;
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				
				while(hasNext) {
					hasNext = false;
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{answered}", "false").replace("{per}", "50").replace("{page}", String.valueOf(page)));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
					
					if(!"null".equals(String.valueOf(map.get("error")))) {
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", map.get("error"));
						continue;
					}
					
					tmonDataList = (List<Map<String, Object>>)map.get("items");
					
					if(tmonDataList.size() < 1) {
						apiInfoMap.put("code", "404");
						apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					for(Map<String, Object> m : tmonDataList) {
						
						Paqnamoment paqnamoment = new Paqnamoment();
						paqnamoment.setPaCode(paCode);
						paqnamoment.setPaGroupCode("09"); 
						paqnamoment.setPaCounselNo(m.get("articleNo").toString());												
						try {
							paqnamoment.setCounselDate(DateUtil.toTimestamp(m.get("createDate").toString(), DateUtil.TMON_DATETIME_FORMAT));							
						} catch (ParseException e) {
							paqnamoment.setCounselDate(DateUtil.toTimestamp(m.get("createDate").toString(), DateUtil.TMON_DATEMIN_FORMAT));
						}
						paqnamoment.setTitle("티몬 QNA ");
						paqnamoment.setQuestComment(m.get("content").toString());
						paqnamoment.setPaGoodsCode(m.get("dealNo").toString());
						paqnamoment.setPaOrderNo(m.containsKey("tmonOrderNo")?m.get("tmonOrderNo").toString():"");
						paqnamoment.setMsgGb(msgGb);
						paqnamoment.setInsertId(procId);
						paqnamoment.setModifyId(procId);
						paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paqnamoment.setCounselGb("01");
						
						paqnamomentList.add(paqnamoment);
					}
					
					hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
					page++;
				}
			
				log.info("04.상품Q&A 저장");
				if(paqnamomentList.size() > 0) {
					rtnMsg = paCounselService.savePaQnaTx(paqnamomentList,msgGb);
					if(!Constants.SAVE_SUCCESS.equals(rtnMsg)){
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", rtnMsg);
					}
				}				
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}		
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "상품문의 답변 등록", notes = "상품문의 답변 등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/qna-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaInsert(HttpServletRequest request) throws Exception{
		
		PaqnamVO paQna = null;
		String msgGb = "00"; //일반상담 
		String	dateTime 		= "";
		String	rtnMsg 			= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		ParamMap	paramMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		String prg_id = "IF_PATMONAPI_02_002";
		int targetCount = 0;
		int procCount = 0;
		
		try {
			log.info("===== 티몬 상품 문의  답변등록 API Start =====");	
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			dateTime = systemService.getSysdatetimeToString();
			
			String url = apiInfoMap.get("url").toString();
			
			paramMap.put("msgGb", msgGb);
			log.info("1. 티몬 상품 문의  답변등록 API 대상 검색");	
			List<PaqnamVO> paqnamomentList = paTmonCounselService.selectPaTmonAnsQna(paramMap);
			
			targetCount = paqnamomentList.size();
			for (int j = 0 ; j < paqnamomentList.size(); j++) {
				paQna = paqnamomentList.get(j);
				
				apiInfoMap.put("paCode", paQna.getPaCode());
				apiInfoMap.put("url", url.replace("{articleNo}", paQna.getPaCounselNo()));
				apiDataMap.put("content", paQna.getProcNote());
				
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
			
				if(!"null".equals(String.valueOf(map.get("error")))) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
					continue;
				}else {
					
					paQna.setModifyId(Constants.PA_TMON_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40");
					paQna.setTitle("");
					paQna.setProcId(Constants.PA_TMON_PROC_ID);
					paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paTmonCounselService.savePaTmonQnaTransTx(paQna);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("msg.cannot_save"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
						procCount++;
					}
				}
			}
		} catch (Exception e) {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== 티몬 상품 문의 답변등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "CS문의 조회", notes = "CS문의 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cs-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaCsList(HttpServletRequest request, 
			@ApiParam(name="fromDate",	required=false,	value="FROM날짜",	defaultValue = "")	@RequestParam(value="fromDate",	required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",	required=false,	value="TO날짜",	defaultValue = "")	@RequestParam(value="toDate",	required=false, defaultValue = "") String toDate,
			@ApiParam(name="procId",	required=false,	value="처리자ID",	defaultValue = "")	@RequestParam(value="procId",	required=false, defaultValue = "PATMON") String procId ) throws Exception{
		
		String		rtnMsg 		= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		List<Map<String, Object>> tmonDataList = new ArrayList<Map<String, Object>>();
		List<String> csInquirySeqNoList = new ArrayList<String>();
		String msgGb = "10";
		String paCode       = "";
		boolean hasNext = true;
		int page = 1;
		
		log.info("===== CS문의  조회 API Start =====");
		
		log.info("01.API 기본정보 세팅");
		String prg_id = "IF_PATMONAPI_02_003";
		//CS문의에 상품 코드 안넘어 올수 있음. 쿼리 확인 필요
		
		try {
			
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "2");
			fromDate  =  retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_TMON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				hasNext = true;
				page = 1;
				
				while(hasNext) {
					hasNext = false;
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{per}", "50").replace("{page}", String.valueOf(page)));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
					
					if(!"null".equals(String.valueOf(map.get("error")))) {
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", map.get("error"));
						continue;
					}
					
					tmonDataList = (List<Map<String, Object>>)map.get("items");
					
					if(tmonDataList.size() < 1) {
						apiInfoMap.put("code", "404");
						apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					for(Map<String, Object> m : tmonDataList) {
						String paCounselNo = m.get("csInquirySeqNo") + ":" + m.get("csInquiryMessageGroupSeqNo");
						int result = paTmonCounselService.selectPaCounselNoCheck(paCounselNo);
						if( result ==  0) {
							csInquirySeqNoList.add(m.get("csInquirySeqNo").toString());
						}
					}
					
					if(csInquirySeqNoList.size() > 0) {
						setQnaCsListInfo(request,csInquirySeqNoList,paqnamomentList,paCode,"paTmon");
					}
					
					hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
					page++;					
				}
				
				if(paqnamomentList.size() > 0) {
					log.info("04.상품Q&A 저장");
					rtnMsg = paCounselService.savePaQnaTx(paqnamomentList,msgGb);
					
					if(!Constants.SAVE_SUCCESS.equals(rtnMsg)){
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", rtnMsg);
					}
				}
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "CS문의 상세 조회", notes = "CS문의 상세 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cs-listInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaCsListInfo(HttpServletRequest request,
			@ApiParam(name="csInquirySeqNoList",	required=true ) @RequestParam(value="csInquirySeqNoList", required=true) List<String> csInquirySeqNoList,
			@ApiParam(name="paqnamomentList",		required=true ) @RequestParam(value="paqnamomentList", 	  required=true) List<Paqnamoment> paqnamomentList,			
			@ApiParam(name="paCode",				required=true ) @RequestParam(value="paCode",			  required=true) String paCode,
			@ApiParam(name="procId",				required=false) @RequestParam(value="procId",			  required=false, defaultValue = "PATMON") String procId ) throws Exception{
		
		String	dateTime 		= "";
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		List<Map<String, Object>> optionDealInfoList = null;
		//List<Map<String, Object>> inquiryFileList = null;
		Map<String, Object> categoryInfo = null;
		Map<String, Object> counselorInquiry = null;
		List<Map<String, Object>> inquiryMessageGroupList = null;
		
		String prg_id = "IF_PATMONAPI_02_004";
		String msgGb = "10"; //CS문의 
		dateTime = systemService.getSysdatetimeToString();
		int targetCount = 0;
		int procCount = 0;
		
		try {
			
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			targetCount = csInquirySeqNoList.size();
			for ( int j = 0; j < csInquirySeqNoList.size(); j++) {
				
				apiInfoMap.put("paCode", paCode);
				apiInfoMap.put("url", url.replace("{csInquirySeqNo}", csInquirySeqNoList.get(j)));
				
				log.info(prg_id + " - 01.티몬 CS문의 상세조회  통신 [start]");
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
				
				if(!"null".equals(String.valueOf(map.get("error")))) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
					continue;
				}
				
				log.info(prg_id + " - 01.티몬 CS문의 상세조회 값 세팅");
				optionDealInfoList = (List<Map<String, Object>>)map.get("optionDealInfoList");
				//inquiryFileList = (List<Map<String, Object>>)map.get("inquiryFileList");
				inquiryMessageGroupList = (List<Map<String, Object>>)map.get("inquiryMessageGroupList");
				categoryInfo = (Map<String, Object>)map.get("categoryInfo");
				counselorInquiry = (Map<String, Object>)inquiryMessageGroupList.get(0).get("counselorInquiry");
				
				String PaCounselNo = map.get("csInquirySeqNo").toString() + ":" + inquiryMessageGroupList.get(0).get("csInquiryMessageGroupSeqNo").toString();
				
								
				Paqnamoment paqnamoment = new Paqnamoment();
				paqnamoment.setPaCode(paCode);
				paqnamoment.setPaGroupCode("09"); //티몬
				paqnamoment.setPaCounselNo(PaCounselNo);
				try {
					paqnamoment.setCounselDate(DateUtil.toTimestamp(inquiryMessageGroupList.get(0).get("createDate").toString(), DateUtil.TMON_DATETIME_FORMAT));							
				} catch (ParseException e) {
					paqnamoment.setCounselDate(DateUtil.toTimestamp(inquiryMessageGroupList.get(0).get("createDate").toString(), DateUtil.TMON_DATEMIN_FORMAT));
				}
				paqnamoment.setTitle("티몬 CS상품 문의");
				paqnamoment.setQuestComment(ComUtil.NVL(counselorInquiry.get("message").toString(),"내용없음"));
				paqnamoment.setCounselGb("07");
				paqnamoment.setPaGoodsCode(map.containsKey("dealNo")?map.get("dealNo").toString():"");
				paqnamoment.setPaGoodsDtCode(optionDealInfoList.get(0).containsKey("optionDealNo")?optionDealInfoList.get(0).get("optionDealNo").toString():"");
				paqnamoment.setPaOrderNo(map.containsKey("orderNo")?map.get("orderNo").toString():"");
				paqnamoment.setPaCustNo("");
				paqnamoment.setCustTel("");
				paqnamoment.setMsgGb(msgGb);
				paqnamoment.setToken(categoryInfo.get("seqNo").toString());
				paqnamoment.setInsertId(procId);
				paqnamoment.setModifyId(procId);
				paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
				
				paqnamomentList.add(paqnamoment);
				
			}
		} catch (Exception e) {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
		
	@ApiOperation(value = "CS문의 답변등록", notes = "CS문의 답변등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cs-listInfo-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaCsListInfoInsert(HttpServletRequest request) throws Exception{
		String	dateTime 		= "";
		String msgGb = "10"; // 긴급메시지
		String prg_id = "IF_PATMONAPI_02_005";
		String	rtnMsg 			= Constants.SAVE_SUCCESS;
		PaqnamVO paQna = null;
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		ParamMap	paramMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		
		log.info("===== CS문의 답변등록API Start =====");
		
		log.info("01.CS문의 답변등록 API 기본정보 세팅");
		dateTime = systemService.getSysdatetimeToString();
		int targetCount = 0;
		int procCount = 0;
		
		try {
			
			log.info("===== CS문의 답변등록 API Start =====");	
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate("", "", "2");
			String url = apiInfoMap.get("url").toString();
			
			paramMap.put("msgGb", msgGb);
			log.info("1. 티몬 CS 문의  답변등록 API 대상 검색");	
			List<PaqnamVO> paqnamomentList = paTmonCounselService.selectPaTmonAnsQna(paramMap);
				
			targetCount = paqnamomentList.size();
			for (int j = 0 ; j < paqnamomentList.size(); j++) {
			
				paQna = paqnamomentList.get(j);
				apiInfoMap.put("paCode", paQna.getPaCode());
					
				String paCounselNo[] =  paQna.getPaCounselNo().toString().split(":");				
				apiInfoMap.put("url", url.replace("{csInquirySeqNo}",paCounselNo[0]).replace("{csInquiryMessageGroupSeqNo}", paCounselNo[1]));
				
				if("4".equals(paQna.getToken()) || "6".equals(paQna.getToken())) {
					apiDataMap.put("expectedStartDate",retrieveDate.getString("FROM_DATE"));
					apiDataMap.put("expectedEndDate", retrieveDate.getString("TO_DATE"));
				}
				apiDataMap.put("possibilityYn", "Y");	// 처리가능 여부를 어떻게 넣을지 고민해야 함. 
				
				apiDataMap.put("message", paQna.getProcNote());
						
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
				
				if(!"null".equals(String.valueOf(map.get("error"))) && !String.valueOf(map.get("error")).contains("처리가능여부를")) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
					continue;
				} else if("false".equals(String.valueOf(map.get("success")))) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("message"));
					continue;
				} else {
						
					paQna.setModifyId(Constants.PA_TMON_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40");
					paQna.setTitle("");
					paQna.setProcId(Constants.PA_TMON_PROC_ID);
					paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paTmonCounselService.savePaTmonQnaTransTx(paQna);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("msg.cannot_save"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
						procCount++;
					}
				}
			}
			
		} catch (Exception e) {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
