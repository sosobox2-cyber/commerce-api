package com.cware.netshopping.pawemp.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pawemp.v2.domain.ShipPolicy;
import com.cware.netshopping.pawemp.v2.domain.ShipPolicyPost;

/**
 * 위메프 배송 API 호출
 */
@Service
public class PaWempSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaWempApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());


	// 배송 정책 등록/수정 POST
	// 상품을 등록,수정하는데 필요한 기초정보를 등록,수정 할 수 있습니다
	private static final String REGISTER_SHIPPING_POLICY = "/product/in/setSellerShipPolicy";
	private static final String UPDATE_SHIPPING_POLICY = "/product/in/setSellerShipPolicy";

	/**
	 * 배송정책 등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param shipPolicy
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, String noShipIsland,
			String installYn, ShipPolicy shipPolicy, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.WEMP_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + shipCostCode + noShipIsland + installYn + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("위메프-배송정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.WEMP.code());
		apiLog.setProcessId(PaGroup.WEMP.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShipPolicy> requestEntity = new HttpEntity<>(shipPolicy, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ShipPolicyPost> response = restTemplate.exchange(uri, method, requestEntity, ShipPolicyPost.class);

			ShipPolicyPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(result.getData().getReturnMsg());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return result.getData().getReturnKey();

		} catch (RestClientResponseException ex) {
			log.error("배송정책등록={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorWemp(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송정책등록={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송정책등록={}-{} {}", entpCode, entpManSeq, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 배송정책 수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param noShipIsland
	 * @param installYn
	 * @param shipPolicy
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, String noShipIsland,
			String installYn, ShipPolicy shipPolicy, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.WEMP_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + shipCostCode + noShipIsland + installYn + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_POLICY");
		apiLog.setApiNote("위메프-배송정책수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.WEMP.code());
		apiLog.setProcessId(PaGroup.WEMP.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShipPolicy> requestEntity = new HttpEntity<>(shipPolicy, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ShipPolicyPost> response = restTemplate.exchange(uri, method, requestEntity, ShipPolicyPost.class);

			ShipPolicyPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(result.getData().getReturnMsg());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return result.getData().getReturnKey();

		} catch (RestClientResponseException ex) {
			log.error("배송정책수정={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorWemp(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송정책수정={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송정책수정={}-{} {}", entpCode, entpManSeq, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

}
