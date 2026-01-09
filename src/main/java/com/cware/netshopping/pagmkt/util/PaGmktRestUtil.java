package com.cware.netshopping.pagmkt.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;


@Repository("com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
public class PaGmktRestUtil  extends AbstractService {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	
	/**
	 * 함수명 : getConnection
	 * @param  PaGmktAbstractRest, ParamMap  
	 * @return String
	 * @throws Exception
	 * Param  Info  PaGmktAbstractRest= requst 생성 객체 , ParamMap("apiCode", "urlParameter", 각 requst 생성시 필요한 값)
	 * Return Info	통신 결과로 받은 Response 
	 */
	public String getConnection(PaGmktAbstractRest rest, ParamMap param) throws Exception {
		String url 				= "";
		String siteId 			= null;
		ParamMap urlResultParam = new ParamMap();
		String body 			= null;
		String result			= null;
		HttpMethod method 		= null;
		String urlParameter 	= param.getString("urlParameter");
		String realServerYn 	= systemService.getConfig("PA_REAL_SERVER_YN").getVal();
		reSettingApiCode(param);
		
		//= URL, Connect Method, Parameter Setting
		urlResultParam  = getApiUrl(param.getString("apiCode"));  //api_code
		method = getMethod(urlResultParam.getString("METHOD"));	
		url = urlResultParam.getString("URL");
     	url = url.replace("{urlParameter}", urlParameter);
     
     	
		//= Setting Requset Body
		if (rest != null) {
			body = rest.getRequstBody(param);
		}
		
		//= Setting SiteId
		siteId = settingSellerId(param, realServerYn);

		param.put("url"		, url);
     	param.put("method"	, method);
		param.put("body"	, body);
		param.put("siteId"	, siteId);
		//= Check developeServer Or realServer
		//checkRealServerYnForApiCall(param, realServerYn, siteGb);
     	//= Connect and get responseData
		result = connectServer(param); 
	
		return result; 
	}
	
	/**
	 * 함수명 : connectServer
	 * @param  String, String, HttpMethod,ParamMap   
	 * @return String
	 * @throws Exception
	 * Param  Info 	ParamMap
	 * Return Info	ESM 통신에 사용할 실질적인 API Code 사용
	 */
	private void reSettingApiCode(ParamMap paramMap){
		// G마켓의 경우 IF_PAGMKTAPI_V2_03%
		// 옥션의       IF_PAIACAPI_V2_00
		// CloseHistory나 APITRACKING에서 해당 방식으로 사용 하지만 ESM 통신과는 하나의 API 명세(IF_PAGMKTAPI_V2_00)를 사용하기위해 REPLACE 해준다.
		String apiCode = paramMap.getString("apiCode");
		paramMap.put("orgApiCode"	, apiCode);
		apiCode = apiCode.replace("IAC", "GMKT");
		paramMap.put("apiCode"		, apiCode);
	}
	
	/**
	 * 함수명 : connectServer
	 * @param  String, String, HttpMethod,ParamMap   
	 * @return String
	 * @throws Exception
	 * Param  Info 	ParamMap
	 * Return Info	통신 결과로 받은 Response 
	 */
	public String connectServer(ParamMap param) throws Exception  {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		
		if("IF_PAGMKTAPI_V2_01_014_O".equals(param.getString("apiCode"))
				|| "IF_PAGMKTAPI_V2_01_014_B".equals(param.getString("apiCode"))){
			factory.setConnectTimeout(Integer.parseInt(systemService.getConfig("PAGMKTV2_CONNECT_TIMEOUT").getVal()) * 1000);
			factory.setReadTimeout(Integer.parseInt(systemService.getConfig("PAGMKTV2_READ_TIMEOUT").getVal()) * 1000);
		}
		RestTemplate restTemplate = new RestTemplate(factory);
		
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);
		converters.add(stringConverter);
		restTemplate.setMessageConverters(converters);
		double timeS = 0;
		double timeE = 0;
		String responseTime = null;
		Algorithm algorithm = null;
		String secretKey;
		secretKey = "4IeKRn5/O0Su4MPnlopgFA==";
		algorithm = Algorithm.HMAC256(secretKey); //에러시 함수의 throw를 탐.

		String result = null;	
		String siteId = param.getString("siteId");
		String body	  = param.getString("body");
		String url 	  = param.getString("url");
		HttpMethod method = (HttpMethod) param.getObject("method");
		
		HashMap<String, Object> header = new HashMap<>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		header.put("kid", "skstoa");

		String token = JWT.create()
				.withHeader(header)
				.withIssuer("www.skstoa.com")
				.withIssuedAt(new java.util.Date()).withSubject("sell") //sell만 사용, ad는 광고 계정
				.withAudience("sa.esmplus.com")
				.withClaim("ssi", siteId) //user 띄어쓰기 금지, 2018.10.23 기준 g마켓만 개발
				.sign(algorithm);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		//log.info("=====request======================");
		//log.info("["+method+"]"+url);
		//log.info(headers+"");
		//log.info(body+"");
		
		ResponseEntity<String> response = null;
		try{
			log.info("=====response=====================");
				
			timeS = System.nanoTime(); // G마켓 호출 API 호출 시작 시간
			response = restTemplate.exchange(url, method, entity, String.class);
			timeE = System.nanoTime(); // G마켓 호출 API 호출 종료 시간
			result = checkResponseResult(param.getString("orgApiCode"), response);
		}
		catch (final HttpClientErrorException ee){
			timeE = System.nanoTime(); // G마켓 호출 API 호출 종료 시간
			log.info("API RESPONSE: {}", response);
			result = param.getString("orgApiCode") + " " + ee.getStatusCode() + " : " + ee.getResponseBodyAsString();
			checkErrorResponseResult(param.getString("orgApiCode") , ee);
			//throw processException("msg.jwt_connect_error" , new String[] { result });
		}catch(HttpStatusCodeException e) {
			log.error("API HTTP ERROR: {} ", e.getResponseBodyAsString());
			result = param.getString("orgApiCode")+ e.getResponseBodyAsString();
			throw processException("msg.jwt_connect_error" , new String[] { result });
		}catch(Exception e){
			timeE = System.nanoTime(); // G마켓 호출 API 호출 종료 시간
			log.error("API ERROR: {}", e.getMessage(), e);
			result = param.getString("orgApiCode") + e.getMessage();
			throw processException("msg.jwt_connect_error" , new String[] { result });
		}finally{
			responseTime = Double.toString((timeE-timeS)/1000000000); //ex) 11초면  11.863670103
			log.info("== Api Response Time == :: " + responseTime);
			param.put("responseTime", responseTime);
			//= 전문 저장
			insertPaRequestMap(param,headers,body,result);
			param.put("apiCode", param.get("orgApiCode"));
		}
		
		return result;
	}
	
	private String insertPaRequestMap(ParamMap param,HttpHeaders headers,String body, String result) throws Exception{
		PaRequestMap paRequestMap = new PaRequestMap();
		paRequestMap.setPaCode(param.getString("paCode"));
		//paRequestMap.setReqApiCode(param.getString("orgApiCode"));
		paRequestMap.setReqApiCode(param.getString("apiCode"));
		paRequestMap.setReqUrl("["+param.getString("method")+"]"+param.getString("url"));
		paRequestMap.setReqHeader(headers+"");
		paRequestMap.setRequestMap(body);
		paRequestMap.setResponseMap(result);
		paRequestMap.setRemark(param.getString("responseTime"));
		systemService.insertPaRequestMapTx(paRequestMap);
		return result;
	}
	
	private String checkResponseResult(String apiCode , ResponseEntity<String> result) throws Exception{
		String passErrCode = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String message = "";

		//Step 1) Null Check - 1
		if(result ==null || result.equals("")){
			throw processException("msg.jwt_connect_error" ,  new String[] { "Response is Null" }); //호출 함수에서 실패여부 따로 체크
		}

		//Step 2) Null Check - 2
		resultMap = ComUtil.splitJson(result.getBody());

		if (resultMap.get("ResultCode") ==null ){
			return result.getBody(); //호출 함수에서 실패여부 따로 체크
		}

		//Step 3) Setting API Result-Code And Message
		passErrCode	 = String.valueOf(resultMap.get("ResultCode").toString());

		if (resultMap.get("Message") != null && !resultMap.get("Message").equals("")){
			message = resultMap.get("Message").toString();
		}else{
			message =  result.getBody();
		}
		
		//Step 4) Check Management logic what Each api set And Return Exception if API Don't have Right Result-Code
		errorCodeManaging(apiCode, passErrCode, message);
		
		return result.getBody();
		
	}
	
	private void checkErrorResponseResult(String apiCode , HttpClientErrorException ee) throws Exception{
		
		String ResponseBody = ee.getResponseBodyAsString();
		String errorCode = ee.getStatusCode().toString();
		errorCodeManaging(apiCode, errorCode ,ResponseBody);
		
		//return apiCode + " " + ee.getStatusCode() + " : " + ee.getResponseBodyAsString();
	}
	
	private void errorCodeManaging(String apiCode, String passErrCode, String message) throws Exception{
		
		//= 1) 공통 Pass
		if( passErrCode.equals("0") ) return;
		
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_02_004": 
		case "IF_PAIACAPI_V2_02_004" :
			if(message.indexOf("해당 주문건은 배송중 상태입니다")  > -1 ) return;
			if(message.indexOf("해당 주문건은 클레임 상태입니다")  > -1 ) return;
			break;
		case "IF_PAGMKTAPI_V2_02_009": //미수령 조회
		case "IF_PAIACAPI_V2_02_009" :
			//Crystal Huh가 코드 1100은 조회된 기간에 조회 대상이 없다고 말했지만 고객 게시판 문의 조회같은 경우는 1000으로 날아옴..
			if(passErrCode.equals("1100")) return; 
			break;
		case "IF_PAGMKTAPI_V2_03_015": //교환 재발송 처리 완료	
		case "IF_PAIACAPI_V2_03_015" :
			if(message.indexOf("이미 교환 완료되었습니다") > -1 ) return;
			break;
		case "IF_PAGMKTAPI_V2_05_002" : //고객 게시판 문의 조회
		case "IF_PAGMKTAPI_V2_05_004" :	//고객 긴급메시지 조회
			//API ERROR와 조회기간에 조회대상 없음이 구분되지 않음. "조회된 기간에 조회 대상이 없습니다"라는 텍스트는 API가 공통으로 쓰는 텍스트가 아니라
			//G마켓 API마다 조회결과가 없을때 메세지가 각각 다르기 때문에 부득이 이렇게 처리함...
			if(message.indexOf("조회된 기간에 조회 대상이 없습니다") > -1 ) return;
			break;
		case "IF_PAGMKTAPI_V2_00_008" :	
		case "IF_PAGMKTAPI_V2_00_009" :
			if(message.indexOf("동일한 출하지") > -1 ) return;
			break;
		}
		throw processException("msg.jwt_connect_error" ,  new String[] { apiCode + " " + passErrCode + " : " + message} );		
	}
		
	//= Tcode B710에 저장된 통신 방식을 HttpMethod로 변환
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
	 
	//= apicode를 이용하여 Url 및 통신 방식 리턴
	private ParamMap getApiUrl(String apiCode) throws Exception{
		String baseURL = ConfigUtil.getString("PAGMKTV2_COM_BASE_URL");
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		apiInfo = systemService.selectPaApiInfo4Url(apiCode);
		
		paramMap.put("URL" , baseURL + apiInfo.get("API_URL"));
		paramMap.put("METHOD" , apiInfo.get("REMARK1"));
	
		return paramMap;
	}
	

	//= 개발서버인지 리얼서버인지에 따라서 (개발이라면) 실행가능한 API인지 체크한다.
	// 본 함수를 사용하려면 settingSellerId의 siteGb = "G:skstoatest";를 주석처리 해줘야한다.
	@SuppressWarnings("unused")
	private void checkRealServerYnForApiCall(ParamMap param , String realServerYn, String siteGb) throws Exception{
		String apiCode = param.getString("apiCode"); 
		
		if(siteGb.equals("G:skstoatest")) return;
		
		if (!"Y".equals(realServerYn)){
        	if(!PaAvailbleApiCode.searchApi(apiCode)) throw processException("msg.jwt_connect_error" , new String[] {apiCode + " : 해당 api는 개발서버에서 실행 할 수 없습니다"});
	    }
		
		return;
		
	}
	
	//= Seller ID 값을 세팅해준다.
	private String settingSellerId(ParamMap param , String realServerYn) throws Exception{
		String siteId = null;
		
		if(param.getString("siteGb").equals("PAE")){
		}
		
		//운영
		if("Y".equals(realServerYn)){
			switch(param.getString("siteGb")){
				//PA-EBAY = GMARKET+AUCTION
				case "PAE":
					siteId = param.getString("paCode").equals("21")?"G:skstoa,A:skstoa":"G:skstoamall,A:skstoamall";
					break;
				//PA-GMARKET
				case "PAG":
					siteId = param.getString("paCode").equals("21")?"G:skstoa":"G:skstoamall";
					break;
				//PA-AUCTION
				case "PAA":
					siteId = param.getString("paCode").equals("21")?"A:skstoa":"A:skstoamall";
					break;
				
				default:
					throw processException("msg.jwt_connect_error" , new String[] {"오류"});
			}
		}else{
		//개발
			switch(param.getString("siteGb")){
				//PA-EBAY = GMARKET+AUCTION
				case "PAE":
					siteId = param.getString("paCode").equals("21")?"G:skstoatest,A:skstoatest":"G:skstoatest,A:stoamalltest";
					break;
				//PA-GMARKET
				case "PAG":
					siteId = param.getString("paCode").equals("21")?"G:skstoatest":"G:skstoatest";
					break;
				//PA-AUCTION
				case "PAA":
					siteId = param.getString("paCode").equals("21")?"A:skstoatest":"A:stoamalltest";
					break;
				default:
					throw processException("msg.jwt_connect_error" , new String[] {"JWT 통신 아이디 설정 오류"});
			}
		}
		
		return siteId;
	}
	
	
	
}

