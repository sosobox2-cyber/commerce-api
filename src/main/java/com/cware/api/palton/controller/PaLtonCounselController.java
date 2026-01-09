package com.cware.api.palton.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.palton.counsel.service.PaLtonCounselService;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/palton/counsel", description="상담")
@Controller("com.cware.api.palton.PaLtonCounselController")
@RequestMapping(value="/palton/counsel")
public class PaLtonCounselController extends AbstractController  {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "common.system.systemProcess")
    private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
 
	@Resource(name = "palton.counsel.paLtonCounselService")
	private PaLtonCounselService paLtonCounselService;
	
	@Autowired
	private PaLtonConnectUtil paLtonConnectUtil;
	
	@SuppressWarnings("unchecked")
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
			@ApiParam(name ="procId",  value="처리자ID",  defaultValue = "")				   @RequestParam(value = "procId", required=false, defaultValue = "PALTON") String procId ) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = "";
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_001";
		String startDate = "";
		String endDate = "";
		String msgGb = "00"; //일반상담 
		
		try {
			log.info("===== 롯데온 상품 QNA 조회 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("method", method);
        	paramMap.put("code", "200");
			paramMap.put("message", "OK");
			paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
			log.info("02.롯데온 상품 QNA 조회 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(조회기간 시작일(yyyyMMddHHmmss)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), -3, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), 0, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			}

			paramMap.put("url", apiInfo.get("API_URL"));
			bodyMap.put("trGrpCd", "SR");
			bodyMap.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
			bodyMap.put("regStrDttm", startDate);
			bodyMap.put("regEndDttm", endDate);
			bodyMap.put("rowsPerPage", 100);					// 한페이지에 100개씩 조회
			bodyMap.put("qnaStatCd", "NPROC");					// 미처리인 것들만 조회
			
			for(int q = 0; q < Constants.PA_LTON_CONTRACT_CNT; q++) {						// 반복문 으로 쓰는거 구린대 개선할 수 없을까 
				if(q == 0) {
					paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
				}
				else {
					paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
				}
				
				int locationNo = 0;
				boolean existsCounsel = true;
				
				while(existsCounsel) {
					rtnMsg = Constants.SAVE_FAIL;
					locationNo++;
					bodyMap.put("pageNo", locationNo);
					
					log.info("02.롯데온 상품 QNA 조회 API 조회 통신");
					map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {	
						List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) map.get("data");
						
						log.info("03.롯데온 상품 QNA 조회 API 저장");
						
						List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
						
						if(data.size() > 0) {
							for(int i = 0; i < data.size(); i++) {
								Paqnamoment paqnamoment = new Paqnamoment();
								paqnamoment.setPaCode(paramMap.getString("paCode"));
								paqnamoment.setPaGroupCode("08"); 
								paqnamoment.setPaCounselNo(data.get(i).get("pdQnaNo").toString());
								paqnamoment.setCounselDate(DateUtil.toTimestamp(data.get(i).get("regDttm").toString(),"yyyyMMddHHmmss"));
								paqnamoment.setTitle("롯데온 상품QNA ");
								paqnamoment.setQuestComment(data.get(i).get("qstCnts").toString());
								paqnamoment.setPaGoodsCode(data.get(i).get("spdNo").toString());
								paqnamoment.setPaGoodsDtCode(data.get(i).get("sitmNo").toString());
								paqnamoment.setMsgGb(msgGb);
								paqnamoment.setInsertId(procId);
								paqnamoment.setModifyId(procId);
								paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								paqnamoment.setCounselGb("01");
								
								switch(data.get(i).get("qstTypCd").toString()){
								case "DSGN_CLR" :
									//("사이즈/용량");
									paqnamoment.setToken("01");						// 구분하는 값을 넣기 위하여 토큰을 사용함 
									break;
								case "DP_INFO" :
									//("디자인/색상");
									paqnamoment.setToken("02");
									break;
								case "SZ_CAPA" :
									//("상품정보");
									paqnamoment.setToken("03");
									break;
								case "USE_EPN" :
									//("사용설명");
									paqnamoment.setToken("04");
									break;
								case "CMPN" :
									//("구성품");
									paqnamoment.setToken("05");
									break;
								default :
									//("기타");
									paqnamoment.setToken("06");
									break;
								}
								
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
					}else {
						paramMap.put("code", "500");
						paramMap.put("message", map.get("rsltMsg"));
						existsCounsel = false;
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품 QNA 조회 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품Q&A 답변 등록", notes = "상품Q&A 답변 등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/qna-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setQnaInsert(HttpServletRequest request) throws Exception{
		
		PaqnamVO paQna = null;
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = null;
		ArrayList<Map<String,Object>> ansInfo = null;
		Map<String,Object> ansInfo_detail = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		StringBuffer sb = new StringBuffer();
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_002";
		int targetCount = 0;
		int procCount = 0;
		String msg = "";
		
		try {
			log.info("===== 롯데온 상품 QNA 답변등록 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
        	paramMap.put("method", method);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	
			log.info("02.롯데온 상품 QNA 답변등록 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("url", apiInfo.get("API_URL"));
			
			List<PaqnamVO> ansList = paLtonCounselService.selectPaLtonAnsQna(paramMap);
			
			if(ansList.size() > 0){
				
				try {
					targetCount = ansList.size();
					
					for(int i=0; i<targetCount; i++){
						bodyMap = new ParamMap();
						ansInfo = new ArrayList<Map<String,Object>>();
						paQna = ansList.get(i);
						ansInfo_detail = new HashMap<String, Object>();
						
						ansInfo_detail.put("trGrpCd", "SR");
						ansInfo_detail.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
						ansInfo_detail.put("pdQnaNo", paQna.getPaCounselNo());
						ansInfo_detail.put("spdNo", paQna.getPaGoodsCode());
						ansInfo_detail.put("sitmNo", paQna.getPaGoodsDtCode());
						ansInfo_detail.put("ansCnts", paQna.getProcNote());
						
						if(paQna.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
							paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
							ansInfo_detail.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));
						}else {
							paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
							ansInfo_detail.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));
						}
						ansInfo.add(ansInfo_detail);
						bodyMap.put("ansInfo", ansInfo);
						
						log.info("03.롯데온 상품 QNA 답변등록 API 호출");
						map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); 
						
						if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {	
							List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) map.get("data");
							if(Constants.PA_LTON_SUCCESS_CODE.equals(data.get(0).get("resultCode"))) {
								
								paramMap.put("code", "200");
								paramMap.put("message", "OK");	
								
								paQna.setModifyId(Constants.PA_LTON_PROC_ID);
								paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paQna.setProcGb("40");
								paQna.setTitle("");
								paQna.setProcId(Constants.PA_LTON_PROC_ID);
								paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
	
								rtnMsg = paLtonCounselService.savePaLtonQnaTransTx(paQna);
								
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									paramMap.put("code","404");
									paramMap.put("message",getMessage("msg.cannot_save"));
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
									procCount++;
								}
							}else {
								 paramMap.put("code","500");
								 paramMap.put("message", map.get("rsltMsg"));
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
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? msg + e.getMessage().substring(0, 3950) : msg+ e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
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
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품 QNA 답변등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	//테스트 필요
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "판매자 연락목록 조회", notes = "판매자 연락목록 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value ="/seller-contact", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSellerContact(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate",   required=false, defaultValue = "") String toDate,
			@ApiParam(name ="procId",  value="처리자ID",  defaultValue = "")				   @RequestParam(value = "procId", required=false, defaultValue = "PALTON") String procId ) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = "";
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_003";
		String startDate = "";
		String endDate = "";
		String msgGb = "10"; //일반상담 
		
		try {
			log.info("===== 롯데온 판매자 연락목록 조회 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);	
			paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);	
        	paramMap.put("method", method);
        	paramMap.put("code", "200");
			paramMap.put("message", "OK");	
		
			log.info("02.롯데온 판매자 연락목록 조회 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(조회기간 시작일(yyyyMMddHHmmss)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), -3, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), 0, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			}

			paramMap.put("url", apiInfo.get("API_URL"));
			bodyMap.put("scStrtDt", startDate);
			bodyMap.put("scEndDt", endDate);
			bodyMap.put("rowsPerPage", 50);					// 한페이지에 100개씩 조회
			
			for(int q = 0; q < Constants.PA_LTON_CONTRACT_CNT; q++) {						
				if(q == 0) {
					paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
				}
				else {
					paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
				}
				
				int locationNo = 0;
				boolean existsCounsel = true;
				
				while(existsCounsel) {
					rtnMsg = Constants.SAVE_FAIL;
					locationNo++;
					bodyMap.put("pageNo", locationNo);
					
					log.info("02.롯데온 판매자 연락목록 조회 API 조회 통신");
					map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("rsltCd"))) {	
						List<HashMap<String, Object>> rsltList = (List<HashMap<String, Object>>) map.get("rsltList");
						
						log.info("03.롯데온 판매자 연락목록 조회 API 저장");
						
						List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
						
						if(rsltList.size() > 0) {
							for(int i = 0; i < rsltList.size(); i++) {
								if("RCV".equals(rsltList.get(i).get("sndDvsCd"))) continue; //스토아 답변일 경우 pass
								if(rsltList.get(i).get("ctctTtl").toString().contains("미수령신고 확인 요청")) continue; // 미수령신고 확인 요청일 경우 pass
								Paqnamoment paqnamoment = new Paqnamoment();
								paqnamoment.setPaCode(paramMap.getString("paCode"));
								paqnamoment.setPaGroupCode("08"); 
			 					paqnamoment.setPaCounselNo(rsltList.get(i).get("slrCtctGrpNo").toString());						
								paqnamoment.setCounselDate(DateUtil.toTimestamp(rsltList.get(i).get("sndDttm").toString(),"yyyyMMddHHmmss"));
								paqnamoment.setTitle(rsltList.get(i).get("ctctTtl").toString());
								paqnamoment.setQuestComment(rsltList.get(i).get("ctctCnts").toString());
								paqnamoment.setPaGoodsCode(rsltList.get(i).get("spdNo").toString());
								paqnamoment.setPaGoodsDtCode(rsltList.get(i).get("sitmNo").toString());
								paqnamoment.setPaOrderNo(rsltList.get(i).get("odNo") != null ? rsltList.get(i).get("odNo").toString() : "");
								paqnamoment.setMsgGb(msgGb);
								paqnamoment.setInsertId(procId);
								paqnamoment.setModifyId(procId);
								paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								paqnamoment.setCounselGb("07");
								
								paqnamomentList.add(paqnamoment);
							}
							
							if(paqnamomentList.size() >0) {
								log.info("04.상품Q&A 저장");
								rtnMsg = paCounselService.savePaQnaTx(paqnamomentList,msgGb);								
							}
							
							if(Constants.SAVE_SUCCESS.equals(rtnMsg) || paqnamomentList.size() == 0){
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
					} else if("3000".equals(map.get("rsltCd"))) {
						//조회된 내용이 없을떄
						paramMap.put("code", "404");
						paramMap.put("message", map.get("rsltMsg"));
						existsCounsel = false;
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", map.get("rsltMsg"));
						existsCounsel = false;
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 판매자 연락목록 조회 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	
	}
	
	//테스트 필요
	@ApiOperation(value = "판매자 연락답변 등록", notes = "판매자 연락답변 등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/update-seller-contact", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> updateSellerContact(HttpServletRequest request) throws Exception{
		
		PaqnamVO paQna = null;
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_004";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		StringBuffer sb = new StringBuffer();
		
		try {
			log.info("===== 롯데온판매자 연락답변 등록 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);	
        	paramMap.put("method", method);
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);	
        	
			log.info("02.롯데온 상품 QNA 답변등록 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("url", apiInfo.get("API_URL"));
			
			List<PaqnamVO> ansList = paLtonCounselService.updateSellerContact(paramMap);
			
			if(ansList.size() > 0){
				
				try {
					targetCount = ansList.size();
					
					for(int i=0; i<targetCount; i++){
						bodyMap = new ParamMap();
						paQna = ansList.get(i);
					
						bodyMap.put("slrCtctGrpNo", paQna.getPaCounselNo());						
						bodyMap.put("ctctTtl", "SK스토아 판매자연락 답변등록");
						bodyMap.put("ctctCnts", paQna.getProcNote());	
						
						if(paQna.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
							paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
						}else {
							paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
						}
						
						log.info("03.롯데온 상품 QNA 답변등록 API 호출");
						map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); 
						
						if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("rsltCd"))) {	

							paramMap.put("code", "200");
							paramMap.put("message", "OK");	
							
							paQna.setModifyId(Constants.PA_LTON_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40");
							paQna.setTitle("");
							paQna.setProcId(Constants.PA_LTON_PROC_ID);
							paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

							rtnMsg = paLtonCounselService.savePaLtonQnaTransTx(paQna);
							
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								paramMap.put("code","404");
								paramMap.put("message",getMessage("msg.cannot_save"));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
								procCount++;
							}
						}else {
							 paramMap.put("code","500");
							 paramMap.put("message", map.get("rsltMsg"));
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			} else {
				 paramMap.put("code","404");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
			}
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? msg + e.getMessage().substring(0, 3950) : msg+ e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
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
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품 QNA 답변등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "판매자 문의목록 조회", notes = "판매자 문의목록 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/seller-inquiry-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSellerInquiryList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate",   required=false, defaultValue = "") String toDate,
			@ApiParam(name ="procId",  value="처리자ID",  defaultValue = "")				   @RequestParam(value = "procId", required=false, defaultValue = "PALTON") String procId ) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = "";
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_005";
		String startDate = "";
		String endDate = "";
		String msgGb = "20"; // 판매자 문의 목록조회
		
		try {
			log.info("===== 롯데온 판매자 문의목록 조회 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("method", method);
        	paramMap.put("code", "200");
			paramMap.put("message", "OK");
			paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
			log.info("02.롯데온 판매자 문의목록 조회 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}	
			//예외(조회기간 시작일(yyyyMMddHHmmss)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), -3, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateTimeAsString(), 0, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			}

			paramMap.put("url", apiInfo.get("API_URL"));
			bodyMap.put("scStrtDt", startDate);
			bodyMap.put("scEndDt", endDate);
			bodyMap.put("rowsPerPage", 50);								// 한페이지에 50개씩 조회
			bodyMap.put("slrInqProcStatCd", "UNANS");					// 미처리인 것들만 조회
			
			for(int q = 0; q < Constants.PA_LTON_CONTRACT_CNT; q++) {					
				if(q == 0) {
					paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
				}
				else {
					paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
					bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
				}
				
				int locationNo = 0;
				boolean existsCounsel = true;
				
				while(existsCounsel) {
					rtnMsg = Constants.SAVE_FAIL;
					locationNo++;
					bodyMap.put("pageNo", locationNo);
					
					log.info("02.롯데온 판매자 문의목록 조회 API 조회 통신");
					map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("rsltCd"))) {	
						List<HashMap<String, Object>> rsltList = (List<HashMap<String, Object>>) map.get("rsltList");
						
						log.info("03.롯데온 판매자 문의목록 조회 API 저장");
						
						List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
						
						if(rsltList.size() > 0) {
							for(int i = 0; i < rsltList.size(); i++) {
								String commentUrl = "";
								String content = rsltList.get(i).get("inqCnts").toString();
								
								List<HashMap<String, String>> atflList = (List<HashMap<String, String>>) map.get("atflList");
								Paqnamoment paqnamoment = new Paqnamoment();
								paqnamoment.setPaCode(paramMap.getString("paCode"));
								paqnamoment.setPaGroupCode("08"); 
								paqnamoment.setPaCounselNo(rsltList.get(i).get("slrInqNo").toString());
								paqnamoment.setCounselDate(DateUtil.toTimestamp(rsltList.get(i).get("accpDttm").toString(),"yyyyMMddHHmmss"));			
								paqnamoment.setTitle(" "); 
								paqnamoment.setPaOrderNo(rsltList.get(i).get("odNo") != null ? rsltList.get(i).get("odNo").toString() : "");
								paqnamoment.setPaGoodsCode(rsltList.get(i).get("spdNo").toString());
								paqnamoment.setPaGoodsDtCode(rsltList.get(i).get("sitmNo").toString());
								paqnamoment.setMsgGb(msgGb);
								paqnamoment.setInsertId(procId);
								paqnamoment.setModifyId(procId);
								paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								paqnamoment.setCounselGb("05");
								
								atflList = (List<HashMap<String, String>>)rsltList.get(i).get("atflList");
								if(atflList != null) {
									commentUrl = "\n\n 첨부파일 ";
									for(int j = 0; j < atflList.size(); j++) {
										String atflUrl = atflList.get(j).get("atflUrl");
										commentUrl += "\n" + (j+1) + ") "+ atflUrl;
									}
								}
								if(content.length() > 1500) {
									content = ComUtil.subStringBytes(rsltList.get(i).get("inqCnts").toString(), 1500) + "...";
								}
								paqnamoment.setQuestComment(content + commentUrl);
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
					} else if("3000".equals(map.get("rsltCd"))) {
						//조회된 내용이 없을떄
						paramMap.put("code", "404");
						paramMap.put("message", map.get("rsltMsg"));
						existsCounsel = false;
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", map.get("rsltMsg"));
						existsCounsel = false;
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품 QNA 조회 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "판매자 문의답변 등록", notes = "판매자 문의답변 등록", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/update-seller-inquiry", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> updateSellerInquiry(HttpServletRequest request) throws Exception{
		
		PaqnamVO paQna = null;
		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String prg_id = "IF_PALTONAPI_02_006";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		StringBuffer sb = new StringBuffer();
		
		try {
			log.info("===== 롯데온 판매자 문의답변 등록 API Start =====");	
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
        	paramMap.put("method", method);
        	paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	
			log.info("02.롯데온 판매자 문의답변 등록 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("url", apiInfo.get("API_URL"));
			
			List<PaqnamVO> ansList = paLtonCounselService.updateSellerInquiry(paramMap);
			
			if(ansList.size() > 0){
				
				try {
					targetCount = ansList.size();
					
					for(int i=0; i<targetCount; i++){
						bodyMap = new ParamMap();
						paQna = ansList.get(i);
					
						bodyMap.put("slrInqNo", paQna.getPaCounselNo());
						bodyMap.put("ansCnts", paQna.getProcNote());	
						
						if(paQna.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
							paramMap.put("paCode", Constants.PA_LTON_BROAD_CODE);
						}else {
							paramMap.put("paCode", Constants.PA_LTON_ONLINE_CODE);
						}
						
						log.info("03.롯데온  판매자 문의답변 등록 API 호출");
						map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); 
						
						if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("rsltCd"))) {	

							paramMap.put("code", "200");
							paramMap.put("message", "OK");	
							
							paQna.setModifyId(Constants.PA_LTON_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40");
							paQna.setTitle("");
							paQna.setProcId(Constants.PA_LTON_PROC_ID);
							paQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

							rtnMsg = paLtonCounselService.savePaLtonQnaTransTx(paQna);
							
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								paramMap.put("code","404");
								paramMap.put("message",getMessage("msg.cannot_save"));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
								procCount++;
							}
						}else {
							 paramMap.put("code","500");
							 paramMap.put("message", map.get("rsltMsg"));
						}
					}
				} catch (Exception e) {
					sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : " + e.getMessage() + "|");
				}
			} else {
				 paramMap.put("code","404");
				 paramMap.put("message",getMessage("errors.no.select") + rtnMsg);
			}
		}catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + "|";
			
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? msg + e.getMessage().substring(0, 3950) : msg+ e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + " | ";
					if(targetCount == procCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온  판매자 문의답변 등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}