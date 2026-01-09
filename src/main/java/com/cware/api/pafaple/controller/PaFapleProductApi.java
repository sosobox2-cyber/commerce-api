package com.cware.api.pafaple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pafaple.service.PaFapleProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pafaple/product", description="패션플러스 상품 입점 및 수정")
@RestController
@RequestMapping(path = "/pafaple/product")
public class PaFapleProductApi {

	@Autowired
	PaFapleProductService paFapleProductService;
	
	/**
	 * 패션플러스 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "패션플러스 상품 신규입점", notes = "패션플러스 상품 신규입점", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo,
			@RequestParam(defaultValue = "INSERT") String modCase) {
		
		if(modCase.equals("INSERT") || modCase.equals("BRAND")) {
			ResponseMsg result = paFapleProductService.registerProduct(goodsCode, paCode, procId, transBatchNo,modCase);	
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("400", "잘못된 modCase"), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * 패션플러스 상품 수정
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "패션플러스 상품 수정", notes = "패션플러스 상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 패션플러스 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "패션플러스 상품 개별 전송", notes = "패션플러스 상품 개별 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId) {
		
		ResponseMsg result = paFapleProductService.transProduct(goodsCode, paCode, procId);
	
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 패션플러스 브랜드 변경으로 인해 상품판매 중단된 상품  >  신규 브랜드로 새로 등록
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "신규 브랜드로 새로 등록", notes = "패션플러스 브랜드 변경으로 인해 상품판매 중단된 상품  > 신규 브랜드로 새로 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/brandReRegister")
	public ResponseEntity<?> brandReRegister( @RequestParam String paCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleProductService.brandReRegisterProduct(paCode, procId);	
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
