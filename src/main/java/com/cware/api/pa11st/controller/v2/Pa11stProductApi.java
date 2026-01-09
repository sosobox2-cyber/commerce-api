package com.cware.api.pa11st.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pa11st.v2.domain.Product;
import com.cware.netshopping.pa11st.v2.service.Pa11stProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/v2/product", description="상품v2")
@RestController
@RequestMapping(path = "/pa11st/v2/product")
public class Pa11stProductApi {

	@Autowired
	Pa11stProductService pa11stProductService;

	/**
	 * 11번가 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "11번가 상품 개별 전송", notes = "11번가 상품 개별 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId) {
		
		ResponseMsg result = pa11stProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 11번가 상품조회
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 상품조회", notes = "11번가 상품조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping
	public ResponseEntity<?> info(@RequestParam String goodsCode, @RequestParam String productNo,
			@RequestParam String paCode, @RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		Product product = pa11stProductService.getProduct(goodsCode, productNo, paCode, procId, transBatchNo);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	/**
	 * 11번가 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "11번가 상품 신규입점", notes = "11번가 상품 신규입점", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = pa11stProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 11번가 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "11번가 상품 수정", notes = "11번가 상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = pa11stProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 11번가 상품 배송불가지역 적용
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 상품 배송불가지역 적용", notes = "11번가 상품 배송불가지역 적용", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/statImpossibleAddress")
	public ResponseEntity<?> statImpossibleAddress(@RequestParam String goodsCode, @RequestParam String productNo,
			@RequestParam String paCode, @RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		String result = pa11stProductService.statImpossibleAddress(goodsCode, productNo, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 11번가 상품 배송불가지역 해제
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "11번가 상품 배송불가지역 해제", notes = "11번가 상품 배송불가지역 해제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/stopImpossibleAddress")
	public ResponseEntity<?> stopImpossibleAddress(@RequestParam String goodsCode, @RequestParam String productNo,
			@RequestParam String paCode, @RequestParam(defaultValue = "EN11STAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		String result = pa11stProductService.stopImpossibleAddress(goodsCode, productNo, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
