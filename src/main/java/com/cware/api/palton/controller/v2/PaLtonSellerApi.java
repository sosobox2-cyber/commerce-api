package com.cware.api.palton.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.palton.v2.service.PaLtonSellerService;

@RestController
@RequestMapping(path = "/palton/v2/seller")
public class PaLtonSellerApi {

	@Autowired
	PaLtonSellerService paLtonSellerService;

	/**
	 * 롯데온 출고/반품지 등록
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENLTONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paLtonSellerService.registerEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 롯데온 출고/반품지 수정
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENLTONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paLtonSellerService.updateEntpSlip(entpCode, entpManSeq, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 롯데온 배송비정책 등록
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENLTONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paLtonSellerService.registerShipCost(entpCode, shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 롯데온 배송비정책 수정
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
			@RequestParam String paCode, @RequestParam(defaultValue = "ENLTONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paLtonSellerService.updateShipCost(entpCode, shipCostCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 롯데온 추가배송비정책 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/addshipcost/register")
	public ResponseEntity<?> registerShipCost(@RequestParam Double islandCost, @RequestParam Double jejuCost,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENLTONAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {

		ResponseMsg result = paLtonSellerService.registerAddShipCost(islandCost, jejuCost, paCode, procId,
				transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
