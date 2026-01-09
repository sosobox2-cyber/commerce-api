package com.cware.api.paqeen.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.cware.netshopping.paqeen.service.PaQeenCancelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/paqeen/cancel", description="퀸잇 취소")
@Controller
@RequestMapping(value = "/paqeen/cancel")
public class PaQeenCancelController extends AbstractController {
	
	@Autowired
	@Qualifier("paqeen.cancel.paQeenCancelService")
	private PaQeenCancelService paQeenCancelService;
	
	@Autowired
	private PaQeenClaimController paQeenClaimController;
	
	/*취소신청,승인 대상 수집 API*/
	@ApiOperation(value = "취소 대상 조회  API", notes = "20: 취소 신청, 21 : 취소 완료", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-list/{cancelStatus}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> getcancelList(
			@PathVariable("cancelStatus") String cancelStatus,
			@RequestParam(value = "satetAt", required = false, defaultValue = "") String satetAt,
			@RequestParam(value = "endAt", required = false, defaultValue = "") String endAt,
			HttpServletRequest request) throws Exception {
		
		// STEP 1. 취소 대상 조회 
		paQeenCancelService.getCancelList( satetAt, endAt, request, cancelStatus);
		
		// STEP 2. 취소 승인 
		paQeenCancelService.cancelConfirmProc(request);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/*취소 반려 API*/
/*
	@ApiOperation(value = "취소 반려  API", notes = "", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-refusal-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> cancelRefusalProc(
			HttpServletRequest request) throws Exception {
		
		paQeenCancelService.cancelRefusalProc(request);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	*/
	
	/**
	 * 취소승인
	 * @param claimNo
	 * @param orderNo
	 * @param optionManagementCd
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "취소승인 API (BO호출용)", notes = "취소승인 API (BO호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-approval-proc-bo", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(
			@RequestParam(value = "orderId", 		    required = true) String orderId,
			@RequestParam(value = "ticketId", 		    required = true) String ticketId,
			@RequestParam(value = "quantity",			required = true) String quantity,
			@RequestParam(value = "paCode", 		    required = true) String paCode,
			HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		requestMap.put("PA_CODE", paCode);
		requestMap.put("TICKET_ID", ticketId);
		requestMap.put("ORDER_ID", orderId);
		requestMap.put("QUANTITY", quantity);
		requestMap.put("OUT_BEF_CLAIM_GB", "1");
		requestMap.put("CLAIM_STATUS", "20");
		apiInfoMap = paQeenCancelService.cancelConfirmBo(requestMap, request);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 일괄취소반품
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "일괄취소반품 API", notes = "일괄취소반품 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/orderSoldOut", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paGroupCode", "15");
		// STEP1. 품절취소반품, 일괄취소반품 대상 조회 
		List<HashMap<String, Object>> cancelList = paQeenCancelService.selectPaQeenSoldOutordList(paramMap);
		for(HashMap<String, Object> cancelItem : cancelList) {
			// STEP2. 대상 취소 요청  
			paQeenCancelService.cancelRequest(cancelItem, request);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 모바일 자동취소 (일괄취소반품)
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = "모바일 자동취소 (일괄취소반품)", notes = "모바일 자동취소 (일괄취소반품)", httpMethod = "GET", produces = "application/json")
		@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request) throws Exception {
		
		// STEP1. 품절취소반품, 일괄취소반품 대상 조회 
		List<HashMap<String, String>> cancelList = paQeenCancelService.selectPaMobileOrderAutoCancelList();
		for(HashMap<String, String> cancelItem : cancelList) {
			// STEP2. 대상 취소 요청  
			paQeenCancelService.mobileOrderSoldOut(cancelItem, request);
		}
		// STEP3. 취소 완료 조회  API 호출
		getcancelList("21", "", "", request);
		
		// STEP4. 반품대상수집  API 호출
		paQeenClaimController.getReturnList(request, "30", "", "");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
