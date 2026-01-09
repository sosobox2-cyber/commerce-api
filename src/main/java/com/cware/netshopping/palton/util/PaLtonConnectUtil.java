package com.cware.netshopping.palton.util;

import java.util.Map;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException; 
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale; 
import java.util.TimeZone;
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
import org.springframework.web.client.RestTemplate;

@Repository("com.cware.netshopping.palton.util.PaLtonConnectUtil")
public class PaLtonConnectUtil extends AbstractService{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	private static final int    CONN_TIMEOUT = 100; 
	
	
	public void getApiInfo(String apiCode, ParamMap apiInfoMap) throws Exception {
		HashMap<String, String> apiInfo		= new HashMap<String, String>();
		
		apiInfoMap.put("apiCode"		, apiCode);
		apiInfoMap.put("broadCode"		, Constants.PA_LTON_BROAD_CODE);
		apiInfoMap.put("onlineCode"		, Constants.PA_LTON_ONLINE_CODE);
		apiInfoMap.put("siteGb"			, Constants.PA_LTON_PROC_ID);
		apiInfoMap.put("startDate"		, systemService.getSysdatetimeToString());
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		apiInfoMap.put("method"			, apiInfo.get("REMARK1"));
		apiInfoMap.put("url"			, apiInfo.get("API_URL"));
		apiInfoMap.put("paBroad"		, apiInfo.get(Constants.PA_BROAD));
		apiInfoMap.put("paOnline"		, apiInfo.get(Constants.PA_ONLINE));
		apiInfoMap.put("code"			, "");
		apiInfoMap.put("message"		, "");
	}
	
	public void checkDuplication(String prg_id, ParamMap apiInfoMap) throws Exception {
		String before3Hour = DateUtil.addHour(DateUtil.getCurrentDateTimeAsString(), -3, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		String duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		apiInfoMap.put("duplicateCheck", duplicateCheck);
		
		if ("1".equals(duplicateCheck))	{
			HashMap<String, String> closeHistoryMap = systemService.selectCloseHistory(prg_id);
			if(DateUtil.compareTo(before3Hour, closeHistoryMap.get("MODIFY_DATE")) > 0 ) {
				apiInfoMap.put("duplicateCheck", "0");
				return;
			}
			throw processException("msg.batch_process_duplicated", new String[] {prg_id});
		}
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
	
	public void checkException(ParamMap apiInfoMap , Exception e) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		
		String resultCode = "1".equals(duplicateCheck) ? "490" : "500";
		String resultMsg  = "1".equals(duplicateCheck) ? getMessage("errors.duplicate") : PaLtonComUtill.getErrorMessage(e);
		log.error(apiInfoMap.getString("message") , e);
		
		apiInfoMap.put("code"		, resultCode);
		apiInfoMap.put("message"	, resultMsg);
	}
	
	/**
	 * 롯데온 통신 모듈
	 * @param ParamMap/  apiInfoParam = 통신에 관련된 정보, apiDataObject =Body에 필요한 정보
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String,Object> apiGetObjectByLtn(ParamMap apiInfoParam, Object apiDataObject) throws Exception {
		String body					 = "";
		String result 				 = "";
		//=1) Object(paramMap 또는 ArrayList)로 만들어진 데이터로 RequestBody를 생성
		//JSON 결과를 List로 만들고 싶으면 paramObject를 list로 Map으로 만들고 싶으면 ParmaMap으로 만들어서 보내면 됩니다.
		body = getBody(apiDataObject);
		apiInfoParam.put("body", body);
		
		//=2) 실제 통신을 통해 결과를 받아온다.
		result = connectLtonReal(apiInfoParam);  //result = connectLtonFake(paramMap);
		
		//=3) JSON을 MAP으로 만들어준다 
		Map<String,Object> returnMap = ComUtil.splitJson(result); 
		returnMap.put("paCode", apiInfoParam.getString("paCode"));
		return returnMap;
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
	
	/**
	 * 롯데온 통신 모듈 
	 * @param apiCode, method, url, secKey 
	 * 		  paCode ,body 필수
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private String connectLtonReal(ParamMap paramMap) throws Exception {
		
		String openAPIUrl 	 = ConfigUtil.getString("PALTON_BASE_URL");
		String stringResult  = "";
		String secKey 	  	 = ConfigUtil.getString("PALTON_API_KEY");
		String url	  	  	 = openAPIUrl + paramMap.getString("url");
		HttpMethod method 	 = "GET".equals(paramMap.getString("method").toUpperCase()) ? HttpMethod.GET : HttpMethod.POST;  //GET, POST만 지원
		String body       	 = paramMap.getString("body");
		StopWatch watch 	 = new StopWatch();
		
		// = https://onpick-api.lotteon.com 도메인을 사용하는 API 예외처리
		if(paramMap.getString("url").indexOf("onpick") > -1) {
			url = paramMap.getString("url");
		}
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout	(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout		(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			RestTemplate restTemplate = new RestTemplate(factory);		
			HttpHeaders headers 	  = new HttpHeaders();
			
			headers.set ("Authorization"	, "Bearer " + secKey);
			headers.set ("Accept-Language"	, Locale.KOREA.toString()); 
			headers.set ("X-Timezone"		, TimeZone.getDefault().getID()); //TimeZone.getTimeZone("JST")
			headers.set ("Cache-Control"	, "no-cache"); 
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
				
			watch.start();
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity , String.class);
			watch.stop();
			
			log.info("url 	 :: [" + method.toString() +"]" + url );
//			log.info("header :: " + headers.toString() );
//			log.info("body	 :: " + body );
			
			stringResult = response.getBody();
			//response.getStatusCode();
			//response.getStatusCodeValue();
			
		}catch (final HttpClientErrorException ee){
			log.error(ee.toString());
			throw processException("pa.lton_connect_error" , new String[] { PaLtonComUtill.getErrorMessage(ee)} );
		}catch (Exception e) {
			log.error(e.toString());
			throw processException("pa.lton_connect_error" , new String[] { PaLtonComUtill.getErrorMessage(e) });
		}finally { 		
			paramMap.put("result"		, stringResult);
			paramMap.put("body"			, body);
			paramMap.put("responseTime"	, String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(paramMap);
		}
		
		return stringResult;
	}
	
	private void saveRequestMap(ParamMap param) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= param.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_LTON_BROAD_CODE; 
			
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
	
	
	/*
	private String connectLtonFake(ParamMap paramMap) throws Exception { //TODO 추후에 삭제 (개발과정에서 만든 함수)
		
		String body   = paramMap.getString("body");
		System.out.println(body.toString());
		PaLtonTestCode result = new PaLtonTestCode();
		//String a =  result.test2();
		return null;
	}
	*/
			
}
