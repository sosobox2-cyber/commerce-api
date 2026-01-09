package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.common.code.Application;
import com.cware.partner.coupang.batch.PriceProductBatch;

@SpringBootTest
class PriceProductServiceTest {

	@Autowired
	private PriceProductService priceProductService;

	@Autowired
	private PriceProductBatch priceProductBatch;

	@Test
	void test() {
		priceProductService.updatePriceProduct("24878488", "51", Application.ID.code(), 0);
	}

	@Test
	void testBatch() {
		priceProductBatch.updatePriceProducts();
	}

}
