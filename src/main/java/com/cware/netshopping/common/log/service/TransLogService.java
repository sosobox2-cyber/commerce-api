package com.cware.netshopping.common.log.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.util.StringUtil;

@Service
public class TransLogService {

	@Autowired
	private TransLogMapper logMapper;

	public boolean logTransApiReq(PaTransApi apiLog, RequestEntity<?> requestEntity) {
		apiLog.setApiUrl(requestEntity.getMethod().name() + " " + requestEntity.getUrl().toString());
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.objectToJson(requestEntity.getBody()));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}

	public boolean logTransApiReqXml(PaTransApi apiLog, RequestEntity<?> requestEntity) {
		apiLog.setApiUrl(requestEntity.getMethod().name() + " " + requestEntity.getUrl().toString());
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.objectToXml(requestEntity.getBody()));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiReq(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.truncate(StringUtil.objectToJson(requestEntity.getBody()), 120_000));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}

	public boolean logTransApiReqXml(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.objectToXml(requestEntity.getBody()));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}

	public boolean logTransApiReqSoap(PaTransApi apiLog, Object request) {
		apiLog.setRequestPayload(StringUtil.objectToXml(request));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiReqNaver(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.truncate(requestEntity.getBody().toString(), 120_000));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiReqTdeal(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.truncate(requestEntity.getBody().toString(), 120_000));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiReqFaple(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.truncate(requestEntity.getBody().toString(), 120_000));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiReqQeen(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.truncate(requestEntity.getBody().toString(), 120_000));
		return logMapper.insertPaTransApiReq(apiLog) > 0;
	}
	
	public boolean logTransApiRes(PaTransApi apiLog) {
		apiLog.setResultMsg(StringUtil.truncate(apiLog.getResultMsg(), 4000));
		apiLog.setResponsePayload(StringUtil.truncate(apiLog.getResponsePayload(), 120_000));
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	public boolean logTransApiResError(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");
		
		String message = (String)result.get("message");
		String code = StringUtil.truncate(String.valueOf(result.get("code")), 100);

		if (message == null) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransApiResErrorEbay(PaTransApi apiLog, RestClientResponseException ex) {
		apiLog.setSuccessYn("0");
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		
		String message = (String)result.get("Message");
		String resultCode = String.valueOf(result.get("ResultCode"));
		
		if (message == null) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(resultCode)) {
			resultCode = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(resultCode);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransApiResErrorWemp(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");
		
		String message = String.valueOf(result.get("error"));
		String code = String.valueOf(result.get("resultCode"));

		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransApiResErrorLotteon(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");
		
		String message = String.valueOf(result.get("message"));
		String code = String.valueOf(result.get("returnCode"));

		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransApiResErrorTmon(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");

		String message = String.valueOf(result.get("error"));
		String code = String.valueOf(ex.getRawStatusCode());

		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	@SuppressWarnings("unchecked")
	public boolean logTransApiResErrorKakao(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");

		String message = String.valueOf(result.get("msg"));
		String code = String.valueOf(result.get("code"));

		Map<String, Object> extraError = (Map<String, Object>)result.get("extras");

		if (extraError != null) {
			message = String.valueOf(extraError.get("error_message"));
			code = String.valueOf(extraError.get("error_code"));

			String validation = String.valueOf(extraError.get("validation"));
			if (!"null".equals(validation)) message = validation;
		}
		
		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransApiResErrorKakaoSeller(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setSuccessYn("0");

		String message = String.valueOf(result.get("errorMessage"));
		String code = String.valueOf(result.get("errorCode"));
		String validation = String.valueOf(result.get("validation"));
		if (!"null".equals(validation)) message = validation;
		
		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	@SuppressWarnings("unchecked")
	public boolean logTransApiResErrorHalfSeller(PaTransApi apiLog, RestClientResponseException ex) {
		
		Map<String, Object> resultMap = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		Map<String, Object> resultStatus = (Map<String, Object>)resultMap.get("resultStatus");
		
		apiLog.setSuccessYn("0");

		String message = String.valueOf(resultStatus.get("message"));
		String code = String.valueOf(resultStatus.get("code"));	
		
		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	
	public boolean logTransApiResErrorNaverV3(PaTransApi apiLog, RestClientResponseException ex) throws UnsupportedEncodingException {
		
		Map<String, Object> resultMap = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		
		apiLog.setSuccessYn("0");
		
		String message = String.valueOf(resultMap.get("message"));
		String traceId = String.valueOf(resultMap.get("traceId"));
		String invalidInputs = String.valueOf(resultMap.get("invalidInputs"));
		
		if (!"null".equals(traceId)) {
			message += "[traceId] = " + traceId;
		}
		if (!"null".equals(invalidInputs)) {
			message += "[invalidInputs] = " + invalidInputs;
		}
		
		String code = String.valueOf(resultMap.get("code"));			
				
		String responseBodyAsString = "";
		
		// 4XX에러 OR GW 발생시 Content-type의 인코딩 셋이 null로 RestClientResponseException 발생
		// RestClientResponseException의 기본 인코딩셋 'ISO-8859-1'을 'UTF-8'로 변환 처리
		if(ex.getResponseHeaders().getContentType().getCharset() == null) {
			responseBodyAsString = URLDecoder.decode(ex.getResponseBodyAsString(), "ISO-8859-1");
			responseBodyAsString = new String(responseBodyAsString.getBytes("ISO-8859-1"), "UTF-8");
			
			resultMap = StringUtil.jsonToMap(responseBodyAsString);
			message = String.valueOf(resultMap.get("message"));
		}else {
			responseBodyAsString = ex.getResponseBodyAsString();
		}		
		
		if ("null".equals(message)) {
			message = responseBodyAsString;
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(responseBodyAsString);
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	@SuppressWarnings("unchecked")
	public boolean logTransApiResErrorTdeal(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		String message = "";
		String detailMessage = "";
		apiLog.setSuccessYn("0");
		
		if(result.get("detail") instanceof List<?>) {
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) result.get("detail");
			for (Map<String, Object> detail : detailList) {
				detailMessage += String.valueOf(detail.get("message")) + "/";
			}
			message = String.valueOf(result.get("message")) + "-" + detailMessage;
			
		} else {
			message = String.valueOf(result.get("message"));
		}
		String code = StringUtil.truncate(String.valueOf(result.get("code")), 100);

		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(code)) {
			code = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(code);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	public boolean logTransApiResErrorFaple(PaTransApi apiLog, RestClientResponseException ex) {
		apiLog.setSuccessYn("0");
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		
		String message = String.valueOf(result.get("Message"));
		String resultCode = String.valueOf(result.get("Status"));
		
		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(resultCode)) {
			resultCode = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(resultCode);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		apiLog.setResponsePayload(result!=null ?StringUtil.truncate(result.toString(), 4000): StringUtil.truncate(ex.getResponseBodyAsString(), 4000));
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	public boolean logTransApiResErrorQeen(PaTransApi apiLog, RestClientResponseException ex) {
		apiLog.setSuccessYn("0");
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		
		String message = String.valueOf(result.get("msg"));
		String resultCode = String.valueOf(result.get("code"));
		
		if ("null".equals(message)) {
			message = ex.getResponseBodyAsString();
		}
		if ("null".equals(resultCode)) {
			resultCode = String.valueOf(ex.getRawStatusCode());
		}
		
		apiLog.setResultCode(resultCode);
		apiLog.setResultMsg(StringUtil.truncate(message, 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		apiLog.setResponsePayload(result!=null ?StringUtil.truncate(result.toString(), 4000): StringUtil.truncate(ex.getResponseBodyAsString(), 4000));
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	public boolean logTransApiResErrorXml(PaTransApi apiLog, RestClientResponseException ex) {
		apiLog.setSuccessYn("0");
		apiLog.setResultCode(String.valueOf(ex.getRawStatusCode()));
		apiLog.setResultMsg(StringUtil.truncate(ex.getResponseBodyAsString(), 4000));
		apiLog.setResponsePayload(ex.getResponseBodyAsString());
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}
	
	public boolean logTransApiResError(PaTransApi apiLog, Exception e) {
		apiLog.setSuccessYn("0");
		apiLog.setResultCode("Exception");
		apiLog.setResultMsg(e.getMessage());
		apiLog.setResponsePayload(ExceptionUtils.getStackTrace(e));
		return logMapper.updatePaTransApiRes(apiLog) > 0;
	}

	public boolean logTransServiceStart(PaTransService serviceLog) {
		return logMapper.insertPaTransServiceStart(serviceLog) > 0;
	}

	public boolean logTransServiceEnd(PaTransService serviceLog) {
		serviceLog.setSuccessYn("200".equals(serviceLog.getResultCode()) ? "1" : "0");
		serviceLog.setResultMsg(StringUtil.truncate(serviceLog.getResultMsg(), 4000));
		return logMapper.updatePaTransServiceEnd(serviceLog) > 0;
	}

	public boolean logTransApi(PaTransApi apiLog, RequestEntity<?> requestEntity) {
		apiLog.setApiUrl(requestEntity.getMethod().name() + " " + requestEntity.getUrl().toString());
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		return logMapper.insertPaTransApi(apiLog) > 0;
	}

	public boolean logTransApi(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		return logMapper.insertPaTransApi(apiLog) > 0;
	}
	
}
