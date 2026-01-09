package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.pacopn.counsel.service.PaCopnCounselService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Api(value = "/pacopn/counsel", description = "쿠팡 문의")
@Controller("com.cware.api.pacopn.PaCopnCounselController")
@RequestMapping(value = "/pacopn/counsel")
public class PaCopnCounselController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "pacopn.counsel.paCopnCounselService")
	private PaCopnCounselService paCopnCounselService;
	
	/**
	 * 상품별고객문의조회 (흐름 및 히스토리 관리)
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품별고객문의조회", notes = "상품별고객문의조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 304, message = "데이터 처리에 실패 하였습니다."),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류가 발생하였습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate"  , required = false, value = "종료일자") @RequestParam(value = "toDate"  , required = false) String toDate) throws Exception{
	
		ParamMap paramMap = new ParamMap();
		
		String startDate = "";
		String endDate = "";
		String duplicateCheck = "";
		String prg_id = "IF_PACOPNAPI_02_001";
		
		int totalPage = 0;
		int currentPage = 1;
		
		try{
			endDate = ComUtil.NVL(toDate).length() == 8 ? toDate : DateUtil.getCurrentDateAsString();
			startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("procId", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("startDateQ", startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
			paramMap.put("endDateQ", endDate.substring(0, 4)   + "-" + endDate.substring(4, 6)   + "-" + endDate.substring(6, 8));
			
			log.info("[상품별고객문의조회] API 중복실행 검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				//변수초기화
				totalPage = 0;
				currentPage = 1;
				
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
				}
				
				procGoodsCounselList(paramMap, currentPage);
				
				if(Constants.PA_COPN_SUCCESS_OK.equals(paramMap.getString("code"))){
					totalPage = Integer.parseInt(paramMap.getString("totalPages"));
				} else if("404".equals(paramMap.getString("code"))){
					totalPage = 0;
				} else {
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
				
				if(totalPage >= 2){
					for(int i=currentPage++; i<totalPage; i++){
						procGoodsCounselList(paramMap, currentPage);
						currentPage++;
					}
				}
			}			
			
		} catch (Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			
			log.error(e.getMessage(), e);
		} finally {
			try{
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			} catch(Exception e) {
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			log.info("03.[상품별고객문의조회]저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 상품별고객문의조회 (API 호출 및 데이터 처리)
	 * @param paramMap
	 * @param page
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	private ParamMap procGoodsCounselList(ParamMap paramMap, int currentPage) throws Exception{
		JsonObject responseObj = null;
		JsonArray contentList = null;
		JsonObject content = null;
		
		HashMap<String, String> apiInfo = null;
		
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = null;
		
		String[] apiKeys = null;
		String msgGb = "00";
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int totalCount = 0;
		
		try{
			log.info("[상품별고객문의조회] API 정보조회(Repeat)");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(
					apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0]))
					.addParameter("vendorid"      , apiKeys[0])
					.addParameter("inquiryStartAt", paramMap.getString("startDateQ"))
					.addParameter("inquiryEndAt"  , paramMap.getString("endDateQ"))
					.addParameter("answeredType"  , "ALL")
					.addParameter("pageNum"       , Integer.toString(currentPage))
					.addParameter("pageSize"      , Constants.PA_COPN_COUNT_PER_PAGE));
				
			if("200".equals(responseObj.get("code").getAsString())){
				paramMap.put("totalPages", responseObj.get("data").getAsJsonObject().get("pagination").getAsJsonObject().get("totalPages").getAsString());
				contentList = responseObj.get("data").getAsJsonObject().get("content").getAsJsonArray();
				totalCount = contentList.size();
				
				if(totalCount > 0){
					for(int i=0; i<totalCount; i++){
						content = contentList.get(i).getAsJsonObject();
						// 20221108 LEEJY 상담내용이 없는 상담데이터는 스킵처리 한다.
						if(content.get("content").isJsonNull()) {
							continue;
						}
						String note = ComUtil.NVL(content.get("content").getAsString());
						int contentLength = note.getBytes("UTF-8").length;
						
						paqnamoment = new Paqnamoment();
						
						paqnamoment.setPaCode(paramMap.getString("paCode"));
						paqnamoment.setPaGroupCode("05");
						paqnamoment.setPaCounselNo(content.get("inquiryId").getAsString());
						if(!content.get("orderIds").isJsonNull() && !"[]".equals(content.get("orderIds").toString()) && !"".equals(content.get("orderIds").toString())){
							paqnamoment.setPaOrderNo(content.get("orderIds").getAsJsonArray().get(0).getAsString());
						}else{
							paqnamoment.setPaOrderNo("");
						}
						paqnamoment.setCounselDate(DateUtil.toTimestamp(content.get("inquiryAt").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
						paqnamoment.setPaGoodsCode(content.get("productId").getAsString());
						paqnamoment.setPaGoodsDtCode(content.get("vendorItemId").getAsString());
						paqnamoment.setTitle("쿠팡고객문의");
						if(contentLength > 200){
							paqnamoment.setQuestComment(ComUtil.subStringBytes(note, 185) + "...(내용잘림)");
						} else {
							paqnamoment.setQuestComment(ComUtil.text2db(content.get("content").getAsString()));
						}
						paqnamoment.setCounselGb("01");
						paqnamoment.setCustName("");
						paqnamoment.setCustTel("");
						paqnamoment.setMsgGb(msgGb); //일반상담 Q&A
						paqnamoment.setInsertId     (paramMap.getString("procId"));
						paqnamoment.setModifyId     (paramMap.getString("procId"));
						paqnamoment.setInsertDate   (DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
						paqnamoment.setModifyDate   (DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
						
						paqnamomentList.add(paqnamoment);
					}
					rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code", "200");
						paramMap.put("message", "OK");
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", rtnMsg);
					}
				} else {
					paramMap.put("code", "404");
					paramMap.put("message", getMessage("errors.no.select"));
				}
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", responseObj.get("message").getAsString());
			}
		} catch(Exception e) {
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			
			log.error(e.getMessage(), e);
			return paramMap;
		}
		return paramMap;
	}
	
	/**
	 * 쿠팡고객문의답변
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "쿠팡고객문의답변", notes = "쿠팡고객문의답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCounselReply(HttpServletRequest request){
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		List<PaqnamVO> qnaReplyList = null;
		PaqnamVO qnaReply           = null;
		
		StringBuffer buffer = new StringBuffer();
		String[] apiKeys = null;
		
		String duplicateCheck = "";
		String prg_id = "IF_PACOPNAPI_02_002";
		String rtnMsg   = Constants.SAVE_SUCCESS;
		String msg      = "";
		
		int totalCount   = 0;
		int successCount = 0;
		String msgGb	= "00"; //고객문의답변
		
		
		try{
			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("procId", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("msgGb", msgGb);
		
			log.info("02.[고객문의답변]API 중복실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			qnaReplyList = paCopnCounselService.selectPaCopnCounselReply(paramMap);
			
			totalCount = qnaReplyList.size();
			
			if(totalCount > 0){
				for(int i=0; i<totalCount; i++){
					qnaReply = qnaReplyList.get(i);
					if(qnaReply.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("replyBy", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("replyBy", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
					}
					
					apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
					
					requestObj = new JsonObject();
					requestObj.addProperty("content" , qnaReply.getProcNote());
					requestObj.addProperty("vendorId", apiKeys[0]);
					requestObj.addProperty("replyBy" , paramMap.getString("replyBy"));
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(
							apiInfo.get("API_URL").replaceAll("#vendorId#" , apiKeys[0])
							.replaceAll("#inquiryId#", qnaReply.getPaCounselNo())), "POST", new GsonBuilder().create().toJson(requestObj));
					
					if("200".equals(responseObj.get("code").getAsString()) ||
							("400".equals(responseObj.get("code").getAsString()) && "삭제된 상품문의에는 더 이상 답변할 수 없습니다.".equals(responseObj.get("message").getAsString()))
								){
						qnaReply.setModifyId  (Constants.PA_COPN_PROC_ID);
						qnaReply.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
						qnaReply.setProcGb    ("40");
						qnaReply.setTitle     (responseObj.get("message").getAsString());
						
						rtnMsg = paCopnCounselService.savePaCopnCounselReplyTx(qnaReply);
						
						if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
							successCount++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").getAsString());
						
						buffer.append((i == 0 ? "" : ", ") + "PA_COUNSEL_SEQ: " + qnaReply.getPaCounselSeq() + " MSG: " + paramMap.getString("message"));
					}
				}
			} else {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
				
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}			
			
		} catch(Exception e) {
			msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString() + " || ";
			paramMap.put("code", "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(e.getMessage(), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try{
				if("200".equals(paramMap.getString("code"))){
					msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString();
					paramMap.put("code"   , (totalCount == successCount) ? "200" : "500");
					paramMap.put("message", msg);
				}
				
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		log.info("03.[고객문의답변]저장 완료 API END");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 쿠팡 콜센터 문의조회 (흐름 및 히스토리 관리)
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "콜센터문의조회", notes="콜센터문의조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/urgent-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> emerNoticeList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate"  , required = false, value = "종료일자") @RequestParam(value = "toDate"  , required = false) String toDate) throws Exception{
		
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		
		String prg_id = "IF_PACOPNAPI_02_003";
		
		String startDate = "";
		String endDate   = "";
		String duplicateCheck  = "";
		
		int totalPage    = 0;
		int currentPage  = 1;
		
		try{
			endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
			startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("procId", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("startDateQ", startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
			paramMap.put("endDateQ"  , endDate.substring(0, 4)   + "-" + endDate.substring(4, 6)   + "-" + endDate.substring(6, 8));
			
			log.info("[콜센터문의조회] 02.API 중복실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				//변수초기화
				totalPage    = 0;
				currentPage  = 1;
				
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
				}
				
				for(int i=0; i<2; i++){
					paramMap.put("counselStatus", (i == 0) ? "NO_ANSWER" : "TRANSFER");
					currentPage = 1;
					
					procUrgentCounselList(paramMap, apiInfo, currentPage);
					
					if(Constants.PA_COPN_SUCCESS_OK.equals(paramMap.getString("code"))){
						totalPage = Integer.parseInt(paramMap.getString("totalPages"));
					} else if("404".equals(paramMap.getString("code"))){
						totalPage = 0;
					} else {
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					
					if(totalPage >= 2){
						for(int j=currentPage++; j<totalPage; j++){
							procUrgentCounselList(paramMap, apiInfo, currentPage);
							
							currentPage++;
						}
					}
				}
			}
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			
			log.error(e.getMessage(), e);
		}finally{
			try{
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 쿠팡 콜센터 문의조회 (API 호출 및 데이터 처리)
	 * @param paramMap
	 * @param apiInfo
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ParamMap procUrgentCounselList(ParamMap paramMap, HashMap<String, String> apiInfo, int currentPage) throws Exception{
		JsonObject responseObj          = null;
		List<JsonObject> contentList    = null;
		JsonObject content              = null;
		List<JsonObject> replyList      = null;
		JsonObject reply                = null;
		int totalCount = 0;

		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = null;

		Gson gson = new Gson();

		String[] apiKeys = null;

		String counselStatus = paramMap.getString("counselStatus");
		String rtnMsg        = Constants.SAVE_SUCCESS;
		String msgGb = "40"; //콜센터 문의 답변필요 : 40, 확인 필요 : 50

		try{
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");

			responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(
					apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0]))
			.addParameter("vendorId"      , apiKeys[0])
			.addParameter("inquiryStartAt", paramMap.getString("startDateQ"))
			.addParameter("inquiryEndAt"  , paramMap.getString("endDateQ"))
			.addParameter("partnerCounselingStatus", counselStatus)
			.addParameter("pageNum"       , Integer.toString(currentPage))
			.addParameter("pageSize"      , Constants.PA_COPN_COUNT_PER_PAGE));

			if("200".equals(responseObj.get("code").getAsString())){
				paramMap.put("totalPages", responseObj.get("data").getAsJsonObject().get("pagination").getAsJsonObject().get("totalPages").getAsString());
				contentList = (ArrayList<JsonObject>) gson.fromJson(responseObj.get("data").getAsJsonObject().get("content").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());

				totalCount = contentList.size();
				if(totalCount > 0){
					paqnamomentList = new ArrayList<Paqnamoment>();

					for(int i=0; i<contentList.size(); i++){
						content = contentList.get(i).getAsJsonObject();
						replyList = (ArrayList<JsonObject>) gson.fromJson(content.get("replies").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());

						if("requestAnswer".equals(content.get("csPartnerCounselingStatus").getAsString())){
							for(int j=0; j<replyList.size(); j++){
								reply = replyList.get(j).getAsJsonObject();
									if("csAgent".equals(reply.get("answerType").getAsString())){
										paqnamoment = new Paqnamoment();
	
										paqnamoment.setPaCode(paramMap.getString("paCode"));
										paqnamoment.setPaGroupCode("05");
										paqnamoment.setPaCounselNo(reply.get("answerId").getAsString());
										if(!content.get("orderId").isJsonNull() && !"[]".equals(content.get("orderId").toString()) && !"".equals(content.get("orderId").toString())){
											paqnamoment.setPaOrderNo(content.get("orderId").toString());
										}else{
											paqnamoment.setPaOrderNo("");
										}
										paqnamoment.setCounselDate(DateUtil.toTimestamp(content.get("inquiryAt").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
										//상품코드가 여러개일 경우 혼란을 야기할 수 있어 넣지 않는다.
										try{
											paqnamoment.setPaGoodsDtCode(content.get("vendorItemId").getAsString());	
										}catch (Exception e) {
											paqnamoment.setPaGoodsDtCode("");
										}
										paqnamoment.setQuestComment(ComUtil.text2db(reply.get("content").getAsString()));
										paqnamoment.setCounselGb("07");
										paqnamoment.setCustName("");
										paqnamoment.setCustTel("");
										if("NO_ANSWER".equals(counselStatus)){
											msgGb = "40";
											paqnamoment.setTitle("쿠팡 콜센터[답변필요]");
											paqnamoment.setMsgGb(msgGb); //콜센터 문의 '40'
										}else{
											msgGb = "50";
											paqnamoment.setTitle("쿠팡 콜센터[확인필요]");
											paqnamoment.setMsgGb(msgGb); //콜센터 문의 미확인건 '50'
										}
										paqnamoment.setToken(content.get("inquiryId").getAsString());
										paqnamoment.setInsertId     (paramMap.getString("procId"));
										paqnamoment.setModifyId     (paramMap.getString("procId"));
										paqnamoment.setInsertDate   (DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
										paqnamoment.setModifyDate   (DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
	
										paqnamomentList.add(paqnamoment);
									}
							}
						}else{
							continue;
						}
					}
					if(paqnamomentList.size() > 0){
						rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					}
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code", "200");
						paramMap.put("message", "OK");
					}else{
						paramMap.put("code", "500");
						paramMap.put("message", rtnMsg);
					}
				}else{
					paramMap.put("code", "404");
					paramMap.put("message", getMessage("errors.no.select"));
				}
			}else{
				paramMap.put("code"   , "500");
				paramMap.put("message", responseObj.get("message").getAsString());
			}
		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());

			log.error(e.getMessage(), e);
			return paramMap;
		}
		return paramMap;
	}

	@ApiOperation(value = "콜센터 문의답변", notes = "콜센터문의답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/urgent-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentCounselReply(HttpServletRequest request) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		
		JsonObject requestObj  		= null;
		JsonObject responseObj 		= null;
		List<PaqnamVO> qnaReplyList = null;
		PaqnamVO qnaReply           = null;
		
		StringBuffer buffer = new StringBuffer();
		String[] apiKeys = null;
		
		String duplicateCheck = "";
		String prg_id = "IF_PACOPNAPI_02_004";
		String rtnMsg   = Constants.SAVE_SUCCESS;
		String msg      = "";
		String msgGb	= "40";
		
		int totalCount   = 0;
		int successCount = 0;
		
		try{
			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("procId", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("msgGb", msgGb);
			
			log.info("02.[콜센터문의답변]API 중복실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			qnaReplyList = paCopnCounselService.selectPaCopnCounselReply(paramMap);
			
			totalCount = qnaReplyList.size();
			
			if(totalCount > 0){
				for(int i=0; i<totalCount; i++){
					qnaReply = qnaReplyList.get(i);
					if(qnaReply.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("replyBy", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("replyBy", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
					}
					
					//msg 2~1000자 사이값만 등록 가능
					String procNote = "";
					if(qnaReply.getProcNote().length() <= 2){
						procNote = qnaReply.getProcNote() + "..";
						qnaReply.setProcNote(procNote);
					}else if(qnaReply.getProcNote().length() > 999){
						procNote = qnaReply.getProcNote().substring(0, 995) + "...";
						qnaReply.setProcNote(procNote);
					}
					
					apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
					
					requestObj = new JsonObject();
					requestObj.addProperty("content" , qnaReply.getProcNote());
					requestObj.addProperty("vendorId", apiKeys[0]);
					requestObj.addProperty("inquiryId", qnaReply.getToken());
					requestObj.addProperty("replyBy" , paramMap.getString("replyBy"));
					requestObj.addProperty("parentAnswerId" , qnaReply.getPaCounselNo());
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(
							apiInfo.get("API_URL").replaceAll("#vendorId#" , apiKeys[0])
							.replaceAll("#inquiryId#", qnaReply.getToken())), "POST", new GsonBuilder().create().toJson(requestObj));
					
					if("200".equals(responseObj.get("code").getAsString())||
							("400".equals(responseObj.get("code").getAsString()) && 
									("The inquiry can't be answer. It can do only inquiryStatus:progress, partnerTransferStatus:requestAnswer".equals(responseObj.get("message").getAsString())
								  || "The inquiryId doesn't belongs to the vendorId, have no permisson to reply this inquiry".equals(responseObj.get("message").getAsString()))
							)
					){
						qnaReply.setModifyId  (Constants.PA_COPN_PROC_ID);
						qnaReply.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
						qnaReply.setProcGb    ("40");
						qnaReply.setTitle     (responseObj.get("message").getAsString());
						
						rtnMsg = paCopnCounselService.savePaCopnCounselReplyTx(qnaReply);
						
						if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
							successCount++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").getAsString());
						
						buffer.append((i == 0 ? "" : ", ") + "PA_COUNSEL_SEQ: " + qnaReply.getPaCounselSeq() + " MSG: " + paramMap.getString("message"));
					}
				}
			} else {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
				
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
		}catch(Exception e){
			msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString() + " || ";
			paramMap.put("code", "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(e.getMessage(), e);
			
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				if("200".equals(paramMap.getString("code"))){
					msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString();
					
					paramMap.put("code"   , (totalCount == successCount) ? "200" : "500");
					paramMap.put("message", msg);
				}
				
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		log.info("03.[콜센터문의답변] 저장 완료 API END");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "콜센터 문의확인", notes = "콜센터문의확인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/urgent-counsel-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> urgentCounselConfirm(HttpServletRequest request) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		
		List<PaqnamVO> confirmList = null;
		PaqnamVO confirm          = null;
		
		StringBuffer buffer = new StringBuffer();
		String[] apiKeys	= null;
		
		String duplicateCheck = "";
		String prg_id = "IF_PACOPNAPI_02_005";
		String rtnMsg   = Constants.SAVE_SUCCESS;
		String msg      = "";
		String msgGb	= "50";
		
		int totalCount   = 0;
		int successCount = 0;
		log.info("===== 01.[콜센터문의확인]API Start =====");
		try{
			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("procId", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("msgGb", msgGb);
			
			log.info("02.[콜센터문의확인]API 중복실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			confirmList = paCopnCounselService.selectPaCopnCounselReply(paramMap);
			
			totalCount = confirmList.size();
			
			if(totalCount > 0){
				for(int i=0; i<totalCount; i++){
					confirm = confirmList.get(i);
					if(confirm.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("confirmBy", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
					}else{
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("confirmBy", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
					}
					
					apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
					
					requestObj = new JsonObject();
					requestObj.addProperty("confirmBy", paramMap.getString("confirmBy"));
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(
							apiInfo.get("API_URL")
							.replaceAll("#vendorId#" , apiKeys[0])
							.replaceAll("#inquiryId#", confirm.getToken())), "POST", new GsonBuilder().create().toJson(requestObj));
					
					if("200".equals(responseObj.get("code").getAsString())||
							("400".equals(responseObj.get("code").getAsString()) && 
									("The inquiry can't be confirm. It can do only inquiryStatus:complete, partnerTransferStatus:requestAnswer".equals(responseObj.get("message").getAsString())
								|| responseObj.get("message").getAsString().contains("상담이 종료된 문의입니다")))
								){
						confirm.setModifyId	 (Constants.PA_COPN_PROC_ID);
						confirm.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), DateUtil.CWARE_JAVA_DATETIME_FORMAT));
						confirm.setProcGb	 ("40");
						confirm.setTitle	 (responseObj.get("message").getAsString());
						
						rtnMsg = paCopnCounselService.savePaCopnCounselReplyTx(confirm);
						
						if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
							paramMap.put("code", "200");
							paramMap.put("message", "OK");
							successCount++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").getAsString());
						
						buffer.append((i == 0 ? "" : ", ") + "EMERNOTI_NO: " + confirm.getToken() + " MSG: " + paramMap.getString("message"));
					}
				}
			}else{
				paramMap.put("code"   , "404");
				paramMap.put("message", getMessage("errors.no.select"));
				
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString() + " || ";
			paramMap.put("code"   , "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(e.getMessage(), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				if("200".equals(paramMap.getString("code"))){
					msg = "전체: " + totalCount + " | 성공: " + successCount + " | 실패: " + buffer.toString() + " || ";
					
					paramMap.put("code"   , (totalCount == successCount) ? "200": "500");
					paramMap.put("message", msg);
				}
				
				systemService.insertApiTrackingTx(request, paramMap);
				log.info("===== 03.[콜센터문의확인]API End =====");
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}