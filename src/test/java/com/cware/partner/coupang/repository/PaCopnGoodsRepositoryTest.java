package com.cware.partner.coupang.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaCopnGoodsRepositoryTest {

	@Autowired
	private PaCopnGoodsRepository paCopnGoodsRepository;

	@Test
	void testTransTargetList() {

		Pageable pageable = PageRequest.of(0, 10);
		System.out.println(paCopnGoodsRepository.findTransTargetList(pageable).getContent());
	}

	@Test
	void testUpdateTargetList() {

		Pageable pageable = PageRequest.of(0, 10);
		System.out.println(paCopnGoodsRepository.findUpdateTargetList(pageable).getContent());
	}

	@Test
	void testResumeSaleTargetList() {

		Pageable pageable = PageRequest.of(0, 10);
		System.out.println(paCopnGoodsRepository.findResumeSaleTargetList(pageable).getContent());
	}

	@Test
	void testPriceTargetList() {

		Pageable pageable = PageRequest.of(0, 10);
		paCopnGoodsRepository.findPriceTargetList(pageable);
	}
}

