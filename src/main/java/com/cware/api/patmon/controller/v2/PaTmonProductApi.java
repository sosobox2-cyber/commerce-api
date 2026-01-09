package com.cware.api.patmon.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.patmon.v2.service.PaTmonProductService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/patmon/v2/product")
public class PaTmonProductApi {

	@Autowired
	PaTmonProductService paTmonProductService;

	/**
	 * 티몬 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTMONAPI") String procId) {
		
		ResponseMsg result = paTmonProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 티몬 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTmonProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 티몬 옵션 등록
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/option/register")
	public ResponseEntity<?> registerOption(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTmonProductService.registerProductOption(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
	/**
	 * 티몬 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTmonProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
