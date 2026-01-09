package com.cware.api.passg.controller;


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
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.passg.claim.service.PaSsgClaimService;
import com.cware.netshopping.passg.counsel.service.PaSsgCounselService;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/passg/counsel", description="상담")
@Controller("com.cware.api.passg.PaSsgCounselController")
@RequestMapping(value = "/passg/counsel")
public class PaSsgCounselController extends AbstractController  {
	
private transient static Logger log = LoggerFactory.getLogger(PaSsgCounselController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "passg.counsel.paSsgCounselService")
	private PaSsgCounselService paSsgCounselService;
	
	@Resource(name = "passg.claim.paSsgClaimService")
	private PaSsgClaimService paSsgClaimService;
	
	@Resource(name = "com.cware.api.passg.PaSsgAsycController")
	private PaSsgAsyncController paSsgAsyncController;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Autowired
	PaSsgConnectUtil paSsgConnectUtil;
	
	/**
	 * 상품Q&A 리스트 조회 API
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품Q&A 리스트 조회", notes = "상품Q&A 리스트 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/qna-list", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> qnaList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,			
			@ApiParam(name = "toDate",   required = false, value = "종료일", defaultValue = "") @RequestParam(value = "toDate",   required = false, defaultValue = "") String toDate,
			@ApiParam(name = "procId",   value = "처리자ID",  defaultValue = "")				  @RequestParam(value = "procId", required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId
			) throws Exception {
		
		Map<String, Object> map 				= new HashMap<String, Object>();
		List<Map<String, Object>> ssgDataList 	= null;
		String paCode							= "";
		String prg_id 							= "";
		String rtnMsg							= "";
		String msgGb							= "00";  //일반상담
		ParamMap apiInfoMap						= new ParamMap();
		ParamMap apiDataMap						= new ParamMap();
		Map<String, Object> postngReq	    	= new HashMap<String, Object>();
		String	dateTime 						= systemService.getSysdatetimeToString();
		
		try {
			prg_id = "IF_PASSGAPI_04_001";
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);

			fromDate = ComUtil.NVL(fromDate).length() == 12 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), -3, DateUtil.DEFAULT_JAVA_DATE_FORMAT).substring(0, 12); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 12 ? toDate   : DateUtil.getCurrentDateTimeAsString().substring(0, 12); // 조회종료일
			
			for(int i = 0; i < Constants.PA_SSG_CONTRACT_CNT ; i++) {
				paCode = ( i == 0 ) ? Constants.PA_SSG_BROAD_CODE : Constants.PA_SSG_ONLINE_CODE;
				ssgDataList = new ArrayList<Map<String, Object>>();
				apiInfoMap.put("paCode", paCode);
				rtnMsg += paCode + ":";
				
				postngReq.put("qnaStartDt", fromDate);
				postngReq.put("qnaEndDt", toDate);
				
				apiDataMap.put("postngReq", postngReq);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				Map<String,Object> result = (Map<String,Object>)map.get("result");
				
				if(!"00".equals(String.valueOf(result.get("resultCode")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += result.get("resultMessage") + "(" + result.get("resultDesc") + ")/";
					continue;
				}
				List<Map<String, Object>> qnaList = ((List<Map<String, Object>>)result.get("qnaList"));
				if("".equals(String.valueOf(qnaList.get(0)))) continue;
				
				if(qnaList.get(0).get("qna") instanceof Map<?, ?>) { // 목록이 하나일 경우 List가 아닌 Map 형식으로 넘겨주므로 형변환 오류 방지를 위함
					ssgDataList.add((Map<String, Object>) qnaList.get(0).get("qna"));
				} else {
					ssgDataList = (List<Map<String, Object>>)qnaList.get(0).get("qna");
				}
				
				if(ssgDataList.size() < 1) {
					apiInfoMap.put("code", "404");
					rtnMsg += getMessage("pa.not_exists_process_list") + "/";
					continue;
				}	
				
				for(Map<String, Object> m : ssgDataList) {
					try {
						PaqnamVO paqnamVo = new PaqnamVO();
						paqnamVo.setPaCode(paCode);
						paqnamVo.setPaGroupCode("10");
						paqnamVo.setPaCounselDtSeq("1");
						paqnamVo.setPaCounselNo(m.get("postngId").toString());
						paqnamVo.setCounselDate(DateUtil.toTimestamp(m.get("regDts").toString(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
						paqnamVo.setCustName(m.get("regpeId").toString());
						paqnamVo.setTitle(m.get("postngTitleNm").toString());
						paqnamVo.setProcNote(m.get("postngCntt").toString());
						paqnamVo.setProcId(procId);
						paqnamVo.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paqnamVo.setPaGoodsCode(m.get("itemId").toString());
						paqnamVo.setPaOrderNo(m.containsKey("ordNo") ? m.get("ordNo").toString().replace("-", "") : "");
						paqnamVo.setMsgGb(msgGb);
						paqnamVo.setInsertId(procId);
						paqnamVo.setModifyId(procId);
						paqnamVo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paqnamVo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						paqnamVo.setCounselGb("01"); //상품
						paqnamVo.setProcGb("10");
						paqnamVo.setTransYn("0");
						paqnamVo.setToken("");
						
						paSsgCounselService.savePaQnaTx(paqnamVo);
						
					}catch (Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "PaCounselNo:" + m.get("postngId").toString() + " :: " + e.getMessage() + "/";
					}
				}
				
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		qnaInputMain(request);
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품Q&A 답변 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품Q&A 답변", notes = "상품Q&A 답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/qna-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> qnaInsert(HttpServletRequest request) throws Exception {
		
		Map<String, Object> map 	   = new HashMap<String, Object>();
		List<PaqnamVO> paqnamomentList = new ArrayList<PaqnamVO>();
		String prg_id 				   = "";
		String rtnMsg				   = "";
		String msgGb 				   = "00"; //일반상담
		ParamMap apiInfoMap			   = new ParamMap();
		ParamMap apiDataMap			   = new ParamMap();
		ParamMap paramMap			   = new ParamMap();
		Map<String, Object> postngReq  = new HashMap<String, Object>();
		String	dateTime 			   = systemService.getSysdatetimeToString();
		
		try {
			
			//=1) API Parameter Setting
			prg_id = "IF_PASSGAPI_04_002";
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("msgGb", msgGb);
			
			paqnamomentList = paSsgCounselService.selectPaSsgAnsQna(paramMap);
			
			for(PaqnamVO paQna : paqnamomentList) {
				try {
					apiInfoMap.put("paCode", paQna.getPaCode());
					postngReq.put("postngId", paQna.getPaCounselNo());
					postngReq.put("postngCntt", paQna.getProcNote());
					apiDataMap.put("postngReq", postngReq);
						
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					Map<String, Object> result = (Map<String, Object>)map.get("result");
						
					paQna.setModifyId(Constants.PA_SSG_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40");
					paQna.setTitle("");
					paQna.setMsgGb(msgGb);
					paQna.setProcId(Constants.PA_SSG_PROC_ID);
					paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					if(!"00".equals(String.valueOf(result.get("resultCode")))) {
						if(result.get("resultDesc").toString().contains("존재하지 않는 문의")) {
							paSsgCounselService.savePaSsgQnaTransTx(paQna);
						}else {
							apiInfoMap.put("code", "500");
							rtnMsg += "postngId:" + result.get("postngId") + "::" + result.get("resultMessage") + "(" + result.get("resultDesc") + ")/";
						}
						continue;
					} else {
						rtnMsg = Constants.SAVE_SUCCESS;
					}
						
					paSsgCounselService.savePaSsgQnaTransTx(paQna);
					
				}catch (Exception e) {
					rtnMsg += "PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void qnaInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PASSG_CUSTCOUNSEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"	, prg_id);
		paramMap.put("siteGb"	, Constants.PA_SSG_PROC_ID);
		
		try {
			paSsgConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<PaqnamVO> paqnaList = paSsgCounselService.selectPaSsgQna();
			
			for(PaqnamVO paqnam : paqnaList){
				try {
					paSsgAsyncController.custCounselInputAsync(paqnam, request);
				} catch(Exception e) {
					log.error(prg_id + " - EE.고객상담 내역 생성 오류", e);
					continue;
				}
			}
		} catch(Exception e) {
			paSsgConnectUtil.checkException(paramMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, paramMap);
		}
	}
	
	
	
	/**
	 * 쪽지 목록 조회
	 * @param fromDate(yyyyMMdd)
	 * @param toDate(yyyyMMdd)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쪽지 목록 조회", notes = "쪽지 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/note-list", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> retrieveNotes(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,			
			@ApiParam(name = "toDate",   required = false, value = "종료일", defaultValue = "") @RequestParam(value = "toDate",   required = false, defaultValue = "") String toDate,
			@ApiParam(name = "procId",   value = "처리자ID",  defaultValue = "")				  @RequestParam(value = "procId", required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId
			) throws Exception {
		
		List<Map<String, Object>> ssgDataList = null;
		List<String> noteDtlList 			  = new ArrayList<String>();
		Map<String, Object> map 			  = new HashMap<String, Object>();
		String paCode		= "";
		String prg_id 		= "";
		String rtnMsg		= "";
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		int page = 0;
		
		try {
			prg_id = "IF_PASSGAPI_04_003";
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);

			ParamMap retrieveDate = paSsgConnectUtil.getRetrieveDate(fromDate, toDate);
			fromDate = retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			String url = apiInfoMap.get("url").toString();
			
			paCode = Constants.PA_SSG_BROAD_CODE;	// 쪽지 API는 상위KEY 로 통신해서 A1으로 통일
			ssgDataList = new ArrayList<Map<String, Object>>();
			apiInfoMap.put("paCode", paCode);
			apiInfoMap.put("paBroad", ConfigUtil.getString("PASSG_API_KEY"));
			boolean hasNext = true;
			page = 1;
			
			while(hasNext) {
				
				apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{page}", String.valueOf(page)));
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				Map<String, Object> result = (Map<String, Object>)map.get("result");
				
				if(!"00".equals(String.valueOf(result.get("resultCode")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += result.get("resultMessage") + "(" + result.get("resultDesc") + ")/";
					continue;
				}
				
				if(result.get("resultCount").toString().equals("0")) break; // 조회 후 목록 없을 시 break
				
				result = (Map<String, Object>) result.get("resultData");
				
				if(result.get("noteList") instanceof Map<?, ?>) { // 목록이 하나일 경우 List가 아닌 Map 형식으로 넘겨주므로 형변환 오류 방지를 위함
					ssgDataList.add((Map<String, Object>) result.get("noteList"));
				} else {
					ssgDataList = (List<Map<String, Object>>)result.get("noteList");
				}
				
				if(ssgDataList.size() < 1) {
					apiInfoMap.put("code", "404");
					rtnMsg += getMessage("pa.not_exists_process_list") + "/";
					continue;
				}	
				
				if(ssgDataList.size() > 0) {
					for(Map<String, Object> m : ssgDataList) {
						ParamMap chkParam = new ParamMap();
						
						if(m.get("lastUser").toString().contains("나")) continue;
						
						chkParam.put("paCounselNo", m.get("boNtId").toString());
						chkParam.put("paCounselDate", m.get("lstRegDts").toString());
						
						int resultCnt = paSsgCounselService.selectPaCounselNoCheck(chkParam); // 대화형식이기 때문에 NoSeq 를 Token 에 저장하여 마지막 답장 날짜와 같이 중복 체크
						if(resultCnt == 0) noteDtlList.add(chkParam.get("paCounselNo").toString());	// 상세 조회 필요한 건들 add
					}
					
					if(noteDtlList.size() > 0) {
						setQnaNoteListInfo(request, noteDtlList, paCode); // 쪽지목록 상세조회API
					}
					
					page++;
				} else {
					apiInfoMap.put("code","404");
					rtnMsg += getMessage("pa.not_exists_process_list");
					hasNext = false;
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		noteInputMain(request); // 쪽지 데이터 생성
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 쪽지 목록 상세 조회
	 * @param request
	 * @param noteDtlList
	 * @param paCode
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쪽지 목록 상세 조회", notes = "쪽지 목록 상세 조회", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/note-listInfo", method = RequestMethod.GET) 
	@ResponseBody
	private ResponseEntity<?> setQnaNoteListInfo(HttpServletRequest request, 
			@ApiParam(name="noteDtlList", required=true) @RequestParam(value="noteDtlList", required=true) List<String> noteDtlList, 
			@ApiParam(name="paCode",	  required=true) @RequestParam(value="paCode",		required=true) String paCode) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> ssgDataList = null;
		ParamMap apiInfoMap = new ParamMap();
		String dateTime = "";
		String rtnMsg   = "";
		String prg_id   = "IF_PASSGAPI_04_004";
		String msgGb    = "20"; // SSG 교환 상담을 10번으로 만들어서 옥션 쪽지 20 사용
		
		dateTime = systemService.getSysdatetimeToString();
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			apiInfoMap.put("paBroad", ConfigUtil.getString("PASSG_API_KEY"));
			apiInfoMap.put("paCode", paCode);
			
			for(String paCounselNo : noteDtlList) {
				ssgDataList = new ArrayList<Map<String, Object>>();
				apiInfoMap.put("url", url.replace("{boNtId}", paCounselNo));
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, null);
				Map<String, Object> result = (Map<String, Object>)map.get("result");
				
				if(!"00".equals(String.valueOf(result.get("resultCode")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += result.get("resultMessage") + "(" + result.get("resultDesc") + ")/";
					continue;
				}
				
				result = (Map<String, Object>) result.get("resultData");
				
				if(result.get("talkList") instanceof Map<?, ?>) { // 목록이 하나일 경우 List가 아닌 Map 형식으로 넘겨주므로 형변환 오류 방지를 위함
					ssgDataList.add((Map<String, Object>) result.get("talkList"));
				} else {
					ssgDataList = (List<Map<String, Object>>)result.get("talkList");
				}
								
				if(ssgDataList.size() > 0) {
					for(Map<String, Object> m : ssgDataList) {
						  PaqnamVO paqnamVo = new PaqnamVO(); 
						  paqnamVo.setPaCode(paCode);
						  paqnamVo.setPaGroupCode("10"); 
						  paqnamVo.setPaCounselDtSeq(m.get("ntSeq").toString());
						  paqnamVo.setPaCounselNo(m.get("boNtId").toString());
						  paqnamVo.setCounselDate(DateUtil.toTimestamp(m.get("regDts").toString(), "yyyy-MM-dd HH:mm:ss"));  // 형식 다르면 전혀 다른 날짜 리턴해줌
						  paqnamVo.setTitle("SSG " + m.get("ntTaskTypeCd").toString() + " 쪽지");
						  paqnamVo.setProcNote((m.get("isMeYn").toString().equals("N") ? "Q:" : "A:") +  m.get("ntCntt").toString()); // 상대 내용 : Q, SK스토아 답장 : A
						  paqnamVo.setProcId(Constants.PA_SSG_PROC_ID);
						  paqnamVo.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						  paqnamVo.setPaGoodsCode(m.containsKey("itemId") ? m.get("itemId").toString() : "");
						  paqnamVo.setPaOrderNo(m.containsKey("ordNo") ? m.get("ordNo").toString() : "");
						  paqnamVo.setMsgGb(msgGb); 
						  paqnamVo.setInsertId(Constants.PA_SSG_PROC_ID);
						  paqnamVo.setModifyId(Constants.PA_SSG_PROC_ID);
						  paqnamVo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						  paqnamVo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss")); 
						  paqnamVo.setCounselGb("07"); //긴급 건으로 처리
						  paqnamVo.setProcGb("10"); 
						  paqnamVo.setTransYn("0");
						  paqnamVo.setToken(m.get("ntSeq").toString());
						  
						  paSsgCounselService.savePaQnaTx(paqnamVo);
					}
				} else {
					apiInfoMap.put("code","404");
					rtnMsg += getMessage("pa.not_exists_process_list");
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
		
	}
	
	/**
	 * 쪽지 답장 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쪽지 답변", notes = "쪽지 답변", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/note-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> noteInsert(HttpServletRequest request) throws Exception {
		
		Map<String, Object> map 	   = new HashMap<String, Object>();
		Map<String, Object> postngReq  = new HashMap<String, Object>();
		List<PaqnamVO> noteAnswertList = new ArrayList<PaqnamVO>();
		
		String prg_id 				   = "";
		String rtnMsg				   = "";
		String msgGb 				   = "20";
		ParamMap apiInfoMap			   = new ParamMap();
		ParamMap apiDataMap			   = new ParamMap();
		ParamMap paramMap			   = new ParamMap();
		String	dateTime 			   = systemService.getSysdatetimeToString();
		
		try {
			prg_id = "IF_PASSGAPI_04_005";
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("msgGb", msgGb);
			
			noteAnswertList = paSsgCounselService.selectPaNoteAnswerList(paramMap);
			
			for(PaqnamVO paNote : noteAnswertList) {
				try {
					apiInfoMap.put("paCode", paNote.getPaCode());
					apiInfoMap.put("paBroad", ConfigUtil.getString("PASSG_API_KEY"));
					postngReq.put("boNtId", paNote.getPaCounselNo());
					postngReq.put("ntCntt", paNote.getProcNote());
					apiDataMap.put("note", postngReq);
					
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					Map<String, Object> result = (Map<String, Object>)map.get("result");
					
					paNote.setModifyId(Constants.PA_SSG_PROC_ID);
					paNote.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paNote.setProcGb("40");
					paNote.setTitle("");
					paNote.setMsgGb(msgGb);
					paNote.setProcId(Constants.PA_SSG_PROC_ID);
					paNote.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					if(!"00".equals(String.valueOf(result.get("resultCode")))) {
						if(result.get("resultDesc") != null && result.get("resultDesc").toString().contains("상담이 종료 되었습니다")) {
							paNote.setModifyId(Constants.PA_SSG_PROC_ID);
							paNote.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paNote.setProcGb("40");
							paNote.setTitle("");
							paNote.setMsgGb(msgGb);
							paNote.setProcId(Constants.PA_SSG_PROC_ID);
							paNote.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							rtnMsg = Constants.SAVE_SUCCESS;
						} else {
							apiInfoMap.put("code", "500");
							rtnMsg += "PA_COUNSEL_SEQ : " + paNote.getPaCounselSeq() + "::" + result.get("resultMessage") + "(" + result.get("resultDesc") + ")/";
							continue;
						}
					} else {
						rtnMsg = Constants.SAVE_SUCCESS;
					}
					
					paSsgCounselService.savePaSsgQnaTransTx(paNote);
				} catch(Exception e) {
					rtnMsg += "PA_COUNSEL_SEQ : " + paNote.getPaCounselSeq() + " : " + e.getMessage() + "/";
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void noteInputMain(HttpServletRequest request) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String prg_id = "PASSG_NOTECOUNSEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("siteGb" , Constants.PA_SSG_PROC_ID);
		
		hashMap.put("sequence_type", "COUNSEL_SEQ");
		
		try {
			paSsgConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<PaqnamVO> panoteList = paSsgCounselService.selectPaSsgNote();
			
			for(PaqnamVO panotem : panoteList){
				try {
					String counselSeq = "";
					counselSeq = (String)systemDAO.selectSequenceNo(hashMap);
					
					if (counselSeq.equals("")) {
						throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
					}
					panotem.setCounselSeq(counselSeq);
					
					paSsgAsyncController.noteCounselInputAsync(panotem, request);
				} catch(Exception e) {
					log.error(prg_id + " - EE.쪽지 내역 생성 오류", e);
					continue;
				}
			}
		} catch(Exception e) {
			paSsgConnectUtil.checkException(paramMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, paramMap);
		}
	}
}
