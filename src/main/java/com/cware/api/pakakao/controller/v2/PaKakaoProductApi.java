package com.cware.api.pakakao.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pakakao.v2.domain.ProductRequest;
import com.cware.netshopping.pakakao.v2.service.PaKakaoProductService;

@RestController
@RequestMapping(path = "/pakakao/v2/product")
public class PaKakaoProductApi {

	@Autowired
	PaKakaoProductService paKakaoProductService;

	/**
	 * 카카오 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENKAKAOAPI") String procId) {
		
		ResponseMsg result = paKakaoProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 카카오 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENKAKAOAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paKakaoProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 카카오 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENKAKAOAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paKakaoProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 카카오 상품 조회
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/info")
	public ResponseEntity<?> info(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam String productId,
			@RequestParam(defaultValue = "ENKAKAOAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ProductRequest product = paKakaoProductService.getProduct(goodsCode, productId, paCode, procId, transBatchNo);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

}
