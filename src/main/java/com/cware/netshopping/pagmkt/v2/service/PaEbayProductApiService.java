package com.cware.netshopping.pagmkt.v2.service;

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
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pagmkt.v2.domain.Product;
import com.cware.netshopping.pagmkt.v2.domain.ProductResponse;

/**
 * 이베이 상품 API 호출
 * https://etapi.ebaykorea.com/goods/categories-item-001/categories-docid-2779/
 */
@Service
public class PaEbayProductApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaEbayApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품 일괄 등록 POST
	// 상품이 등록되면 일괄 수정으로 진행할 수 있고 기능별 수정기능을 사용하여 정보 수정가능
	// 2.0 상품은 마스터상품번호(goodsNo)에 속한 사이트(G마켓/옥션)상품번호가 채번되는 구조로,
	// 상품관련 API는 모두 마스터상품번호를 기준으로 통신하며, 주문/배송/클레임/정산 API는 사이트상품번호만 내려가기 때문에 모든 상품번호를
	// 관리
	// 상품등록 성공 시, 바로 판매 가능한 상태로 진행되고, 실패 시, 실패 사유에 따라 정보 수정하여 재등록 필요
	private static final String CREATE_PRODUCT = "/item/v1/goods?isSync=true";

	// 상품 일괄 수정 PUT
	// 상품수정 API는 전체정보를 일괄 수정할 경우에만 사용
	// 그 외는 각 기능별로 수정가능한 Partial API로 각 정보를 수정
	private static final String UPDATE_PRODUCT = "/item/v1/goods/{goodsNo}?isSync=true";

	// 상품 조회 GET
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/item/v1/goods/{goodsNo}";

	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param paGroupCode
	 * @param transServiceNo
	 * @return
	 */
	public ProductResponse registerProduct(String goodsCode, Product product, String paCode, String paGroupCode,
			long transServiceNo) {

		String uri = CREATE_PRODUCT;

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateToken(method.name(), uri, paCode, paGroupCode);

		uri = apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("이베이-상품생성["+paGroupCode+"]");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.AUCTION.code().equals(paGroupCode) ? paGroupCode : PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(authorization));

		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductResponse> response = restTemplate.exchange(uri, method, requestEntity,
					ProductResponse.class);

			ProductResponse result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
//			log.error("상품등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
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
	 * @param esmGoodsCode
	 * @param product
	 * @param paCode
	 * @param paGroupCode
	 * @param transServiceNo
	 * @return
	 */
	public ProductResponse updateProduct(String goodsCode,  String esmGoodsCode, Product product, String paCode, String paGroupCode,
			long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsNo", esmGoodsCode);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateToken(method.name(), uri, paCode, paGroupCode);

		uri = apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("이베이-상품수정["+paGroupCode+"]");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.AUCTION.code().equals(paGroupCode) ? paGroupCode : PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(authorization));

		try {

			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductResponse> response = restTemplate.exchange(uri, method, requestEntity,
					ProductResponse.class);

			ProductResponse result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
//			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
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
	 * @param goodsCode 상품코드
	 * @param esmGoodsCode 제휴사마스터상품코드
	 * @param paCode 제휴사코드
	 * @param paGroupCode 제휴사그룹
	 * @param transServiceNo 서비스번호
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String goodsCode, String esmGoodsCode, String paCode, String paGroupCode,
			long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsNo", esmGoodsCode);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateToken(method.name(), uri, paCode, paGroupCode);

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("이베이-상품조회["+paGroupCode+"]");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.AUCTION.code().equals(paGroupCode) ? paGroupCode : PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());

		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, authorization);

			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<Product> response = restTemplate.exchange(requestEntity, Product.class);

			Product result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			return result;

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

}
