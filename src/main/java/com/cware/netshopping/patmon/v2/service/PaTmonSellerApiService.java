package com.cware.netshopping.patmon.v2.service;

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
import com.cware.netshopping.patmon.v2.domain.Authorization;
import com.cware.netshopping.patmon.v2.domain.DeliveryTemplate;
import com.cware.netshopping.patmon.v2.domain.PartnerAddress;

/**
 * 티몬 배송 API 호출 
 * https://doc-interwork.tmon.co.kr/static/docs/api/deliverydeal/partner/delivery/delivery_add.html
 */
@Service
public class PaTmonSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaTmonApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 배송지 주소 등록 POST
	// SPC에서 등록하는 것과 동일하며, API를 통해서도 배송지/반송지 등을 입력할 수 있습니다.
	// 전화번호는 1544-1234 같은 8자리의 전화번호일 경우라도 02-1544-1234와 같이 지역번호를 붙여주어야 합니다.
	// 080으로 시작하는 번호는 사용할 수 없습니다.
	// http header 의 X-Partner-Id 기준으로 등록됩니다.
	private static final String REGISTER_ADDRESS = "/api/{vendorId}/partners/addresses";

	// 배송지 주소 수정 POST
	// 생성된 배송지 정보를 수정할 수 있습니다.
	private static final String UPDATE_ADDRESS = "/api/{vendorId}/partners/addresses/{addressNo}";

	// 배송템플릿 등록 POST
	// 배송템플릿은 파트너별로 커스터마이즈가 가능하며 배송비, 반품배송비, 추가배송비 등을 미리 저장하여 딜 등록 시점에서 꺼내어 사용하는 구조입니다. 
	// 이전에 사용한 배송정책과 유사한 구조입니다.
	// 상품에 관련한 배송템플릿을 미리 등록합니다. 
	// 배송 템플릿은 배송비와 관련된 항목을 미리 등록하여 재사용하기 위한 목적이므로 동일한 배송템플릿을 만들지 않아야 합니다. 
	// 생성된 템플릿은 수정이나 삭제가 불가능합니다. 
	// 정상적으로 등록된 배송템플릿은 숫자형태의 값 deliveryTemplateNo를 반환합니다.
	// 딜 등록시 반드시 필수적으로 필요하므로 딜 등록 이전에 생성해야합니다.
	// 배송템플릿 등록시점에 입력되는 productType 항목은 향후 딜생성시 입력되는 productType 항목과 반드시 일치해야 합니다.
	// 배송 템플릿 정책은 등록 이후 수정 및 삭제가 불가능합니다.
	private static final String REGISTER_SHIPPING_POLICY = "/api/{vendorId}/partners/templates";
	
	/**
	 * 배송지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param partnerAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerAddress(String entpCode, String entpManSeq, PartnerAddress partnerAddress, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_ADDRESS);

		HttpMethod method = HttpMethod.POST;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_ADDRESS");
		apiLog.setApiNote("티몬-배송지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());
		
		try {
			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);

			HttpEntity<PartnerAddress> requestEntity = new HttpEntity<>(partnerAddress, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<PartnerAddress> response = restTemplate.exchange(uri, method, requestEntity, PartnerAddress.class);

			PartnerAddress result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			
			String addressNo = result.getNo();
			apiLog.setSuccessYn(addressNo == null ? "0" : "1");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaTmonApiRequest.API_SUCCESS_CODE : PaTmonApiRequest.API_ERROR_CODE);
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return addressNo;
			
		} catch (RestClientResponseException ex) {
			log.error("배송지등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송지등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송지등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 배송지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param partnerAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateAddress(String entpCode, String entpManSeq, PartnerAddress partnerAddress, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_ADDRESS);

		HttpMethod method = HttpMethod.PUT;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_ADDRESS");
		apiLog.setApiNote("티몬-배송지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());
		
		try {
			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());
			params.put("addressNo", partnerAddress.getNo());

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);

			HttpEntity<PartnerAddress> requestEntity = new HttpEntity<>(partnerAddress, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<PartnerAddress> response = restTemplate.exchange(uri, method, requestEntity, PartnerAddress.class);

			PartnerAddress result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			
			String addressNo = result.getNo();
			apiLog.setSuccessYn(addressNo == null ? "0" : "1");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaTmonApiRequest.API_SUCCESS_CODE : PaTmonApiRequest.API_ERROR_CODE);
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return addressNo;
			
		} catch (RestClientResponseException ex) {
			log.error("배송지수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송지수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송지수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 배송템플릿 등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param deliveryTemplate
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, DeliveryTemplate deliveryTemplate, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);

		HttpMethod method = HttpMethod.POST;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq+ shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("티몬-배송템플릿등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());
		
		try {
			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);

			HttpEntity<DeliveryTemplate> requestEntity = new HttpEntity<>(deliveryTemplate, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);

			String deliveryTemplateNo = String.valueOf(StringUtil.jsonToMap(result).get("deliveryTemplateNo"));
			apiLog.setSuccessYn("null".equals(deliveryTemplateNo) ? "0" : "1");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaTmonApiRequest.API_SUCCESS_CODE : PaTmonApiRequest.API_ERROR_CODE);
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return deliveryTemplateNo;
			
		} catch (RestClientResponseException ex) {
			log.error("배송템플릿등록={} {} {}", entpCode, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송템플릿등록={} {} {}", entpCode, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송템플릿등록={} {}", entpCode, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
}
