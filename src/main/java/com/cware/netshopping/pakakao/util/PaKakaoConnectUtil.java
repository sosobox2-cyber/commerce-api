package com.cware.netshopping.pakakao.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("com.cware.netshopping.pakakao.util.PaKakaoConnectUtil")
public class PaKakaoConnectUtil extends AbstractService{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	private static final int    CONN_TIMEOUT = 100; 
	
	public void checkDuplication(String prg_id, ParamMap apiInfoMap) throws Exception {
		String duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		apiInfoMap.put("duplicateCheck", duplicateCheck);
		if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
	}
	
	public void checkException(ParamMap apiInfoMap , Exception e) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		
		String resultCode = "1".equals(duplicateCheck) ? "490" : "500";
		String resultMsg  = "1".equals(duplicateCheck) ? getMessage("errors.duplicate") : PaKakaoComUtill.getErrorMessage(e);
		log.error(apiInfoMap.getString("message") , e);
		
		apiInfoMap.put("code"		, resultCode);
		apiInfoMap.put("message"	, resultMsg);
	}
	
	public void closeApi(HttpServletRequest request, ParamMap apiInfoMap) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		String prg_id		  = apiInfoMap.getString("apiCode");
		
		if("".equals(apiInfoMap.getString("code")))		  apiInfoMap.put("code"		, "200"); //성공
		if("".equals(apiInfoMap.getString("message"))) 	  apiInfoMap.put("message"	, "OK");
		
		try {
			systemService.insertApiTrackingTx(request, apiInfoMap);
			
			if("0".equals(duplicateCheck)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}catch (Exception e) {
			log.error("ERROR - ApiTracking_CloseHistory" + e.toString());
		}
	}
	
	public void getApiInfo(String apiCode, ParamMap apiInfoMap) throws Exception {
		HashMap<String, String> apiInfo		= new HashMap<String, String>();
		
		apiInfoMap.put("apiCode"		, apiCode);
		apiInfoMap.put("broadCode"		, Constants.PA_KAKAO_BROAD_CODE);
		apiInfoMap.put("onlineCode"		, Constants.PA_KAKAO_ONLINE_CODE);
		apiInfoMap.put("siteGb"			, Constants.PA_KAKAO_PROC_ID);
		apiInfoMap.put("startDate"		, systemService.getSysdatetimeToString());
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		apiInfoMap.put("method"			, apiInfo.get("REMARK1"));
		apiInfoMap.put("url"			, apiInfo.get("API_URL"));
		apiInfoMap.put("paBroad"		, apiInfo.get(Constants.PA_BROAD));
		apiInfoMap.put("paOnline"		, apiInfo.get(Constants.PA_ONLINE));
		apiInfoMap.put("code"			, "200");
		apiInfoMap.put("message"		, "OK");
	}
	
	/**
	 * 카카오 통신 모듈
	 * @param ParamMap/  apiInfoParam = 통신에 관련된 정보, apiDataObject =Body에 필요한 정보
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String,Object> apiGetObjectByKakao(ParamMap apiInfoParam, Object apiDataObject) throws Exception {
		String body					 = "";
		Map<String,Object> returnMap = null;
		Map<String,Object> resultMap = null;
		//=1) Object(paramMap 또는 ArrayList)로 만들어진 데이터로 RequestBody를 생성
		//JSON 결과를 List로 만들고 싶으면 paramObject를 list로 Map으로 만들고 싶으면 ParmaMap으로 만들어서 보내면 됩니다.
		body = getBody(apiDataObject);
		apiInfoParam.put("body", body);
		
		//=2) 실제 통신을 통해 결과를 받아온다.
		resultMap = connectKakaoReal(apiInfoParam);
		
		//=3) JSON을 MAP으로 만들어준다 
		if(null == resultMap.get("body")) {
			returnMap = new HashMap<String,Object>();
			returnMap.put("body", null);
		} else {
			returnMap = ComUtil.splitJson(ComUtil.objToStr(resultMap.get("body")));
		}
		returnMap.put("paCode", apiInfoParam.getString("paCode"));
		returnMap.put("statusCode",resultMap.get("statusCode"));
		
		return returnMap;
	}
	
	/**
	 * 카카오 통신 모듈 
	 * @param apiCode, method, url, secKey 
	 * 		  paCode ,body 필수
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Map<String,Object> connectKakaoReal(ParamMap paramMap) throws Exception {
		
		String openAPIUrl 	 = ConfigUtil.getString("PAKAKAO_COM_BASE_URL");
		String apiKey_admin  = ConfigUtil.getString("PAKAKAO_API_KEY");
		String apiKey_seller = "";
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		if(Constants.PA_KAKAO_BROAD_CODE.equals(paramMap.get("paCode").toString())) {
			apiKey_seller = paramMap.get("paBroad").toString();
		} else {
			apiKey_seller = paramMap.get("paOnline").toString();
		}
		
		String body			= paramMap.get("body").toString();
		StopWatch watch		= new StopWatch();
		HttpMethod method	= getMethod(paramMap.get("method").toString());
		String url			= openAPIUrl + paramMap.get("url").toString();
		
		HttpEntity entity = null;
		ResponseEntity<String> response = null;
		
		//body 안 주는 API 배열
		String[] prgIdArr = new String[] {"IF_PAKAKAOAPI_03_002", "IF_PAKAKAOAPI_03_004", "IF_PAKAKAOAPI_03_005", "IF_PAKAKAOAPI_04_001", "IF_PAKAKAOAPI_04_002", "IF_PAKAKAOAPI_04_003", "IF_PAKAKAOAPI_04_004", "IF_PAKAKAOAPI_04_005", "IF_PAKAKAOAPI_04_006"
										, "IF_PAKAKAOAPI_04_007", "IF_PAKAKAOAPI_04_008", "IF_PAKAKAOAPI_04_009", "IF_PAKAKAOAPI_04_010", "IF_PAKAKAOAPI_04_011", "IF_PAKAKAOAPI_04_012", "IF_PAKAKAOAPI_04_013", "IF_PAKAKAOAPI_04_014"};
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout	(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout		(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			RestTemplate restTemplate = new RestTemplate(factory);		
			HttpHeaders headers 	  = new HttpHeaders();
			
			headers.set ("Authorization"	, "KakaoAK "+apiKey_admin);
			headers.set ("Target-Authorization"	, "KakaoAK "+apiKey_seller);			
			headers.set ("channel-ids"	, "101"); //톡스토어 : 101, 선물하기 : 1, 톡스토어, 선물하기 동시 호출 : 1,101
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			entity = new HttpEntity<>(body, headers);

			watch.start();
			response = restTemplate.exchange(url, method, entity , String.class);
			watch.stop();
			
			if(response.getBody()==null && Arrays.asList(prgIdArr).contains(paramMap.get("apiCode").toString())) {
				resultMap.put("body", "{ \"code\" : \"" + String.valueOf(response.getStatusCodeValue()) + "\"}");
				resultMap.put("statusCode",response.getStatusCode());
			} else {
				resultMap.put("body", response.getBody());
				resultMap.put("statusCode",response.getStatusCode());
			}
			
		}catch (final HttpServerErrorException e1){ 
			log.error(e1.getResponseBodyAsString());
			if("".equals(e1.getResponseBodyAsString())) {
				throw processException("pa.connect_error" , new String[] { PaKakaoComUtill.getErrorMessage(e1) });
			}else {
				resultMap.put("statusCode", e1.getStatusCode());
				resultMap.put("body", e1.getResponseBodyAsString());
			}
		}catch (final HttpClientErrorException e2) {
			log.error(e2.getResponseBodyAsString());
			if("".equals(e2.getResponseBodyAsString())) {
				throw processException("pa.connect_error" , new String[] { PaKakaoComUtill.getErrorMessage(e2) });
			}else {
				resultMap.put("statusCode", e2.getStatusCode());
				resultMap.put("body", e2.getResponseBodyAsString());
			}
		} catch (Exception e) {
			log.error(e.toString());
			throw processException("pa.connect_error" , new String[] { PaKakaoComUtill.getErrorMessage(e) });
		}finally { 		
			paramMap.put("result"		, resultMap.get("body"));
			paramMap.put("body"			, body);
			paramMap.put("responseTime"	, String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(paramMap); 
		}
		
		return resultMap;
	}
	
	private HttpMethod getMethod(String method){
		switch (method) {
		case "POST":
			return HttpMethod.POST;
		case "GET":
			return HttpMethod.GET;
		case "PUT":
			return HttpMethod.PUT;
		case "DELETE":
			return HttpMethod.DELETE;
		case "GP":  
		default:
			return HttpMethod.GET;
		}
	}
	
	private void saveRequestMap(ParamMap param) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= param.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_KAKAO_BROAD_CODE; 
			
			paRequestMap.setPaCode			(paCode);
			paRequestMap.setReqApiCode		(param.getString("apiCode"));
			paRequestMap.setReqUrl			("["+param.getString("method")+"]"+param.getString("url"));
			paRequestMap.setReqHeader		(""); 
			paRequestMap.setRequestMap		(param.getString("body"));
			paRequestMap.setResponseMap		(param.getString("result"));
			paRequestMap.setRemark			(param.getString("responseTime"));
			
			systemService.insertPaRequestMapTx(paRequestMap);
		}
		catch ( Exception e ) {
			//ignore
			log.error(e.toString());
		}
	}
	
	private String getBody(Object paramObject) throws Exception {
		ObjectMapper objectMapper   	= new ObjectMapper();
		String 	     result				= "";
		Object		 requestObejct		= null;
		
		if(paramObject == null) return "";
		
		if(paramObject instanceof ArrayList<?> ) {
			requestObejct = new ArrayList<Map<String , Object>>();
			requestObejct = paramObject;
		}else if(paramObject instanceof ParamMap){
			requestObejct = new  HashMap<String , Object>();
			requestObejct = ((ParamMap)paramObject).get();
		}else {
			return "";
		}
		
		try {
			result = objectMapper.writeValueAsString(requestObejct);
		} catch (JsonGenerationException e) {
			result = "";
			e.printStackTrace();
		} catch (JsonMappingException e) {
			result = "";
			e.printStackTrace();
		} catch (IOException e) {
			result = "";
			e.printStackTrace();
		}
			
		return result;
	}
	
	@SuppressWarnings({"unchecked" })
	public ParamMap parseKakaoErrorResponse (Map<String, Object> responseMap) {
		ParamMap returnMap = new ParamMap();
		
		returnMap.put("errorCode", "500");
		returnMap.put("errorMsg", "response parsing error");
		
		try {
			if(responseMap.containsKey("msg")) {
				returnMap.put("errorCode", responseMap.get("code").toString());
				returnMap.put("errorMsg", responseMap.get("msg").toString());
			}			
			
			if(responseMap.containsKey("extras")) {
				Map<String,Object> extras = (Map<String, Object>) responseMap.get("extras");
				returnMap.put("errorCode", extras.get("error_code").toString());
				returnMap.put("errorMsg", extras.get("error_message").toString());
				
				if(extras.containsKey("validation")) {
					returnMap.put("errorMsg", extras.get("validation").toString());
				}
			}
			
			if(responseMap.containsKey("errorMessage")) {
				returnMap.put("errorMsg", responseMap.get("errorMessage").toString());
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return returnMap;
	}

}
