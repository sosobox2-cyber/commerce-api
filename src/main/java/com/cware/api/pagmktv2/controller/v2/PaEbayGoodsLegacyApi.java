package com.cware.api.pagmktv2.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.pagmkt.v2.service.PaEbayProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/goods", description="레거시 상품등록 API")
@RestController
@RequestMapping(path = "/pagmktv2/goods")
public class PaEbayGoodsLegacyApi {

	@Autowired
	PaEbayProductService paEbayProductService;

	/**
	 * 레거시 상품등록 API 매핑
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@ApiOperation(value = "레거시 상품등록 API 매핑", notes = "레거시 상품등록 API 매핑", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value={"/post-goods", "/put-goods"})
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENEBAYAPI") String procId) {
		
		ResponseMsg result = paEbayProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
