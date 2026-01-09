package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.pawemp.claim.model.Claim;
import com.cware.netshopping.pawemp.claim.model.GetClaimListRequest;
import com.cware.netshopping.pawemp.claim.model.GetClaimListResponse;
import com.cware.netshopping.pawemp.claim.model.SetClaimApprove;
import com.cware.netshopping.pawemp.claim.model.SetClaimPickup;
import com.cware.netshopping.pawemp.claim.model.SetClaimPickupComplete;
import com.cware.netshopping.pawemp.claim.service.PaWempClaimService;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
@ApiIgnore
@Api(value="/pawemp/return", description="반품")
@Controller("com.cware.api.pawemp.PaWempReturnController")
@RequestMapping(value="/pawemp/return")
public class PaWempClaimController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
	public SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	public PaWempApiService paWempApiService;
	
	@Resource(name = "com.cware.api.pawemp.PaWempAsyncController")
	private PaWempAsyncController asyncController;
	
	@Resource(name = "pawemp.claim.paWempClaimService")
	public PaWempClaimService paWempClaimService;
	/**
	 * 반품신청 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품신청 목록조회", notes = "반품신청 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")
			   })
	@RequestMapping(value = "/return-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck = "";
		String apiCode = "IF_PAWEMPAPI_03_031";
		
		
		log.info("===== 위메프 반품신청목록조회 API Start=====");
		String startDate = paWempApiService.makeDateTimeStart(fromDate);
		String endDate   = paWempApiService.makeDateTimeEnd(toDate);
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int count=0 ; count<Constants.PA_WEMP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
				}
				
				log.info("04.반품신청목록 조회 API 호출 (paCode " + paramMap.getString("paCode") + ")");
					
				for(int i = 0; i < 2; i++) {
					String claimStatus = "";
					if(i == 0) {
						claimStatus = "REQUEST";
					} else {
						claimStatus = "APPROVE";
					}
				
					GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
					getClaimListRequest.setFromDate(startDate);
					getClaimListRequest.setToDate(endDate);
					getClaimListRequest.setClaimType("RETURN");
					//getClaimListRequest.setClaimStatus("REQUEST");
					getClaimListRequest.setClaimStatus(claimStatus);
					GetClaimListResponse list = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
					if(list.getClaim().isEmpty()){
						paramMap.put("code"   , "404");
					}else{
						PaWempClaimList wempClaim = null;
						for(Claim claimObject : list.getClaim()){
							wempClaim = paWempClaimService.makePaWempClaimList(claimObject, "30", paramMap.getString("paCode")); //PA_ORDER_GB 30:반품
							
							//위메프 반품생성
							paWempClaimService.savePaWempClaimTx(wempClaim);
							paramMap.put("code","200");
							paramMap.put("message", "OK");
						}
					}
				}
			}
			
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 반품신청목록조회 API End =====");
			//반품생성 호출
			returnInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}

	@ApiOperation(value = "반품철회 목록조회", notes = "반품철회 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")
			   })
	@RequestMapping(value = "/return-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnWithdrawList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck = "";
		String apiCode = "IF_PAWEMPAPI_03_032";
		
		
		log.info("===== 위메프 반품철회목록조회 API Start=====");
		String startDate = paWempApiService.makeDateTimeStart(fromDate);
		String endDate   = paWempApiService.makeDateTimeEnd(toDate);
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int count=0 ; count<Constants.PA_WEMP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
				}
				
				log.info("04.반품철회목록조회 조회 API 호출 (paCode " + paramMap.getString("paCode") + ")");
				GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
				getClaimListRequest.setFromDate(startDate);
				getClaimListRequest.setToDate(endDate);
				getClaimListRequest.setClaimType("RETURN");
				getClaimListRequest.setClaimStatus("WITHDRAW");
				getClaimListRequest.setSearchDateType("WITHDRAW");
				GetClaimListResponse list = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
				if(list.getClaim().isEmpty()){
					paramMap.put("code"   , "404");
				}else{
					PaWempClaimList wempClaim = null;
					for(Claim claimObject : list.getClaim()){
						wempClaim = paWempClaimService.makePaWempClaimList(claimObject, "31", paramMap.getString("paCode")); //PA_ORDER_GB 30:반품

						//위메프 반품철회생성
						paWempClaimService.savePaWempReturnCancelTx(wempClaim);
					}
					paramMap.put("code","200");
					paramMap.put("message", "OK");
				}
			}
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 반품철회목록조회 API End =====");
			//반품철회데이터생성
			returnCancelMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	//한 주문에 동일 상품의 단품 001 / 002 주문 시 묶음배송 두개 동시 반품 접수 시 묶음 반품 접수. 부분 반품 회수 처리 불가.
	//첫 번째 상품 반품 완료 시 반품수거요청 되며 위메프 상태는 두 상품 모두 반품수거요청처리 됨.
	//만약 두번째 상품이 반품 완료 안되었을 경우 반품수거 요청처리 상태로 대기. TSDCHECK를 통해 나가고 있지 않은 상품이 있는지 체크 예정.
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품수거요청 전송", notes = "반품수거요청 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/return-pickup-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnPickupProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_034";
		
		List<Object> pickupList        = null;
		HashMap<String, Object> pickup = null;
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		log.info("===== 위메프 반품수거요청 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			pickupList = paWempClaimService.selectPickupList();
			totalCnt   = pickupList.size();
			ReturnData returnData = null;
			SetClaimPickup setClaimPickup = null;
			if(totalCnt>0){
				for(int i=0; i<pickupList.size(); i++){
					
					pickup = (HashMap<String, Object>) pickupList.get(i);
					setClaimPickup = new SetClaimPickup();
					setClaimPickup.setClaimBundleNo(Long.parseLong(pickup.get("PA_CLAIM_NO").toString()));
					setClaimPickup.setClaimType("RETURN");
					if("DIRECT".equals(pickup.get("DELIVERY_COMPANY_CODE").toString())){
						setClaimPickup.setShipMethod("DIRECT");
						setClaimPickup.setInvoiceNo(pickup.get("SUB_INVOICE_NO").toString());
						setClaimPickup.setScheduleShipDate(pickup.get("DELY_HOPE_DATE").toString());
					}else{
						setClaimPickup.setShipMethod("PARCEL");
						setClaimPickup.setParcelCompanyCode(pickup.get("DELIVERY_COMPANY_CODE").toString());
						if(pickup.get("INVOICE_NO") == null || "".equals(pickup.get("INVOICE_NO").toString()) || !ComUtil.isNumber(pickup.get("INVOICE_NO").toString())){
							setClaimPickup.setInvoiceNo(pickup.get("SUB_INVOICE_NO").toString());
						}else{
							setClaimPickup.setInvoiceNo(pickup.get("INVOICE_NO").toString());
						}
					}
					
					if(pickup.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE)) {
						paramMap.put("paName", Constants.PA_BROAD);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
					}
					
					try{
						returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimPickup, ReturnData.class, paramMap.getString("paName"));
						if(returnData.getReturnKey() ==1 || StringUtils.contains(returnData.getReturnMsg(), "수거요청대기") //수거요청대기 상태에서만 가능합니다.(이미 수거요청 한 건에 대해서 체크)
								|| StringUtils.contains(returnData.getReturnMsg(), "신청단계만")){ //클레임 신청단계만 가능합니다.(이미 반품완료 건에 대해서 체크)
							executedRtn = paWempClaimService.updatePickupResultTx(pickup);
							if(executedRtn > 0){
								successCnt++;
							}
						} else if (StringUtils.contains(returnData.getReturnMsg(), "동일합니다")) {//수거 송장이 원송장 정보와 동일합니다. 송장번호 확인해주시기 바랍니다.(송장번호 동일한거 넣었을 경우 DIRECT처리.)
							
							setClaimPickup = new SetClaimPickup();
							setClaimPickup.setClaimBundleNo(Long.parseLong(pickup.get("PA_CLAIM_NO").toString()));
							setClaimPickup.setClaimType("RETURN");
							setClaimPickup.setShipMethod("DIRECT");
							setClaimPickup.setInvoiceNo(pickup.get("SUB_INVOICE_NO").toString());
							setClaimPickup.setScheduleShipDate(pickup.get("DELY_HOPE_DATE").toString());
							
							returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimPickup, ReturnData.class, paramMap.getString("paName"));
							
							if(returnData.getReturnKey() ==1 || StringUtils.contains(returnData.getReturnMsg(), "수거요청대기")){
								executedRtn = paWempClaimService.updatePickupResultTx(pickup);
								if(executedRtn > 0){
									successCnt++;
								}
							} else {
								paramMap.put("code"   , "500");
								paramMap.put("message", "PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
								log.error("PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
							}
						} else{
							paramMap.put("code"   , "500");
							paramMap.put("message", "PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
							log.error("PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
						}
					}catch(WmpApiException e){
						String errMsg = e.getMessage();
						String[] msg = errMsg.split("error:");
						if(msg.length > 1){
							errMsg = msg[1].replaceAll("\"", "");
						}
						paramMap.put("code","500");
						paramMap.put("message", errMsg);
					}
				}
			}
			
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system") + " : " + paramMap.getString("message"));
				}
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 반품수거요청 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}

	//묶음 반품 접수 시 모든 상품이 수거 되었을 경우에만 반품 수거 완료 전송.
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품수거완료 전송", notes = "반품수거완료 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/pickup-complete-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> pickupCompleteProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_036";
		String paName = "";
		
		List<Object> returnApprovalList        = null;
		HashMap<String, Object> returnApproval = null;
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		log.info("===== 위메프 반품수거완료 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			returnApprovalList = paWempClaimService.selectReceiveConfirmList();
			totalCnt = returnApprovalList.size();
			ReturnData returnData = null;
			SetClaimPickupComplete setClaimPickupComplete = null;
			
			if(totalCnt>0){
				for(int i=0; i<returnApprovalList.size(); i++){
					returnApproval = (HashMap<String, Object>) returnApprovalList.get(i);
					
					setClaimPickupComplete = new SetClaimPickupComplete();
					setClaimPickupComplete.setClaimBundleNo(Long.parseLong(returnApproval.get("PA_CLAIM_NO").toString()));
					setClaimPickupComplete.setClaimType("RETURN");
					if(returnApproval.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE)) {
						paName = Constants.PA_BROAD;
					}
					else {
						paName = Constants.PA_ONLINE;
					}
					
					returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimPickupComplete, ReturnData.class, paName);
					
					if(returnData.getReturnKey() == 1 || StringUtils.contains(returnData.getReturnMsg(), "수거완료된") //수거완료된 클레임입니다.
							 || StringUtils.contains(returnData.getReturnMsg(), "신청/보류")){ //클레임 신청/보류 단계만 가능합니다.
						executedRtn = paWempClaimService.updateReceiveConfirmResultTx(returnApproval);
						if(executedRtn > 0){
							successCnt++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", returnData.getReturnMsg());
					}
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", apiCode);
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("===== 위메프 반품수거완료 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품클레임승인 전송", notes = "반품클레임 승인 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/return-approval-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnApprovalProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_038";
		String paName = "";
		
		List<Object> returnApprovalList        = null;
		HashMap<String, Object> returnApproval = null;
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		log.info("===== 위메프 반품클레임승인 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			//수거완료된 데이터
			returnApprovalList = paWempClaimService.selectReturnApprovalList();
			totalCnt = returnApprovalList.size();
			ReturnData returnData = null;
			SetClaimApprove setClaimApprove = null;
			
			if(totalCnt>0){
				for(int i=0; i<returnApprovalList.size(); i++){
					returnApproval = (HashMap<String, Object>) returnApprovalList.get(i);
					
					setClaimApprove = new SetClaimApprove();
					setClaimApprove.setClaimBundleNo(Long.parseLong(returnApproval.get("PA_CLAIM_NO").toString()));
					setClaimApprove.setClaimType("RETURN");
					if(returnApproval.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE)) {
						paName = Constants.PA_BROAD;
					} else {
						paName = Constants.PA_ONLINE;
					}
					
					returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimApprove, ReturnData.class, paName);
					
					if(returnData.getReturnKey() == 1 || StringUtils.contains(returnData.getReturnMsg(), "완료된") || StringUtils.contains(returnData.getReturnMsg(), "승인된")){
						executedRtn = paWempClaimService.updateReturnApprovalResultTx(returnApproval);
						if(executedRtn > 0){
							successCnt++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", returnData.getReturnMsg());
					}
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
			
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 반품클레임승인 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}

  /* 필요한지 검토 필요
	@ApiOperation(value = "반품클레임보류 전송", notes = "반품클레임 보류 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/return-hold-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnHoldProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HashMap<String, String> subApiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_037";
		
		log.info("===== 위메프 반품클레임보류 전송 API Start=====");
		String startDate = paWempApiService.makeDateTimeDayBefore(30); //30일전부터
		String endDate   = paWempApiService.makeDateTimeEnd("");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"   , apiCode);
			paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("startDate" , dateTime);
			//이거 알아서 잘 세팅 필요
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", "IF_PAWEMPAPI_03_037");
			paramMap.put("apiCode"   , "IF_PAWEMPAPI_03_031");
			subApiInfo = systemService.selectPaApiInfo(paramMap);
			subApiInfo.put("apiInfo", "IF_PAWEMPAPI_03_031");
			paramMap.put("apiCode"   , apiCode);
			
			for(int count=0 ; count<Constants.PA_WEMP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
				}
			
				//클레임조회(수거완료)를 먼저 실행
				GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
				getClaimListRequest.setFromDate(startDate);
				getClaimListRequest.setToDate(endDate);
				getClaimListRequest.setClaimType("RETURN");
				getClaimListRequest.setClaimStatus("REQUEST");
				GetClaimListResponse list = (GetClaimListResponse) paWempApiService.callWApiObejct(subApiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
				if(list.getClaim().isEmpty()){
					paramMap.put("code"   , "404");
				}else{
					PaWempClaimList wempClaim = null;
					List<PaWempClaimList> claimList = new ArrayList<PaWempClaimList>();
					for(Claim claimObject : list.getClaim()){
						Pickup pickup = claimObject.getPickup();
						//수거완료 : O723 (15)
						if(StringUtils.equals(pickup.getShipStatus(),"수거완료")){
							wempClaim = new PaWempClaimList();
							wempClaim.setPaClaimNo(Long.toString(claimObject.getClaimBundleNo()));
							wempClaim.setPaOrderNo(Long.toString(claimObject.getPurchaseNo()));
							wempClaim.setPaShipNo(Long.toString(claimObject.getBundleNo()));
							claimList.add(wempClaim);
						}
					}
					
					List<PaWempClaimList> holdReturnList = paWempClaimService.selectHoldClaimListTx(claimList);
					SetClaimPending setClaimPending = null;
					ReturnData returnData = null;
					for(PaWempClaimList holdObject : holdReturnList){
						setClaimPending = new SetClaimPending();
						setClaimPending.setClaimType("RETURN");
						setClaimPending.setPendingReasonCode("RPD5");
						// DETAIL 내용 필요
						setClaimPending.setPendingReasonDetail("반품임시보류");
						setClaimPending.setClaimBundleNo(Long.parseLong(holdObject.getPaClaimNo()));
						returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimPending, ReturnData.class, paramMap.getString("paName"));
						if(returnData.getReturnKey() ==1){
							paWempClaimService.updatePaHoldYnTx(holdObject);
						}
					}
				}
			}
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 반품클레임보류 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	*/
	
	/**
	 * 반품생성
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품생성", notes = "반품생성", httpMethod = "GET")
	@RequestMapping(value = "/return-input", method = RequestMethod.GET)
	@ResponseBody
	public void returnInputMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_RETURN_INPUT";
		
		log.info("===== wemp make Return Start =====");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prgId});
			List<Object> orderClaimTargetList = paWempClaimService.selectOrderReturnTargetList();
			
			HashMap<String, Object> hmSheet = null;
			int procCnt = orderClaimTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList.get(i);
					asyncController.returnInputAsync(hmSheet, request);
				} catch (Exception e) {
					continue;
				}
			}
		
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("=========================== wemp make Return End =========================");
		}
		return;
	}
	
	/**
	 * 반품철회
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품철회", notes = "반품철회", httpMethod = "GET")
	@RequestMapping(value = "/return-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void returnCancelMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_RETURN_CANCEL";
		
		List<Object> returnCancelTargetList        = null;
		HashMap<String, Object> returnCancelTarget = null;
		
		log.info("===== wemp Return Cancel Start =====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { prgId });
			}
			
			returnCancelTargetList = paWempClaimService.selectReturnCancelTargetList();
			if(returnCancelTargetList.size() > 0){
				for(int i=0; i<returnCancelTargetList.size(); i++){
					try{
						returnCancelTarget = new HashMap<String, Object>();
						returnCancelTarget = (HashMap<String, Object>) returnCancelTargetList.get(i);
						
						asyncController.returnCancelAsync(returnCancelTarget, request);
					}catch(Exception e){
						continue;
					}
				}
			}else{
				log.error("Error Msg: " + getMessage("errors.no.select"));
			}
		}catch(Exception e){
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("===== wemp Return Cancel End =====");
		}
		return;
	}
}
