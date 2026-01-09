package com.cware.partner.sync.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.sync.domain.entity.PaCustShipCost;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaFapleShipCostRepositoryTest {

	@Autowired
	private PaFapleShipCostRepository paFapleShipCostRepository;

	@Test
	void test() {

		Double result = paFapleShipCostRepository.getIslandReturnShipCost("209176", "FR001");
		System.out.println(result);

	}


}
