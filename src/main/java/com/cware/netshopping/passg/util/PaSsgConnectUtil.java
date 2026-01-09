package com.cware.netshopping.passg.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

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

@Repository("com.cware.netshopping.passg.util.PaSsgConnectUtil")
public class PaSsgConnectUtil extends AbstractService{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	private static final int    CONN_TIMEOUT = 3; 
	private static final int    READ_TIMEOUT = 200; 
	
	public void checkDuplication(String prg_id, ParamMap apiInfoMap) throws Exception {
		String duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		apiInfoMap.put("duplicateCheck", duplicateCheck);
		if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
	}
	
	public void checkException(ParamMap apiInfoMap , Exception e) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		
		String resultCode = "1".equals(duplicateCheck) ? "490" : "500";
		String resultMsg  = "1".equals(duplicateCheck) ? getMessage("errors.duplicate") : PaSsgComUtill.getErrorMessage(e);
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
		apiInfoMap.put("broadCode"		, Constants.PA_SSG_BROAD_CODE);
		apiInfoMap.put("onlineCode"		, Constants.PA_SSG_ONLINE_CODE);
		apiInfoMap.put("siteGb"			, Constants.PA_SSG_PROC_ID);
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
	 * 신세계 통신 모듈
	 * @param ParamMap/  apiInfoParam = 통신에 관련된 정보, apiDataObject =Body에 필요한 정보
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String,Object> apiGetObjectBySsg(ParamMap apiInfoParam, Object apiDataObject) throws Exception {
		String result 				 = "";
		String body					 = "";
		Map<String,Object> returnMap = null;
		//=1) Object(paramMap 또는 ArrayList)로 만들어진 데이터로 RequestBody를 생성
		//JSON 결과를 List로 만들고 싶으면 paramObject를 list로 Map으로 만들고 싶으면 ParmaMap으로 만들어서 보내면 됩니다.
		body = getBody(apiDataObject);
		apiInfoParam.put("body", body);
		
		//=2) 실제 통신을 통해 결과를 받아온다.
		result = connectSsgReal(apiInfoParam);
		
		//=3) JSON을 MAP으로 만들어준다 
		returnMap = ComUtil.splitJson(result);
		returnMap.put("paCode", apiInfoParam.getString("paCode"));
		
		return returnMap;
	}
	
	/**
	 * 신세계 통신 모듈 
	 * @param apiCode, method, url, secKey 
	 * 		  paCode ,body 필수
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private String connectSsgReal(ParamMap paramMap) throws Exception {
		
		String openAPIUrl 	 = ConfigUtil.getString("PASSG_COM_BASE_URL");
		String apiKey 	  	 = "";
		if(Constants.PA_SSG_BROAD_CODE.equals(paramMap.get("paCode").toString())) {
			apiKey = paramMap.get("paBroad").toString();
		} else {
			apiKey = paramMap.get("paOnline").toString();
		}
		
		String body			= paramMap.get("body").toString();
		StopWatch watch		= new StopWatch();
		HttpMethod method	= getMethod(paramMap.get("method").toString());
		String url			= openAPIUrl + paramMap.get("url").toString();
		
		String stringResult	= "";
		HttpEntity entity = null;
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout	(CONN_TIMEOUT * 1000); //커넥트 타임아웃 설정 3초
			factory.setReadTimeout		(READ_TIMEOUT * 1000); //타임아웃 설정 200초
			RestTemplate restTemplate = new RestTemplate(factory);		
			HttpHeaders headers 	  = new HttpHeaders();
			
			headers.set ("Authorization"	, apiKey);
			headers.set ("Accept"			, "application/json");
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			entity = new HttpEntity<>(body, headers);

			watch.start();
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity , String.class);
			watch.stop();
			
			stringResult = response.getBody();
			
		}catch (final HttpServerErrorException e1){
			log.error(e1.getResponseBodyAsString());
			if("".equals(e1.getResponseBodyAsString())) {
				throw processException("pa.connect_error" , new String[] { PaSsgComUtill.getErrorMessage(e1) });
			}else {
				stringResult = e1.getResponseBodyAsString();
			}
		}catch (Exception e) {
			log.error(e.toString());
			throw processException("pa.connect_error" , new String[] { PaSsgComUtill.getErrorMessage(e) });
		}finally { 		
			paramMap.put("result"		, stringResult);
			paramMap.put("body"			, body);
			paramMap.put("responseTime"	, String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(paramMap); 
		}
		
		return stringResult;
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
			if("".equals(paCode) ) paCode = Constants.PA_SSG_BROAD_CODE; 
			
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
	
	public ParamMap getRetrieveDate(String fromDate, String toDate) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		SimpleDateFormat counsel_format = new SimpleDateFormat("yyyy-MM-dd");
		
		if(ComUtil.NVL(toDate).length() == 8 && ComUtil.NVL(fromDate).length() == 8) { // 쪽지
			fromDate = fromDate.substring(0,4) + "-" + fromDate.substring(4,6) + "-" + fromDate.substring(6,8);
			toDate   = toDate.substring(0,4) + "-" + toDate.substring(4,6) +  "-" + toDate.substring(6,8);
			
			paramMap.put("FROM_DATE", fromDate);
			paramMap.put("TO_DATE"	, toDate);
		} else {
			paramMap.put("FROM_DATE", counsel_format.format(DateUtil.addDay(systemService.getSysdate(), -1)));
			paramMap.put("TO_DATE"	, counsel_format.format(systemService.getSysdate()));
		}
		return paramMap;
	}
}
