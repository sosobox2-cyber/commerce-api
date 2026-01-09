package com.cware.api.patdeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.patdeal.service.PaTdealSellerService;

@RestController
@RequestMapping(path = "/patdeal/seller")
public class PaTdealSellerApi {
	
	@Autowired
	PaTdealSellerService paTdealSellerService;
				
	/**
	 * 티딜 입출고 주소 생성하기
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/address/register")
	public ResponseEntity<?> register(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTdealSellerService.registerEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 입출고 주소 수정하기
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/address/update")
	public ResponseEntity<?> update(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paTdealSellerService.updateEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 배송비 템플릿 생성 (추가배송비 설정 생성 + 배송비 템플릿 그룹 생성)
	 * 
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.registerShipCost(entpCode, shipManSeq, returnManSeq, 
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 배송비 템플릿 수정 (추가배송비 설정 수정 + 배송비 템플릿 그룹 수정)
	 * 
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.updateShipCost(entpCode, shipManSeq, returnManSeq, 
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 지역별 추가배송비 설정 생성
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/areafees/register")
	public ResponseEntity<?> registerAreaFees(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.registerAreaFees(entpCode, shipManSeq, returnManSeq,
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 지역별 추가배송비 설정 수정
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/areafees/update")
	public ResponseEntity<?> updateAreaFees(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.updateAreaFees(entpCode, shipManSeq, returnManSeq,
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 배송비 템플릿 그룹 생성
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcostgroup/register")
	public ResponseEntity<?> registerShipCostGroup(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.registerShipCostGroup(entpCode, shipManSeq, returnManSeq,
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 티딜 배송비 템플릿 그룹 수정
	 * 
	 * @param entpCode
	 * @param shipManSeq
	 * @param returnManSeq
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcostgroup/update")
	public ResponseEntity<?> updateShipCostGroup(@RequestParam String entpCode, @RequestParam String shipManSeq,
			@RequestParam String returnManSeq, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paTdealSellerService.updateShipCostGroup(entpCode, shipManSeq, returnManSeq,
				shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
