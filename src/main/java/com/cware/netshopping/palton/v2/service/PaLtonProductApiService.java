package com.cware.netshopping.palton.v2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.palton.v2.domain.Product;
import com.cware.netshopping.palton.v2.domain.ProductPost;
import com.cware.netshopping.palton.v2.domain.ProductReq;
import com.cware.netshopping.palton.v2.domain.ProductResult;

/**
 * 롯데온 상품 API 호출
 * https://api.lotteon.com/apiService/
 */
@Service
public class PaLtonProductApiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaLtonApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 상품등록 POST
	// 상품을 생성한다. 상품은 한개 이상의 아이템으로 구성된다.
	private static final String CREATE_PRODUCT = "/v1/openapi/product/v1/product/registration/request";

	// 승인 상품 수정 POST
	// 승인된 상품의 정보를 수정한다.
	private static final String UPDATE_PRODUCT = "/v1/openapi/product/v1/product/modification/request";

	// 상품 상세조회 POST
	// 상품을 상세조회한다. 상품에 속해있는 단품까지 조회한다.
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/v1/openapi/product/v1/product/detail";

	// 거래처번호
	@Value("${partner.lotteon.api.tr.no}")
	String TR_NO;
	@Value("${partner.lotteon.api.tv.lrtr.no}")
	String TV_LRTR_NO;
	@Value("${partner.lotteon.api.online.lrtr.no}")
	String ONLINE_LRTR_NO;
	
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
		
		uri =  apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("롯데온-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		product.setTrGrpCd("SR"); // 거래처그룹코드 일반셀러 : SR
		product.setTrNo(TR_NO); // 거래처번호
		product.setLrtrNo(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);
		
		ProductReq productReq = new ProductReq();
		List<Product> spdLst = new ArrayList<Product>();
		spdLst.add(product);
		productReq.setSpdLst(spdLst);

		HttpEntity<ProductReq> requestEntity = new HttpEntity<>(productReq, apiRequest.createHttpHeaders());
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<ProductPost> response = restTemplate.exchange(uri, method, requestEntity, ProductPost.class);

			ProductPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			
			String spdNo = null;
			if (result.getData() != null && result.getData().size() > 0) {
				apiLog.setResultCode(result.getData().get(0).getResultCode());
				apiLog.setResultMsg(result.getData().get(0).getResultMessage());
				spdNo = result.getData().get(0).getSpdNo();
			}

			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return spdNo;

		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
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
		
		uri =  apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("롯데온-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		product.setTrGrpCd("SR"); // 거래처그룹코드 일반셀러 : SR
		product.setTrNo(TR_NO); // 거래처번호
		product.setLrtrNo(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);

		ProductReq productReq = new ProductReq();
		List<Product> spdLst = new ArrayList<Product>();
		spdLst.add(product);
		productReq.setSpdLst(spdLst);

		HttpEntity<ProductReq> requestEntity = new HttpEntity<>(productReq, apiRequest.createHttpHeaders());
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductPost> response = restTemplate.exchange(uri, method, requestEntity, ProductPost.class);

			ProductPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			
			String spdNo = null;
			if (result.getData() != null && result.getData().size() > 0) {
				apiLog.setResultCode(result.getData().get(0).getResultCode());
				apiLog.setResultMsg(result.getData().get(0).getResultMessage());
				spdNo = result.getData().get(0).getSpdNo();
			}

			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return spdNo;

		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
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
	 * @param spdNo
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String goodsCode, String spdNo, String paCode, long transServiceNo) throws Exception {

		String uri = GET_PRODUCT_BY_PRODUCT_ID;

		HttpMethod method = HttpMethod.POST;

		uri =  apiRequest.LOTTEON_GATEWAY + uri;


		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("롯데온-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		Map<String, String> body = new HashMap<String, String>();
		body.put("trGrpCd", "SR"); // 거래처그룹코드 일반셀러 : SR
		body.put("trNo", TR_NO); // 거래처번호
		body.put("lrtrNo", PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO); // 하위거래처번호

		body.put("spdNo", spdNo); // 판매자상품번호

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body,
				apiRequest.createHttpHeaders());
		try {
			
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductResult> response = restTemplate.exchange(uri, method, requestEntity, ProductResult.class);

			ProductResult result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());


			return result.getData();

		} catch (RestClientResponseException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={} ", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}

}
