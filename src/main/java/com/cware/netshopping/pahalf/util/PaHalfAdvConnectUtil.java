package com.cware.netshopping.pahalf.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;

@Repository("com.cware.netshopping.palton.util.PaHalfAdvConnectUtil")
public class PaHalfAdvConnectUtil {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaHalfApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception 
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> registerProduct(String goodsCode, long transServiceNo, ParamMap apiInfoMap, Object apiDataMap) throws Exception {
		
		String uri = apiInfoMap.getString("url");
		String apiName = apiInfoMap.getString("apiName");
		String queryString = apiInfoMap.getString("queryString");
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
		
		uri =  apiRequest.HALF_GATEWAY + uri + queryString;
		
		String body = apiRequest.getBody(apiDataMap);
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("하프클럽(상품등록)-" + apiName);
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.HALF.code());
		apiLog.setProcessId(PaGroup.HALF.processId());		
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(body);
		
		apiInfoMap.put("body", body);
	
		try {
			ResponseEntity<String> response = null;
			
			if(uri.contains("postPrdCntsDtlInfo")||uri.contains("putPrdCntsDtlInfo")) { //상품 컨텐츠 상세 등록		
				MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
				parameters.set("boundary", "------");
				parameters.add("metaData", apiLog.getRequestPayload());
				
				HttpEntity<MultiValueMap<String, Object>> requestMultiEntity = new HttpEntity<>(parameters ,apiRequest.createHttpHeaders(uri, apiInfoMap));
				transLogService.logTransApi(apiLog, requestMultiEntity);
				response = restTemplate.exchange(uri, method, requestMultiEntity, String.class);				
			} else {
				HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(uri, apiInfoMap));	
				transLogService.logTransApi(apiLog, requestEntity);
				response = restTemplate.exchange(uri, method, requestEntity, String.class);
			}

			Map<String, Object> resultMap = StringUtil.jsonToMap(response.getBody());
			
			Map<String, Object> resultStatus =  (Map<String, Object>)resultMap.get("resultStatus");
			
			String code = String.valueOf(resultStatus.get("code"));
			String message = String.valueOf(resultStatus.get("message"));
			
			apiLog.setResponsePayload(response.getBody());
			
			apiLog.setSuccessYn("200".equals(code) ? "1" : "0");
		
			if("0".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaHalfApiRequest.API_ERROR_CODE);
				apiLog.setResultMsg(message);
			}else {
				apiLog.setResultCode(PaHalfApiRequest.API_SUCCESS_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			resultMap.put("paCode", apiInfoMap.getString("paCode"));

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return resultMap;
			
		} catch (RestClientResponseException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorHalfSeller(apiLog, ex);
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
	 * @throws Exception 
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> updateProduct(String goodsCode, long transServiceNo, ParamMap apiInfoMap, Object apiDataMap) throws Exception {

		String uri = apiInfoMap.getString("url");
		String apiName = apiInfoMap.getString("apiName");
		String queryString = apiInfoMap.getString("queryString");
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
		
		uri =  apiRequest.HALF_GATEWAY + uri + queryString;
		
		String body = apiRequest.getBody(apiDataMap);
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("하프클럽(상품수정)-" + apiName);
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.HALF.code());
		apiLog.setProcessId(PaGroup.HALF.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(body);
	
		try {
			ResponseEntity<String> response = null;
			
			if(uri.contains("postPrdCntsDtlInfo") || uri.contains("putPrdCntsDtlInfo")) { //상품 컨텐츠 상세 수정			
				MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
				parameters.set("boundary", "------");
				parameters.add("metaData", apiLog.getRequestPayload());
				
				HttpEntity<MultiValueMap<String, Object>> requestMultiEntity = new HttpEntity<>(parameters ,apiRequest.createHttpHeaders(uri, apiInfoMap));
				transLogService.logTransApi(apiLog, requestMultiEntity);
				response = restTemplate.exchange(uri, method, requestMultiEntity, String.class);				
			} else {
				HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(uri, apiInfoMap));	
				transLogService.logTransApi(apiLog, requestEntity);
				response = restTemplate.exchange(uri, method, requestEntity, String.class);
			}
						
			Map<String, Object> resultMap = StringUtil.jsonToMap(response.getBody());	
			Map<String, Object> resultStatus = (Map<String, Object>)resultMap.get("resultStatus");
			
			String code = String.valueOf(resultStatus.get("code"));
			String message = String.valueOf(resultStatus.get("message"));
			
			apiLog.setResponsePayload(response.getBody());
			
			apiLog.setSuccessYn("200".equals(code) ? "1" : "0");

			if("0".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaHalfApiRequest.API_ERROR_CODE);
				apiLog.setResultMsg(message);
			}else {
				apiLog.setResultCode(PaHalfApiRequest.API_SUCCESS_CODE);
			}			
			
			transLogService.logTransApiRes(apiLog);

			resultMap.put("paCode", apiInfoMap.getString("paCode"));
			
			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return resultMap;
			
		} catch (RestClientResponseException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorHalfSeller(apiLog, ex);
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
	 * 배송탬플릿 API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 * @throws Exception 
	 */
	
	public Map<String,Object> registerSellerAddress(String entpCode, String shipManSeq, String returnManSeq, String shipCostCode , long transServiceNo, ParamMap apiInfoMap, Object apiDataMap) throws Exception {

		String uri = apiInfoMap.getString("url");
		String apiName = apiInfoMap.getString("apiName");
		String queryString = apiInfoMap.getString("queryString");
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
		
		uri =  apiRequest.HALF_GATEWAY + uri + queryString;
		
		String body = apiRequest.getBody(apiDataMap);
		
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + shipManSeq + returnManSeq + shipCostCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_SELLER_ADDRESS");
		apiLog.setApiNote("하프클럽-" + apiName);
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.HALF.code());
		apiLog.setProcessId(PaGroup.HALF.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(body);
	
		try {
			ResponseEntity<String> response = null;
			
			HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(uri, apiInfoMap));	
			transLogService.logTransApi(apiLog, requestEntity);
			response = restTemplate.exchange(uri, method, requestEntity, String.class);

			Map<String, Object> resultMap = StringUtil.jsonToMap(response.getBody());
			
			@SuppressWarnings("unchecked")
			Map<String, Object> resultStatus =  (Map<String, Object>)resultMap.get("resultStatus");
			
			String code = String.valueOf(resultStatus.get("code"));
			String message = String.valueOf(resultStatus.get("message"));
			
			apiLog.setResponsePayload(response.getBody());
			
			apiLog.setSuccessYn("200".equals(code) ? "1" : "0");
            
			if("0".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaHalfApiRequest.API_ERROR_CODE);
				apiLog.setResultMsg(message);
			}else {
				apiLog.setResultCode(PaHalfApiRequest.API_SUCCESS_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			resultMap.put("paCode", apiInfoMap.getString("paCode"));
			
			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return resultMap;
			
		} catch (RestClientResponseException ex) {
			log.error("판매자주소={}-{} {} {} {}", entpCode, shipManSeq, returnManSeq, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorHalfSeller(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("판매자주소={}-{} {} {} {}", entpCode, shipManSeq, returnManSeq, shipCostCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("판매자주소={}-{} {} {} {}", entpCode, shipManSeq, returnManSeq, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}		

}
