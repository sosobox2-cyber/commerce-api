package com.cware.api.paqeen.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.paqeen.service.PaQeenCommonService;

import io.swagger.annotations.Api;

@Api(value="/paqeen/common", description="퀸잇 common 브랜드/카테고리/정보고시")
@RestController
@RequestMapping(path = "/paqeen/common")
public class PaQeenCommonApi {
	
	@Autowired
	PaQeenCommonService paQeenCommonService;
	
	
	/**
	 * 퀸잇 브랜드 조회
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/brand")
	public ResponseEntity<?> registerBrand(@RequestParam(value = "paCode", required = false) String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = null;
		if(paCode == null) {
			for(int i = 0; i< Constants.PA_QEEN_CONTRACT_CNT ; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				result = paQeenCommonService.registerBrand(paCode, procId, transBatchNo);	
			}
		}else {
			result = paQeenCommonService.registerBrand(paCode, procId, transBatchNo);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 퀸잇 카테고리 조회
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/category")
	public ResponseEntity<?> registerCategory( @RequestParam(value = "paCode", required = false) String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = null;
		if(paCode == null) {
			for(int i = 0; i< Constants.PA_QEEN_CONTRACT_CNT ; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				result = paQeenCommonService.registerCategory(paCode, procId, transBatchNo);	
			}
		}else {
			result = paQeenCommonService.registerCategory(paCode, procId, transBatchNo);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 퀸잇 정보고시 조회
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/offer")
	public ResponseEntity<?> registerOffer(@RequestParam(value = "paCode", required = false) String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
			
		ResponseMsg result = paQeenCommonService.registerOffer(paCode, procId, transBatchNo);	
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 퀸잇 카테고리별 정보고시 조회
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="/category-offer")
	public ResponseEntity<?> registerCategoryOffer(@RequestParam(value = "paCode", required = false) String paCode,
			@RequestParam(defaultValue = "ENQEENAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		ResponseMsg result = null;
		if(paCode == null) {
			for(int i = 0; i< Constants.PA_QEEN_CONTRACT_CNT ; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				result = paQeenCommonService.registerCategoryOffer(paCode, procId, transBatchNo);	
			}
		}else {
			result = paQeenCommonService.registerCategoryOffer(paCode, procId, transBatchNo);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
