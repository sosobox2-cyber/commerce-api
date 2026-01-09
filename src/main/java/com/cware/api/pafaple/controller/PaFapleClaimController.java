package com.cware.api.pafaple.controller;

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
import com.cware.netshopping.pafaple.service.PaFapleClaimService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

	@Api(value="/pafaple/claim", description="패션플러스 반품/교환")
	@Controller("com.cware.api.pafaple.PaFapleClaimController")
	@RequestMapping(value="/pafaple/claim")
	public class PaFapleClaimController extends AbstractController {

	@Autowired
	@Qualifier("pafaple.claim.paFapleClaimService")
	private PaFapleClaimService paFapleClaimService;
	
	/*반품 대상 수집  API*/
	@ApiOperation(value = "반품대상수집  API", notes = "1: 반품완료 , 2 : 반품미처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/30", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getReturnList(HttpServletRequest request,
			@RequestParam(value = "processFlag", required = true, defaultValue = "2") String processFlag,
			  @RequestParam(value = "fromDate", required = false) String fromDate,
			    @RequestParam(value = "toDate", required = false) String toDate) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		paFapleClaimService.getReturnList(processFlag, fromDate, toDate, request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/**
	 * 반품완료 및 회수송장
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품완료 및 회수송장등록  API", notes = "반품완료 및 회수송장등록  API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/return-slip-out-proc", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> returnSlipOutProc(HttpServletRequest request) throws Exception {
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		responseMsg = paFapleClaimService.returnCompleProc(request);
	
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/**
	 * 반품 철회 조회
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품 철회조회 API", notes = "반품 철회조회 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-status/31", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> getReturnCancelList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
		    @RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		responseMsg = paFapleClaimService.getReturnCancelList(fromDate, toDate, request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환 접수 대상 수집  API*/
	@ApiOperation(value = "교환 접수대상수집  API", notes = "40: 교환 접수, 41 :교환 철회, 42: 교환 배송중 조회 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getExchangeList(HttpServletRequest request,
			  @PathVariable("claimGb") String claimGb,
			  @RequestParam(value = "fromDate", required = false) String fromDate,
			  @RequestParam(value = "toDate", required = false) String toDate) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		responseMsg = paFapleClaimService.getExchangeList(fromDate, toDate, claimGb, request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환 철회 대상 수집  API*/
	@ApiOperation(value = "교환 철회대상수집  API", notes = "41: 교환취소", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/41", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getExchangeCancelList(HttpServletRequest request,
			  @RequestParam(value = "fromDate", required = false) String fromDate,
			    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		responseMsg = paFapleClaimService.getExchangeCancelList(fromDate, toDate, request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환 회수 송장 등록  API*/
	@ApiOperation(value = "교환 회수 송장 등록  API", notes = "45: 교환회수", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/slip-out-proc/45", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> exchangeSlipOutProc(HttpServletRequest request ) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		responseMsg = paFapleClaimService.exchangeSlipOutProc(request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환 발송 처리   API*/
	@ApiOperation(value = "교환 발송  API", notes = "교환 발송처리 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/exchange-slip-out-proc", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> exchangeSlipOutProcStart(HttpServletRequest request ) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		responseMsg = paFapleClaimService.exchangeSlipOutProcStart(request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
}
