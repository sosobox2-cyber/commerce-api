package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.counsel.model.GetNoticeResponse;
import com.cware.netshopping.pawemp.counsel.model.QuestionData;
import com.cware.netshopping.pawemp.counsel.model.SetAnswerRequest;
import com.cware.netshopping.pawemp.counsel.service.PaWempCounselService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
@ApiIgnore
@Api(value="/pawemp/counsel", description="상담")
@Controller("com.cware.api.pawemp.PaWempCounselController")
@RequestMapping(value="/pawemp/counsel")
public class PaWempCounselController extends AbstractController {
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pawemp.counsel.paWempCounselService")
	private PaWempCounselService paWempCounselService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;
	
	/**
	 * 상품Q&A 조회 IF_PAWEMPAPI_02_001
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A 조회", notes = "상품Q&A 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselList(HttpServletRequest request, 
			@ApiParam(name="basicDate", required=false, value="등록일시", defaultValue = "") @RequestParam(value="basicDate", required=false, defaultValue = "") String basicDate ) throws Exception{
	    /** 
	     *  basicDate
	     *  등록 일시 
	     *  (YYYY-MM-DD 형식으로 입력하셔야 합니다. ex:2019-05-30)
	     */
		ParamMap paramMap      = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String rtnMsg          = Constants.SAVE_SUCCESS;
		String apiCode = "IF_PAWEMPAPI_02_001";
		String queryParams;
		
		List<Paqnamoment> paqnamomentList = null;
		
		log.info("===== 상품Q&A 조회 API Start=====");
		
