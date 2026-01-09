package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PartnerProductServiceTest {

	@Autowired
	private PartnerProductService transProductService;

	@Test
	void testRegister() {
		transProductService.registerProduct("22415037", "52", 0);
	}

	@Test
	void testUpdate() {
		transProductService.updateProduct("22408834", "51", 0);
	}

}
