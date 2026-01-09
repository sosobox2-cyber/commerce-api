package com.cware.partner.coupang.service;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.coupang.domain.Product;
import com.cware.partner.coupang.domain.ProductStatus;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ProductApiServiceTest {

	@Autowired
	private ProductApiService productApiService;

	@Autowired
	private PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	CommonService commonService;
	@Autowired
	CodeService codeService;

	@Test
	void testRecentChangeStatus() {

		Pageable pageable = PageRequest.of(0, 1000);

		Slice<PaCopnGoods> slice = null;
		int targetCnt = 0;

		Timestamp startDate = commonService.currentDate();
		System.out.println(Timestamp.from(startDate.toInstant().minus(7, ChronoUnit.DAYS)));

		while (true) {

			slice = copnGoodsRepository.findRejectTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			for (PaCopnGoods copnGoods : targetList) {
				ProductStatus productStatus = productApiService.getRecentChangeStatus(copnGoods.getSellerProductId(),
						copnGoods.getPaCode());
				log.info("No.{} 상품:{} {}", ++targetCnt, copnGoods.getGoodsCode(), productStatus);
			}

			if (!slice.hasNext())
				break;

		}
	}

	@Test
	void testGetRecentChangeStatus() {

		ProductStatus productStatus = productApiService.getRecentChangeStatus("13171165251", "52");
		log.info("상품: {}", productStatus);

	}

	@Test
	void testGetProduct() {

		Product product = productApiService.getProduct("22408368", "13150779218", "52", 0);
		log.info("상품: {}", product);

	}

	@Test
	void testRecentStatus() {

		Pageable pageable = PageRequest.of(0, 1000);

		Slice<PaCopnGoods> slice = null;
		int targetCnt = 0;

		while (true) {

			slice = copnGoodsRepository.findStatusTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			for (PaCopnGoods copnGoods : targetList) {
				Product product = productApiService.getProduct("", copnGoods.getSellerProductId(), copnGoods.getPaCode(), 0);
				log.info("No.{} 상품:{} {} {} 판매상태:{} 제휴상태:{} 현재승인상태:{} {} {}", ++targetCnt, copnGoods.getGoodsCode(),
						copnGoods.getSellerProductId(), product.getProductId(),
						copnGoods.getPaSaleGb(), copnGoods.getPaStatus(), copnGoods.getApprovalStatus(),
						product.getStatusName(), codeService.getApprovalStatus(product.getStatusName()));
				log.info("옵션:{} {}", product.getItems().size(), product.getItems());
			}

			if (!slice.hasNext())
				break;

		}
	}

}
