package com.cware.api.paqeen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.paqeen.message.ProductConfirmResoponseMsg;
import com.cware.netshopping.paqeen.service.PaQeenProductService;

import io.swagger.annotations.Api;

@Api(value="/paqeen/product", description="퀸잇 상품")
@RestController
@RequestMapping(path = "/paqeen/product")
public class PaQeenProductApi {

	@Autowired
	PaQeenProductService paQeenProductService;
	
	/**
	 * 퀸잇 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paQeenProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);	
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	
	}
	
	/**
	 * 퀸잇 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paQeenProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);	
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 퀸잇 상품 승인(등록/수정 후 필수)
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="confirm")
	public ResponseEntity<?> confirm(@RequestParam(required = true) String goodsCode, @RequestParam(required = true) Integer productProposalId, @RequestParam(required = true) String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ProductConfirmResoponseMsg result = paQeenProductService.confirmProduct(goodsCode, productProposalId ,paCode, procId, transBatchNo);	
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 퀸잇 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId) {
		
		ResponseMsg result = paQeenProductService.transProduct(goodsCode, paCode, procId);
	
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
