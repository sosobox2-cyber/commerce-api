package com.cware.partner.coupang.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.common.repository.PaGoodsPriceApplyRepository;
import com.cware.partner.sync.domain.entity.PaGoodsPriceApply;
import com.cware.partner.sync.domain.entity.PaPromoTarget;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaGoodsPriceApplyRepositoryTest {

	@Autowired
	private PaGoodsPriceApplyRepository goodsPriceApplyRepository;

	@Test
	void testFindGoodsPriceApply() {

		Optional<PaGoodsPriceApply> price = goodsPriceApplyRepository.findGoodsPriceApply("20018617", "05", "52");
		System.out.println(price);
	}

	@Test
	void testFindTargetDeletePromo() {
		PaGoodsPriceApply priceApply = PaGoodsPriceApply.builder().goodsCode("24878488")
				.paCode("51")
				.couponPromoNo(null)
				.build();
		List<PaPromoTarget> list = goodsPriceApplyRepository.findTargetDeletePromo(priceApply);
		System.out.println(list);
	}
}
