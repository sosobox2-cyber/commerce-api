package com.cware.api.pagmktv2.controller;

import java.util.HashMap;
import java.util.List;

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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.pagmkt.cancel.service.PaGmktCancelService;
import com.cware.netshopping.pagmkt.order.service.PaGmktOrderService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktCancelRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/order", description="G마켓 주문")
@Controller("com.cware.api.pagmktv2.PaGmktV2OrderController")
@RequestMapping(value="/pagmktv2/order")
public class PaGmktV2OrderController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.order.PaGmktOrderService")
	private PaGmktOrderService paGmktOrderService;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController paGmktAsycController;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	    
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "pagmkt.cancel.PaGmktCancelService")
	private PaGmktCancelService paGmktCancelService;
	
	
	
	/**
	 * G마켓 무통장 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "G마켓 무통장 주문접수 데이터 생성", notes = "G마켓 무통장 주문접수 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/preOrder-input", method = RequestMethod.GET)
	@ResponseBody
	public void preOrderInputMain(HttpServletRequest request, String paGroupCode) throws Exception {
		String duplicateCheck = "";
		HashMap<String, String> hmSheet = null;
		ParamMap paramMap = new ParamMap();
		String   message = "";
		String apiCode   	= "PAGMKT_PREORDER_INPUT";
		int failCount = 0;	
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		String isLocalYn 	= CommonUtil.getLocalOrNot(request);
		
		try{
			
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
				
			//= 주문 미처리 리스트 조회	
			List<Object> orderInputTargetList = paGmktOrderService.selectPreOrderInputTargetList(paramMap);
			
			if(orderInputTargetList == null){
				message = "selectOrderInputTargetList Size = 0" ;
			}
			//= 조회된 데이터를 통해 Async 호출	
			for(Object orderMap : orderInputTargetList){
				hmSheet = new HashMap<>();
				hmSheet = (HashMap<String, String>) orderMap;
				try{
					paGmktAsycController.orderInputAsync(hmSheet, isLocalYn);
				}catch(Exception e){
					failCount++;
					continue;
				}
			}//End of for
			
			message += CommonUtil.setResultMessage(apiCode, failCount);
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
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
		}		
	}
	
	/**
	 * G마켓 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "G마켓 주문접수 데이터 생성 ", notes = "G마켓 주문접수 데이터 생성 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-input/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public void orderInputMain(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode ) throws Exception {
		int failCount 			= 0;
		String   message 		= "";
		String duplicateCheck 	= "";
		HashMap<String, String> hmSheet = null;
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, "PAGMKT_ORDER_INPUT");
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		String apiCode   	= paramMap.getString("apiCode");
		String isLocalYn 	= CommonUtil.getLocalOrNot(request);
		String promoAllowTerm = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );	// 프로모션 연동 종료 건 조회 허용 시간 
		
		try{			
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			//= 주문 미처리 리스트 조회	
			List<Object> orderInputTargetList = paGmktOrderService.selectOrderInputTargetList(paramMap);
			if(orderInputTargetList == null){
				message = "selectOrderInputTargetList Size = 0" ;
			}
			//= 조회된 데이터를 통해 Async 호출	
			for(Object orderMap : orderInputTargetList){
				hmSheet = new HashMap<>();
				hmSheet = (HashMap<String, String>) orderMap;
				hmSheet.put("PAPROMO_ALLOW_TERM", promoAllowTerm);
				
				try{
					paGmktAsycController.orderInputAsync(hmSheet, isLocalYn);
				}catch(Exception e){
					failCount++;
					continue;
				}
			}//End of for
			message += CommonUtil.setResultMessage(apiCode , failCount);
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
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
		}		
	}
	
	
	/**
	 * G마켓 주문취소 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "G마켓 주문취소 데이터 생성", notes = "G마켓 주문취소 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-input", method = RequestMethod.GET)
	@ResponseBody
	public void cancelInputMain(String paGroupCode) throws Exception{ 
		String duplicateCheck 				= "";
		String prg_id 						= "PAGMKT_CANCEL_INPUT";
		HashMap<String, Object> hmSheet 	= null;
		ParamMap paramMap 					= new ParamMap();
		String message 						= "";
		int 	failCount					= 0;
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paGroupCode"	, paGroupCode);

		CommonUtil.setParams(paramMap);

		try{
			
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
				
			//= 주문 미처리 리스트 조회	
			List<Object> cancelInputTargetList  = paGmktOrderService.selectCancelInputTargetList(paramMap);
			
			if(cancelInputTargetList == null){
				message = "selectOrderInputTargetList Size = 0" ;
			}

			//= 조회된 데이터를 통해 Async 호출	
			for(int i = 0; cancelInputTargetList.size() > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) cancelInputTargetList.get(i);
					paGmktAsycController.cancelInputAsync(hmSheet);
				} catch (Exception e) {
					failCount++;
					continue;
				}
			}//End of for

			message += CommonUtil.setResultMessage("PAGMKT_CANCEL_INPUT", failCount);
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
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}		
	}
	
	/**
	 * G마켓 취소 승인
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public String confirmCancel(List<Object> cancelList, PaGmktAbstractRest rest, ParamMap paramMap) {

		HashMap<String, Object> cancelMap = null;
		int errorCount = 0;
		int cancelConfirmCount = 0;
		int cancelRefuseCount  = 0;
		int cancelHoldCount    = 0;
		String status = null;
		String message = null;
		
		for( Object ob : cancelList ){
			
			cancelMap = (HashMap<String, Object>) ob;
			
			try{
				
				status = paGmktCancelService.saveCancelConfirmProcTx(cancelMap , rest, paramMap);  
				
				switch(status){
					case "CONFIRM":
						cancelConfirmCount++;
					break;
					case "REFUSE":
						cancelRefuseCount++;
					break;
					default:
						cancelHoldCount ++;
					break;	
				}
				
			}catch(Exception e){
				errorCount ++;
				continue;
			}
			
		}//end of for	
		message = "취소 승인 : " + cancelConfirmCount + "건 취소 거부 : " + cancelRefuseCount + "건 미처리중 : " + cancelHoldCount + "건 오류발생 : " + errorCount + "건"; 
		return message;
	}
	
	/**
	 * G마켓 주문거부(비 품절 처리)
	 * @return Map
	 * @throws Exception
	 */	
	@ApiOperation(value = "G마켓 주문거부(비 품절 처리)", notes = "G마켓 주문거부(비 품절 처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-claim-Cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> refusalOrderNotSoldOut(
			@ApiParam(name = "contrNo", value = "주문번호", defaultValue = "") 	@RequestParam(value = "contrNo",	required = true) String contrNo,				
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value = "paCode",		required = true) String paCode,
			HttpServletRequest request) throws Exception{
		
		PaGmktAbstractRest rest = new PaGmktCancelRest();
		String apiCode = "IF_PAGMKTAPI_V2_02_004";
		String duplicateCheck = "";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("urlParameter", contrNo);
		paramMap.put("startDate", systemService.getSysdatetimeToString());		
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", paCode);
		 
		try{		
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
	
			//=  실질적으로 api를 호출하여 각 Order를  주문 확인(배송 준비중) 상태로 만들어 주는 부분
			restUtil.getConnection(rest,  paramMap);
			
			paramMap.put("code", "200");    
			paramMap.put("message", contrNo + "판매 취소 성공(비 품절 처리)");
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
		}		

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * G마켓 주문거부(품절처리)
	 * @return Map
	 * @throws Exception
	 */	
	@ApiOperation(value = "G마켓 주문거부(품절처리)", notes = "G마켓 주문거부(품절처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-claim-Cancel-SoldOut", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> refusalOrderForSoldOut(
			@ApiParam(name = "contrNo", value = "주문번호", defaultValue = "") 	@RequestParam(value = "contrNo",	required = true) String contrNo,				
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value = "paCode",		required = true) String paCode,
			HttpServletRequest request) throws Exception{

		PaGmktAbstractRest rest = new PaGmktCancelRest();
		String apiCode = "IF_PAGMKTAPI_V2_02_003";
		String duplicateCheck = "";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("urlParameter", contrNo);
		
		paramMap.put("startDate", systemService.getSysdatetimeToString());		
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", paCode);
		
				
		try{		
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
	
			restUtil.getConnection(rest,  paramMap);
			
			paramMap.put("message", contrNo + "판매 취소 성공(품절 처리)");
			paramMap.put("message", "OK");
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
		}		

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "G마켓 무통장 주문접수 입금 데이터 처리", notes = "G마켓 무통장 주문접수 입금 데이터 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-update/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public void orderUpdateMain(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode ) throws Exception {
		String duplicateCheck = "";
		HashMap<String, String> hmSheet = null;
		String   message = "";
		int failCount = 0;
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, "PAGMKT_ORDER_UPDATE");
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		String apiCode = paramMap.getString("apiCode");
		String isLocalYn 	= CommonUtil.getLocalOrNot(request);

		
		try{
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
				
			//= 결제완료 무통장 주문  미처리 리스트 조회	
			List<Object> preOrderInputTargetList = paGmktOrderService.selectPreOrderUpdateTargetList(paramMap);
			
			if(preOrderInputTargetList == null){
				message = "selectPreOrderInputTargetList Size = 0" ;
			}
			//= 조회된 데이터를 통해 Async 호출	
			for(Object orderMap : preOrderInputTargetList){
				hmSheet = new HashMap<>();
				hmSheet = (HashMap<String, String>) orderMap;
				hmSheet.put("isLocalYn"	,	isLocalYn);
				try{
					//아직 G마켓으로 부터 취소된 무통장이 처리되지 않으면 일단 Update 보류
					if(!CommonUtil.existUnAttendedCount(hmSheet.get("PA_ORDER_NO").toString())){
						paGmktAsycController.preOrderUpdateAsync(hmSheet);
					}
				}catch(Exception e){
					failCount++;
					continue;
				}
			}//End of for
			
			message += CommonUtil.setResultMessage(apiCode , failCount);
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
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
		}				
	}
}
