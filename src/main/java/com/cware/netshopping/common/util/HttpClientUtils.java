package com.cware.netshopping.common.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

public class HttpClientUtils {

	static class HttpClientHolder {
		static HttpClient httpClient = create();
	}

	public static HttpClient getDefaultInstance() {
		return HttpClientHolder.httpClient;
	}

	public static HttpClient create() {
		return create(false);
	}

	public static HttpClient create(boolean single) {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUserAgent(params, null);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		
		ClientConnectionManager clientConnectionManager;
		
		if (single) {
			clientConnectionManager = new BasicClientConnectionManager(schemeRegistry);
		} else {
			PoolingClientConnectionManager tsccm = new PoolingClientConnectionManager(schemeRegistry);
			tsccm.setDefaultMaxPerRoute(5);
			tsccm.setMaxTotal(100);
			clientConnectionManager = tsccm;
		}
		
		DefaultHttpClient httpclient = new DefaultHttpClient(clientConnectionManager, params);
		httpclient.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
		
		return httpclient;
	}
	
	public static String getResponseText(String url) {
		return getResponseText(url, null, "UTF-8");
	}

	public static String getResponseText(String url, Map<String, String> params) {
		return getResponseText(url, params, "UTF-8");
	}

	public static String getResponseText(String url,
			Map<String, String> params, Map<String, String> headers) {
		return getResponseText(url, params, headers, "UTF-8");
	}

	public static String getResponseText(String url, Map<String, String> params, String encoding) {
		return getResponseText(url, params, null, encoding);
	}

	public static String getResponseText(String url, Map<String, String> params, Map<String, String> headers, String encoding) {
		HttpGet httpRequest = null;
		try {
			StringBuilder sb = new StringBuilder(url);
			if (params != null && params.size() > 0) {
				sb.append(url.indexOf('?') < 0 ? '?' : '&');
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding)).append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			
			httpRequest = new HttpGet(sb.toString());
			if (headers != null && headers.size() > 0){
				for (Map.Entry<String, String> entry : headers.entrySet()){
					httpRequest.addHeader(entry.getKey(), entry.getValue());
				}
			}
			return getDefaultInstance().execute(httpRequest, new BasicResponseHandler());
		} catch (Exception e) {
			httpRequest.abort();
		}
		return null;
	}

	public static String postResponseText(String url, Map<String, String> params) {
		return postResponseText(url, params, "UTF-8");
	}

	public static String postResponseText(String url,
			Map<String, String> params, Map<String, String> headers) {
		return postResponseText(url, params, headers, "UTF-8");
	}

	public static String postResponseText(String url,
			Map<String, String> params, String encoding) {
		return postResponseText(url, params, null, encoding);
	}
	
	public static String postResponseText(String url, JSONObject json, Map<String, String> headers, String encoding) {
		HttpPost httpRequest = new HttpPost(url);
		try {
			if (json != null) {
				httpRequest.setEntity(new StringEntity(json.toString(),encoding));
			}
			if (headers != null && headers.size() > 0){
				for (Map.Entry<String, String> entry : headers.entrySet()){
					httpRequest.addHeader(entry.getKey(), entry.getValue());
				}
			}
			return getDefaultInstance().execute(httpRequest, new BasicResponseHandler());
		} catch (Exception e) {
			httpRequest.abort();
		}
		return null;
	}

	public static String postResponseText(String url, Map<String, String> params, Map<String, String> headers, String encoding) {
		HttpPost httpRequest = new HttpPost(url);
		try {
			if (params != null && params.size() > 0) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()){
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpRequest.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			}
			if (headers != null && headers.size() > 0){
				for (Map.Entry<String, String> entry : headers.entrySet()){
					httpRequest.addHeader(entry.getKey(), entry.getValue());
				}
			}
			return getDefaultInstance().execute(httpRequest, new BasicResponseHandler());
		} catch (Exception e) {
			httpRequest.abort();
		}
		return null;
	}

	public static String post(String url, String entity) {
		return invoke("POST", url, entity);
	}

	public static String put(String url, String entity) {
		return invoke("PUT", url, entity);
	}

	public static String delete(String url) {
		return invoke("DELETE", url, null);
	}

	public static String get(String url) {
		return invoke("GET", url, null);
	}

	private static String invoke(String method, String url, String entity) {
		HttpRequestBase httpRequest = null;
		if (method.equalsIgnoreCase("GET")){
			httpRequest = new HttpGet(url);
		} else if (method.equalsIgnoreCase("POST")) {
			httpRequest = new HttpPost(url);
		} else if (method.equalsIgnoreCase("PUT")) {
			httpRequest = new HttpPut(url);
		} else if (method.equalsIgnoreCase("DELETE")) {
			httpRequest = new HttpDelete(url);
		}
		try {
			if (entity != null){
				((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(entity, "UTF-8"));
			}
			return getDefaultInstance().execute(httpRequest, new BasicResponseHandler());
		} catch (Exception e) {
			httpRequest.abort();
		}
		return null;
	}
}
