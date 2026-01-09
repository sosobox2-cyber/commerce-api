package com.cware.partner.coupang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cware.partner.coupang.domain.Category;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CategoryApiServiceTest {

	@Autowired
	private CategoryApiService categoryApiService;

	@Test
	void testGetCategoryMeta() {
		Category category = categoryApiService.getCategoryMeta("62718");
		log.info("카테고리: {}", category);
		log.info("정보고시: {}", category.getNoticeCategories());
	}

}
