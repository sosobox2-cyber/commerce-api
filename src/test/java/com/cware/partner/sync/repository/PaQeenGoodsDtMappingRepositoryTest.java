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
import com.cware.partner.sync.domain.entity.PaQeenGoodsDtMapping;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaQeenGoodsDtMappingRepositoryTest {

	@Autowired
	private PaQeenGoodsDtMappingRepository paQeenGoodsDtMappingRepository;

	@Test
	void test2() {
		
		Optional<PaQeenGoodsDtMapping>  result = paQeenGoodsDtMappingRepository.findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc("22414233", "001","F1","화이트/100(77)");
		if(result.isPresent()) {
			System.out.println(result.get());
		}
		
	}


}
