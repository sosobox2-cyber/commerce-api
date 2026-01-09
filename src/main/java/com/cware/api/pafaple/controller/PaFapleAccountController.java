package com.cware.api.pafaple.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pafaple.service.PaFapleAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pafaple/account", description="패션플러스 정산")
@Controller("com.cware.api.pafaple.PaFapleAccountController")
@RequestMapping(value="/pafaple/Account")
public class PaFapleAccountController {

	@Autowired
	@Qualifier("pafaple.account.paFapleAccountService")
	private PaFapleAccountService paFapleAccountService;
	
	/**
	 * 판매분정산내역 조회
	 * @param stdYMD
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "판매분정산내역", notes = "판매분정산내역", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/case-order-settle-list", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> getFapleOrderSettlementList(
			@RequestParam(value = "stdYMD", required = false) String stdYMD,
			HttpServletRequest request) throws Exception {
		 ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		 responseMsg = paFapleAccountService.getOrderSettlementList(stdYMD,request);
		
		 return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/**
	 * 배송료정산내역 조회
	 * @param stdYMD
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "배송료정산내역", notes = "배송료정산내역", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/case-ship-settle-list", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> getFapleShipSettlementList(
			@RequestParam(value = "stdYMD", required = false) String stdYMD,
			HttpServletRequest request) throws Exception {
		 ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		 responseMsg = paFapleAccountService.getShipSettlementList(stdYMD,request);
		
		 return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
}
