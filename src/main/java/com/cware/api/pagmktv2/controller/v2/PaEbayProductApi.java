package com.cware.api.pagmktv2.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pagmkt.v2.domain.Product;
import com.cware.netshopping.pagmkt.v2.service.PaEbayProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/paebay/v2/product", description="이베이 상품")
@RestController
@RequestMapping(path = "/paebay/v2/product")
public class PaEbayProductApi {

	@Autowired
	PaEbayProductService paEbayProductService;

	/**
	 * 이베이 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "이베이 상품 개별 전송", notes = "이베이 상품 개별 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam String goodsCode, 
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId) {
		
		ResponseMsg result = paEbayProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 이베이 상품조회
	 * 
	 * @param goodsCode
	 * @param esmGoodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@ApiOperation(value = "이베이 상품조회", notes = "이베이 상품조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping
	public ResponseEntity<?> info(@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam String goodsCode, 
			@ApiParam(name = "esmGoodsCode", value = "제휴사상품코드", defaultValue = "") 				@RequestParam String esmGoodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 						@RequestParam String paCode, 
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") 			@RequestParam(defaultValue = "ENEBAYAPI") String procId,
			@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0") 			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		Product product = paEbayProductService.getProduct(goodsCode, esmGoodsCode, paCode, "02,03", procId,
				transBatchNo);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	/**
	 * 이베이 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "이베이 상품 신규입점", notes = "이베이 상품 신규입점", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="register")
	public ResponseEntity<?> register(@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam String goodsCode, 
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId,
			@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0")@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paEbayProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 이베이 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "이베이 상품 수정", notes = "이베이 상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="update")
	public ResponseEntity<?> update(@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam String goodsCode, 
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "ENEBAYAPI") @RequestParam(defaultValue = "ENEBAYAPI") String procId,
			@ApiParam(name = "transBatchNo", value = "transBatchNo", defaultValue = "0")@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paEbayProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
