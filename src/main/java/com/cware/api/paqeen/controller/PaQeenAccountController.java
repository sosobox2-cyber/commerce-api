package com.cware.api.paqeen.controller;

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
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.paqeen.service.PaQeenAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(value="/paqeen/account", description="정산")
@Controller("com.cware.api.paqeen.PaQeenAccountController")
@RequestMapping(value="/paqeen/account")
public class PaQeenAccountController extends AbstractController {
	
	@Autowired
	@Qualifier("paqeen.account.paQeenAccountService")
	private PaQeenAccountService paQeenAccountService;
	
	/**
	 * 파트너 정산 상세 데이터 조회하기
	 * 
	 * @param request
	 * @param procId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "파트너 정산 상세 데이터 조회하기", notes = "파트너 정산 상세 데이터 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/case-settle-list-detail", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> getQeenAccountSettlementList(HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate
			) throws Exception {  
		
		ResponseMsg result = paQeenAccountService.getQeenAccountSettlementList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
