package com.cware.netshopping.pawemp.system.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ApiHistoryUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.cware.netshopping.pawemp.system.exception.WmpException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WempAPIUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(WempAPIUtil.class);
	
	/**
	 * 위메프 API호출 - LIST로 리턴
	 * @param url
	 * @param apiMethod
	 * @param requestObject
	 * @param clazz
	 * @return
	 * @throws WmpException
	 */
	public static <T> List<T> callToList(String query, String apiMethod,Object requestObject,Class<T[]> clazz,HashMap<String,String> apiInfo, String paName) throws WmpException , WmpApiException{
		try {
			JsonElement element = callPaWempAPI(query, apiMethod, requestObject,apiInfo, paName);
			T[] arr = new Gson().fromJson(element, clazz);
			return Arrays.asList(arr);
		} catch(WmpApiException e){
			LOG.debug("WEMP API RETURN CODE : " + e.getMessage());
			throw new WmpApiException(e.getMessage());
		}catch (Exception e) {
			LOG.debug("WEMP API ERROR : " + e.getMessage());
			return null;
		}
	}
	/**
	 * 위메프 API호출 - OBJECT로 리턴
	 * @param url
	 * @param apiMethod
	 * @param requestObject
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws WmpException
	 */
	public static <T> Object callToObject(String query, String apiMethod,Object requestObject,Class<T> clazz,HashMap<String,String> apiInfo, String paName) throws WmpException , WmpApiException{
		try {
			JsonElement element = callPaWempAPI(query,apiMethod, requestObject,apiInfo, paName);
			return new Gson().fromJson(element, clazz);
		} catch(WmpApiException e){
			LOG.debug("WEMP API RETURN CODE : " + e.getMessage());
			throw new WmpApiException(e.getMessage());
		}catch (Exception e) {
			LOG.debug("WEMP API ERROR : " + e.getMessage());
			return null;
		}
	}
	
	public static JsonElement callPaWempAPI(String query,String apiMethod,Object requestObject,HashMap<String,String> apiInfo, String paName) throws WmpException,WmpApiException {
		URIBuilder uriBuilder = new URIBuilder();
		RequestConfig requestConfig = null;
		CloseableHttpClient client = null;
		HttpGet requestGet = null;
		HttpPost requestPost = null;
		CloseableHttpResponse response = null;
		JsonObject resultObject = new JsonObject();
		JsonElement returnElement = null;
		Header[] headers = null;
		String requestString = new GsonBuilder().create().toJson(requestObject);
		String requestHeader = "";
		String apiKey = apiInfo.get(paName);
		String apiUrl = apiInfo.get("API_URL");
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT"))
				.setConnectTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT"))
				.setConnectionRequestTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT")).build();
		client = HttpClientBuilder.create().build();

		uriBuilder.setScheme(ConfigUtil.getString("WEMP_SCHEMA"))
		  .setHost(ConfigUtil.getString("WEMP_HOST"))
		  .setPath(apiUrl);
		try {

			if ("GET".equals(apiMethod)) {		
				if(StringUtils.isNotBlank(query)){
					String[] params = query.split("&");
					for(String queryParams : params){
						String[] param = queryParams.split("=");
						if(param.length > 1) {
							uriBuilder.addParameter(param[0], param[1]);
						}
					}
				}
				
				if(uriBuilder.build().toString() != null) {
					requestGet = new HttpGet(uriBuilder.build().toString());
				}
				requestGet.setConfig(requestConfig);
				requestGet.addHeader("apiKey", apiKey);
				requestGet.addHeader("content-type", "application/json");
				response = client.execute(requestGet);
				
				headers = requestGet.getAllHeaders();

			} else if ("POST".equals(apiMethod)) {
				requestString = new GsonBuilder().create().toJson(requestObject);
				requestPost = new HttpPost(uriBuilder.build().toString());
				requestPost.setConfig(requestConfig);
				StringEntity params = new StringEntity(requestString, "UTF-8");
				requestPost.addHeader("apiKey", apiKey);
				requestPost.addHeader("content-type", "application/json");
				requestPost.setEntity(params);
				response = client.execute(requestPost);
				
				headers = requestPost.getAllHeaders();
			}
			
			HttpEntity entity = response.getEntity();
            Gson gson = new Gson();
            resultObject = (JsonObject) gson.fromJson(EntityUtils.toString(entity), JsonObject.class);
			
			int headerCnt = 0;
			for(Header h : headers) {
				if(headerCnt == 0) {
					requestHeader += "{";
				}
				else {
					if(headerCnt <= headers.length-1) requestHeader += ", ";
				}
				requestHeader += h.getName() + "=";
				requestHeader += "["+h.getValue()+"]";
				if(headerCnt++ == headers.length-1) requestHeader += "}";
			}
			
			PaRequestMap paRequestMap = new PaRequestMap();
			if(paName.equals(Constants.PA_BROAD)) {
				paRequestMap.setPaCode(Constants.PA_WEMP_BROAD_CODE);
			}
			else {
				paRequestMap.setPaCode(Constants.PA_WEMP_ONLINE_CODE);
			}
			paRequestMap.setReqApiCode(apiInfo.get("apiInfo").toString());
			paRequestMap.setReqUrl("["+apiMethod+"]"+apiUrl);
			paRequestMap.setReqHeader(requestHeader);
			paRequestMap.setRequestMap(requestString);
			paRequestMap.setResponseMap(resultObject.toString());
			paRequestMap.setRemark("");
			ApiHistoryUtil.insertPaRequestMap(paRequestMap);
			
			if(StringUtils.equals(resultObject.get("resultCode").toString(), "200")){
				returnElement = resultObject.get("data");
			}else{
				throw new WmpApiException("resultCode:"+resultObject.get("resultCode").toString()+"||error:"+resultObject.get("error").toString());
			}
		}
		catch (WmpApiException e) {
			throw new WmpApiException(e.getMessage(), e);
		}
		catch (Exception e) {
			throw new WmpException(e.getMessage(),e);
		}finally{
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return returnElement;
	}
}
