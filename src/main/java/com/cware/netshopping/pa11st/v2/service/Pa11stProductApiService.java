package com.cware.netshopping.pa11st.v2.service;

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

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pa11st.v2.domain.ClientMessage;
import com.cware.netshopping.pa11st.v2.domain.Product;

/**
 * 11번가 상품 API 호출
 * http://openapi.11st.co.kr/openapi/OpenApiGuide.tmall?categoryNo=81
 */
@Service
public class Pa11stProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Pa11stApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품등록/단일상품등록 POST
	// API 상품등록/단일상품 서비스입니다. 
	// 각각의 Element에 유의해서 상품등록을 해주세요. 
	// 초기 개발시에는 반드시 Seller Office 상품등록과 병행해서 개발진행해주세요.
	// 상품등록은 Method Type 이 POST 타입입니다. 유의해주세요. 
	// API KEY 는 HttpHeaders 에서 읽어들입니다. 반드시 header에 실어 주셔야합니다.
	// XML 데이타는 InputStream 에서 읽어들입니다. 반드시 body에 실어 주셔야합니다.
	private static final String CREATE_PRODUCT = "/rest/prodservices/product";

	// 상품수정/단일상품수정 PUT
	// API 상품수정/단일상품수정 서비스입니다. 
	// 각각의 Element에 유의해서 상품수정을 해주세요. 
	// 초기 개발시에는 반드시 Seller Office 상품등록과 병행해서 개발진행해주세요.
	// 상품수정은 기존에 등록된 상품위에 수정될 데이터를 덮어쓰는 형태입니다. 
	// 기존 데이터는 사라지고 수정되는 정보로 교체됩니다. 
	// 수정시에도 등록처럼 XML 전문을 입력하셔야합니다. 
	// 상품수정은 Method Type 이 PUT 타입입니다. 상품등록과 Method Type 이 다르니 유의해주세요. 
	// API KEY 는 HttpHeaders 에서 읽어들입니다. 반드시 header에 실어 주셔야합니다. 
	// XML 데이타는 InputStream 에서 읽어들입니다. 반드시 body에 실어 주셔야합니다. 
	private static final String UPDATE_PRODUCT = "/rest/prodservices/product/{prdNo}";

	// 단일 상품 조회 GET
	// 11번가에 등록된 상품의 정보를 상품번호로 조회
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/rest/prodmarketservice/prodmarket/{prdNo}";
	
	// 배송불가지역 적용 PUT
	// 상품별로 배송불가지역을 적용할 수 있습니다
	private static final String STAT_PRODUCT_IMPOSSIBLE_ADDRESS = "/rest/areaservice/stat/ImpossibleAddress/{impossibleNo}/{prdNo}";

	// 배송불가지역 해제 PUT
	// 상품별로 배송불가지역을 해제할 수 있습니다.
	private static final String STOP_PRODUCT_IMPOSSIBLE_ADDRESS = "/rest/areaservice/stop/ImpossibleAddress/{prdNo}";
	
	//배송불가지역 템플릿번호
	private static final String IMPOSSIBLE_NO_TV = "2831879";
	
	//배송불가지역 템플릿번호
	private static final String IMPOSSIBLE_NO_ONLINE = "2831896";

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
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("11번가-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			ClientMessage clientMessage = StringUtil.xmlToObject(result, ClientMessage.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(StringUtils.hasText(clientMessage.getProductNo()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(clientMessage.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			String productNo = clientMessage.getProductNo();
			
			return productNo;

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
	public String updateProduct(String goodsCode, Product product, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("prdNo", product.getPrdNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;

		uri =  apiRequest.SK11ST_GATEWAY + uri;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("11번가-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(product, apiRequest.createHttpHeaders(paCode));
		
		try {
			
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			ClientMessage clientMessage = StringUtil.xmlToObject(result, ClientMessage.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(StringUtils.hasText(clientMessage.getProductNo()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? "200" : "500");
			apiLog.setResultMsg(clientMessage.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			String productNo = clientMessage.getProductNo();
			
			return productNo;

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
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
	 * @param productNo
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String goodsCode, String productNo, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("prdNo", productNo);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("11번가-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		try {
			
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, paCode);
			
			transLogService.logTransApiReqXml(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			Product product = StringUtil.xmlToObject(result, Product.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(StringUtils.hasText(product.getPrdNo()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(product.getMessage());
			transLogService.logTransApiRes(apiLog);

			return product;

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}
	
	/**
	 * 상품 배송불가지역 적용
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public String statImpossibleAddress(String goodsCode, String productNo, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(STAT_PRODUCT_IMPOSSIBLE_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("prdNo", productNo);
		
		if(PaCode.SK11ST_TV.code().equals(paCode)) {
			params.put("impossibleNo", IMPOSSIBLE_NO_TV);
		}else {
			params.put("impossibleNo", IMPOSSIBLE_NO_ONLINE);
		}
		
		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;

		uri =  apiRequest.SK11ST_GATEWAY + uri;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("STAT_PRODUCT_IMPOSSIBLE_ADDRESS");
		apiLog.setApiNote("11번가-상품 배송불가지역 적용");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(apiRequest.createHttpHeaders(paCode));
		
		try {
			
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			ClientMessage clientMessage = StringUtil.xmlToObject(result, ClientMessage.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("200".equals(clientMessage.getResultCode()) ? "1" : "0");
			if(clientMessage.getMessage() != null && clientMessage.getMessage().contains("이미 적용된 배송불가지역")) {
				apiLog.setSuccessYn("1");
			}
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? "200" : "500");
			apiLog.setResultMsg(clientMessage.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			String resultCode = clientMessage.getResultCode();
			
			return resultCode;

		} catch (RestClientResponseException ex) {
			log.error("상품 배송불가지역 적용 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품 배송불가지역 적용 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			transLogService.logTransApiResError(apiLog, e);
			log.error("상품 배송불가지역 적용 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 상품 배송불가지역 해제
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public String stopImpossibleAddress(String goodsCode, String productNo, String paCode, long transServiceNo) throws Exception {

		UriTemplate template = new UriTemplate(STOP_PRODUCT_IMPOSSIBLE_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("prdNo", productNo);
		
		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;

		uri =  apiRequest.SK11ST_GATEWAY + uri;
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("STOP_PRODUCT_IMPOSSIBLE_ADDRESS");
		apiLog.setApiNote("11번가-상품 배송불가지역 해제");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<Product> requestEntity = new HttpEntity<>(apiRequest.createHttpHeaders(paCode));
		
		try {
			
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			ClientMessage clientMessage = StringUtil.xmlToObject(result, ClientMessage.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("200".equals(clientMessage.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? "200" : "500");
			apiLog.setResultMsg(clientMessage.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			String resultCode = clientMessage.getResultCode();
			
			return resultCode;

		} catch (RestClientResponseException ex) {
			log.error("상품 배송불가지역 해제 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("상품 배송불가지역 해제 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			transLogService.logTransApiResError(apiLog, e);
			log.error("상품 배송불가지역 해제 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

}
