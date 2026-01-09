package com.cware.api.paintp.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.paintp.v2.service.PaIntpSellerService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/paintp/v2/seller")
public class PaIntpSellerApi {

	@Autowired
	PaIntpSellerService paIntpSellerService;

	/**
	 * 인터파크 반품배송지 등록
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paIntpSellerService.registerEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 인터파크 반품배송지 수정
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paIntpSellerService.updateEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 인터파크 배송비정책 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcost/register")
	public ResponseEntity<?> registerShipCost(@RequestParam String entpCode, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paIntpSellerService.registerShipCost(entpCode, shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 인터파크 배송비정책 수정
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/shipcost/update")
	public ResponseEntity<?> upateShipCost(@RequestParam String entpCode, @RequestParam String shipCostCode,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENINTPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paIntpSellerService.updateShipCost(entpCode, shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
