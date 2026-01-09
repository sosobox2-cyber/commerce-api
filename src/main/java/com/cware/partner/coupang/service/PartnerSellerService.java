package com.cware.partner.coupang.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.domain.ResponseMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 출고지 연동 (파트너API호출)
 */
@Slf4j
@Service
public class PartnerSellerService {

	@Autowired
	RestTemplate restTemplate;

	// API서버
	@Value("${partner.coupang.api.host}")
	String API_SERVER;

	// 제휴사 출고지등록 API URL
	@Value("${partner.coupang.api.seller.outbound.register}")
	String SELLER_OUTBOUND_REGISTER_API;

	// 제휴사 출고지수정 API URL
	@Value("${partner.coupang.api.seller.outbound.update}")
	String SELLER_OUTBOUND_UPDATE_API;

	// 출고지등록
	public int registerOutbound(String entpCode, String entpManSeq, String paCode, long transBatchNo) {

		UriTemplate template = new UriTemplate(SELLER_OUTBOUND_REGISTER_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put("entpCode", entpCode);
		params.put("entpManSeq", entpManSeq);
		params.put("paCode", paCode);
		params.put("procId", Application.ID.code());
		params.put("transBatchNo", String.valueOf(transBatchNo));

		String uri = template.expand(params).toString();

		log.info("출고지등록 요청 ====> {} ", uri);
		ResponseEntity<ResponseMsg> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.GET, null,
				ResponseMsg.class);
		ResponseMsg result = response.getBody();

		// 성공이 아니면 에러로 로깅
		if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			log.error("등록 실패 ====> {}-{} {}", entpCode, entpManSeq, result);
			if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) return 0;
			return -1;
		}

		log.info("등록 결과 ====> {}-{} {}", entpCode, entpManSeq, result);

		return 1;
	}

	// 출고지수정
	public int updateOutbound(String entpCode, String entpManSeq, String paCode, long transBatchNo) {

		UriTemplate template = new UriTemplate(SELLER_OUTBOUND_UPDATE_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put("entpCode", entpCode);
		params.put("entpManSeq", entpManSeq);
		params.put("paCode", paCode);
		params.put("procId", Application.ID.code());
		params.put("transBatchNo", String.valueOf(transBatchNo));

		String uri = template.expand(params).toString();

		log.info("출고지수정 요청 ====> {} ", uri);
		ResponseEntity<ResponseMsg> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.GET, null,
				ResponseMsg.class);
		ResponseMsg result = response.getBody();

		// 성공이 아니면 에러로 로깅
		if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			log.error("출고지수정 실패 ====> {}-{} {}", entpCode, entpManSeq, result);
			if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) return 0;
			return -1;
		}

		log.info("출고지수정 결과 ====> {}-{} {}", entpCode, entpManSeq, result);

		return 1;
	}

}
