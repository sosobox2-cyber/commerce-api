package com.cware.partner.common.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

import com.cware.partner.common.domain.entity.PaTransApi;
import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.common.repository.PaTransApiRepository;
import com.cware.partner.common.repository.PaTransServiceRepository;
import com.cware.partner.common.util.StringUtil;

@Service
public class TransLogService {

	@Autowired
	private PaTransApiRepository apiRepository;

	@Autowired
	private PaTransServiceRepository serviceRepository;

	@Autowired
	CommonService commonService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaTransApi logTransApiReq(PaTransApi apiLog, RequestEntity<?> requestEntity) {
		apiLog.setApiUrl(requestEntity.getMethod().name() + " " + requestEntity.getUrl().toString());
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.objectToJson(requestEntity.getBody()));
		apiLog.setRequestDate(commonService.currentTimestamp());
		return apiRepository.saveAndFlush(apiLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaTransApi logTransApiReq(PaTransApi apiLog, HttpEntity<?> requestEntity) {
		apiLog.setRequestHeader(requestEntity.getHeaders().toString());
		apiLog.setRequestPayload(StringUtil.objectToJson(requestEntity.getBody()));
		apiLog.setRequestDate(commonService.currentTimestamp());
		return  apiRepository.saveAndFlush(apiLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void logTransApiRes(PaTransApi apiLog) {
		apiLog.setResponseDate(commonService.currentTimestamp());
		apiLog.setResultMsg(StringUtil.truncate(apiLog.getResultMsg(), 4000));
//		apiLog.setResponsePayload(StringUtil.truncate(apiLog.getResponsePayload(), 120_000));
		apiRepository.updateResponse(apiLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void logTransApiResError(PaTransApi apiLog, RestClientResponseException ex) {
		Map<String, Object> result = StringUtil.jsonToMap(ex.getResponseBodyAsString());
		apiLog.setResponseDate(commonService.currentTimestamp());
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
		apiRepository.updateResponse(apiLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void logTransApiResError(PaTransApi apiLog, Exception e) {
		apiLog.setResponseDate(commonService.currentTimestamp());
		apiLog.setSuccessYn("0");
		apiLog.setResultCode("Exception");
		apiLog.setResultMsg(e.getMessage());

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		apiLog.setResponsePayload(sw.toString());

		apiRepository.updateResponse(apiLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaTransService logTransServiceStart(PaTransService serviceLog) {
		serviceLog.setStartDate(commonService.currentTimestamp());
		return serviceRepository.saveAndFlush(serviceLog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void logTransServiceEnd(PaTransService serviceLog) {
		serviceLog.setEndDate(commonService.currentTimestamp());
		serviceLog.setSuccessYn("200".equals(serviceLog.getResultCode()) ? "1" : "0");
		serviceLog.setResultMsg(StringUtil.truncate(serviceLog.getResultMsg(), 4000));
		serviceRepository.updateResult(serviceLog);
	}
}
