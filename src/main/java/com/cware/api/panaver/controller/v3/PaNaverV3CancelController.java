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
import com.cware.netshopping.panaver.v3.service.PaNaverV3CancelService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.panaver.v3.PaNaverV3CancelController")
@RequestMapping(path = "/panaver/v3/cancel")
public class PaNaverV3CancelController extends AbstractController{
	
	@Autowired
	@Qualifier("panaver.v3.cancel.paNaverV3CancelService")
	private PaNaverV3CancelService paNaverV3CancelService;
	
	/**
	 * 취소 요청 승인
	 * 
	 * @param orderId
	 * @param productOrderId
	 * @param outBefClaimGb
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "취소 요청 승인", notes = "취소 요청 승인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/approval")
	public ResponseEntity<?> approval(
			@ApiParam(name = "orderId", required = true, value = "주문 번호") @RequestParam(value = "orderId", required = true) String orderId,
			@ApiParam(name = "productOrderId", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = true) String productOrderId,
			@ApiParam(name = "outBefClaimGb", required = false, value = "반품 처리 구분") @RequestParam(value = "outBefClaimGb", required = false, defaultValue = "0") String outBefClaimGb,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3CancelService.approvalCancel(orderId, productOrderId, outBefClaimGb, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 취소 요청
	 * 
	 * @param productOrderId
	 * @param paOrderNo
	 * @param paOrderSeq
	 * @param cancelReasonCode
	 * @param paCode
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "취소 요청", notes = "취소 요청", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/request")
	public ResponseEntity<?> request(
			@ApiParam(name = "productOrderId", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = true) String productOrderId,
			@ApiParam(name = "paOrderNo", required = false, value = "제휴사 주문 번호") @RequestParam(value = "paOrderNo", required = false, defaultValue = "") String paOrderNo,
			@ApiParam(name = "paOrderSeq", required = false, value = "제휴사 주문 순번") @RequestParam(value = "paOrderSeq", required = false, defaultValue = "") String paOrderSeq,
			@ApiParam(name = "cancelReasonCode", required = false, value = "취소 사유 코드") @RequestParam(value = "cancelReasonCode", required = false, defaultValue = "2") int cancelReasonCode,
			@ApiParam(name = "paCode", required = false, value = "제휴사 구분 코드") @RequestParam(value = "paCode", required = false, defaultValue = "41") String paCode,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId,
			HttpServletRequest request) throws Exception {
		
		ResponseMsg result = paNaverV3CancelService.requestCancel(productOrderId, paOrderNo, paOrderSeq, paCode, cancelReasonCode, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
