package com.cware.api.patmon.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.patmon.v2.service.PaTmonSellerService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/patmon/v2/seller")
public class PaTmonSellerApi {

	@Autowired
	PaTmonSellerService paTmonSellerService;

	/**
	 * 티몬 배송지 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/address/register")
	public ResponseEntity<?> register(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTmonSellerService.registerEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 티몬 배송지 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/address/update")
	public ResponseEntity<?> update(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTmonSellerService.updateEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 티몬 배송템플릿 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/shippolicy/register")
	public ResponseEntity<?> registerShipPolicy(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String productType, @RequestParam String shipCostCode,
			@RequestParam String applyDate, @RequestParam String noShipIsland, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENTMONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTmonSellerService.registerShippingPolicy(entpCode, shipManSeq, returnManSeq, productType,
				shipCostCode, applyDate, noShipIsland, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
