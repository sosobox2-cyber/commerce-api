package com.cware.netshopping.pawemp.v2.service;

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
import com.cware.netshopping.pawemp.v2.domain.Product;
import com.cware.netshopping.pawemp.v2.domain.ProductGet;
import com.cware.netshopping.pawemp.v2.domain.ProductPost;

/**
 * 위메프 상품 API 호출
 */
@Service
public class PaWempProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaWempApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품일괄등록 POST
	// 상품에 전체정보를 일괄로 등록,수정 처리하게 되며, 필수 항목이 누락될 경우 등록이 불가 합니다.
	// 상품번호(productNo)를 입력하지 않으면 등록, 입력하면 수정으로 판단합니다.
	private static final String CREATE_PRODUCT = "/product/in/setProduct";

	// 상품일괄수정 POST
	// 상품에 전체정보를 일괄로 등록,수정 처리하게 되며, 필수 항목이 누락될 경우 등록이 불가 합니다.
	// 상품번호(productNo)를 입력하지 않으면 등록, 입력하면 수정으로 판단합니다.
	private static final String UPDATE_PRODUCT = "/product/in/setProduct";

	// 상품 조회 GET
	// 파라미터를 통해 등록한 상품정보를 조회 할 수 있습니다
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/product/out/getProduct?productNo={productNo}";

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
		
		uri =  apiRequest.WEMP_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("위메프-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.WEMP.code());
		apiLog.setProcessId(PaGroup.WEMP.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ProductPost> response = restTemplate.exchange(uri, method, requestEntity, ProductPost.class);

			ProductPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(result.getData().getReturnMsg());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			String productNo = result.getData().getProductNo();
			
			return productNo;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorWemp(apiLog, ex);
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
	public String updateProduct(String goodsCode, Product product, String paCode, long transServiceNo) {

		String uri = UPDATE_PRODUCT;

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.WEMP_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("위메프-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.WEMP.code());
		apiLog.setProcessId(PaGroup.WEMP.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductPost> response = restTemplate.exchange(uri, method, requestEntity, ProductPost.class);

			ProductPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(result.getData().getReturnMsg());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			String productNo = result.getData().getProductNo();
			
			return productNo;

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorWemp(apiLog, ex);
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
	 * @param productNo
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String goodsCode, String productNo, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("productNo", productNo);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("위메프-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.WEMP.code());
		apiLog.setProcessId(PaGroup.WEMP.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		try {
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, paCode);

			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ProductGet> response = restTemplate.exchange(requestEntity, ProductGet.class);

			ProductGet result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			transLogService.logTransApiRes(apiLog);

			return result.getData();

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorWemp(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}

}
