package com.cware.netshopping.pakakao.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pakakao.v2.domain.ProductRequest;

/**
 * 카카오 상품 API 호출
 */
@Service
public class PaKakaoProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaKakaoApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품 등록 POST
	// 카테고리/모델명/브랜드/제조사 검색, 원산지 전체목록, 판매자 주소록 조회, 이미지 업로드 API 등을 통해
	// 상품등록 API의 요청 데이터를 구성해야 합니다.
	private static final String CREATE_PRODUCT = "/v1/store/product/register";

	// 상품 수정 POST
	// 상품 수정 요청시, 변경 대상이 아닌 항목까지 포함하여 모든 상품정보 항목을 입력해서 전달해야 합니다.
	// 누락시 해당 상품정보에 이전에 입력된 값은 삭제되며, 필수 입력 항목인 경우에는 Validation 오류가 납니다.
	// 상품 수정 API 사용 중에는 다음과 같은 주의사항을 숙지하신 후 사용해야 합니다.
	// - 기존에 등록된 상품을 수정하는 요청시에는 반드시 모든 필드를 입력해야 합니다.
	// - 변경을 원하지 않는 필드를 제외하고 요청하는 경우, 해당 필드는 현재 상태 유지가 아닌 기본 값으로 변경 될 수 있습니다.
	// - 각 필드의 현재 상태를 알 수 없다면 상품 조회 Api를 사용해 전체 필드를 얻어 온 후,
	// 해당 Object에서 변경을 원하는 필드만 수정해서 상품 수정 Api의 요청으로 사용해주시기 바랍니다.
	// - 각 요청 데이터 타입 항목의 각 수정 시 주의사항을 반드시 확인해야 합니다.
	// - 상세설명에 외부 이미지 url로 이미지를 넣으신 경우에도 상품 등록/수정시 카카오 내부 url로 변환해서 저장됩니다. 
	// 입력하신 이미지 url의 이미지가 수정된 경우에는 반드시 판매자센터>상품수정 화면을 통해 수정된 이미지를 다시 등록해 주셔야 합니다.
	private static final String UPDATE_PRODUCT = "/v1/store/product/update";

	// 상품 조회 GET
	// 전체 상품정보를 필요로 할 때 사용합니다. 상품번호 또는 판매자 상품코드로 조회 할 수 있습니다.
	// 판매자 상품코드로 조회시에는 상품정보가 배열 형태로 반환됩니다.
	// (판매자 상품코드에 대해 유일성 체크를 하지 않으므로 배열 형식을 사용합니다.)
	// 상품 조회에 반환되는 타입은 productId와 productSaleStatus가 추가로 들어가며 그 외는 ProductRequest 와 동일합니다.
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/v1/store/product?productId={productId}";

	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerProduct(String goodsCode, ProductRequest product, String paCode, long transServiceNo) {

		String uri = CREATE_PRODUCT;

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.KAKAO_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("카카오-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.KAKAO.code());
		apiLog.setProcessId(PaGroup.KAKAO.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(StringUtil.objectToJson(product));
		
		HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApi(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			
			String productId = String.valueOf(StringUtil.jsonToMap(result).get("productId"));

			apiLog.setResponsePayload(result);
			
			apiLog.setSuccessYn("null".equals(productId) || "0".equals(productId) ? "0" : "1");
			apiLog.setResultCode(PaKakaoApiRequest.API_SUCCESS_CODE);
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return productId;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorKakao(apiLog, ex);
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
	 * 상품수정API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateProduct(String goodsCode, ProductRequest product, String paCode, long transServiceNo) {

		String uri = UPDATE_PRODUCT;

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.KAKAO_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("카카오-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.KAKAO.code());
		apiLog.setProcessId(PaGroup.KAKAO.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(StringUtil.objectToJson(product));

		HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApi(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			
			String productId = String.valueOf(StringUtil.jsonToMap(result).get("productId"));

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("null".equals(productId) || "0".equals(productId) ? "0" : "1");
			apiLog.setResultCode(PaKakaoApiRequest.API_SUCCESS_CODE);
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return productId;
			
		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorKakao(apiLog, ex);
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
	 * 
	 * @param goodsCode
	 * @param productId
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public ProductRequest getProduct(String goodsCode, String productId, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("productId", productId);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("카카오-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.KAKAO.code());
		apiLog.setProcessId(PaGroup.KAKAO.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		try {
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, paCode);

			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ProductRequest> response = restTemplate.exchange(requestEntity, ProductRequest.class);

			ProductRequest result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("1");
			apiLog.setResultCode(PaKakaoApiRequest.API_SUCCESS_CODE);
			transLogService.logTransApiRes(apiLog);

			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorKakao(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}

}
