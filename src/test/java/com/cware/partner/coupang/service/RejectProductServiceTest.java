package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RejectProductServiceTest {


	@Autowired
	private RejectProductService rejectProductService;

	@Test
	void test() {
		rejectProductService.updateRejectProducts();
	}
}
