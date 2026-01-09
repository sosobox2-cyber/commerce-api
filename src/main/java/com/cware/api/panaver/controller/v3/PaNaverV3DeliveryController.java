package com.cware.api.panaver.controller.v3;

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
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller("com.cware.api.panaver.v3.PaNaverV3DeliveryController")
@RequestMapping(path = "/panaver/v3/delivery")
public class PaNaverV3DeliveryController extends AbstractController{
	
	
	@Autowired 
	@Qualifier("panaver.v3.delivery.paNaverV3DeliveryService")
	private PaNaverV3DeliveryService paNaverV3DeliveryService;
	
	/**
	 * 발주 확인 처리
	 * 
	 * @param request
	 * @param changedProductOrderInfo
	 * @param procId
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "발주 확인 처리", notes = "발주 확인 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "발주 처리 요청 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-confirm-proc")
	public ResponseEntity<?> getOrderConfirmProc(HttpServletRequest request,
			@RequestParam(value = "productOrderId", required = true) String productOrderId,
			@RequestParam(defaultValue = "PANAVER") String procId) throws Exception {  
		
		ResponseMsg result = paNaverV3DeliveryService.orderConfirmProc(productOrderId, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	
	/**
	 * 발송  처리
	 * 
	 * @param request
	 * @param procId
	 * @param productOrderId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "발송 처리", notes = "발송 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "발송 처리 요청 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/slip-out-proc")
	public ResponseEntity<?> getSlipOutProc(HttpServletRequest request,
			@RequestParam(defaultValue = "PANAVER") String procId,
			@RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId) throws Exception {  
		
		ResponseMsg result = paNaverV3DeliveryService.slipOutProc(procId, request, productOrderId);

		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	/**
	 * 발송  지연 처리
	 * 
	 * @param request
	 * @param procId
	 * @param productOrderId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "발송 지연 처리", notes = "발송 지연 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "발송 지연 처리  실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/delivery-delay-proc")
	public ResponseEntity<?> getDeliveryDelyProc(HttpServletRequest request,
			@RequestParam(defaultValue = "PANAVER") String procId,
			@RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId) throws Exception {  
		
		ResponseMsg result = paNaverV3DeliveryService.deliveryDelayProc(procId, request, productOrderId);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
}
