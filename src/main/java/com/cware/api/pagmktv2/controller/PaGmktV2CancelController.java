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
import org.springframework.web.bind.annotation.RequestBody;
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
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pagmkt.cancel.service.PaGmktCancelService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktCancelRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pagmktv2/cancel", description="취소")
@Controller("com.cware.api.pagmktv2.PaGmktV2CancelController")
@RequestMapping(value="/pagmktv2/cancel")
public class PaGmktV2CancelController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2OrderController")
	private PaGmktV2OrderController paGmktOrderController;	
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController asycController;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "pagmkt.cancel.PaGmktCancelService")
	private PaGmktCancelService paGmktCancelService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;

	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2ClaimController")
	private PaGmktV2ClaimController paGmktClaimController;
	
	
	/**
	 * 6.1 취소 조회, 철회 , 상담원 완료 ,직권취소
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "취소 조회, 철회 , 상담원 완료 ,직권취소", notes = "취소 조회, 철회 , 상담원 완료 ,직권취소", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-claim-Cancels/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setMakerClaimList(HttpServletRequest request,
		@ApiParam(name = "paGroupCode", value = "제휴사 그룹코드", defaultValue = "") @PathVariable("paGroupCode") String paGroupCode,
		@ApiParam(name = "claimStatus", value = "claim상태 (ClaimReady, ClaimReject, Claimwithdrawn, ClaimDone, ClaimEnd, ClaimFinish)", defaultValue = "") @RequestParam(value="claimStatus", required=false, defaultValue = "ClaimReady") String claimStatus) 
		throws Exception{
	   
		PaGmktAbstractRest rest = new PaGmktCancelRest();
		String apiCode = "IF_PAGMKTAPI_V2_03_001"; 
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		paramMap.put("FROM_DATE"	, DateUtil.addDay(systemService.getSysdate(),  -5 ));
		paramMap.put("TO_DATE"		, DateUtil.addDay(systemService.getSysdate(),  +1 ));
		paramMap.put("ORDER_NO"		, "0");
		paramMap.put("PAY_NO"		, "0");
		paramMap.put("claimType"	, claimStatus);
		CommonUtil.setParams(paramMap);

		switch(claimStatus){
		
			case "ClaimReady": 		

				paramMap.put("CLAIM_TYPE" , "1");
				paramMap.put("TYPE"		  , "2");
				return setClaimReady(rest, paramMap);

			case "ClaimReject": case "Claimwithdrawn": 
					
				paramMap.put("CLAIM_TYPE" , "4");
				paramMap.put("TYPE"		  , "2");
				return setClaimwithdrawn(request, rest, paramMap);
				
			case "ClaimDone": //상담원을 통한 취소 완료 목록
				
				paramMap.put("CLAIM_TYPE" , "5");
				paramMap.put("TYPE"		  , "3");
				return setClaimCompleted(rest, paramMap);
			
			case "ClaimEnd": //취소 완료 목록 조회
				paramMap.put("FROM_DATE"	, DateUtil.addDay(systemService.getSysdate(),  -1 ));
				paramMap.put("TO_DATE"		, DateUtil.addDay(systemService.getSysdate(),  +1 ));
				paramMap.put("CLAIM_TYPE" , "3");
				paramMap.put("TYPE"		  , "3");
				return setClaimCompleted(rest, paramMap);
		
			case "ClaimFinish": //취소 완료 목록 조회(Auction 송금 후 취소)
				paramMap.put("CLAIM_TYPE" , "6");
				paramMap.put("TYPE"		  , "3");
				return setClaimCompleted(rest, paramMap);
			
			/*case "ClaimPreOrder": //무통장 미입금건 취소	
				return setClaimCompletedP(paramMap);*/
			default :
				break;
			
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("304","ETC ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	/**
	 * 6.2 취소승인 , 취소거부
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "취소승인 , 취소거부", notes = "취소승인 , 취소거부", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-claim-Cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(
			@RequestBody PaGmktAbstractRest rest, 
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") String paGroupCode) 
		throws Exception{
		
		if(rest == null) rest 	= new PaGmktCancelRest();
		ParamMap paramMap 		= new ParamMap();
		String duplicateCheck 	= "";
		String apiCode 			= "IF_PAGMKTAPI_V2_03_002";
		String message 			= null;		

		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"  , paGroupCode);
		CommonUtil.setParams(paramMap);

		try{
			//= 중복 실행 Check
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) 취소 대기 List 조회  - PaGmktclaimList에는 데이터 존재, Paorderm에는 20 데이터가 없는 리스트
			List<Object> cancelList = paGmktCancelService.selectPaGmktOrdCancelList(paramMap);
			
			message = paGmktOrderController.confirmCancel(cancelList, rest, paramMap);
			CommonUtil.failCheck(message, paramMap);
			
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
		}

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 6.2 취소승인 BO - 제휴 취소 승인 처리 P에서 호출
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "취소승인 BO - 제휴 취소 승인 처리 P에서 호출", notes = "취소승인 BO - 제휴 취소 승인 처리 P에서 호출", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-claim-Cancel-bo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProcBO(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode"		, required=true) String paCode,
			@ApiParam(name = "paOrderNo", value = "장바구니번호(결제번호)", defaultValue = "") @RequestParam(value="paOrderNo"		, required=true) String paOrderNo,
			@ApiParam(name = "paOrderSeq", value = "제휴사 주문번호", defaultValue = "") @RequestParam(value="paOrderSeq"	, required=true) String paOrderSeq,
			@ApiParam(name = "paClaimSeq", value = "주문순번 ", defaultValue = "") @RequestParam(value="paClaimSeq"	, required=true) String paClaimSeq,
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") @RequestParam(value="paGroupCode"	, required=true) String paGroupCode
			) throws Exception{
		
		
		PaGmktAbstractRest rest = new PaGmktCancelRest();
		HashMap<String, Object> cancelMap = null;
		HashMap<String,Object> map		  = null; 
		String apiCode = "IF_PAGMKTAPI_V2_03_002B";            
		String duplicateCheck = "";
		String message = "";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode", paGroupCode);
		CommonUtil.setParams(paramMap);


		try{			
			//= 중복 실행 Check	
			log.info("===== 취소 승인 API Start =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			map = new HashMap<String,Object>();
			map.put("paOrderNo"		,paOrderNo);
			map.put("paOrderSeq"	,paOrderSeq);
			map.put("paClaimSeq"	,paClaimSeq);
			map.put("paGroupCode", paGroupCode);
			
			//= Step 4) Cancel Confrim
			cancelMap  = paGmktCancelService.selectPaGmktOrdCancel(map);
			
		
			if(cancelMap != null && cancelMap.size() > 0){
				paGmktCancelService.saveCancelConfirmTx(rest ,cancelMap, paramMap);
				message = "취소승인("+paCode+") 장바구니 번호: "+ paOrderNo + " 주문번호 : " + paOrderSeq ;
			}else{
				message = "해당 주문번호로 취소승인 대기 건이 없습니다.("+paCode+") 장바구니 번호: "+ paOrderNo + " 주문번호 : " + paOrderSeq + "@FAIL";	
			}
		
			CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
				
			CommonUtil.dealException(se, paramMap, "제휴사 번호 : "+ paCode +" 장바구니번호 : " + paOrderNo +" 주문번호 :" + paOrderSeq);
			log.error(paramMap.getString("message"), se);
			
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			
		}finally {
			
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 취소 승인 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 6.3 판매취소
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "판매취소", notes = "판매취소", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/soldOut-cancel/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
	   
		PaGmktAbstractRest rest = new PaGmktCancelRest();
		String duplicateCheck 	= "";
		String apiCode = "IF_PAGMKTAPI_V2_03_003_BO"; 
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		try {
			log.info("===== ebay 판매취소 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			//= Step 1) 판매취소 List 조회
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
	        if(cancelList.size() > 0){
				for (int i = 0; i < cancelList.size(); i++) {
					HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
					
					if("06".equals(cancelMap.get("ORDER_CANCEL_YN").toString())) {
						asycController.reqSaleRefusalProc(cancelMap, false); //품절처리
					} else {
						asycController.reqSaleRefusalProc(cancelMap, true); //비품절처리
					}
				}
	        }
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			
		} finally {
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== ebay 판매취소 API END =====");
			
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200","SUCCESS"), HttpStatus.OK);
	}
	
	/**
	 * 모바일 자동취소 (품절취소반품)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiIgnore
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request, 
		@PathVariable("paGroupCode") String paGroupCode) 
		throws Exception{
	   
		String duplicateCheck 	= "";
		String apiCode = "IF_PAGMKTAPI_V2_03_021"; // IF_PAIACAPI_V2_03_021 (옥션의 경우)
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode); 
		CommonUtil.setParams(paramMap);
		String message = "";
		
		try {
			log.info("===== ebay 모바일자동취소 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			// 판매취소 List 조회
			List<Object> cancelList = paGmktCancelService.selectPaMobileOrderAutoCancelList(paramMap);
			
	        if(cancelList.size() > 0) {
				for (int i = 0; i < cancelList.size(); i++) {
					HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
					message += asycController.moblieReqSaleRefusalProc(cancelMap); // 모바일 자동취소 (품절취소반품) 처리
				}
	        } else {
	        	message = "처리할 데이터가 없습니다.";
	        }
	        
	        CommonUtil.failCheck(message, paramMap);
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try{
				CommonUtil.dealSuccess(paramMap);
				//취소 데이터 생성
				paGmktClaimController.orderClaimMain(request, paGroupCode);
				
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== ebay 모바일자동취소 API END =====");
		}
		
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200","SUCCESS"), HttpStatus.OK);
	}
	
	//= Information 취소 목록조회
	//= Called by setMakerClaimList
	private ResponseEntity<?> setClaimReady(PaGmktAbstractRest rest, ParamMap paramMap ) throws Exception{
			
		String apiCode	   = paramMap.getString("apiCode");
		String paGroupCode = paramMap.getString("paGroupCode");
		String duplicateCheck = "";
		int	   count 	  = Constants.PA_GMKT_CONTRACT_CNT;
	    String broadCode  = Constants.PA_GMKT_BROAD_CODE;
	  	String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
	  	String paCode = null;
	  	String message = "";
	    
		try{			
			//= 중복 실행 Check
			log.info("===== 취소 조회 API Start =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
				
			for (int i= 0; i < count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				message += saveCancelList(rest, paramMap);
			}	
			
			CommonUtil.failCheck(message, paramMap);
			
			//= Step 4) Cancel Confrim
			cancelConfirmProc(rest, paGroupCode);
			
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
			log.info("===== 취소 조회 API END =====");
		}
		
		paGmktOrderController.cancelInputMain(paGroupCode); //= 취소데이터 생성
			
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	//= Information 취소 목록 조회 API 통신 결과 TPAGMKTCANCELLIST Insert
	//= Called by setClaimReady
	private String saveCancelList(PaGmktAbstractRest rest,  ParamMap paramMap) throws Exception{

		ArrayList<HashMap<String, Object>> cancelList = new ArrayList<HashMap<String, Object>>();
		PaGmkCancel  pagmkcancel = new PaGmkCancel();
		int    failCount = 0;
		int	   successCount = 0;
		
		cancelList = getConnectionAndResponseForCancel(rest,paramMap);
			
		for(HashMap<String, Object> cancelMap : cancelList){
	
			try{
				pagmkcancel = paGmktCancelService.setPagmktCancelVo(cancelMap, paramMap);
				successCount += paGmktCancelService.saveCancelReqListTx(pagmkcancel);
				
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap ,e);
				failCount++;
				continue;
			}
			
		}//end of for
		
		return CommonUtil.setResultMessage("취소", paramMap.getString("paCode"), successCount, failCount);	
	}
		
	//= Information 취소 철회(거부) 목록 조회 
	//= Called by   setMakerClaimList
	private ResponseEntity<?> setClaimwithdrawn(HttpServletRequest request, PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{
			
		String duplicateCheck = "";
		int	   count 	  = Constants.PA_GMKT_CONTRACT_CNT;
	    String broadCode  = Constants.PA_GMKT_BROAD_CODE;
	  	String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
	  	String paCode = null;
	  	String message = "";
		
		try{
			
			//= 중복 실행 Check
			log.info("===== 취소 철회 API Start =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
				
			for (int i= 0; i < count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				message += saveCancelListWithdraw(rest, paramMap);
			}	
				
			CommonUtil.failCheck(message, paramMap);

		}catch (Exception se) {
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
			log.info("===== 취소 철회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	//= Information 취소 철회 목록  API 통신 및 결과 TPAGMKTCANCELLIST Update 
	//= Called by   setClaimwithdrawn
	private String saveCancelListWithdraw(PaGmktAbstractRest rest,  ParamMap paramMap) throws Exception{
		
		ArrayList<HashMap<String, Object>> cancelList = new ArrayList<HashMap<String, Object>>();
		PaGmkCancel  pagmkcancel = new PaGmkCancel();
		int failCount = 0;
		int successCount = 0;
		
		cancelList = getConnectionAndResponseForCancel(rest,paramMap);
		
		for(HashMap<String, Object> cancelMap : cancelList){
			
			try{
				pagmkcancel = paGmktCancelService.setPagmktCancelVo(cancelMap, paramMap);
				successCount += paGmktCancelService.saveCancelWithdrawList(pagmkcancel);
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
				failCount++;
				continue;
			}
		}//end of for
		
		return CommonUtil.setResultMessage("취소 철회", paramMap.getString("paCode"), successCount, failCount);	
	}
			
	
	private ResponseEntity<?> setClaimCompletedP(ParamMap paramMap) throws Exception{
		String duplicateCheck 					= "";
		PaGmkCancel  pagmkcancel 				= new PaGmkCancel();
	    List<HashMap<String,Object>> cancelList = new ArrayList<HashMap<String,Object>>();
	    int failCount   						= 0;
	    String message   						= "";
	    String paGroupCode 						= paramMap.getString("paGroupCode");
	    
	    try{
			//= 중복 실행 Check
			log.info("===== 무통장 자동 취소API END =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			cancelList = paGmktCancelService.selectUnpaidPreOrderList(paramMap);
					
			for(HashMap<String,Object> cancel : cancelList) {
				try{
					pagmkcancel = paGmktCancelService.setPagmktCancelVo2(cancel, paramMap);
					paGmktCancelService.saveCancelCompleteListTx(pagmkcancel);		
				}catch(Exception e ){
					failCount++;
				}
		    }
		    
		    message = CommonUtil.setResultMessage("무통장 자동 취소", failCount);	
			CommonUtil.failCheck(message, paramMap);
			
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
			log.info("===== 무통장 자동 취소API END =====");
		}
	    
		paGmktOrderController.cancelInputMain(paGroupCode); //= 취소데이터 생성

		return new ResponseEntity<ResponseMsg>	(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	
	//= Information 상담원 취소완료 및 직권 취소 처리
	//= Called by   setMakerClaimList
	private ResponseEntity<?> setClaimCompleted(PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{
		String duplicateCheck = "";
		String paGroupCode    = paramMap.getString("paGroupCode");
		int	   count 	  = Constants.PA_GMKT_CONTRACT_CNT;
	    String broadCode  = Constants.PA_GMKT_BROAD_CODE;
	  	String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
	  	String paCode = null;
	  	String message = "";
	
		try{
			//= 중복 실행 Check
			log.info("===== 취소 완료 목록 조회 API Start =====");

			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			
			for (int i= 0; i < count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				message += saveCancelCompleteList(rest,paramMap);
			}
			
			CommonUtil.failCheck(message, paramMap);
			
		}catch (Exception se) {
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
			log.info("===== 취소 완료 목록 조회 API END =====");
		}
		
		paGmktOrderController.cancelInputMain(paGroupCode); //= 취소데이터 생성
		
		return new ResponseEntity<ResponseMsg>	(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
		
	
	//= Information 취소완료목록  API 통신 및 결과 TPAGMKTCANCELLIST Insert  
	//= Called by   setClaimCompleted
	private String saveCancelCompleteList(PaGmktAbstractRest rest,  ParamMap paramMap) throws Exception{
		ArrayList<HashMap<String, Object>> cancelList = new ArrayList<HashMap<String, Object>>();
		PaGmkCancel  pagmkcancel = new PaGmkCancel();
		int successCount  = 0;
		int failCount 	  = 0;
		
		cancelList = getConnectionAndResponseForCancel(rest,paramMap);

		
		for(HashMap<String, Object> cancelMap : cancelList){
			try{	
				pagmkcancel = paGmktCancelService.setPagmktCancelVo(cancelMap, paramMap);
				successCount += paGmktCancelService.saveCancelCompleteListTx(pagmkcancel);
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
				failCount++;
				continue;
			}
		} //end of for
		
		return CommonUtil.setResultMessage("직권 취소", paramMap.getString("paCode"), successCount, failCount);	
	}
	
	
	//= Information G마켓 취소 통신 후 결과값을 cancelList으로 생성.
	@SuppressWarnings("unchecked")
	private ArrayList<HashMap<String, Object>> getConnectionAndResponseForCancel(PaGmktAbstractRest rest,  ParamMap paramMap ) throws Exception{

		Map<String, Object> mainMap = new HashMap<String, Object>();
		String response = restUtil.getConnection(rest,  paramMap);
		mainMap = ComUtil.splitJson(response);
		if(mainMap == null) throw processException("ERROR");
		return (ArrayList<HashMap<String, Object>>) mainMap.get("Data");
	}
			
}
