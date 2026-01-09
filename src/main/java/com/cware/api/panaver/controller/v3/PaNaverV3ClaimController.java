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
import com.cware.netshopping.panaver.v3.service.PaNaverV3ClaimService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller("com.cware.api.panaver.v3.PaNaverV3ClaimController")
@RequestMapping(path = "/panaver/v3/claim")
public class PaNaverV3ClaimController extends AbstractController{
	
	
	@Autowired 
	@Qualifier("panaver.v3.claim.paNaverV3ClaimService")
	private PaNaverV3ClaimService paNaverV3ClaimService;
	
	/**
	 * 반품 승인
	 * 
	 * @param request
	 * @param procId
	 * @param productOrderId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "반품 승인", notes = "반품 승인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "반품 승인 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/return-confirm-proc")
	public ResponseEntity<?> getReturnConfirmProc(HttpServletRequest request,
			@RequestParam(defaultValue = "PANAVER") String procId,
			@RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId) throws Exception {  
		
		ResponseMsg result = paNaverV3ClaimService.returnConfirmProc(procId, request, productOrderId);

		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 반품 보류
	 * 
	 * @param request
	 * @param productOrderId
	 * @param holdbackClassType
	 * @param holdbackReturnDetailReason
	 * @param extraReturnFeeAmount
	 * @param procId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "반품 보류", notes = "반품 보류", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "반품 보류 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/return-holdback-proc")
	public ResponseEntity<?> getReturnHoldbackProc(HttpServletRequest request,
			@RequestParam(value = "productOrderId", required = true) String productOrderId,
			@RequestParam(value = "holdbackClassType", defaultValue = "ETC") String holdbackClassType,
			@RequestParam(value = "holdbackReturnDetailReason", defaultValue = "기타") String holdbackReturnDetailReason,
			@RequestParam(value = "extraReturnFeeAmount", defaultValue = "0") Double extraReturnFeeAmount,
			@RequestParam(defaultValue = "PANAVER") String procId) throws Exception {  
		
		ResponseMsg result = paNaverV3ClaimService.returnHoldbackProc(productOrderId,holdbackClassType, holdbackReturnDetailReason, extraReturnFeeAmount, procId, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 반품 보류 해제
	 * 
	 * @param request
	 * @param procId
	 * @param productOrderId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "반품 보류 해제", notes = "반품 보류 해제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "반품 보류 해제 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/return-holdback-release-proc")
	public ResponseEntity<?> getReturnHoldbackReleaseProc(HttpServletRequest request,
			@RequestParam(value = "productOrderId", required = true) String productOrderId,
			@RequestParam(defaultValue = "PANAVER") String procId) throws Exception {  
		
		ResponseMsg result = paNaverV3ClaimService.returnHoldbackReleaseProc(productOrderId,procId, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	
}
