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
import com.cware.netshopping.panaver.v3.service.PaNaverV3ExchangeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.panaver.v3.PaNaverV3ExchangeController")
@RequestMapping(path = "/panaver/v3/exchange")
public class PaNaverV3ExchangeController extends AbstractController{
	
	@Autowired
	@Qualifier("panaver.v3.exchange.paNaverV3ExchangeService")
	private PaNaverV3ExchangeService paNaverV3ExchangeService;
	
	/**
	 * 교환 수거 완료 처리
	 * 
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "교환 수거 완료 처리", notes = "교환 수거 완료 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/collect-approval")
	public ResponseEntity<?> collect(
			@ApiParam(name = "productOrderId", required = false, value = "상품주문번호") @RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3ExchangeService.approvalCollect(productOrderId, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 교환 재배송 처리
	 * 
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "교환 재배송 처리", notes = "교환 재배송 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/dispatch")
	public ResponseEntity<?> dispatch(
			@ApiParam(name = "productOrderId", required = false, value = "상품주문번호") @RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3ExchangeService.dispatch(productOrderId, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 교환 거부(철회)
	 * 
	 * @param productOrderId
	 * @param rejectExchangeReason
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "교환 거부(철회)", notes = "교환 거부(철회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/reject")
	public ResponseEntity<?> reject(
			@ApiParam(name = "productOrderId", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = true) String productOrderId,
			@ApiParam(name = "rejectExchangeReason", required = true, value = "교환 거부 사유") @RequestParam(value = "rejectExchangeReason", required = true) String rejectExchangeReason,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3ExchangeService.reject(productOrderId, rejectExchangeReason, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 교환 보류
	 * 
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "교환 보류", notes = "교환 보류", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/holdback")
	public ResponseEntity<?> holdback(
		    @ApiParam(name = "productOrderId", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = true) String productOrderId,
		    @ApiParam(name = "holdbackClassType", required = false, value = "보류 유형") @RequestParam(value = "holdbackClassType", required = false, defaultValue = "ETC") String holdbackClassType,
		    @ApiParam(name = "holdbackExchangeDetailReason", required = false, value = "보류 상세 사유") @RequestParam(value = "holdbackExchangeDetailReason", required = false, defaultValue = "DELIVERY") String holdbackExchangeDetailReason,
		    @ApiParam(name = "extraExchangeFeeAmount", required = false, value = "기타 교환 비용") @RequestParam(value = "extraExchangeFeeAmount", required = false, defaultValue = "0") Double extraExchangeFeeAmount,
		    @ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3ExchangeService.holdback(productOrderId, holdbackClassType, holdbackExchangeDetailReason, extraExchangeFeeAmount, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 교환 보류 해제
	 * 
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "교환 보류 해제", notes = "교환 보류 해제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/holdback-release")
	public ResponseEntity<?> release(
			@ApiParam(name = "productOrderId", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = true) String productOrderId,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3ExchangeService.releaseHoldback(productOrderId, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
