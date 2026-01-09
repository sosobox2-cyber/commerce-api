package com.cware.api.paintp.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.paintp.v2.service.PaIntpProductService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/paintp/v2/product")
public class PaIntpProductApi {

	@Autowired
	PaIntpProductService paIntpProductService;

	/**
	 * 인터파크 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENINTPAPI") String procId) {
		
		ResponseMsg result = paIntpProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 인터파크 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paIntpProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 인터파크 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paIntpProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
