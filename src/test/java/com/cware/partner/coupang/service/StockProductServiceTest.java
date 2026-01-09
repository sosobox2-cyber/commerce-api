package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.common.code.Application;
import com.cware.partner.coupang.batch.StockProductBatch;

@SpringBootTest
class StockProductServiceTest {

	@Autowired
	private StockProductService stockProductService;

	@Autowired
	private StockProductBatch stockProductBatch;

	@Test
	void test() {
		stockProductService.updateStockProduct("22408829", "51", Application.ID.code(), 0);
	}


	@Test
	void testBatch() {
		stockProductBatch.updateStockProducts();
	}

}
