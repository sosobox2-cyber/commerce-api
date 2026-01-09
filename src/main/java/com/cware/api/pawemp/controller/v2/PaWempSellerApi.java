package com.cware.api.pawemp.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pawemp.v2.service.PaWempSellerService;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/pawemp/v2/seller")
public class PaWempSellerApi {

	@Autowired
	PaWempSellerService paWempSellerService;

	/**
	 * 위메프 배송비정책 등록
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/shippolicy/register")
	public ResponseEntity<?> registerShipPolicy(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String shipCostCode, @RequestParam String noShipIsland, @RequestParam String installYn,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENWEMPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paWempSellerService.registerShippingPolicy(entpCode, entpManSeq, shipCostCode,
				noShipIsland, installYn, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 위메프 배송비정책 수정
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/shippolicy/update")
	public ResponseEntity<?> updateShipPolicy(@RequestParam String entpCode, @RequestParam String entpManSeq,
			@RequestParam String shipCostCode, @RequestParam String noShipIsland, @RequestParam String installYn,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENWEMPAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paWempSellerService.updateShippingPolicy(entpCode, entpManSeq, shipCostCode,
				noShipIsland, installYn, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
