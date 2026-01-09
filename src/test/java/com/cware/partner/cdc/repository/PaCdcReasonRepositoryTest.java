package com.cware.partner.cdc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cware.partner.common.domain.entity.PaCdcReason;
import com.cware.partner.common.repository.PaCdcReasonRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PaCdcReasonRepositoryTest {

	@Autowired
	private PaCdcReasonRepository paCdcReasonRepository;

	@Test
	void test() {
		List<PaCdcReason> list = paCdcReasonRepository.findAll();
		assertThat(list.size()).isGreaterThan(0);

	}

}
