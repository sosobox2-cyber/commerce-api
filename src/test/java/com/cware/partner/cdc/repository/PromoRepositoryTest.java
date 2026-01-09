package com.cware.partner.cdc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.common.domain.GoodsTarget;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PromoRepositoryTest {

	@Autowired
	private PromoRepository promoRepository;

	// 테스트용 cdc동기화 날짜
	//Timestamp LAST_CDC_DATE = Timestamp.valueOf(LocalDateTime.now().minusMonths(1));
	Timestamp LAST_CDC_DATE = Timestamp.valueOf(LocalDateTime.now().minusSeconds(360));
	List<String> paGroupCodes = new ArrayList<String>();

	@BeforeEach
	void init() {
		paGroupCodes.add("05");
	}

	@Test
	void test() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPromoApplyTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);

		slice = promoRepository.findPromoEndTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);

		slice = promoRepository.findPromoStatusTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);

	}

	@Test
	void testPaPromoExceptTargetList() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}
	
	
	//프로모션 제외 (업체)
	@Test
	void testPaPromoExceptEntpList() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptEntpList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}
	
	//프로모션 제외 (브랜드)
	@Test
	void testPaPromoExceptBrandList() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptBrandList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}
	
	//프로모션 제외 종료 (상품)
	@Test
	void testPaPromoExceptEndTargetList() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptEndTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}
	
	
	//프로모션 제외 종료 (업체)
	@Test
	void testPaPromoExceptEndEntpList() {

		Pageable pageable = PageRequest.of(0, 100);
		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptEndEntpList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}

	//프로모션 제외 종료 (브랜드)
	@Test
	void testPaPromoExceptEndBrandList() {

		Slice<GoodsTarget> slice = promoRepository.findPaPromoExceptEndBrandList(LAST_CDC_DATE, 0, 10000, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
	}
	
	@Test
	void testPromoExceptTargetList() {

		Slice<GoodsTarget> slice = promoRepository.findPromoExceptTargetList(LAST_CDC_DATE, paGroupCodes);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isEqualTo(0);
	}

}
