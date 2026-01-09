package com.cware.netshopping.pawemp.common.process.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.pawemp.common.process.PaWempApiProcess;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.cware.netshopping.pawemp.system.exception.WmpException;
import com.cware.netshopping.pawemp.system.service.PaWempSystemService;
import com.cware.netshopping.pawemp.system.util.WempAPIUtil;

@Service("pawemp.common.paWempApiProcess")
public class PaWempApiProcessImpl extends AbstractService implements PaWempApiProcess {
	
	@Resource(name = "pawemp.system.paWempSystemService")
	public PaWempSystemService paWempSystemService;
	
	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Class<T[]> clazz, String paName) throws WmpException, WmpApiException{
		return callWApiList(apiInfo, apiMethod, null, clazz,null, paName);
	}
	
	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Class<T> clazz, String paName) throws WmpException, WmpApiException{
		return callWApiObejct(apiInfo, apiMethod, null, clazz,null, paName);
	}
	
	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Class<T[]> clazz ,String query, String paName) throws WmpException, WmpApiException{
		return callWApiList(apiInfo, apiMethod, null, clazz,query, paName);
	}
	
	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Class<T> clazz , String query, String paName) throws WmpException, WmpApiException{
		return callWApiObejct(apiInfo, apiMethod, null, clazz,query, paName);
	}

	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T[]> clazz, String paName) throws WmpException, WmpApiException{
		return callWApiList(apiInfo, apiMethod, requestObject, clazz,null, paName);
	}
	
	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T> clazz, String paName) throws WmpException, WmpApiException{
		return callWApiObejct(apiInfo, apiMethod, requestObject, clazz,null, paName);
	}
	
	@Override
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T[]> clazz , String query, String paName) throws WmpException, WmpApiException{
		return WempAPIUtil.callToList(query, apiMethod, requestObject, clazz, apiInfo, paName);
	}
	
	@Override
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T> clazz , String query, String paName) throws WmpException, WmpApiException{
		return WempAPIUtil.callToObject(query, apiMethod, requestObject, clazz, apiInfo, paName);
	}
	
	@Override
	public String makeDateTimeStart(String targetDate) {
		//위메프는 조회 시간이 24시간 미만이어야 함. 어제지금시간+10분 ~ 지금시간+9분
		if(ComUtil.NVL(targetDate).length() == 8) {
			return targetDate.substring(0, 4) + "-" + targetDate.substring(4, 6) + "-" + targetDate.substring(6, 8) + " 00:00:00";
		} else {
			return DateUtil.toString(DateUtil.addMinute(DateUtil.addDay(new Date(), -1), 10), DateUtil.WEMP_DATETIME_FORMAT); 
		}
	}
	
	@Override
	public String makeDateTimeDayBefore(int dayCnt) {
		//몇일전 날짜부터+지금시간+10분 ~ 지금시간+9분
		return DateUtil.toString(DateUtil.addMinute(DateUtil.addDay(new Date(), -dayCnt), 10), DateUtil.WEMP_DATETIME_FORMAT); 
	}
	
	@Override
	public String makeDateTimeEnd(String targetDate) {
		//위메프는 조회 시간이 24시간 미만이어야 함. 어제지금시간+10분 ~ 지금시간+9분
		if(ComUtil.NVL(targetDate).length() == 8) {
			return targetDate.substring(0, 4) + "-" + targetDate.substring(4, 6) + "-" + targetDate.substring(6, 8) + " 23:59:59";
		} else {
			return DateUtil.toString(DateUtil.addMinute(new Date(), 9), DateUtil.WEMP_DATETIME_FORMAT);
		}
	}

}
