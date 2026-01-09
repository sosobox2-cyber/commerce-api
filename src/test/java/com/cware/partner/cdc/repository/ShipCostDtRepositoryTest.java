package com.cware.partner.cdc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
class ShipCostDtRepositoryTest {

	@Autowired
	private ShipCostDtRepository shipCostDtRepository;

	// 테스트용 cdc동기화 날짜
	Timestamp LAST_CDC_DATE = Timestamp.valueOf(LocalDateTime.now().minusMonths(1));

	@Test
	void test() {

		Pageable pageable = PageRequest.of(0, 100);
		List<String> paGroupCodes = new ArrayList<String>();
		paGroupCodes.add("05");
		Slice<GoodsTarget> goodsSlice = shipCostDtRepository.findShipCostApplyTargetList(LAST_CDC_DATE, 0, pageable, paGroupCodes);
		System.out.println(goodsSlice.getSize());
		assertThat(goodsSlice.getSize()).isGreaterThan(0);

	}

}
