package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.common.code.Application;
import com.cware.partner.coupang.batch.ResumeSaleProductBatch;
import com.cware.partner.coupang.batch.StopSaleProductBatch;

@SpringBootTest
class SaleProductServiceTest {


	@Autowired
	private SaleProductService saleProductService;

	@Autowired
	private StopSaleProductBatch stopSaleProductBatch;

	@Autowired
	private ResumeSaleProductBatch resumeSaleProductBatch;

	@Test
	void test() {
		saleProductService.stopSaleProduct("20937462", "52", Application.ID.code(), 0);
	}

	@Test
	void testStopSaleBatch() {
		stopSaleProductBatch.stopSaleProducts();
	}

	@Test
	void testResumeSaleBatch() {
		resumeSaleProductBatch.resumeSaleProducts();
	}
}
