package com.cware.api.pagmktv2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.claim.repository.PaGmktClaimDAO;
import com.cware.netshopping.pagmkt.claim.service.PaGmktClaimService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktClaimRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/return", description="반품")
@Controller("com.cware.api.pagmktv2.PaGmktV2ReturnController")
@RequestMapping(value="/pagmktv2/return")
public class PaGmktV2ReturnController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimService")
	private PaGmktClaimService paGmktClaimService;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2OrderController")
	private PaGmktV2OrderController paGmktOrderController;	
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController asycController;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimDAO")
	private PaGmktClaimDAO PaGmktClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
		
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2ClaimController")
	private PaGmktV2ClaimController paGmktClaimController;
	
	/**
	 * 6.5 반품신청/취소 목록조회
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "6.5 반품신청/취소 목록조회", notes = "6.5 반품신청/취소 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-claim-Returns/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnList(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode, @RequestParam(value="claimStatus", required=false, defaultValue = "ClaimReady") String claimStatus) throws Exception{
		String apiCode 			= "IF_PAGMKTAPI_V2_03_005";            
		PaGmktAbstractRest rest = new PaGmktClaimRest();
		String duplicateCheck 	= "";
		String message 			= null;
		ParamMap paramMap 		= new ParamMap();

		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		paramMap.put("ORDER_NO"		, 0);
		paramMap.put("Pay_No"		, 0);
		paramMap.put("PAGE_INDEX"	, 0);
		paramMap.put("FROM_DATE"	, DateUtil.addDay(systemService.getSysdate(), 	 -5));
		paramMap.put("TO_DATE"		, DateUtil.addDay(systemService.getSysdate(),    +1));
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
		
		String type = "";
		for ( int i = 0; i < 6; i++) {
			if (i == 0) {
				type = "ClaimReady";
			} else if (i == 1) {
				type = "ClaimIng";
			} else if (i == 2) {
				type = "ClaimReject";
			} else if (i == 3) {
				type = "ClaimDone";
			} else if (i == 4) {
				type = "ClaimRefundDone";
			} else {
				type = "ClaimRefundHold";
			}
			paramMap.put("claimType"	, type);
			try{			
				//= 중복 실행 Check
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
				
				switch(type){
				
				case "ClaimReady":
					log.info("===== 반품 목록 조회 API START =====");
					paramMap.put("CLAIM_STATUS"		, "ClaimReady");
					paramMap.put("RETURN_STATUS"	, 1);
					paramMap.put("TYPE"				, 2);
					paramMap.put("CLAIM_TYPE"		, "Return");
					break;
					
				case "ClaimIng":	
					//G마켓에서 특정 데이터는 (예를들어 교환건 반품전환 또는 교환 -> 교환취소 -> 반품 데이터) 반품 목록조회로 조회가 안되는 문제가 있다.
					log.info("===== 수거완료 목록 조회 API START =====");	
					paramMap.put("CLAIM_STATUS"		, "ClaimReady");
					paramMap.put("RETURN_STATUS"	, 2);
					paramMap.put("TYPE"				, 2);
					paramMap.put("CLAIM_TYPE"		, "Return");			    
					break;
				
				case "ClaimReject":
					log.info("===== 반품철회 목록 조회 API START =====");
					paramMap.put("CLAIM_STATUS"		, "ClaimReject");
					paramMap.put("RETURN_STATUS"	, 5);
					paramMap.put("TYPE"				, 3);
					paramMap.put("CLAIM_TYPE"		, "ReturnCancel");
					break;
				
				case "ClaimDone":
					log.info("===== 직권 반품완료 목록 조회 API START =====");
					paramMap.put("CLAIM_STATUS"		, "ClaimDone");
					paramMap.put("RETURN_STATUS"	, 6);
					paramMap.put("TYPE"				, 3);
					paramMap.put("CLAIM_TYPE"		, "ReturnDone");	
					break;
				
				case "ClaimRefundDone":
					log.info("===== 반품 환불완료 목록 조회 API START =====");
					paramMap.put("CLAIM_STATUS"		, "ClaimDone");
					paramMap.put("RETURN_STATUS"	, 4);
					paramMap.put("TYPE"				, 3);
					paramMap.put("CLAIM_TYPE"		, "ReturnDone");	
					break;
				
				case "ClaimRefundHold":	
					log.info("===== 환불보류 목록 조회 API START =====");	
					paramMap.put("CLAIM_STATUS"		, "ClaimReady");
					paramMap.put("RETURN_STATUS"	, 3);
					paramMap.put("TYPE"				, 2);
					paramMap.put("CLAIM_TYPE"		, "Return");			    
					break;
				
				default:	
					throw processException( "errors.cannot  ", new String[] {"해당 API는 준비되지 않았습니다."} );
				}
				
				message = paGmktClaimController.savePagmktClaimList(rest ,paramMap);
				CommonUtil.failCheck(message, paramMap);
		
			} catch (Exception se) {
				CommonUtil.dealException(se, paramMap);
				log.error(paramMap.getString("message"), se);
				//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			}finally {
				
				try{
					CommonUtil.dealSuccess(paramMap);
				}catch(Exception e){
					log.error("ApiTracking Insert Error : "+e.getMessage());
				}
				
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				log.info("===== 반품 처리 API END =====");
			}
	
			switch(type){
			
				case "ClaimReady": 
				case "ClaimIng"	:
				case "ClaimDone"://ClaimRefundDone 은 ClaimDone에 포함
					//=IF_PAGMKTAPI_03_012 반품생성 호출
					paGmktClaimController.orderClaimMain(request, paGroupCode);     
					break;
				
				case "ClaimReject":
				  	//=IF_PAGMKTAPI_03_013 반품취소생성 조회 호출
					paGmktClaimController.claimCancelMain(request, paGroupCode);
			       	break;	
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	
	
	/**
	 * 6.6 반품수거정보등록
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "6.6 반품수거정보등록", notes = "6.6 반품수거정보등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-claim-pickup/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnPickupProc(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		String apiCode 							= "IF_PAGMKTAPI_V2_03_006";            
		PaGmktAbstractRest rest 				= new PaGmktClaimRest();
	    List<Map<String,String>> returnPickList = new ArrayList<Map<String,String>>();
		String broadCode  						= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 						= Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  						= null;
		int	   count 	 						= Constants.PA_GMKT_CONTRACT_CNT;
		String duplicateCheck 					= "";
		ParamMap paramMap 						= new ParamMap();
		String messageFor50 					= "";
		String messageFor55 					= "";

		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
		
		try{			
			//= 중복 실행 Check
			log.info("===== 반품 수거 정보 조회 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			
			for(int i = 0; i < count; i++  ){
				
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				
				returnPickList = paGmktClaimService.selectReturnPickup50List(paramMap);
				messageFor50 += paGmktClaimController.sendShippingInfoToGmaket(rest,	 paramMap, returnPickList, "50");
				
				returnPickList = paGmktClaimService.selectReturnPickup60List(paramMap);
				messageFor55 += paGmktClaimController.sendShippingInfoToGmaket(rest, 	 paramMap, returnPickList , "55");
		
			}//end of for
			
			CommonUtil.failCheck(messageFor50 + messageFor55, paramMap);
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}		
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 반품 수거 정보 조회 API END =====");
		}
		
		returnConfrim(paGroupCode);
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	
	/**
	 * 6.8 반품승인
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("unchecked")
	public void returnConfrim(String paGroupCode) throws Exception{		
		PaGmktAbstractRest rest 			= new PaGmktClaimRest();
		String apiCode 						= "IF_PAGMKTAPI_V2_03_008";            
		String duplicateCheck 				= "";
		List<Object> returnConfirmList 		= new ArrayList<>();
		HashMap<String, Object> returnMap	= null;
		ParamMap paramMap 					= new ParamMap();
		String broadCode  					= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 					= Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  					= null;
		int	   count 	  					= Constants.PA_GMKT_CONTRACT_CNT;
		int    successCount 				= 0;
		int	   failCount 					= 0;
		String message      				= "";
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
		
		try{
			log.info("===== 반품 승인 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0 ; i < count ; i++){
				
				successCount = 0;
				failCount	 = 0;
				
 				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				returnConfirmList = paGmktClaimService.selectReturnConfirmList(paramMap);
				
				for(Object returnConfrim : returnConfirmList){
					returnMap = (HashMap<String, Object>) returnConfrim;
					try{
						successCount += paGmktClaimService.saveReturnConfirmProcTx(rest , paramMap, returnMap);
					}catch(Exception e){
						failCount++;
						continue;
					}
				}//end of for	
				message += CommonUtil.setResultMessage("반품 승인" , paCode, successCount, failCount) + "\n";

			}//end of for
		
		CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 반품 승인 API END =====");
		}
	}	
	
	
	@ApiOperation(value = "반품승인 API(사용하지 않음)", notes = "반품승인 API(사용하지 않음)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-claim-return", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfrimNotUse(HttpServletRequest request) throws Exception{
		log.info("반품승인 API는 사용하지 않을것입니다.");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "해당 API는 사용하지 않을 예정입니다."), HttpStatus.OK);	

	}

}
