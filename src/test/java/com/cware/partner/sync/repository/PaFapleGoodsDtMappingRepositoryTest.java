package com.cware.partner.sync.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaFapleGoodsDtMapping;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaFapleGoodsDtMappingRepositoryTest {

	@Autowired
	private PaFapleGoodsDtMappingRepository paFapleGoodsDtMappingRepository;

	@Test
	void test() {

		int result = paFapleGoodsDtMappingRepository.countByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfo("22413686", "002","E1","66");
		System.out.println(result);

	}
	@Test
	void test2() {
		
		Optional<PaFapleGoodsDtMapping>  result = paFapleGoodsDtMappingRepository.findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc("22413686", "002","E1","66");
		if(result.isPresent()) {
			System.out.println(result.get());
		}
		
	}


}
