package com.cware.api.pafaple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pafaple.service.PaFapleSellerService;

import io.swagger.annotations.Api;

@Api(value="/pafaple/seller", description="패션플러스 실적 브랜드 조회")
@RestController
@RequestMapping(path = "/pafaple/seller")
public class PaFapleSellerApi {
	@Autowired
	PaFapleSellerService paFapleSellerService;
	
	
	/**
	 * 패션플러스 실적 브랜드 조회
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/brand")
	public ResponseEntity<?> register(@RequestParam String paCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleSellerService.registerBrand(paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 패션플러스 배송처 등록
	 * @param paCode
	 * @param entpCode
	 * @param brandCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/addr/register")
	public ResponseEntity<?> addrRegister(@RequestParam String paCode, @RequestParam(defaultValue = "0") String entpCode,
			@RequestParam(defaultValue = "0") String brandCode, @RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleSellerService.registerAddr(paCode, entpCode, brandCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 패션플러스 배송처 수정
	 * @param paCode
	 * @param entpCode
	 * @param brandCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/addr/update")
	public ResponseEntity<?> addrUpdate(@RequestParam String paCode, @RequestParam(defaultValue = "0") String entpCode,
			@RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleSellerService.updateAddr(paCode, entpCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 패션플러스 실적 브랜드 등록
	 * @param paCode
	 * @param entpCode
	 * @param brandCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/brand/register")
	public ResponseEntity<?> brandRegister(@RequestParam String paCode, @RequestParam(defaultValue = "0") String entpCode
			, @RequestParam(defaultValue = "0") String brandCode, @RequestParam(defaultValue = "ENFAPLEAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paFapleSellerService.directRegisterBrand(paCode, entpCode, brandCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
