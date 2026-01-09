package com.cware.api.pa11st.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pa11st.v2.service.Pa11stSellerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/v2/seller", description="출고v2")
@RestController
@RequestMapping(path = "/pa11st/v2/seller")
public class Pa11stSellerApi {

	@Autowired
	Pa11stSellerService pa11stSellerService;

	/**
	 * 11번가 출고/회수지 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 출고/회수지 등록", notes = "11번가 출고/회수지 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/address/register")
	public ResponseEntity<?> register(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paAddrGb, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = pa11stSellerService.registerEntpSlip(entpCode, entpManSeq, paAddrGb, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 11번가 출고/회수지 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 출고/회수지 수정", notes = "11번가 출고/회수지 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/address/update")
	public ResponseEntity<?> update(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paAddrGb, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = pa11stSellerService.updateEntpSlip(entpCode, entpManSeq, paAddrGb, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 11번가 출고지배송비 연동
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 출고지배송비 연동", notes = "11번가 출고지배송비 연동", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/shipcost/trans")
	public ResponseEntity<?> transShipCost(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String shipCostCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = pa11stSellerService.transShipCost(entpCode, entpManSeq, shipCostCode, paCode, procId,
				transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
