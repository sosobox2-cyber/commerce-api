package com.cware.api.panaver.controller.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ProductService;

@RestController
@RequestMapping(path = "/panaver/v3/product")
public class PaNaverV3ProductApi {
	
	@Autowired
	PaNaverV3ProductService paNaverV3ProductService;
				
	/**
	 * 네이버 상품 신규입점
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="register")
	public ResponseEntity<?> register(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENNAVERAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverV3ProductService.registerProduct(goodsCode, paCode, procId, transBatchNo);
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			// 옵션매핑
			paNaverV3ProductService.manageOption(goodsCode, paCode, procId, transBatchNo);
		} 
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 상품 이미지 등록
	 * 
	 * @param imageFilePath
	 * @param procId
	 * @return 
	 */
	@GetMapping(value="/product-images")
	public ResponseEntity<?> postImages(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "CMNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverV3ProductService.postImages(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 옵션매핑
	 * 
	 * @param channelProductNo
	 * @param procId
	 * @return 
	 */
	@GetMapping(value="/option")
	public ResponseEntity<?> option(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "CMNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo)  {
		
		ResponseMsg result = paNaverV3ProductService.manageOption(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 상품 목록 조회
	 * 
	 * @param channelProductNo
	 * @param procId
	 * @return 
	 */
	@GetMapping(value="/product-search")
	public ResponseEntity<?> getChannelSearch(String startPage ,String endPage ,@RequestParam(defaultValue = "CMNAVERAPI") String procId)  {
		
		ResponseMsg result = paNaverV3ProductService.getChannelSearch(startPage,endPage, procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 상품목록조회 판매중상품대상
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/product-search-v2")
	public ResponseEntity<?> getChannelSearchV2(String goodsCode ,String paCode ,@RequestParam(defaultValue = "CMNAVERAPI") String procId)  {
		
		ResponseMsg result = paNaverV3ProductService.getChannelSearchV2(goodsCode,paCode, procId);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 네이버 상품 수정[V3]
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@GetMapping(value="update")
	public ResponseEntity<?> update(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "CMNAVERAPI") String procId, @RequestParam(defaultValue = "0") Long transBatchNo) {
		
		ResponseMsg result = paNaverV3ProductService.updateProduct(goodsCode, paCode, procId, transBatchNo);
		
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			// 옵션매핑
			paNaverV3ProductService.manageOption(goodsCode, paCode, procId, transBatchNo);
		}else if(HttpStatus.BAD_REQUEST.name().equals(result.getCode()) && result.getMessage().contains("modelId")) { 
			// 모델ID매핑
			paNaverV3ProductService.manageModelId(goodsCode, paCode, procId, transBatchNo);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 네이버 상품 개별 전송[V3]
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/trans")
	public ResponseEntity<?> trans(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		ResponseMsg result = paNaverV3ProductService.transProduct(goodsCode, paCode, procId);
	
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 모델ID 매핑
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return 
	 */
	@GetMapping(value="/modelId")
	public ResponseEntity<?> getModelId(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "CMNAVERAPI") String procId,
			@RequestParam(defaultValue = "0") Long transBatchNo)  {
		
		ResponseMsg result = paNaverV3ProductService.manageModelId(goodsCode, paCode, procId, transBatchNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
