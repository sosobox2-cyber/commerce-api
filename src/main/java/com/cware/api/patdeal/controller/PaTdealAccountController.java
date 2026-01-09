package com.cware.api.patdeal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.patdeal.service.PaTdealAccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.patdeal.PaTdealAccountController")
@RequestMapping(value="/patdeal/account")
public class PaTdealAccountController extends AbstractController {
	
	@Autowired
	@Qualifier("patdeal.account.paTdealAccountService")
	private PaTdealAccountService paTdealAccountService;
	
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
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/case-settle-list-detail")
	public ResponseEntity<?> getTdealAccountSettlementList(HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate
			) throws Exception {  
		
		ResponseMsg result = paTdealAccountService.getTdealAccountSettlementList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
