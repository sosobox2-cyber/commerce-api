package com.cware.api.pagmktv2.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pagmkt.v2.service.PaEbaySellerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/paebay/v2/seller", description="이베이 배송")
@RestController
@RequestMapping(path = "/paebay/v2/seller")
public class PaEbaySellerApi {

	@Autowired
	PaEbaySellerService paEbaySellerService;

	/**
	 * 이베이 출고/회수지 생성
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "이베이 출고/회수지 생성", notes = "이베이 출고/회수지 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/address/register")
	public ResponseEntity<?> registerAddress(@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam String entpCode, 
		@ApiParam(name = "entpManSeq", value = "업체담당자순번", defaultValue = "") 	@RequestParam String entpManSeq,
		@ApiParam(name = "paAddrGb", value = "담당자구분", defaultValue = "") 			@RequestParam String paAddrGb, 
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId,
		@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0")@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paEbaySellerService.registerEntpSlip(entpCode, entpManSeq, paAddrGb, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 이베이 출고/회수지 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paAddrGb
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "이베이 출고/회수지 수정", notes = "이베이 출고/회수지 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/address/update")
	public ResponseEntity<?> updateAddress(@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam String entpCode, 
		@ApiParam(name = "entpManSeq", value = "업체담당자순번", defaultValue = "") 	@RequestParam String entpManSeq,
		@ApiParam(name = "paAddrGb", value = "담당자구분", defaultValue = "") 			@RequestParam String paAddrGb, 
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId,
		@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0")@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paEbaySellerService.updateEntpSlip(entpCode, entpManSeq, paAddrGb, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 이베이 배송비정책 연동
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "이베이 배송비정책 연동", notes = "이베이 배송비정책 연동", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/shipcost/trans")
	public ResponseEntity<?> transShipCost(@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam String entpCode,
			@ApiParam(name = "entpManSeq", value = "업체담당자순번", defaultValue = "") 	@RequestParam String entpManSeq,
			@ApiParam(name = "shipCostCode", value = "shipCostCode", defaultValue = "") 	@RequestParam String shipCostCode, 
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId,
			@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0")@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paEbaySellerService.transShipCost(entpCode, entpManSeq, shipCostCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
