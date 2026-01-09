package com.cware.api.patdeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.patdeal.service.PaTdealProductService;

@RestController
@RequestMapping(path = "/patdeal/product")
public class PaTdealProductApi {
	
	@Autowired
	PaTdealProductService paTdealProductService;
				
	/**
	 * 티딜 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTDEALAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTdealProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 상품 수정
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTDEALAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTdealProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);
		
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			//옵션 매핑
			paTdealProductService.manageOption(goodsCode, paCode, procId, transBatchNo);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
		
		ResponseMsg result = paTdealProductService.transProduct(goodsCode, paCode, procId);
	
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 티딜 상품 승인
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="confirm")
	public ResponseEntity<?> confirm(@RequestParam String goodsCode, @RequestParam String mallProductNo, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTDEALAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		String successYn = paTdealProductService.confirmProduct(goodsCode,mallProductNo, paCode, procId, transBatchNo);

		return new ResponseEntity<>(successYn, HttpStatus.OK);
	}
	
	/**
	 * 옵션 매핑
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/option")
	public ResponseEntity<?> option(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo)  {
		
		ResponseMsg result = paTdealProductService.manageOption(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
}
