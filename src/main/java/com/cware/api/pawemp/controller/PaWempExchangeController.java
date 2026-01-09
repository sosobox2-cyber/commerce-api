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
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pawemp.claim.model.Claim;
import com.cware.netshopping.pawemp.claim.model.GetClaimListRequest;
import com.cware.netshopping.pawemp.claim.model.GetClaimListResponse;
import com.cware.netshopping.pawemp.claim.model.SetClaimPickup;
import com.cware.netshopping.pawemp.claim.model.SetClaimPickupComplete;
import com.cware.netshopping.pawemp.claim.service.PaWempClaimService;
import com.cware.netshopping.pawemp.common.enums.WempCode;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.exchange.service.PaWempExchangeService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
@ApiIgnore
@Api(value="/pawemp/exchange", description="교환")
@Controller("com.cware.api.pawemp.PaWempExchangeController")
@RequestMapping(value="/pawemp/exchange")
public class PaWempExchangeController extends AbstractController{

	@Resource(name = "common.system.systemService")
	public SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	public PaWempApiService paWempApiService;
	
	@Resource(name = "com.cware.api.pawemp.PaWempAsyncController")
	private PaWempAsyncController asyncController;
	
	@Resource(name ="pawemp.exchange.paWempExchangeService")
	public PaWempExchangeService paWempExchangeService;
	
	@Resource(name = "pawemp.claim.paWempClaimService")
	public PaWempClaimService paWempClaimService;
	
