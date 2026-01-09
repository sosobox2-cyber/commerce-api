package com.cware.netshopping.patmon.v2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
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
import com.cware.netshopping.patmon.v2.domain.Deal;
import com.cware.netshopping.patmon.v2.domain.DealNoMap;
import com.cware.netshopping.patmon.v2.domain.DealOption;
import com.cware.netshopping.patmon.v2.domain.DealUpdateResult;

/**
 * 티몬 상품 API 호출
 * https://doc-interwork.tmon.co.kr/static/docs/api/deliverydeal/deal/deal_prepare.html
 */
@Service
public class PaTmonProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaTmonApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 딜 등록 POST
	// 딜 등록/수정 API는 티몬 내부적으로 부하량이 많은 API이며, 
	// 동일한 딜에 대해서 무분별한 등록/수정 요청을 막기위해 이 API는 딜번호 기준 20분간 10회로 제한됩니다.
	private static final String CREATE_PRODUCT = "/api/{vendorId}/deals";

	// 딜옵션 개별등록 POST
	// 딜 등록후 딜옵션을 더 추가해야 할 경우 사용합니다.
	// 이미 등록된 옵션 딜을 포함하여 옵션 개수가 200개이상시 등록이 제한 됩니다.
	private static final String CREATE_PRODUCT_OPTION = "/api/{vendorId}/deals/{vendorDealNo}/options";

	// 딜 정보 수정 PUT
	// 등록한 딜의 정보를 변경할 수 있습니다. 
	// 필요하다면 등록된 옵션까지 한꺼번에 수정할 수 있습니다. 
	// 기본적으로 수정될 정보만 세팅하면 되나 특정 값은 별도의 API로만 수정이 가능합니다.
	// 수량(재고) 및 판매기간 등 변경은 별도 API에서 처리됩니다.
	private static final String UPDATE_PRODUCT = "/api/{vendorId}/deals/{vendorDealNo}";

	// 딜 단건 조회 GET
	// 이미 알고 있는 연동사딜번호 vendorDelaNo 를 이용하여 티몬에 등록된 딜을 조회
	private static final String GET_PRODUCT_BY_GOODS_NO = "/api/{vendorId}/deals/{vendorDealNo}";

	// 티몬 오퍼레이터를 통해 받은 정책번호
	@Value("${partner.tmon.api.vendor.policy.no}")
	String VENDOR_POLICY_NO;
	
	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param deal
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public DealNoMap registerProduct(String goodsCode, Deal deal, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(CREATE_PRODUCT);

		HttpMethod method = HttpMethod.POST;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("티몬-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());

		
		try {
			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);
			
			deal.setVendorPolicyNo(VENDOR_POLICY_NO);

			HttpEntity<Deal> requestEntity = new HttpEntity<>(deal, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<DealNoMap> response = restTemplate.exchange(uri, method, requestEntity, DealNoMap.class);

			DealNoMap result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			
			String dealNo = null;
			if (result.getDealNo() != null) {
				dealNo = String.valueOf(deal.getExceptYn() > 0 ? result.getDealNo().get(paCode + "-" + goodsCode + "01") : result.getDealNo().get(paCode + "-" + goodsCode));
			}

			apiLog.setSuccessYn("null".equals(dealNo) ? "0" : "1");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaTmonApiRequest.API_SUCCESS_CODE : PaTmonApiRequest.API_ERROR_CODE);
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			result.setTmonDealNo(dealNo);
			
			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품등록 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 딜옵션 개별등록API 호출
	 * 
	 * @param goodsCode
	 * @param dealOptions
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public Map<String, Object> registerProductOption(String goodsCode, List<DealOption> dealOptions, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(CREATE_PRODUCT_OPTION);

		HttpMethod method = HttpMethod.POST;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT_OPTION");
		apiLog.setApiNote("티몬-딜옵션개별등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());
		
		try {


			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());
			params.put("vendorDealNo", paCode + "-" + goodsCode);

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);

			Map<String, Object> body = new HashMap<String, Object>();
			body.put("dealOptions", dealOptions);

			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("1");
			apiLog.setResultCode(PaTmonApiRequest.API_SUCCESS_CODE);
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return StringUtil.jsonToMap(result);

		} catch (RestClientResponseException ex) {
			log.error("딜옵션개별등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("딜옵션개별등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("딜옵션개별등록 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 상품수정API 호출
	 * 
	 * @param goodsCode
	 * @param deal
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateProduct(String goodsCode, Deal deal, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT);

		HttpMethod method = HttpMethod.PUT;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("티몬-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());
		
		try {


			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());
			params.put("vendorDealNo", deal.getVendorDealNo());

			String uri = apiRequest.TMON_GATEWAY + template.expand(params).toString();
			apiLog.setApiUrl(method.name() + " " + uri);

			deal.setVendorPolicyNo(VENDOR_POLICY_NO);
			
			HttpEntity<Deal> requestEntity = new HttpEntity<>(deal, apiRequest.createHttpHeaders(paCode, auth.getAccessToken()));
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DealUpdateResult> response = restTemplate.exchange(uri, method, requestEntity, DealUpdateResult.class);

			DealUpdateResult result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(result.getDeal().getSuccess() && result.getDealOptions().getSuccess() ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaTmonApiRequest.API_SUCCESS_CODE : PaTmonApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(!result.getDeal().getSuccess() ? result.getDeal().getMessage() : "");
			apiLog.setResultMsg(apiLog.getResultMsg() + (!result.getDealOptions().getSuccess() ? result.getDealOptions().getMessage() : ""));
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return apiLog.getResultCode();

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품수정 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}


	/**
	 * 상품 조회

	 * @param goodsCode
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public Deal getProduct(String goodsCode, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_GOODS_NO);

		HttpMethod method = HttpMethod.GET;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_GOODS_NO");
		apiLog.setApiNote("티몬-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.TMON.code());
		apiLog.setProcessId(PaGroup.TMON.processId());

		try {

			Authorization auth = apiRequest.generateToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorId", auth.getVendorId());
			params.put("vendorDealNo", paCode + "-" + goodsCode);

			String uri = template.expand(params).toString();
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, paCode, auth.getAccessToken());
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<Deal> response = restTemplate.exchange(requestEntity, Deal.class);

			Deal result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("1");
			apiLog.setResultCode("200");
			apiLog.setResultMsg(PaTmonApiRequest.API_SUCCESS_CODE);
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTmon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}

}
