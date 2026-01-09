package com.cware.partner.sync.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.sync.domain.QeenGoodsOffer;
import com.cware.partner.sync.domain.entity.PaGoodsOffer;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaGoodsOfferRepositoryTest {

	@Autowired
	private PaGoodsOfferRepository paGoodsOfferRepository;

	@Test
	void test() {

		List<PaGoodsOffer> list = paGoodsOfferRepository.selectGoodsOffer(PaGroup.QEEN.code(), "22408746", "01");
		System.out.println(list.size());

	}
	@Test
	void testQeenGoodsOffer() {
		
		List<QeenGoodsOffer> list = paGoodsOfferRepository.selectQeenGoodsOffer(PaGroup.QEEN.code(), "22414375", "18");
		
		System.out.println(list.size());
		
	}

	@Test
	void testModify() {

		List<PaGoodsOffer> list = paGoodsOfferRepository.selectGoodsOfferModify(PaGroup.GMARKET.code(), "22408746");
		System.out.println(list);

	}

	@Test
	void testNewOffer() {

		List<PaGoodsOffer> list = paGoodsOfferRepository.selectGoodsOfferNew(PaGroup.GMARKET.code(), "22408746");
		System.out.println(list.size());

	}

	@Test
	void testOfferTypeName() {
		String result = paGoodsOfferRepository.getPaOfferTypeName(PaGroup.COUPANG.code(), "20079878");
		System.out.println(result);
	}

	@Test
	void testGetOfferType() {
		String result = paGoodsOfferRepository.getOfferType(PaGroup.GMARKET.code(), "22408743");
		System.out.println(result);
	}


	@Test
	void testGoodsOffer() {
		List<PaGoodsOffer> result = paGoodsOfferRepository.selectGoodsOffer(PaGroup.FAPLE.code(), "22413686", "01");
		System.out.println(result);
	}


	@Test
	void testCoupangNoticeCategoryName() {

		String result = paGoodsOfferRepository.getCoupangNoticeCategoryName("80817", "주방용품");
		System.out.println(result);

		result = paGoodsOfferRepository.getCoupangNoticeCategoryEtcName("80817");
		System.out.println(result);
		result = paGoodsOfferRepository.getCoupangNoticeCategoryDefaultName("80817");
		System.out.println(result);
		System.out.println(result.split(",")[0].replace("\"", "").replace(" ", ""));

	}

	@Test
	void testGoodsOfferByPaOfferType() {

		List<PaGoodsOffer> list = paGoodsOfferRepository.selectGoodsOfferByPaOfferType(PaGroup.COUPANG.code(), "0529");
		System.out.println(list);

	}
}
