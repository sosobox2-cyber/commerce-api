package com.cware.netshopping.pacommon.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;

/**
 * 상품동기화데몬의 동기화 API 호출
 *
 */
@Service
public class SyncProductService {

	// 상품동기화서버
	@Value("${partner.sync.service.host}")
	String API_SERVER;

	// 상품동기화 API 
	@Value("${partner.sync.service.product}")
	String PRODUCT_SYNC_API;

	@Autowired
	TransLogService transLogService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public PaGoodsSync syncProduct(String goodsCode, String paGroupCode, String procId) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]상품동기화");
		serviceLog.setTransBatchNo(0);
		serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		String tag = "상품동기화";

		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);

		UriTemplate template = new UriTemplate(PRODUCT_SYNC_API);
		String uri = template.expand(params).toString();
		
		ResponseMsg responseMsg = new ResponseMsg("", "");
		PaGoodsSync result = new PaGoodsSync();

		try {
			log.info("{} 요청 ====> {} ", tag, uri);
			ResponseEntity<PaGoodsSync> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.GET, null,
					PaGoodsSync.class);
			result = response.getBody();

			log.info("{} 결과 ====> {} {}", tag, goodsCode, result);

			if (result.getTargetCnt() < 0 ) {
				serviceLog.setResultCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
				serviceLog.setResultMsg("등록된 상품이 아닙니다");
			} else if (result.getFilterCnt() > 0 && result.getFilterPaGroup().contains(paGroupCode)) {
				serviceLog.setResultCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				serviceLog.setResultMsg("입점 제외된 상품입니다");
			} else {
				serviceLog.setResultCode(String.valueOf(HttpStatus.OK.value()));
				serviceLog.setResultMsg("isSync: " + (result.getSyncCnt() > 0 ? true : false));
			}
			transLogService.logTransServiceEnd(serviceLog);
			responseMsg.setStatus(result.getTargetCnt());
		} catch (RestClientResponseException ex) {
			log.error("{} goodsCode={}", tag, goodsCode, ex);
			serviceLog.setResultCode(String.valueOf(ex.getRawStatusCode()));
			serviceLog.setResultMsg(StringUtil
					.truncate((String)StringUtil.jsonToMap(ex.getResponseBodyAsString()).get("message"), 4000));
			transLogService.logTransServiceEnd(serviceLog);
		} catch (Exception e) {
			log.error("{} goodsCode={}", tag, goodsCode, e);
			serviceLog.setResultCode("Exception");
			serviceLog.setResultMsg(e.getMessage());
			transLogService.logTransServiceEnd(serviceLog);
		}
		responseMsg.setCode(serviceLog.getResultCode());
		responseMsg.setMessage(serviceLog.getResultMsg());
		result.setResponseMsg(responseMsg);
		
		return result;
	}

}
