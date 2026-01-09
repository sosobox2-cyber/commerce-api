package com.cware.partner.cdc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.common.code.CdcReason;

@SpringBootTest
class ProductCdcServiceTest {

	@Autowired
	private ProductCdcService productCdcService;

	@Test
	void test() {
		productCdcService.executeProductCdc();
	}
	
	@Test
	void test2() {
		productCdcService.executeStockCdc();
	}

	@Test
//	@Transactional
//	@Rollback(value = false)
	void testSaleStart() {
		productCdcService.createCdcGoods(CdcReason.SALE_START);
	}

	@Test
	void testManualStart() {
		productCdcService.createCdcGoods(CdcReason.MANUAL_START);
	}

	@Test
	void testEventStart() {
		productCdcService.createCdcGoods(CdcReason.EVENT_START);
	}

	@Test
	void testEventForSale() {
		productCdcService.createCdcGoods(CdcReason.EVENT_FORSALE);
	}

	@Test
	void testSourcingExceptStart() {
		productCdcService.createCdcGoods(CdcReason.SOURCING_EXCEPT_START);
	}

	@Test
	void testSaleEnd() {
		productCdcService.createCdcGoods(CdcReason.SALE_END);
	}

	@Test
	void testEventEnd() {
		productCdcService.createCdcGoods(CdcReason.EVENT_END);
	}

	@Test
	void testEventStatus() {
		productCdcService.createCdcGoods(CdcReason.EVENT_STATUS);
	}

	@Test
	void testTargetExcept() {
		productCdcService.createCdcGoods(CdcReason.TARGET_EXCEPT);
	}

	@Test
	void testEntpExcept() {
		productCdcService.createCdcGoods(CdcReason.ENTP_EXCEPT);
	}

	@Test
	void testBrandExcept() {
		productCdcService.createCdcGoods(CdcReason.BRAND_EXCEPT);
	}

	@Test
	void testGoodsModify() {
		productCdcService.createCdcGoods(CdcReason.GOODS_MODIFY);
	}

	@Test
	void testPriceApply() {
		productCdcService.createCdcGoods(CdcReason.PRICE_APPLY);
	}

	@Test
	void testPromoApply() {
		productCdcService.createCdcGoods(CdcReason.PROMO_APPLY);
	}

	@Test
	void testPromoEnd() {
		productCdcService.createCdcGoods(CdcReason.PROMO_END);
	}

	@Test
	void testPromoStatus() {
		productCdcService.createCdcGoods(CdcReason.PROMO_STATUS);
	}

	@Test
	void testPromoExcept() {
		productCdcService.createCdcGoods(CdcReason.PROMO_EXCEPT);
	}

	@Test
	void testPromoPaExcept() {
		productCdcService.createCdcGoods(CdcReason.PROMO_PA_EXCEPT);
	}

	@Test
	void testShipCostApply() {
		productCdcService.createCdcGoods(CdcReason.SHIP_COST_APPLY);
	}

	@Test
	void testGoodsdtModify() {
		productCdcService.createCdcGoods(CdcReason.GOODSDT_MODIFY);
	}

	@Test
	void testOfferModify() {
		productCdcService.createCdcGoods(CdcReason.OFFER_MODIFY);
	}

	@Test
	void testDescribeModify() {
		productCdcService.createCdcGoods(CdcReason.DESCRIBE_MODIFY);
	}

	@Test
	void testImageModify() {
		productCdcService.createCdcGoods(CdcReason.IMAGE_MODIFY);
	}

	@Test
	void testInfoImageModify() {
		productCdcService.createCdcGoods(CdcReason.INFO_IMAGE_MODIFY);
	}

	@Test
	void testStockApply() {
		productCdcService.createCdcGoods(CdcReason.STOCK_APPLY);
	}

	@Test
	void testStockChange() {
		productCdcService.createCdcGoods(CdcReason.STOCK_CHANGE);
	}


	@Test
	void testEntpUserModify() {
		productCdcService.createCdcGoods(CdcReason.ENTPUSER_MODIFY);
	}

	@Test
	void testNoticeApply() {
		productCdcService.createCdcGoods(CdcReason.NOTICE_APPLY);
	}

	@Test
	void testNoticeEnd() {
		productCdcService.createCdcGoods(CdcReason.NOTICE_END);
	}

	@Test
	void testNoticeModify() {
		productCdcService.createCdcGoods(CdcReason.NOTICE_MODIFY);
	}

	@Test
	void testNoticeExcept() {
		productCdcService.createCdcGoods(CdcReason.NOTICE_EXCEPT);
	}
	
	@Test
	void testExceptBrandEnd() {
		productCdcService.createCdcGoods(CdcReason.PROMO_PA_EXCEPT_BRAND_END);
	}
	
	@Test
	void testPromoMarginApply() {
		productCdcService.createCdcGoods(CdcReason.PROMO_MARGIN_APPLY);
	}
	
	@Test
	void testPromoMarginEnd() {
		productCdcService.createCdcGoods(CdcReason.PROMO_MARGIN_END);
	}
	
	@Test
	void testPromoMarginStatus() {
		productCdcService.createCdcGoods(CdcReason.PROMO_MARGIN_STATUS);
	}
	
	@Test
	void testCategoryModify() {
		productCdcService.createCdcGoods(CdcReason.CATEGORY_MODIFY);
	}
	
	@Test
	void testEbayCategoryModify() {
		productCdcService.createCdcGoods(CdcReason.EBAY_CATEGORY_MODIFY);
	}

	@Test
	void testTdealDtimageModify() {
		productCdcService.createCdcGoods(CdcReason.TDEAL_DTIMAGE_MODIFY);
	}
	@Test
	void testTdealEventApply() {
		productCdcService.createCdcGoods(CdcReason.TDEAL_EVENT_APPLY);
	}
	@Test
	void testTdealEventEnd() {
		productCdcService.createCdcGoods(CdcReason.TDEAL_EVENT_END);
	}
	
	@Test
	void testEntpHolidayApply() {
		productCdcService.createCdcGoods(CdcReason.ENTP_HOLIDAY_APPLY);
	}
}