	@ApiOperation(value = "교환신청 목록조회", notes = "교환신청 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")
			   })
	@RequestMapping(value = "/exchange-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck = "";
		String apiCode = "IF_PAWEMPAPI_03_041";
		
		log.info("===== 위메프 교환신청목록조회 API Start=====");
		
		String startDateTime = paWempApiService.makeDateTimeStart(fromDate);
		String endDateTime   = paWempApiService.makeDateTimeEnd(toDate);

		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
	        paramMap.put("startDate", dateTime);
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
				
				log.info("04.교환신청목록 조회 API 호출 (paCode " + paramMap.getString("paCode") + ")");
				
				GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
				getClaimListRequest.setFromDate(startDateTime);
				getClaimListRequest.setToDate(endDateTime);
				getClaimListRequest.setClaimType("EXCHANGE");
				getClaimListRequest.setClaimStatus("REQUEST");
				getClaimListRequest.setSearchDateType("REQUEST");
				GetClaimListResponse list = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
				if(list.getClaim().isEmpty()){
					paramMap.put("code"   , "404");
					paramMap.put("message","처리할 내역이 없습니다.");
					log.info("exchangeList no data");
				}else{
					PaWempClaimList wempClaim = null;
					for(Claim claimObject : list.getClaim()){
						wempClaim = paWempClaimService.makePaWempClaimList(claimObject, WempCode.ORDER_GB.EXCHANGE_DELIVERY.getCode(), paramMap.getString("paCode")); //PA_ORDER_GB 40 교환
						
						//위메프 교환생성
						//TPAWEMPCLAIMLIST, TPAWEMPCLAIMITEMLIST, TPAORDERM에 '40', '45' 2개 insert
						paWempExchangeService.saveExchangeListTx(wempClaim);
						paramMap.put("code","200");
						paramMap.put("message", "OK");
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
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 교환신청목록조회 API End =====");
			//교환 생성
			exchangeInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@ApiOperation(value = "교환철회 목록조회", notes = "교환철회 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")
			   })
	@RequestMapping(value = "/exchange-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeWithdrawList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck = "";
		String apiCode = "IF_PAWEMPAPI_03_042";
		
		log.info("===== 위메프 교환철회목록조회 API Start=====");
		String startDateTime = paWempApiService.makeDateTimeStart(fromDate);
		String endDateTime   = paWempApiService.makeDateTimeEnd(toDate);

		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
	        paramMap.put("startDate", dateTime);
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
				
				try {
			
					log.info("04.교환철회목록 조회 API 호출 (paCode " + paramMap.getString("paCode") + ")");
					GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
					getClaimListRequest.setFromDate(startDateTime);
					getClaimListRequest.setToDate(endDateTime);
					getClaimListRequest.setClaimType("EXCHANGE");
					getClaimListRequest.setClaimStatus("WITHDRAW");
					getClaimListRequest.setSearchDateType("WITHDRAW");
					GetClaimListResponse list = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
					if(list.getClaim().isEmpty()){
						paramMap.put("code"   , "404");
						paramMap.put("message","처리할 내역이 없습니다.");
						log.info("exchangeWithdrawList no data");
					}else{
						PaWempClaimList wempClaim = null;
						for(Claim claimObject : list.getClaim()){
							wempClaim = paWempClaimService.makePaWempClaimList(claimObject, WempCode.ORDER_GB.EXCHANGE_DELIVERY_CANCELLATION.getCode(), paramMap.getString("paCode")); //PA_ORDER_GB 41 교환취소
							
							//위메프 교환 취소
							//TPAWEMPCLAIMLIST, TPAWEMPCLAIMITEMLIST, TPAORDERM에 '41', '46' 2개 insert
							paWempExchangeService.saveExchangeCancelListTx(wempClaim);
						}
						paramMap.put("code","200");
						paramMap.put("message", "OK");
					}
				} catch (Exception ee) {
					paramMap.put("code", "500");
					paramMap.put("message", ee.getMessage());
					log.info(ee.getMessage());
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
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 교환철회목록조회 API End =====");
			// 교환 철회 생성
			exchangeCancelMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	//한 주문에 동일 상품의 단품 001 / 002 주문 시 묶음배송 두개 동시 교환 접수 시 묶음 교환 접수. 부분 교환 회수 처리 불가.
	//첫 번째 상품 교환 회수 완료 시 교환수거요청 되며 위메프 상태는 두 상품 모두 교환수거요청처리 됨.
	//만약 두번째 상품이 교환 완료 안되었을 경우 교환수거 요청처리 상태로 대기. TSDCHECK를 통해 나가고 있지 않은 상품이 있는지 체크 예정.
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "교환수거요청 전송", notes = "교환수거요청 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/exchange-pickup-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangePickupProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();	
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_043";
		
		List<Object> pickupList        = null;
		HashMap<String, Object> pickup = null;
		int totalCnt    = 0;
		int successCnt  = 0;
		int excuteCnt = 0;
		
		log.info("===== 위메프 교환수거요청 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
	        paramMap.put("startDate", dateTime);
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
			
			log.info("04.수거요청 대상 조회");
			pickupList =  paWempExchangeService.selectExchangePickupList();
			totalCnt   = pickupList.size();
			ReturnData returnData = null;
			SetClaimPickup setClaimPickup = null;
			if(totalCnt>0){
				for(int i=0; i<pickupList.size(); i++){
					pickup = (HashMap<String, Object>) pickupList.get(i);
					
					setClaimPickup = new SetClaimPickup();
					setClaimPickup.setClaimBundleNo(Long.parseLong(pickup.get("PA_CLAIM_NO").toString()));
					setClaimPickup.setClaimType("EXCHANGE");
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
						if(returnData.getReturnKey() ==1 || StringUtils.contains(returnData.getReturnMsg(), "수거요청대기")){
							excuteCnt = paWempExchangeService.updatePickupProcTx(pickup.get("MAPPING_SEQ").toString());
							if(excuteCnt > 0){
								successCnt++;
							}
						}else{
							paramMap.put("code"   , "500");
							paramMap.put("message", "PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
							log.error("PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + returnData.getReturnMsg());
							
							excuteCnt = paWempExchangeService.updateProcFailTx(pickup.get("PA_CLAIM_NO").toString(), returnData.getReturnMsg(), "45", pickup.get("MAPPING_SEQ").toString()); //ORDER_GB 45:교환회수
							if(excuteCnt < 1) {
								throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
							}
							
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
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}
			
			log.info("===== 위메프 교환수거요청 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "교환수거완료 전송", notes = "교환수거완료 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/exchange-pickup-complete-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> pickupCompleteProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_044";
		String paName = "";
		
		List<Object> returnApprovalList        = null;
		HashMap<String, Object> returnApproval = null;
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		log.info("===== 위메프 교환수거완료 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
	        paramMap.put("startDate", dateTime);
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
	
			log.info("04. 회수확정목록 조회");
			returnApprovalList = paWempExchangeService.selectPickupCompleteList();
			totalCnt = returnApprovalList.size();
			ReturnData returnData = null;
			SetClaimPickupComplete setClaimPickupComplete = null;
			
			if(totalCnt>0){
				for(int i=0; i<returnApprovalList.size(); i++){
					returnApproval = (HashMap<String, Object>) returnApprovalList.get(i);
					
					setClaimPickupComplete = new SetClaimPickupComplete();
					setClaimPickupComplete.setClaimBundleNo(Long.parseLong(returnApproval.get("PA_CLAIM_NO").toString()));
					setClaimPickupComplete.setClaimType("EXCHANGE");
					if(returnApproval.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE)) {
						paName = Constants.PA_BROAD;
					}
					else {
						paName = Constants.PA_ONLINE;
					}
					
					returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimPickupComplete, ReturnData.class, paName);
					
					if(returnData.getReturnKey() == 1){
						executedRtn= paWempExchangeService.updatePickupCompleteProcTx(returnApproval);         
						if(executedRtn > 0){
							successCnt++;
						}
					}else if(returnData.getReturnMsg().replace(" ","").indexOf("수거완료된") >= 0) {
						//이미 수거 완료된 경우에서 처리
						executedRtn= paWempExchangeService.updatePickupCompleteProcTx(returnApproval);         
						if(executedRtn > 0){
							successCnt++;
						}
					}else{
						executedRtn = paWempExchangeService.updateProcFailTx(returnApproval.get("PA_CLAIM_NO").toString(), returnData.getReturnMsg(), "45", ""); //ORDER_GB 45:교환회수
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

			log.info("===== 위메프 교환수거완료 전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "교환배송등록 전송", notes = "교환배송등록 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/exchange-slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_046";
		
		int totalCnt = 0;
		int procCount = 0;
		StringBuffer sb = new StringBuffer();
		String msg = "";
		
		int excuteCnt = 0;
		ParamMap procResultMap = new ParamMap();
		
		List<Object> exchangeDeliveryList = null;
		HashMap<String, Object> exchangeDeliveryVo = null;
		
		log.info("===== 위메프 교환배송등록 전송 API Start=====");
		
		log.info("01.API 기본정보 세팅");
		String dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"   , apiCode);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
	    paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
	    paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	    paramMap.put("startDate" , dateTime);
		
		
		try {
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			log.info("04. 교환배송등록 대상 목록 조회");
			exchangeDeliveryList =  paWempExchangeService.selectExchangeSlipOutTargetList();
			totalCnt = exchangeDeliveryList.size();
			
			if(totalCnt < 1) {
				paramMap.put("code"   , "404");
				paramMap.put("message","처리할 내역이 없습니다.");
				log.info("slipOutProc no data");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("05. 교환배송등록 API 전송 및 결과 DB저장 listCnt:"+exchangeDeliveryList.size());
			for(int i=0; i<exchangeDeliveryList.size(); i++) {
				exchangeDeliveryVo = (HashMap<String, Object>) exchangeDeliveryList.get(i);
				
				try {
					procResultMap = paWempExchangeService.slipOutProc(exchangeDeliveryVo, apiInfo);
					if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
						procCount++;
						//성공으로 update 처리
						excuteCnt = paWempExchangeService.updateDeliveryProcTx(exchangeDeliveryVo);
						if(excuteCnt < 1) {
							log.info("slipOutProc updateDeliveryProcTx fail");
							throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
						}
					} else {
						excuteCnt = paWempExchangeService.updateProcFailTx(exchangeDeliveryVo.get("PA_CLAIM_NO").toString(), procResultMap.getString("result_text"), "40", exchangeDeliveryVo.get("MAPPING_SEQ").toString()); //ORDER_GB 40:교환배송
						if(excuteCnt < 1) {
							log.info("slipOutProc updateProcFailTx fail");
							throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
						}
					}
				} catch (Exception e) {
					sb.append(exchangeDeliveryVo.get("PA_CLAIM_NO").toString() + ": 발송처리 fail - " +e.getMessage() + "|");
				}
				
			}
		}catch(Exception e){
			msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + "|";
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message", e.getMessage().length() > 3950 ? msg+e.getMessage().substring(0, 3950) : msg+e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + " | ";
					
					//대상건수 모두 성공하였을 경우
					if(totalCnt == procCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 교환배송등록전송 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@ApiOperation(value = "교환클레임완료 전송", notes = "교환클레임완료 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/exchange-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeConfirmProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_049";
		
		StringBuffer buffer = new StringBuffer();
		ParamMap procResultMap = new ParamMap();
		
		int excuteCnt = 0;
		int totalCnt   = 0;
		int successCnt = 0;
		
		List<HashMap<String, Object>> completeList = null;
		HashMap<String, Object>       complete     = null;
		
		log.info("===== 위메프 교환클레임완료 전송 API Start=====");
		try {
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			completeList = paWempExchangeService.selectExchangeCompleteList();
			
			totalCnt = completeList.size();
			
			if(totalCnt >= 1){
				for(int i=0; i<completeList.size(); i++){
					complete = (HashMap<String, Object>)completeList.get(i);
					
					procResultMap = paWempExchangeService.setClaimCompleteProc(complete, apiInfo);
					
					if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
						// 교환회수(45)는 TPAORDERM.PA_DO_FLAG '60'(회수확정)으로 업데이트
						// 교환배송(40)은 TPAORDERM.PA_DO_FLAG '80'(배송완료)으로 업데이트
						excuteCnt= paWempExchangeService.updateExchangeEndTx(complete);
						if(excuteCnt < 1) {
							throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
						}else {
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
						}
					} else {
						//교환회수에 실패로 반영
						excuteCnt = paWempExchangeService.updateProcFailTx(complete.get("PA_CLAIM_NO").toString(), procResultMap.getString("result_text"), "40", ""); //ORDER_GB 45:교환회수
						if(excuteCnt < 1) {
							throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
						}
						paramMap.put("code"   , "500");
						paramMap.put("message", procResultMap.getString("result_text"));
					}
					
					if("200".equals(paramMap.getString("code"))){
						successCnt++;
					}else{
						buffer.append((buffer.length() == 0 ? "PA_CLAIM_NO: " : ", ") + complete.get("PA_CLAIM_NO").toString() + " | " + procResultMap.getString("result_text"));
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
				if(!"490".equals(paramMap.getString("code")) && !"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", "전체: " + totalCnt + " | 성공: " + successCnt + " | 실패: " + buffer.toString());
				}
				paramMap.put("siteGb", "PAWEMP");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 교환클레임완료 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	/**
	 * 교환생성
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "교환생성", notes = "교환생성", httpMethod = "GET")
	@RequestMapping(value = "/exchange-input", method = RequestMethod.GET)
	@ResponseBody
	public void exchangeInputMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_EXCHANGE_INPUT";
		
		log.info("===== wemp make Exchange Start =====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { prgId });
			}
			List<Paorderm> orderChangeTargetList = paWempExchangeService.selectOrderChangeTargetList();

			for(int i = 0; orderChangeTargetList.size() > i; i++){
				try{
					asyncController.orderChangeAsync(orderChangeTargetList.get(i), request);
				} catch (Exception e) {
					continue;
				}
			}
		}catch(Exception e){
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("===== wemp make Exchange End =====");
		}
		return;
	}
	
	/**
	 * 교환철회
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "교환철회", notes = "교환철회", httpMethod = "GET")
	@RequestMapping(value = "/exchange-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void exchangeCancelMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_EXCHANGE_CANCEL";
		
		log.info("===== wemp Exchange Cancel Start =====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { prgId });
			}
			
			List<Paorderm> orderChangeCancelTargetList = paWempExchangeService.selectChangeCancelTargetList();

			for(int i = 0; orderChangeCancelTargetList.size() > i; i++){
				try{
					asyncController.changeCancelAsync(orderChangeCancelTargetList.get(i), request);
				} catch (Exception e) {
					continue;
				}
			}
		}catch(Exception e){
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("===== wemp Exchange Cancel End =====");
		}
		return;
	}
}
