package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.common.code.Application;
import com.cware.partner.coupang.batch.StatusProductBatch;

@SpringBootTest
class StatusProductServiceTest {


	@Autowired
	private StatusProductBatch statusProductBatch;

	@Autowired
	private StatusProductService statusProductService;


	@Test
	void test() {
		statusProductBatch.updateStatusProducts();
	}

	@Test
	void testStatus() {
		statusProductService.updateStatusProduct("21206066", "51", Application.ID.code(), 0);
	}

	@Test
	void testOptionCode() {
		statusProductBatch.updateOptionProducts();
	}
}
