package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.coupang.batch.RegisterOutboundBatch;
import com.cware.partner.coupang.batch.UpdateOutboundBatch;

@SpringBootTest
class PartnerSellerServiceTest {

	@Autowired
	private PartnerSellerService partnerSellerService;

	@Autowired
	private RegisterOutboundBatch registerOutboundBatch;

	@Autowired
	private UpdateOutboundBatch updateOutboundBatch;

	@Test
	void testRegister() {
		partnerSellerService.registerOutbound("112656", "011", "51", 0);
	}

	@Test
	void testUpdate() {
		partnerSellerService.updateOutbound("100512", "002", "51", 0);
	}

	@Test
	void testRegisterBatch() {
		registerOutboundBatch.registerOutbound();
	}

	@Test
	void testUpdateBatch() {
		updateOutboundBatch.updateOutbound();
	}
}
