package com.cware.api.paqeen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.paqeen.service.PaQeenSellerService;

@RestController
@RequestMapping(path = "/paqeen/seller")
public class PaQeenSellerApi {
	
	@Autowired
	PaQeenSellerService paQeenSellerService;
	
	/**
	 * 퀸잇 배송정책 등록
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcost/register")
	public ResponseEntity<?> registerShipCost(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENQEENAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paQeenSellerService.registerShipCost(entpCode, shipManSeq, returnManSeq, 
				shipCostCode, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 퀸잇 배송정책 수정
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcost/update")
	public ResponseEntity<?> updateShipCost(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENQEENAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paQeenSellerService.updateShipCost(entpCode, shipManSeq, returnManSeq, 
				shipCostCode, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
