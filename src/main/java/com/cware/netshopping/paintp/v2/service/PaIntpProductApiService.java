package com.cware.netshopping.paintp.v2.service;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.paintp.v2.domain.Item;
import com.cware.netshopping.paintp.v2.domain.Product;
import com.cware.netshopping.paintp.v2.domain.ProductResult;

/**
 * 인터파크 상품 API 호출
 * http://www.interpark.com/openapi/site/APIInsertSpecNew.jsp
 * http://www.interpark.com/openapi/site/APIUpdateSpecNew.jsp
 */
@Service
public class PaIntpProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaIntpApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	@Autowired
	private TransLogMapper logMapper;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품등록 GET
	// 상품 등록 API는 유저(개발자)에게 외부 상품 데이터를 빠르게 등록할 수 있도록 제공하는 API 서비스 입니다. 
	// API는 유저가 지정한 특정 URL의 XML 데이터에 접근하여 데이터를 파싱한 후 인터파크 DB에 상품 등록을 진행하며, 
	// 상품 등록 과정이 완료 되면 결과를 다시 유저에게 XML 데이터로 전달 합니다
	private static final String CREATE_PRODUCT = "/openapi/product/ProductAPIService.do?_method=InsertProductAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 상품등록 인증키
	@Value("${partner.interpark.api.product.register.tv.cite.key}")
	String CREATE_PRODUCT_TV_CITE_KEY;
	@Value("${partner.interpark.api.product.register.tv.secret.key}")
	String CREATE_PRODUCT_TV_SECRET_KEY;
	@Value("${partner.interpark.api.product.register.online.cite.key}")
	String CREATE_PRODUCT_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.product.register.online.secret.key}")
	String CREATE_PRODUCT_ONLINE_SECRET_KEY;
	
	// 상품수정 GET
	// 상품 수정 API는 유저(개발자)에게 인터파크 상품 데이터를 빠르게 수정할 수 있도록 제공하는 API 서비스 입니다. 
	// API는 유저가 지정한 특정 URL의 XML 데이터에 접근하여 데이터를 파싱한 후 상품 수정 사항을 적용하며, 
	// 상품 수정 과정이 완료 되면 결과를 다시 유저에게 XML 데이터로 전달 합니다.
	private static final String UPDATE_PRODUCT = "/openapi/product/ProductAPIService.do?_method=UpdateProductAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 상품수정 인증키
	@Value("${partner.interpark.api.product.update.tv.cite.key}")
	String UPDATE_PRODUCT_TV_CITE_KEY;
	@Value("${partner.interpark.api.product.update.tv.secret.key}")
	String UPDATE_PRODUCT_TV_SECRET_KEY;
	@Value("${partner.interpark.api.product.update.online.cite.key}")
	String UPDATE_PRODUCT_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.product.update.online.secret.key}")
	String UPDATE_PRODUCT_ONLINE_SECRET_KEY;

	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param item
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerProduct(String goodsCode, Item item, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = CREATE_PRODUCT_TV_CITE_KEY;
			secretKey = CREATE_PRODUCT_TV_SECRET_KEY;
		} else {
			citeKey = CREATE_PRODUCT_ONLINE_CITE_KEY;
			secretKey = CREATE_PRODUCT_ONLINE_SECRET_KEY;
		}
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("인터파크-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(CREATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + goodsCode + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			Product product = new Product();
			product.setItem(item);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(Product.class), product, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			
			ProductResult productResult = StringUtil.xmlToObject(result, ProductResult.class);

			apiLog.setSuccessYn(productResult.getSuccess() != null ? "1" : "0");

			if (productResult.getError() != null) {
				apiLog.setResultCode(productResult.getError().getCode());
				apiLog.setResultMsg(productResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			String prdNo = productResult.getSuccess().getPrdNo();
			
			return prdNo;

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
	 * @param item
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateProduct(String goodsCode, Item item, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = UPDATE_PRODUCT_TV_CITE_KEY;
			secretKey = UPDATE_PRODUCT_TV_SECRET_KEY;
		} else {
			citeKey = UPDATE_PRODUCT_ONLINE_CITE_KEY;
			secretKey = UPDATE_PRODUCT_ONLINE_SECRET_KEY;
		}
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("인터파크-상품수정["+item.getSaleStatTp()+"]");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + goodsCode + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			Product product = new Product();
			product.setItem(item);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(Product.class), product, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			
			ProductResult productResult = StringUtil.xmlToObject(result, ProductResult.class);

			apiLog.setSuccessYn(productResult.getSuccess() != null ? "1" : "0");

			if (productResult.getError() != null) {
				apiLog.setResultCode(productResult.getError().getCode());
				apiLog.setResultMsg(productResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			String prdNo = productResult.getSuccess().getPrdNo();
			
			return prdNo;


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

}
