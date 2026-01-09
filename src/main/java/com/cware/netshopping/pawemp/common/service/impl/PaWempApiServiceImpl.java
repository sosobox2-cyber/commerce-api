package com.cware.netshopping.pawemp.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.pawemp.common.process.PaWempApiProcess;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.cware.netshopping.pawemp.system.exception.WmpException;

@Service("pawemp.common.paWempApiService")
public class PaWempApiServiceImpl extends AbstractService implements PaWempApiService {
	
	@Resource(name="pawemp.common.paWempApiProcess")
	private PaWempApiProcess paWempApiProcess;

	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,
			Class<T[]> clazz, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiList(apiInfo, apiMethod, clazz, paName);
	}

	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,
			Class<T> clazz, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiObejct(apiInfo, apiMethod, clazz, paName);
	}

	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,
			Class<T[]> clazz, String query, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiList(apiInfo, apiMethod, clazz, query, paName);
	}

	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,
			Class<T> clazz, String query, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiObejct(apiInfo, apiMethod, clazz, query, paName);
	}

	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,
			Object requestObject, Class<T[]> clazz, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiList(apiInfo, apiMethod, requestObject, clazz, paName);
	}

	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,
			Object requestObject, Class<T> clazz, String paName) throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiObejct(apiInfo, apiMethod, requestObject, clazz, paName);
	}

	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,
			Object requestObject, Class<T[]> clazz, String query, String paName)
			throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiList(apiInfo, apiMethod, requestObject, clazz, query, paName);
	}

	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,
			Object requestObject, Class<T> clazz, String query, String paName)
			throws WmpException, WmpApiException {
		return paWempApiProcess.callWApiObejct(apiInfo, apiMethod, requestObject, clazz, query, paName);
	}

	@Override
	public String makeDateTimeStart(String targetDate) {
		return paWempApiProcess.makeDateTimeStart(targetDate);
	}
	
	@Override
	public String makeDateTimeDayBefore(int dayCnt) {
		return paWempApiProcess.makeDateTimeDayBefore(dayCnt);
	}

	@Override
	public String makeDateTimeEnd(String targetDate) {
		return paWempApiProcess.makeDateTimeEnd(targetDate);
	}
}
