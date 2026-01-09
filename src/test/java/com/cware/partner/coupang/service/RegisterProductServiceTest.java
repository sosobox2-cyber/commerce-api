package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.coupang.batch.RegisterProductBatch;

@SpringBootTest
class RegisterProductServiceTest {


	@Autowired
	private RegisterProductBatch registerProductBatch;

	@Test
	void test() {
		registerProductBatch.registerProducts();
	}
}
