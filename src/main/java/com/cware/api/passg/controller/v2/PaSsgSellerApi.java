package com.cware.api.passg.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.passg.v2.service.PaSsgSellerService;

@RestController
@RequestMapping(path = "/passg/v2/seller")
public class PaSsgSellerApi {

	@Autowired
	PaSsgSellerService paSsgSellerService;

	/**
	 * SSG 업체배송지 등록
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENSSGAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paSsgSellerService.registerEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * SSG 업체배송지 수정
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENSSGAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paSsgSellerService.updateEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * SSG 배송비정책 등록
	 * 
	 * @param shppcstAplUnitCd
	 * @param shppcstPlcyDivCd
	 * @param collectYn
	 * @param shipCostBaseAmt
	 * @param shipCostAmt
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value = "/shipcost/register")
	public ResponseEntity<?> registerShipCost(@RequestParam String shppcstAplUnitCd,
			@RequestParam String shppcstPlcyDivCd, @RequestParam String collectYn,
			@RequestParam Integer shipCostBaseAmt, @RequestParam Integer shipCostAmt, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENSSGAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paSsgSellerService.registerShipCost(shppcstAplUnitCd, shppcstPlcyDivCd, collectYn,
				shipCostBaseAmt, shipCostAmt, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
