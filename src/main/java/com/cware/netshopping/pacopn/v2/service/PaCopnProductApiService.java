package com.cware.netshopping.pacopn.v2.service;

import java.util.HashMap;
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

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pacopn.v2.domain.ApiResponseMsg;
import com.cware.netshopping.pacopn.v2.domain.Product;
import com.cware.netshopping.pacopn.v2.domain.ProductMsg;

/**
 * 쿠팡 상품 API 호출
 * https://developers.coupangcorp.com/hc/ko/sections/360005046534-%EC%83%81%ED%92%88-APIs
 */
@Service
public class PaCopnProductApiService {

	// 방송상품 판매자
	@Value("${partner.coupang.api.tv.vendor.id}")
	String TV_VENDOR_ID;
	@Value("${partner.coupang.api.tv.vendor.user.id}")
	String TV_VENDOR_USER_ID;

	// 온라인상품 판매자
	@Value("${partner.coupang.api.online.vendor.id}")
	String ONLINE_VENDOR_ID;
	@Value("${partner.coupang.api.online.vendor.user.id}")
	String ONLINE_VENDOR_USER_ID;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaCopnApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품 생성 POST
	// 상품을 생성할 때는 쿠팡 규격에 맞는 출고지/반품지, 카테고리, 고시정보, 옵션 정보 등이 필요
	// 쿠팡 OPENAPI 상품등록가이드: https://developers.coupangcorp.com/hc/ko/articles/360034889893
	private static final String CREATE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products";

	// 상품 수정 (승인필요) PUT
	// '상품 조회 API'를 이용하여 조회된 JSON 전문에서 원하는 값 만 수정 후, 전체 JSON 전문을 전송하여 수정
	// 이 API를 사용하면 승인 후에 반영
	// 상품 생성 시 사용한 request body에, [sellerProductId]와 [sellerProductItemId]를 삽입하여 수정 가능
	// - 옵션 수정 : 수정하고 싶은 옵션의 아이템 상단에 [sellerProductItemId]와 [vendorItemId] 삽입 후, 원하는 [attributeValueName]으로 수정하여 전송
	// - 옵션 삭제 : 삭제하고 싶은 옵션의 'items 배열 하위 값'을 제거하고, 남기고 싶은 아이템 상단에 [sellerProductItemId] 삽입 후 전송 (이미 승인완료된 이력이 있는 옵션은 삭제 불가)
	// - 옵션 추가 : [sellerProductItemId]를 입력하지 않고 items 배열을 추가하여 전송
	// ※ [sellerProductItemId]는 상품 조회 API를 이용해 확인
	// ※ 승인 완료된 상품의 판매가격, 재고수량, 판매상태, 할인율 기준가는 상품수정API가 아닌, [상품 아이템별 수량/가격/판매여부/할인율기준가 변경] API를 통해 가능
	private static final String UPDATE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products";

	// 상품 조회 GET
	// 등록상품 ID(sellerProductId)로 등록된 상품의 정보를 조회
	// 상품 가격/재고/판매상태 수정 시 필요한 옵션ID(vendorItemId)를 확인
	// 상품 정보를 조회하여 상품 수정 시 활용할 수 있는 전문을 확인
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}";

	// 상품 승인 요청 PUT
	// 임시저장 상태의 상품은 승인요청->승인완료 단계를 거처 상품 페이지에 노출
	// 주의: "인벤토리(XXXX)는 승인요청 상태가 아닙니다." 메시지는 "업체상품[XXXX]이 승인요청 가능한 상태가 아닙니다." 의 의미
	// '임시저장' 상태에서만 '승인요청'이 가능
	// 상품생성, 수정 API 에서 requested 파라메터를  true 로 입력할 경우 자동으로 판매 승인요청이 진행
	private static final String APPROVE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/approvals";

	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerProduct(String goodsCode, Product product, String paCode, long transServiceNo) {

		String uri = CREATE_PRODUCT;

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		// 판매자ID
		settingVendorId(product, paCode);
		product.setRequested(true); // 자동승인
		
		uri =  apiRequest.COUPANG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("쿠팡-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ApiResponseMsg> response = restTemplate
					.exchange(uri, method, requestEntity, ApiResponseMsg.class);

			ApiResponseMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(PaCopnApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			String sellerProductId = result.getData();
			
			return sellerProductId;

		} catch (RestClientResponseException ex) {
			transLogService.logTransApiResError(apiLog, ex);
			log.error("상품등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
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
	 * 방송/온라인 판매자계정 ID 설정
	 * 
	 * @param product
	 * @param paCode
	 */
	private void settingVendorId(Product product, String paCode) {
		if (PaCode.COUPANG_TV.code().equals(paCode)) {
			product.setVendorId(TV_VENDOR_ID);
			product.setVendorUserId(TV_VENDOR_USER_ID);
		} else {
			product.setVendorId(ONLINE_VENDOR_ID);
			product.setVendorUserId(ONLINE_VENDOR_USER_ID);
		}
	}

	/**
	 * 상품수정API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateProduct(String goodsCode, Product product, String paCode, long transServiceNo) {

		String uri = UPDATE_PRODUCT;

		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		// 판매자ID
		settingVendorId(product, paCode);
		product.setRequested(true); // 자동승인
		
		uri =  apiRequest.COUPANG_GATEWAY + uri;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("쿠팡-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(authorization));
		
		try {
			
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ApiResponseMsg> response = restTemplate
					.exchange(uri, method, requestEntity, ApiResponseMsg.class);

			ApiResponseMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(PaCopnApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			String sellerProductId = result.getData();
			
			return sellerProductId;

		} catch (RestClientResponseException ex) {
			transLogService.logTransApiResError(apiLog, ex);
			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			transLogService.logTransApiResError(apiLog, e);
			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 상품 조회
	 *
	 * @param goodsCode
	 * @param sellerProductId 등록상품ID
	 * @param paCode 제휴사코드
	 * @param transServiceNo
	 * @return
	 * @throws Exception 
	 */
	public Product getProduct(String goodsCode, String sellerProductId, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", sellerProductId);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("쿠팡-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());

		try {
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, authorization);
			
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ProductMsg> response = restTemplate.exchange(requestEntity, ProductMsg.class);

			ProductMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(PaCopnApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);
			
			Product product = result.getData();

			return product;

		} catch (RestClientResponseException ex) {
			transLogService.logTransApiResError(apiLog, ex);
			log.error("상품조회 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}


	/**
	 * 상품승인요청
	 *
	 * @param goodsCode
	 * @param sellerProductId 등록상품ID
	 * @param paCode 제휴사코드
	 * @param transServiceNo
	 * @return
	 * @throws Exception 
	 */
	public String approveProduct(String goodsCode, String sellerProductId, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(APPROVE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", sellerProductId);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("APPROVE_PRODUCT");
		apiLog.setApiNote("쿠팡-상품승인요청");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());

		try {
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, authorization);
			
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ApiResponseMsg> response = restTemplate.exchange(requestEntity, ApiResponseMsg.class);

			ApiResponseMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(PaCopnApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);
			
			return result.getData();

		} catch (RestClientResponseException ex) {
			log.error("sellerProductId={}", sellerProductId, ex);
			transLogService.logTransApiResError(apiLog, ex);
			if (ex.getRawStatusCode() == 400) return sellerProductId;
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("sellerProductId={}", sellerProductId, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}
}
