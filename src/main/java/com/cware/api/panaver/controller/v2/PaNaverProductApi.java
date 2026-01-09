package com.cware.api.panaver.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.product.type.ProductType;
import com.cware.netshopping.panaver.v2.service.PaNaverProductService;

@RestController
@RequestMapping(path = "/panaver/v2/product")
public class PaNaverProductApi {

	@Autowired
	PaNaverProductService paNaverProductService;

	/**
	 * 네이버 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId) {
		
		ResponseMsg result = paNaverProductService.transProduct(goodsCode, paCode, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 네이버 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			// 옵션등록
			paNaverProductService.manageOption(goodsCode, paCode, procId, transBatchNo);
		} 
			
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 네이버 상품 수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			// 옵션등록/수정
			paNaverProductService.manageOption(goodsCode, paCode, procId, transBatchNo);
		} 

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 네이버 상품조회
	 * 
	 * @param goodsCode
	 * @param productId
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> info(@RequestParam String goodsCode, @RequestParam String productId,
			@RequestParam String paCode, @RequestParam(defaultValue = "ENNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ProductType product = paNaverProductService.getProduct(goodsCode, productId, paCode, procId, transBatchNo);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	/**
	 * 네이버 옵션등록/수정
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="option")
	public ResponseEntity<?> option(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverProductService.manageOption(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 네이버 이미지등록
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="image")
	public ResponseEntity<?> image(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverProductService.registerImage(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
