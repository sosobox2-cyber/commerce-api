package com.cware.partner.coupang.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.coupang.service.PriceProductService;
import com.cware.partner.coupang.service.SaleProductService;
import com.cware.partner.coupang.service.StatusProductService;
import com.cware.partner.coupang.service.StockProductService;

@RestController
@RequestMapping(path="/coupang/product")
public class CoupangProductApi {

	@Autowired
	StatusProductService statusProductService;

	@Autowired
	PriceProductService priceProductService;

	@Autowired
	SaleProductService saleProductService;

	@Autowired
	StockProductService stockProductService;


	/**
	 * 상품 상태업데이트
	 *
	 * @param goodsCode
	 * @param sellerProductId
	 * @param currentStatus
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@PutMapping(value="/status")
	public ResponseEntity<?> updateStatusProduct(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPN") String procId, @RequestParam(defaultValue = "0") long transBatchNo) {

		PaTransService serviceLog = statusProductService.updateStatusProduct(goodsCode, paCode, procId, transBatchNo);
		return new ResponseEntity<>(serviceLog, HttpStatus.OK);
	}

	/**
	 * 가격변경
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@PutMapping(value="/price")
	public ResponseEntity<?> updatePriceProduct(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPN") String procId, @RequestParam(defaultValue = "0") long transBatchNo) {

		PaTransService serviceLog = priceProductService.updatePriceProduct(goodsCode, paCode, procId, transBatchNo);
		return new ResponseEntity<>(serviceLog, HttpStatus.OK);
	}

	/**
	 * 판매중지
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@PutMapping(value="/sale-stop")
	public ResponseEntity<?> stopSaleProduct(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPN") String procId, @RequestParam(defaultValue = "0") long transBatchNo) {

		PaTransService serviceLog = saleProductService.stopSaleProduct(goodsCode, paCode, procId, transBatchNo);
		return new ResponseEntity<>(serviceLog, HttpStatus.OK);
	}

	/**
	 * 판매재개
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@PutMapping(value="/sale-resume")
	public ResponseEntity<?> resumeSaleProduct(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPN") String procId, @RequestParam(defaultValue = "0") long transBatchNo) {

		PaTransService serviceLog = saleProductService.resumeSaleProduct(goodsCode, paCode, procId, transBatchNo);
		return new ResponseEntity<>(serviceLog, HttpStatus.OK);
	}

	/**
	 * 재고변경
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@PutMapping(value="/stock")
	public ResponseEntity<?> updateStockProduct(@RequestParam String goodsCode, @RequestParam String paCode,
			@RequestParam(defaultValue = "ENCOPN") String procId, @RequestParam(defaultValue = "0") long transBatchNo) {

		PaTransService serviceLog = stockProductService.updateStockProduct(goodsCode, paCode, procId, transBatchNo);
		return new ResponseEntity<>(serviceLog, HttpStatus.OK);
	}
}
