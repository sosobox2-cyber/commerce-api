package com.cware.api.pafaple.controller;

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
import com.cware.netshopping.pafaple.service.PaFapleDeliveryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pafaple/delivery", description="패션플러스 배송")
@Controller("com.cware.api.pafaple.PaFapleDeliveryController")
@RequestMapping(path = "/pafaple/delivery")
public class PaFapleDeliveryController extends AbstractController{
	
	@Autowired
	@Qualifier("pafaple.delivery.paFapleDeliveryService")
	private PaFapleDeliveryService paFapleDeliveryService;
	
	
	@ApiOperation(value = "주문 목록 조회", notes = "주문 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 목록 조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-info")
	public ResponseEntity<?> getOrderInfo(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false) String fromDate,
		    @RequestParam(value = "toDate", required = false) String toDate) throws Exception {  
		
		paFapleDeliveryService.getOrderList(fromDate, toDate, request);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "배송접수", notes = "배송접수", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 목록 조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-confirm")
	public ResponseEntity<?> orderConfirm(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false) String fromDate,
		    @RequestParam(value = "toDate", required = false) String toDate) throws Exception {  
		
		paFapleDeliveryService.orderConfirm(fromDate, toDate, request);

		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	
	@ApiOperation(value = "송장입력 (발송처리)", notes = "송장입력 (발송처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "배송 접수 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/slip-out-proc")
	public ResponseEntity<?> slipOutProc(HttpServletRequest request,
			 @RequestParam(value = "order_id", required = false	) String order_id,
			 @RequestParam(value = "item_id", required = false	) String item_id ) throws Exception {  
		
		ResponseMsg result = paFapleDeliveryService.slipOutProc(order_id, item_id, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
}
