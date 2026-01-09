package com.cware.partner.common.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.common.domain.entity.PaCdcReason;
import com.cware.partner.common.repository.PaCdcReasonRepository;

@Service
public class CodeService {

	@Autowired
	private PaCdcReasonRepository cdcReasonRepository;

	// 변경데이터캡처 기준정보
	private Map<String, PaCdcReason> cdcReasonMap;

	// 기본 cdc동기화 날짜
	Timestamp LAST_CDC_DATE = Timestamp.valueOf(LocalDateTime.now().minusMonths(1));

	@PostConstruct
	public void loadCodeList() throws Exception {
		loadCdcReason();
	}

	public void loadCdcReason() throws Exception {
		List<PaCdcReason> list = cdcReasonRepository.findAll();

		cdcReasonMap = new HashMap<String, PaCdcReason>();

		for (PaCdcReason cdcReason : list) {
			if (cdcReason.getLastCdcDate() == null) cdcReason.setLastCdcDate(LAST_CDC_DATE);
			cdcReasonMap.put(cdcReason.getCdcReasonCode(), cdcReason);
		}
	}

	public PaCdcReason getCdcReason(String cdcReasonCode) {
		if (cdcReasonMap == null) {
			return null;
		}
		return cdcReasonMap.get(cdcReasonCode);
	}

}
