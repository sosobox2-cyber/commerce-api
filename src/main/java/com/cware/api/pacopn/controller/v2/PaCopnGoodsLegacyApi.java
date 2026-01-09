package com.cware.api.pacopn.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pacopn.v2.service.PaCopnProductService;

@RestController
@RequestMapping(path = "/pacopn/goods")
public class PaCopnGoodsLegacyApi {

	@Autowired
	PaCopnProductService paCopnProductService;

	/**
	 * 레거시 상품등록 API 매핑
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value={"/goods-insert", "/goods-modify"})
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPNAPI") String procId) {
		
		ResponseMsg result = paCopnProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
