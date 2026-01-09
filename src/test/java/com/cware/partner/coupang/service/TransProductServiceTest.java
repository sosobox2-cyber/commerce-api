package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.coupang.batch.PriceProductBatch;
import com.cware.partner.coupang.batch.RegisterOutboundBatch;
import com.cware.partner.coupang.batch.RegisterProductBatch;
import com.cware.partner.coupang.batch.ResumeSaleProductBatch;
import com.cware.partner.coupang.batch.StatusProductBatch;
import com.cware.partner.coupang.batch.StockProductBatch;
import com.cware.partner.coupang.batch.StopSaleProductBatch;
import com.cware.partner.coupang.batch.UpdateOutboundBatch;
import com.cware.partner.coupang.batch.UpdateProductBatch;

@SpringBootTest
class TransProductServiceTest {

	@Autowired
	private RegisterOutboundBatch registerOutboundBatch;

	@Autowired
	private UpdateOutboundBatch updateOutboundBatch;

	@Autowired
	private RegisterProductBatch registerProductBatch;

	@Autowired
	private StatusProductBatch statusProductBatch;

	@Autowired
	private StopSaleProductBatch stopSaleProductBatch;

	@Autowired
	private ResumeSaleProductBatch resumeSaleProductBatch;

	@Autowired
	private PriceProductBatch priceProductBatch;

	@Autowired
	private UpdateProductBatch updateProductService;

	@Autowired
	private StockProductBatch stockProductBatch;

	@Test
	void testRegisterBatch() {
		registerOutboundBatch.registerOutbound();
	}

	@Test
	void testUpdateBatch() {
		updateOutboundBatch.updateOutbound();
	}

	@Test
	void testRegisterProducts() {
		registerProductBatch.registerProducts();
	}

	@Test
	void testUpdateStatusProducts() {
		statusProductBatch.updateStatusProducts();
	}

	@Test
	void testUpdateOptionProducts() {
		statusProductBatch.updateOptionProducts();
	}

	@Test
	void testStopSaleBatch() {
		stopSaleProductBatch.stopSaleProducts();
	}

	@Test
	void testResumeSaleBatch() {
		resumeSaleProductBatch.resumeSaleProducts();
	}

	@Test
	void testUpdatePriceProducts() {
		priceProductBatch.updatePriceProducts();
	}

	@Test
	void testUpdateProducts() {
		updateProductService.updateProducts();
	}

	@Test
	void testUpdateStockProducts() {
		stockProductBatch.updateStockProducts();
	}
}
