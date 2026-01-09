/*
 * @(#)HttpUtils.java	2007/10/10
 *
 * Copyright 2007 Commerceware, Inc. All rights reserved.
 */
package com.cware.netshopping.common.util;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cware.framework.core.basic.ParamMap;

/**
* HttpClient package 를 이용한 HTTP 연결 및 결과 처리를 위한 Class
*
* @version 1.0, 2007/10/10
* @author commerceware.co.kr
*/
public class HttpUtil {

	// log 설정
	private static Log log = LogFactory.getLog(HttpUtil.class);

	/**
	 * GET METHOD CALL
	 * config example
	 * HTTP_CONFIG_ADDRESS        : www.commerceware.co.kr
	 * HTTP_CONFIG_PORT           : 80
	 * HTTP_CONFIG_PROTOCOL       : http
	 * HTTP_CONFIG_TIME_OUT       : 30000
	 * HTTP_CONFIG_ADDRESS_DETAIL : /main/main.jsp
	 * HTTP_CONFIG_CONTENT_TYPE   : application/x-www-form-urlencoded;charset=utf-8
	 *                              text/html;charset=utf-8
	 *                              text/xml;charset=utf-8
	 * EXCEPTION_MESSAGE          : 예외 내역
	 * HTTP_RESPONSE_CODE         : 결과 코드
	 * HTTP_RESPONSE_BODY         : 결과 내역
	 *
	 * @param config
	 * @param params
	 * @return
	 */
	public static ParamMap getGetHttpClient(ParamMap config, Properties params){
		// HttpClient 정의
		HttpClient client = null;
		// Method 방식(GET/POST)
		GetMethod method = null;
		// 호출 결과 코드
		int responseCode = 0;
		// 호출 결과 내역
		String responseBody = "";

		try{

			// HttpClient 초기화
			// etc) client = new HttpClient(new MultiThreadedHttpConnectionManager());
			client = new HttpClient();

			// Host 정보 세팅
			client.getHostConfiguration().setHost(
					config.getString ( "HTTP_CONFIG_ADDRESS"  ),
					config.getInt    ( "HTTP_CONFIG_PORT"     ),
					config.getString ( "HTTP_CONFIG_PROTOCOL" ) );

			// cookie 설정
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

			// Connection 연결시 Timeout 세팅
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(config.getInt("HTTP_CONFIG_TIME_OUT"));

	        // get 방식 호출 : parameter 를 encoding 처리해서 전송
	        method = new GetMethod(config.getString("HTTP_CONFIG_ADDRESS_DETAIL") + encodeParams(params));

	        // get 방식에서만 사용되는 설정.
	        method.setFollowRedirects(true);

		    // Provide custom retry handler is necessary
	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

	        // charset 정의
	        method.getParams().setContentCharset("UTF-8");

	        // Content-Type 정의
	        method.setRequestHeader("Content-Type" , config.getString("HTTP_CONFIG_CONTENT_TYPE") );

	        // Cache-Control 정의
	        method.setRequestHeader("Cache-Control" , "no-cache");

	        // 최종 실행
			responseCode = client.executeMethod(method);

			// 결과 코드에 따른 예외 throw
			if (responseCode != HttpStatus.SC_OK) {
				throw new Exception("Method failed: " + method.getStatusLine());
			}

			// 호출된 url 의  response 내역
			responseBody = method.getResponseBodyAsString();

		}catch(Exception e){
			log.error(e.getMessage());
			responseCode = 0;
			config.put("EXCEPTION_MESSAGE", e.getMessage());
		}finally{
			method.releaseConnection();
			config.put("HTTP_RESPONSE_CODE", responseCode);
			config.put("HTTP_RESPONSE_BODY", responseBody);
		}

		return config;
	}

