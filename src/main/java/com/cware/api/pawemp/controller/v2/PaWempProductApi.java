package com.cware.api.pawemp.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pawemp.v2.service.PaWempProductService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/pawemp/v2/product")
public class PaWempProductApi {

	@Autowired
	PaWempProductService paWempProductService;

	/**
	 * 위메프 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENWEMPAPI") String procId) {
		
		ResponseMsg result = paWempProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 위메프 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENWEMPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paWempProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 위메프 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENWEMPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paWempProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
