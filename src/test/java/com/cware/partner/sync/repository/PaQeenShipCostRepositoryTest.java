package com.cware.partner.sync.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

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

import com.cware.partner.sync.domain.QeenIslandJejuReturnCost;
import com.cware.partner.sync.domain.entity.PaQeenShipCost;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaQeenShipCostRepositoryTest {

	@Autowired
	private PaQeenShipCostRepository paQeenShipCostRepository;

	//@Test
	void test() {
		Pageable pageable = PageRequest.of(0, 100);
		Slice<PaQeenShipCost> slice = paQeenShipCostRepository.findModifyTargetList(pageable);
		System.out.println(slice.getSize());
		assertThat(slice.getSize()).isGreaterThan(0);
		

	}
	
	//@Test
	void test3() {
		QeenIslandJejuReturnCost paQeenShipCost;
		paQeenShipCost = paQeenShipCostRepository.getIslandJejuReturnShipCost("209176","FR013");
		System.out.println(paQeenShipCost.getIslandReturnCost());
		System.out.println(paQeenShipCost.getJejuReturnCost());
		
		
	}
	@Test
	void test4() {
		
		boolean test = paQeenShipCostRepository.getIslandJejuReturnShipCostChk("209176","FR013");
		System.out.println(test);
		System.out.println(paQeenShipCostRepository.getIslandJejuReturnShipCostChk("209176","FR001"));
		
	}


}
