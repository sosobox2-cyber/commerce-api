package com.cware.netshopping.passg.v2.service;

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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.passg.v2.domain.Product;
import com.cware.netshopping.passg.v2.domain.ProductInsert;
import com.cware.netshopping.passg.v2.domain.ProductResult;
import com.cware.netshopping.passg.v2.domain.ProductUpdate;

/**
 * SSG 상품 API 호출
 * https://eapi.ssgadm.com/info/apiGuide.ssg
 */
@Service
public class PaSsgProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaSsgApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품 정보 등록 POST
	// 온라인 상품정보를 등록하는 API입니다. 
	// 상품의 가격,옵션,재고,이미지, 상세설명정보 등과 같은 상품의 필수 정보를 등록합니다.
	// 이미지 파일은 이미지당 최대 5MB 이하만 업로드 가능합니다.
	private static final String CREATE_PRODUCT = "/item/{version}/insertItem.ssg";

	// 상품 정보 수정 POST
	// SSG에 등록된 온라인 상품정보를 수정하는 API입니다. 
	// 항목별 수정이 가능하나, 상품수정항목중 MD승인으로 설정된 상품의 경우에 MD의 승인이 있어야만 수정항목이 반영됩니다.
	// 이미지 파일은 이미지당 최대 5MB 이하만 업로드 가능합니다.
	private static final String UPDATE_PRODUCT = "/item/{version}/updateItem.ssg";

	// 상품 상세 조회 GET
	// 업체가 등록한 상품의 상세 정보를 조회합니다.
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/item/{version}/viewItem.ssg?itemId={itemId}";
	
	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public Product registerProduct(String goodsCode, ProductInsert product, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(CREATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.4");

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri =  apiRequest.SSG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("SSG-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		try {

			HttpEntity<ProductInsert> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity,  String.class);

			String xmlString =response.getBody();
			ProductResult result = StringUtil.xmlToObject(xmlString, ProductResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
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
	public Product updateProduct(String goodsCode, ProductUpdate product, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.3");

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri =  apiRequest.SSG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("SSG-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		try {

			HttpEntity<ProductUpdate> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String xmlString =response.getBody();
			ProductResult result = StringUtil.xmlToObject(xmlString, ProductResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
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
	 * @param itemId
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String goodsCode, String itemId, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.3");
		params.put("itemId", itemId);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("SSG-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, paCode);

			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String xmlString =response.getBody();
			ProductResult result = StringUtil.xmlToObject(xmlString, ProductResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result;

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
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