	/**
	 * POST METHOD CALL
	 * config example
	 * HTTP_CONFIG_ADDRESS        : www.commerceware.co.kr
	 * HTTP_CONFIG_PORT           : 80
	 * HTTP_CONFIG_PROTOCOL       : http
	 * HTTP_CONFIG_TIME_OUT       : 30000
	 * HTTP_CONFIG_ADDRESS_DETAIL : /main/main.jsp
	 * HTTP_CONFIG_CONTENT_TYPE   : application/x-www-form-urlencoded;charset=utf-8
	 *                              text/html;charset=utf-8
	 *                              text/xml;charset=utf-8
	 * EXCEPTION_MESSAGE          : 예외 내역
	 * HTTP_RESPONSE_CODE         : 결과 코드
	 * HTTP_RESPONSE_BODY         : 결과 내역
	 * @param config
	 * @param params
	 * @return
	 */
	public static ParamMap getPostHttpClient(ParamMap config, Properties params){
		// HttpClient 정의
		HttpClient client = null;
		// Method 방식(GET/POST)
		PostMethod method = null;
		// 호출 결과 코드
		int responseCode = 0;
		// 호출 결과 내역
		String responseBody = "";
		// parameter 세팅을 위한 Enumeration
		Enumeration names = null;
		// parameter key 정의
		String name = "";
		// loop 정의
		int i = 0;

		try{

			// HttpClient 초기화
			// etc) client = new HttpClient(new MultiThreadedHttpConnectionManager());
			client = new HttpClient(new MultiThreadedHttpConnectionManager());

			// Host 정보 세팅
			client.getHostConfiguration().setHost(
					config.getString ( "HTTP_CONFIG_ADDRESS"  ),
					config.getInt    ( "HTTP_CONFIG_PORT"     ),
					config.getString ( "HTTP_CONFIG_PROTOCOL" ) );

			// cookie 설정
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

			// Connection 연결시 Timeout 세팅
			// ex) HTTP_CONFIG_TIME_OUT : 30000
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(config.getInt("HTTP_CONFIG_TIME_OUT"));

	        // post 방식 호출
	        method = new PostMethod(config.getString("HTTP_CONFIG_ADDRESS_DETAIL"));

	        // post 방식에서 parameter 세팅
			names = params.propertyNames();
			NameValuePair[] action = new NameValuePair[params.size()];
			while (names.hasMoreElements()) {
				name = (String) names.nextElement();
				action[i++] = new NameValuePair(name, params.getProperty(name));
			}
			method.setRequestBody(action);

		    // Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

	        // charset 정의
			method.getParams().setContentCharset("UTF-8");

	        // Content-Type 정의
	        method.setRequestHeader("Content-Type" , config.getString("HTTP_CONFIG_CONTENT_TYPE") );

	        // Cache-Control 정의
	        method.setRequestHeader("Cache-Control" , "no-cache");

	        // 최종 실행
			responseCode = client.executeMethod(method);

			// 결과 코드에 따른 예외 throw
			if (responseCode != HttpStatus.SC_OK) {
				throw new Exception("Method failed: " + method.getStatusLine());
			}

			// 호출된 url 의  response 내역
			responseBody = method.getResponseBodyAsString();

		}catch(Exception e){
			log.error(e.getMessage()); 
			responseCode = 0;
			config.put("EXCEPTION_MESSAGE", e.getMessage());
		}finally{
			method.releaseConnection();
			config.put("HTTP_RESPONSE_CODE", responseCode);
			config.put("HTTP_RESPONSE_BODY", responseBody);
		}

		return config;
	}

	/**
	 * PARAMETER ENCODING
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String encodeParams(Properties params) throws Exception{
		StringBuffer sb    = new StringBuffer();
		Enumeration  names = params.propertyNames();
		String       name  = "";
		String       value = "";

		if(!params.isEmpty()) sb.append("?");

		while (names.hasMoreElements()) {
			name = (String) names.nextElement();
			value = params.getProperty(name);
			sb.append(URLEncoder.encode(name,"UTF-8") + "=" + URLEncoder.encode(value,"UTF-8") );
			if (names.hasMoreElements()) sb.append("&");
		}

		return sb.toString();
	}
}