		try{
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			String beforeBasicDate = "";
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("startDate", dateTime);
			
			log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			//날짜입력 없을 경우 셋팅 
			if(StringUtils.isBlank(basicDate) || basicDate.length() != 10){
				beforeBasicDate = DateUtil.toString(DateUtil.addHour(new Date(), -6), DateUtil.WEMP_DATE_FORMAT);
				basicDate = DateUtil.toDateString(DateUtil.WEMP_DATE_FORMAT);
				if(beforeBasicDate.equals(basicDate)){ // 현재시간에서 -6시간 후 날짜(yyyy-MM-dd) 와 현재 날짜가 같을 경우 D-1 API 조회 패스
					beforeBasicDate = "";  // D-1조회일자 초기화
				}
			}
			else{ // 날짜 데이터 있을 경우 D-1, D-day 조회
				beforeBasicDate = DateUtil.toString(DateUtil.addDay(DateUtil.getDate(basicDate, DateUtil.WEMP_DATE_FORMAT), -1), DateUtil.WEMP_DATE_FORMAT);
			}
			
			log.info("04.상품Q&A 조회 API 호출");
			Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
			// D-1, D-Day API 조회
			for(int j=0; j<Constants.PA_WEMP_CONTRACT_CNT; j++) {
				if(j==0) {
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					paramMap.put("paName", Constants.PA_ONLINE);
				}
				else {
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					paramMap.put("paName", Constants.PA_BROAD);
				}
				
				paqnamomentList = new ArrayList<Paqnamoment>();
				
				for(int i=0; i < 2; i++){
					String tempBasicDate = (i == 0) ? beforeBasicDate : basicDate;
					if(StringUtils.isBlank(tempBasicDate)){ // D-1일 데이터(beforeBasicDate)가 없으면 API 조회 패스 
						continue;
					}
					queryParams = "basicDate=" + tempBasicDate;
					try {
						List<QuestionData> responseList = paWempApiService.callWApiList(apiInfo, "GET", QuestionData[].class, queryParams, paramMap.getString("paName"));
						
						if(!CollectionUtils.isEmpty(responseList)){ // D-1, D-Day 데이터 합치는 작업(merge)
							for(QuestionData qData : responseList){
								Paqnamoment paqnamoment = new Paqnamoment();
								paqnamoment.setPaCode(paramMap.getString("paCode"));
								paqnamoment.setPaGroupCode("06");
								paqnamoment.setMsgGb("00");
								paqnamoment.setPaCounselNo(qData.getQnaSeq()+"");
								paqnamoment.setCounselDate(DateUtil.toTimestamp(qData.getQuestionDate(), "yyyy-MM-dd HH:mm:ss"));
								paqnamoment.setTitle("상품명:"+qData.getProductName());
								paqnamoment.setQuestComment(qData.getQuestion());
								paqnamoment.setCounselGb("01");
								paqnamoment.setPaGoodsCode(qData.getProductNo()+"");
								paqnamoment.setPaOrderNo(qData.getOrderNo());
								paqnamoment.setOrderYn(StringUtils.isBlank(qData.getOrderNo()) ? "0" : "1");
								paqnamoment.setDisplayYn(qData.getPrivateYn().equals("Y") ? "0" : "1");
								paqnamoment.setInsertId(Constants.PA_WEMP_PROC_ID);
								paqnamoment.setModifyId(Constants.PA_WEMP_PROC_ID);
								paqnamoment.setInsertDate(sysdateTime);
								paqnamoment.setModifyDate(sysdateTime);
								
		        				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        				Date date = new Date(paqnamoment.getCounselDate().getTime());
		        				String afterDate = dateFormat.format(date);
		        				List<String> beforeDateList = paWempCounselService.selectCounselDate(paqnamoment);
		        				boolean isDuple = false;
		        				
		        				for(String beforeDate : beforeDateList) {
		        					if (afterDate.equals(beforeDate)) {
		        						isDuple = true;
		        					}
		        				}
		        				
								log.info("위메프 2차 문의 모니터링 :" + qData.getQnaSeq()+"");
								log.info((DateUtil.toTimestamp(qData.getQuestionDate(), "yyyy-MM-dd HH:mm:ss")).toString());
								
								if(!isDuple) {									
									paqnamomentList.add(paqnamoment);
								}
								
							}
							
							rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, "00");
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
						}
					} catch (Exception ex) {
						paramMap.put("code", "400");
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", apiCode);
			}
			log.info("===== 상품Q&A 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 상품Q&A답변등록 IF_PAWEMPAPI_02_002
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A답변처리", notes = "상품Q&A답변처리", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselProc(HttpServletRequest request) throws Exception{
	    
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String apiCode = "IF_PAWEMPAPI_02_002";
		StringBuffer sb = new StringBuffer();
		String duplicateCheck = "";
		String msg = "";
		int targetCount = 0;
		int procCount = 0;
		String rtnMsg = Constants.SAVE_SUCCESS;
		paramMap.put("code","200");
		
		log.info("===== 상품Q&A답변등록 API Start=====");
		
		try{
			
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("startDate", dateTime);
			
	        log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("04.상품Q&A답변처리 대상 조회");
			List<PaqnamVO> ansList = paWempCounselService.selectPaWempAnsQna();
			
			if(ansList.size() > 0){
				targetCount = ansList.size();
				
				for(PaqnamVO paQna : ansList){
					
					if(paQna.getPaCode().equals(Constants.PA_WEMP_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
					}
					
					// api 파라미터 데이터 조합
					SetAnswerRequest answer = new SetAnswerRequest();
					answer.setQnaSeq(Long.parseLong(paQna.getPaCounselNo()));
					
					//공지사항 완료 처리
					if (paQna.getMsgGb().equals("10")) {
						paQna.setModifyId(Constants.PA_WEMP_PROC_ID);
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40");
						paQna.setTitle("");
						rtnMsg = paWempCounselService.savePaWempQnaTransTx(paQna);
						if (rtnMsg == Constants.SAVE_SUCCESS) {
							procCount++;
						}
						continue;
					} else {
						if (paQna.getProcNote().length() < 10) {
							answer.setAnswer(paQna.getProcNote() + ".........."); // 답변내용(최소10자,최대1500자)
						} else {
							answer.setAnswer(paQna.getProcNote()); // 답변 내용 (최소10자, 최대1500자)
						}
						log.info("05.상품Q&A 답변 API 호출");
						try {
							ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", answer, ReturnData.class,paramMap.getString("paName"));

							if (returnData != null) {
								paramMap.put("code", "200");
								paramMap.put("message", "OK");

								paQna.setModifyId(Constants.PA_WEMP_PROC_ID);
								paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paQna.setProcGb("40"); // = 완료
								paQna.setTitle("");

								rtnMsg = paWempCounselService.savePaWempQnaTransTx(paQna);
								if (rtnMsg == Constants.SAVE_SUCCESS) {
									procCount++;
								}
							} else {
								// API 연결 실패
								sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : 처리실패" + "|");
							}
						} catch (WmpApiException e) {
							if(StringUtils.contains(e.getMessage(), "존재하지")){
								paramMap.put("code", "200");
								paramMap.put("message", "OK");

								paQna.setModifyId(Constants.PA_WEMP_PROC_ID);
								paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paQna.setProcGb("40"); // = 완료
								paQna.setTitle("삭제된 게시물");

								rtnMsg = paWempCounselService.savePaWempQnaTransTx(paQna);
								if (rtnMsg == Constants.SAVE_SUCCESS) {
									procCount++;
								}
							} else {
								sb.append("PA_COUNSEL_SEQ : " + paQna.getPaCounselSeq() + " : 처리실패" + "|");
							}
						}
					}
				}
			 } else {
				 paramMap.put("code","404");
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
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount + " | ";
					if(targetCount != procCount){
						paramMap.put("code", "500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", apiCode);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("===== 상품Q&A답변처리 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 파트너 공지사항 조회 IF_PAWEMPAPI_02_003
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "공지사항 조회", notes = "공지사항 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/notice-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noticeList(HttpServletRequest request, 
			@ApiParam(name="basicDate",   	required=false, value="등록일시",  defaultValue = "") @RequestParam(value="basicDate", required=false, defaultValue = "") String basicDate ) throws Exception{
//		조회 기준 일시 (기준일시로부터 일주일간의 공지 리스트를 	조회합니다. 입력하지 않는 경우 오늘을 기준으로 합니다. 
//		YYYY-MM-DD 형식으로 입력하셔야 합니다. ex:2018-03-30)
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String queryParams = "";
		String apiCode = "IF_PAWEMPAPI_02_003";
		String msgGb = "10";
		int index = 0;
		
		List<Paqnamoment> paqnamomentList = null;
		
		log.info("===== 파트너 공지사항 조회 API Start =====");
		
		try {
			log.info("01.API 기본정보 세팅");
			// = connectionSetting 설정
			String dateTime = systemService.getSysdatetimeToString();

			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("startDate", dateTime);

			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			// 기간입력
			if (StringUtils.isNotBlank(basicDate) && basicDate.length() == 10) {
				queryParams = "basicDate=" + basicDate;
			}

			for (int j = 0; j < Constants.PA_WEMP_CONTRACT_CNT; j++) {
				if (j == 0) {
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					paramMap.put("paName", Constants.PA_ONLINE);
				} else {
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					paramMap.put("paName", Constants.PA_BROAD);
				}

				log.info("04.파트너 공지사항 조회 API 호출");
				try {
					List<GetNoticeResponse> responseList = paWempApiService.callWApiList(apiInfo, "GET", GetNoticeResponse[].class, queryParams,paramMap.getString("paName"));

					if (responseList != null) {
						if (responseList.size() > 0) { // 조회 데이터 있을 경우
							if (paqnamomentList == null) {
								paqnamomentList = new ArrayList<Paqnamoment>();
							}
							Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
							for (GetNoticeResponse nData : responseList) {
								if (nData.getType().equals("개별공지")) {
									Paqnamoment paqnamoment = new Paqnamoment();
									paqnamoment.setPaCode(paramMap.getString("paCode"));
									paqnamoment.setPaGroupCode("06");
									paqnamoment.setMsgGb(msgGb);
									paqnamoment.setPaCounselNo(nData.getRegDate().replace("-", "") + index);
									paqnamoment.setCounselDate(DateUtil.toTimestamp(nData.getRegDate(), "yyyy-MM-dd"));
									paqnamoment.setTitle(nData.getTitle());
									paqnamoment.setQuestComment(nData.getContents());
									paqnamoment.setCounselGb("07");
									paqnamoment.setOrderYn("0");
									paqnamoment.setDisplayYn("0");
									paqnamoment.setInsertId(Constants.PA_WEMP_PROC_ID);
									paqnamoment.setModifyId(Constants.PA_WEMP_PROC_ID);
									paqnamoment.setInsertDate(sysdateTime);
									paqnamoment.setModifyDate(sysdateTime);

									paqnamomentList.add(paqnamoment);
									index++;
								}
							}
						} else {
							paramMap.put("code", "404");
							paramMap.put("message", getMessage("pa.not_exists_process_list"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}
				} catch (Exception ex) {
					paramMap.put("code", "400");
				}
			}
			
			if(paqnamomentList.size() != 0){
				rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
					paramMap.put("code","200");
					paramMap.put("message","OK");
				} else {
					paramMap.put("code","500");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}			
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", apiCode);
			}
			log.info("===== 파트너 공지사항 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
}