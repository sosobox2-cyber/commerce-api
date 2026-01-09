package com.cware.api.paqeen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.paqeen.service.PaQeenDeliveryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/paqeen/delivery", description="퀸잇 주문조회/배송상태/송장번호변경")
@Controller("com.cware.api.paqeen.PaQeenDeliveryController")
@RequestMapping(path = "/paqeen/delivery")
public class PaQeenDeliveryController extends AbstractController{
	
	@Autowired
	@Qualifier("paqeen.delivery.paQeenDeliveryService")
	private PaQeenDeliveryService paQeenDeliveryService;
	
	
	@ApiOperation(value = "주문 목록 조회", notes = "주문 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 목록 조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-info")
	public ResponseEntity<?> getOrderInfo(HttpServletRequest request,
		    @RequestParam(value = "satetAt", required = false) String satetAt,
		    @RequestParam(value = "endAt"	, required = false) String endAt) throws Exception {  
		
		paQeenDeliveryService.getOrderList(satetAt, endAt, request);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "배송상태변경  API", notes = "WAIT:주문처리대기중, PREPARING:배송준비중, IN_DELIVERY:배송중, COMPLETED:배송완료, CANCELLED:배송취소", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-state", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> putOrderState(HttpServletRequest request,
		    @RequestParam(value = "deliveryStateEnum", required = false) String deliveryStateEnum ) throws Exception {  
		
		paQeenDeliveryService.putOrderState(deliveryStateEnum , request);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "송장번호 및 배송사 정보 수정  API", notes = "", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request ) throws Exception {
		
		paQeenDeliveryService.slipOutProc(request);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
