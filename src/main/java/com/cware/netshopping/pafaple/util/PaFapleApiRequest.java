package com.cware.netshopping.pafaple.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.pafaple.domain.Addr;
import com.cware.netshopping.pafaple.domain.Brand;
import com.cware.netshopping.pafaple.domain.SBrand;
import com.cware.netshopping.pafaple.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

/**
 * 패션플러스 API 연동 공통(인증/헤더)
 */
@Service
public class PaFapleApiRequest extends AbstractService {
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Value("${partner.faple.api.tv.custCode}")
	String FAPLE_TV_CUST_CODE; //방송
	
	@Value("${partner.faple.api.online.custCode}")
	String FAPLE_ONLINE_CUST_CODE; //온라인
	
	@Value("${partner.faple.api.gateway}")
	String FAPLE_GATEWAY;
	
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;
		
	private static final int CONN_TIMEOUT = 100;
	private static final int CONN_TIMEOUT_120 = 120;
	
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE 	= "ERROR";
	
	/* 패션플러스 API KEY 생성 */
	public void createApiKey(ParamMap apiBaseInfo) throws Exception {
		TimeZone timeZone = null;
		String custCode = null;
		Calendar calendar = null;
		String openKeyDate = null;
		String openKey = null;
		String requestMakeDate = null;
		String keyBase = null;
		String apiKey = null;
		MessageDigest md = null;
		byte[] messageDigest = null;
		byte[] iv= null;
		SecureRandom random = null;
		IvParameterSpec ivParameterSpec = null;
		SecretKeySpec secretKeySpec = null;
		Cipher cipher = null;
		byte[] encrypted = null;
		byte[] combined = null;
		
		try {
			//계정정보
			if(apiBaseInfo.get("paCode").equals("E1")) {
				custCode = FAPLE_TV_CUST_CODE;	
			}else {
				custCode = FAPLE_ONLINE_CUST_CODE;
			}
			
			// 설정한 시간대를 현재 시스템 시간대로 설정합니다.
			timeZone = TimeZone.getTimeZone("Asia/Seoul");            
            TimeZone.setDefault(timeZone);
            
            calendar = Calendar.getInstance();
            openKeyDate = String.format("%ty%tm%td%tH", calendar, calendar, calendar, calendar);            
            
            md = MessageDigest.getInstance("MD5");
			
	        // 입력 문자열을 바이트 배열로 변환하여 MD5 해시 생성
	        messageDigest = md.digest((custCode + openKeyDate).getBytes());
	        
	        // 16진수로 변환
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : messageDigest) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        
	        openKey = hexString.toString();
            requestMakeDate = String.format("%tY%tm%td%tH%tM%tS", calendar, calendar, calendar, calendar, calendar, calendar);
            keyBase = openKey + "^" + requestMakeDate;
            
            iv = new byte[16];
			random = new SecureRandom();
			random.nextBytes(iv);
			ivParameterSpec = new IvParameterSpec(iv);
			// 암호화 알고리즘 : AES 
			secretKeySpec = new SecretKeySpec(openKey.getBytes(), "AES");
			// 암호화 모드 : CBC / Padding 방식 : PKCS7
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			encrypted = cipher.doFinal(keyBase.getBytes("UTF-8"));
			combined = new byte[iv.length + encrypted.length];
			
			System.arraycopy(iv, 0, combined, 0, iv.length);
			System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
			
			// 암호문 인코딩 방식 : Base64 + UTF-8
			apiKey = Base64.getEncoder().encodeToString(combined);
			
			apiBaseInfo.put("apiKey", apiKey);
			
		} catch (Exception e) {
			
		}
	}
	
	/* Body 생성 (Object(HashMap 또는 ArrayList) 데이터를  String로  전환) */
	public String getBody(Object apiRequestObject) throws Exception {
		ObjectMapper objectMapper = null;
		Object paramObejct = null;
		String body = null;
		
		try {
			if (apiRequestObject instanceof ArrayList<?>) {
				paramObejct = new ArrayList<Map<String, Object>>();
				paramObejct = ((ArrayList<?>) apiRequestObject).get(0);
			} else if (apiRequestObject instanceof ParamMap) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = ((ParamMap) apiRequestObject).get();

			} else if (apiRequestObject instanceof HashMap) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = apiRequestObject;

			} else if (apiRequestObject instanceof Product) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = ((Product) apiRequestObject);

			} else if (apiRequestObject instanceof Brand) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = ((Brand) apiRequestObject);

			}  else if (apiRequestObject instanceof SBrand) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = ((SBrand) apiRequestObject);

			}  else if (apiRequestObject instanceof Addr) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = ((Addr) apiRequestObject);

			} else {
				log.error("apiRequestObject is not suitable.");
			}
			
			objectMapper = new ObjectMapper();
			body = objectMapper.writeValueAsString(paramObejct);
						
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return body;
	}
	
	/* 패션플러스 통신 호출 및 결과 수신 */
	public String connectPaFapleAPILegacy(ParamMap apiBaseInfo) throws Exception {
		HttpHeaders headers = null;
		HttpEntity<String> entity = null;
		HttpMethod method = null;
		HttpComponentsClientHttpRequestFactory factory = null;
		RestTemplate restTemplate = null;
		ResponseEntity<String> response = null;
		StopWatch watch = null;
		String url = null;
		String connectResult = null;
		
		try {
			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("ApiKey", apiBaseInfo.getString("apiKey"));
			
			if("E1".equals(apiBaseInfo.get("paCode"))){
				headers.set("CustCode", FAPLE_TV_CUST_CODE);
			}else {
				headers.set("CustCode",FAPLE_ONLINE_CUST_CODE);
			}
			entity = new HttpEntity<>(apiBaseInfo.getString("body"), headers);
			apiBaseInfo.put("reqHeader",entity.getHeaders().toString());
			
			url = FAPLE_GATEWAY + apiBaseInfo.getString("url");
			method = HttpMethod.valueOf(apiBaseInfo.getString("method"));
			
			factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout(CONN_TIMEOUT_120 * 1000); //타임아웃 설정 120초
			
			restTemplate = new RestTemplate(factory);
			
			watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			
			connectResult = response.getBody();
			
		} catch (final RecoverableDataAccessException rdae) {
			connectPaFapleAPILegacy(apiBaseInfo);
		} catch (final HttpClientErrorException hcee) {
			// 패션플러스의 경우 처리대상(조회결과)이 없는 경우에도 422(statusCode) 오류 발생 시킴
			// 그러므로 해당 catch에서 throw 하지 않고 호출 로직에서 직접 처리
			connectResult = hcee.getResponseBodyAsString();
		} catch (RestClientResponseException ex){
			connectResult = ex.getResponseBodyAsString();// finally에서 utf-8로 변환
			String stringResult = StringUtil.jsonToMap(connectResult).toString();
			log.info("API RESPONSE: {}", stringResult);
            stringResult = "FAPLE(API-RESPONSE) : " + stringResult;
            throw new TransApiException(stringResult, String.valueOf(ex.getRawStatusCode()));
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
			connectResult = getErrorMessage(e);
			throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			apiBaseInfo.put("result"		 , ComUtil.splitJson(connectResult).toString());
			apiBaseInfo.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
		}
		
		return connectResult;
	}

	/* 패션플러스 통신 호출 및 결과 수신 */
	public String connectPaFapleAPI(ParamMap apiBaseInfo, PaTransService serviceLog, ParamMap apiRequestObject) throws Exception {

		String url = FAPLE_GATEWAY + apiBaseInfo.getString("url");
		HttpMethod method = HttpMethod.valueOf(apiBaseInfo.getString("method"));
		String body = apiRequestObject.get("body").toString();
		String connectResult = null;
		
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.FAPLE.processId());	
		apiLog.setApiUrl(method.name()+ " " + url);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("ApiKey", apiBaseInfo.getString("apiKey"));
			
			if("E1".equals(apiBaseInfo.get("paCode"))){
				headers.set("CustCode", FAPLE_TV_CUST_CODE);
			}else {
				headers.set("CustCode",FAPLE_ONLINE_CUST_CODE);
			}
			
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			transLogService.logTransApiReqFaple(apiLog, entity);
			
			log.info("FAPLE API URL : [" + String.valueOf(method) +"] " + url );
			log.info("FAPLE header : " + entity.getHeaders());
			log.info("FAPLE body : " + entity.getBody());
			
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout(CONN_TIMEOUT_120 * 1000); //타임아웃 설정 120초
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
			StopWatch watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			
			apiLog.setResponsePayload(StringUtil.jsonToMap(response.getBody()).toString());
			apiLog.setResultCode(API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
			
			log.info("FAPLE API responseTime : " + String.format("%.6f", watch.getTotalTimeSeconds())); // API 호출 및 응답 수행시간
			
			connectResult = response.getBody();
			
		} catch (final RecoverableDataAccessException rdae) {
			connectPaFapleAPI(apiBaseInfo, serviceLog, apiRequestObject);
		} catch (final HttpClientErrorException hcee) {
			// 패션플러스의 경우 처리대상(조회결과)이 없는 경우에도 422(statusCode) 오류 발생 시킴
			// 그러므로 해당 catch에서 throw 하지 않고 호출 로직에서 직접 처리
			connectResult = hcee.getResponseBodyAsString();
			transLogService.logTransApiResErrorFaple(apiLog, hcee);
		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorFaple(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}		
		catch (Exception e) {
			log.error(serviceLog.getServiceNote() +" ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
		
		return connectResult; 
	}
	
	public void getApiInfo(ParamMap apiBaseInfo) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		apiBaseInfo.put("broadCode"	    , PaCode.FAPLE_TV.code());
		apiBaseInfo.put("onlineCode"	, PaCode.FAPLE_ONLINE.code());
		apiBaseInfo.put("siteGb"		, Constants.PA_FAPLE_PROC_ID);
		apiBaseInfo.put("startDate"		, systemService.getSysdatetimeToString());
		
		apiInfo = systemService.selectPaApiInfo(apiBaseInfo);
		
		apiBaseInfo.put("method"		, apiInfo.get("REMARK1"));
		apiBaseInfo.put("url"			, apiInfo.get("API_URL"));
		
		apiBaseInfo.put("code"			, "200");
		apiBaseInfo.put("message"		, "OK");
		apiBaseInfo.put("apiName"		,  apiInfo.get("API_NAME"));
	}
	
	public static String getErrorMessage(Exception e) {
		String errMsg = "";
		
		if(e.getMessage() != null) {
			errMsg = e.getMessage();
		}else {
			errMsg = e.toString();
		}
		errMsg = errMsg.length() > 3950 ? errMsg.substring(0, 3950) : errMsg;
		return errMsg;
	}
	
	public void saveRequestMap(ParamMap apiInfoMap) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= apiInfoMap.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_FAPLE_GROUP_CODE; 
			
			paRequestMap.setPaCode			(paCode);
			paRequestMap.setReqApiCode		(apiInfoMap.getString("apiCode"));
			paRequestMap.setReqUrl			("["+apiInfoMap.getString("method")+"]"+apiInfoMap.getString("url"));
			paRequestMap.setReqHeader		(apiInfoMap.getString("reqHeader")); 
			paRequestMap.setRequestMap		(apiInfoMap.getString("body"));
			paRequestMap.setResponseMap		(apiInfoMap.getString("result"));
			paRequestMap.setRemark			(apiInfoMap.getString("responseTime"));
			
			systemService.insertPaRequestMapTx(paRequestMap);
		}
		catch ( Exception e ) {
			//ignore
			log.error(e.toString());
		}
	}
	
	public String getErrorMessageUtf8(RestClientResponseException ex) throws UnsupportedEncodingException {
		String responseBodyAsString = "";
		
		// 4XX에러 OR GW 발생시 Content-type의 인코딩 셋이 null로 RestClientResponseException 발생
		// RestClientResponseException의 기본 인코딩셋 'ISO-8859-1'을 'UTF-8'로 변환 처리
		if(ex.getResponseHeaders().getContentType().getCharset() == null) {
			responseBodyAsString = URLDecoder.decode(ex.getResponseBodyAsString(), "ISO-8859-1");
			responseBodyAsString = new String(responseBodyAsString.getBytes("ISO-8859-1"), "UTF-8");
		} else {
			responseBodyAsString = ex.getResponseBodyAsString();
		}
		
		return responseBodyAsString;
	}
}
