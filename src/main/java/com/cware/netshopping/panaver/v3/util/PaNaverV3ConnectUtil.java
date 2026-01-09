package com.cware.netshopping.panaver.v3.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
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
import com.cware.netshopping.panaver.v3.domain.ImagesList;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil")
public class PaNaverV3ConnectUtil {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaNaverV3ApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired 
	MessageSource ms;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * 네이버 Common 통신 API
	 * 
	 * @param PaTransService
	 * @param String
	 * @param Map<String, String>
	 * @param Object
	 * @return Map<String,Object>
	 * @throws Exception
	 */	
	public Map<String,Object> getCommon(PaTransService serviceLog, String pathParameters, Map<String, String> queryParameters, Object apiDataObject) throws Exception {
						
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo(serviceLog.getServiceName());
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
				
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);		
        uri = apiRequest.NAVER_GATEWAY + uri + PaNaverV3ApiRequest.makeQuryString(queryParameters);
				
		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);
		
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.CMNAVER.processId());	
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {			
			// 토큰생성
			String authorization = apiRequest.generateToken();
			
			// API명 추출(헤더 공통 전송용)
			String apiName = apiLog.getApiName();
			
			// API요청 공통 헤더 설정
			HttpHeaders headers 	  = apiRequest.createHeader(method, authorization, apiName);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);
						
			transLogService.logTransApiReqNaver(apiLog, entity);

			// API 통신
			HttpEntity<String> response = restTemplate.exchange(uri ,method, entity, String.class);
			
			apiLog.setResponsePayload(response.getBody());
			apiLog.setResultCode(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
							
			return ComUtil.splitJson(response.getBody()); 
			
		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorNaverV3(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error(serviceLog.getServiceNote() +" ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}	
	
	
	/**
	 * 네이버 이미지 통신 API
	 * 
	 * @param PaTransService
	 * @param String
	 * @param Map<String, String>
	 * @param List<File>
	 * @return ImagesList
	 * @throws Exception
	 */	
		
	public ImagesList getImg(PaTransService serviceLog, String pathParameters, Map<String, String> queryParameters, List<File> fileList) throws Exception {
		
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo("IF_PANAVERAPI_V3_01_005");
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
		
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);		
		uri = apiRequest.NAVER_GATEWAY + uri + PaNaverV3ApiRequest.makeQuryString(queryParameters);
		
		// 마운트된 경로로 File 객체에 담아서 Multipart 형식으로 보낸다.
		MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
		FileSystemResource imageResource  = null;
		for(File file : fileList) {
			imageResource = new FileSystemResource(file);    
			
			multipartData.add("imageFiles", imageResource);
		}
		
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName("IF_PANAVERAPI_V3_01_005");
		apiLog.setApiNote("[V3]네이버 상품-상품 이미지 다건 등록");
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.CMNAVER.processId());	
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {			
			// 토큰생성
			String authorization = apiRequest.generateToken();
			
			// API명 추출(헤더 공통 전송용)
			String apiName = apiLog.getApiName();
			
			// API요청 공통 헤더 설정
			HttpHeaders headers 	  = apiRequest.createHeader(method, authorization, apiName);			
			HttpEntity<MultiValueMap<String, Object>> requestMultiEntity = new HttpEntity<>(multipartData ,headers);
			
			transLogService.logTransApiReqNaver(apiLog, requestMultiEntity);
			
			// API 통신
			HttpEntity<String> response = restTemplate.exchange(uri ,method, requestMultiEntity, String.class);
			
			apiLog.setResponsePayload(response.getBody());
			apiLog.setResultCode(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			ImagesList imageList = objectMapper.convertValue(ComUtil.splitJson(response.getBody()), ImagesList.class);
			
			return imageList;
			
		} catch (RestClientResponseException ex) {
			log.error("[V3]네이버 상품-상품 이미지 다건 등록 ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorNaverV3(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("[V3]네이버 상품-상품 이미지 다건 등록 ={} {}", serviceLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("[V3]네이버 상품-상품 이미지 다건 등록 ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}	
	
	/**
	 * 네이버 Common 통신 API (레거시)
	 * 
	 * @param String
	 * @param String
	 * @param Map<String, String>
	 * @param Object
	 * @return Map<String,Object>
	 * @throws Exception
	 */	
	public Map<String,Object> getCommonLegacy(String apiCode, String pathParameters, Map<String, String> queryParameters, Object apiDataObject) throws Exception {
						
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo(apiCode);
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
				
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);
        apiInfoMap.put("url", uri + PaNaverV3ApiRequest.makeQuryString(queryParameters));
        uri = apiRequest.NAVER_GATEWAY + uri + PaNaverV3ApiRequest.makeQuryString(queryParameters);
				
		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);
		apiInfoMap.put("body", body);
		
		StopWatch watch = new StopWatch();
		HttpEntity<String> response = null;
		String stringResult = "";
		Map<String, Object> errMap = null;
		
		try {			
			// 토큰생성
			String authorization = apiRequest.generateToken();
			
			// API요청 공통 헤더 설정
			HttpHeaders headers  = apiRequest.createHeader(method, authorization, apiCode);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);
			apiInfoMap.put("reqHeader", entity.getHeaders().toString());

			// API 통신
			watch.start();
			response = restTemplate.exchange(uri ,method, entity, String.class);
			watch.stop();
			
			stringResult = response.getBody();
			
			return ComUtil.splitJson(stringResult);
			
		} catch (RestClientResponseException ex){
			stringResult = getErrorMessageUtf8(ex);
			errMap = StringUtil.jsonToMap(stringResult);
			log.info("API RESPONSE: {}", errMap.get("message").toString());
			throw new TransApiException(errMap.get("message").toString(), errMap.get("code").toString());
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
            throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			apiInfoMap.put("result"		 , stringResult);
			apiInfoMap.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(apiInfoMap); // TPAREQUESTMAP_LOG 저장
		}
	}
	
	public void saveRequestMap(ParamMap apiInfoMap) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= apiInfoMap.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_NAVER_CODE; 
			
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
	
	/** paramMap 필수 값 : apiCode startDate siteGb    
	    선택 값 : message, code  **/
	public void dealSuccess(ParamMap paramMap, HttpServletRequest request) throws Exception {
		String rtnCode = paramMap.getString("code");
		
		if(!rtnCode.equals("200") &&  rtnCode.equals("")){
			paramMap.put("code","304");
			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
		}
		
		systemService.insertPaApiTrackingTx(request,paramMap);	
	}
	
	public void dealException(Exception se, ParamMap paramMap){
		String message = null;
		String duplicatrionMessage = ms.getMessage("msg.batch_process_duplicated",new String[] { "" }, null);
		String connectionMessage = ms.getMessage("msg.jwt_connect_error", new String[] { "" }, null);
		String jsonSplitMessage    = ms.getMessage("msg.error_jsonsplit",new String[] { "" }, null);
		
		if(se.getMessage() ==null){
			message = se.toString();
		}else{
			message = se.getMessage();
		}
		
		//= 중복체크
		if(message.contains(duplicatrionMessage)){
			paramMap.put("code","490");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
			
		//= Json 가공 에러
		if(message.contains(jsonSplitMessage)){
			paramMap.put("code","480");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
		
		//= JWT 통신 에러
		if(message.contains(connectionMessage)){
			paramMap.put("code","400");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}

		//= 기타 프로세스 에러
		if(paramMap.getString("code").equals("")){
			paramMap.put("code","500");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
	
		//= 추적이 불가능한 에러
		if(message == null ||message.equals("")){
			paramMap.put("code","304");
			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
		}
		
	}
	
}
