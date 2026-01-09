package com.cware.netshopping.pawemp.common.service;

import java.util.HashMap;
import java.util.List;

import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.cware.netshopping.pawemp.system.exception.WmpException;

public interface PaWempApiService {
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param RESPONSE CLASS
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Class<T[]> clazz, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param RESPONSE CLASS
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Class<T> clazz, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param RESPONSE CLASS
	 * @param QUERY PARAMETER
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Class<T[]> clazz ,String query, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param RESPONSE CLASS
	 * @param QUERY PARAMETER
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Class<T> clazz , String query, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param REQUEST OBJECT
	 * @param RESPONSE CLASS
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T[]> clazz, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param REQUEST OBJECT
	 * @param RESPONSE CLASS
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T> clazz, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param REQUEST OBJECT
	 * @param RESPONSE CLASS
	 * @param QUERY PARAMETER
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> List<T> callWApiList(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T[]> clazz , String query, String paName) throws WmpException, WmpApiException;
	
	/**
	 * @param apiCode
	 * @param GET,POST
	 * @param REQUEST OBJECT
	 * @param RESPONSE CLASS
	 * @param QUERY PARAMETER
	 * @return
	 * @throws WmpException
	 * @throws WmpApiException 
	 */
	public <T> Object callWApiObejct(HashMap<String, String> apiInfo, String apiMethod,Object requestObject,Class<T> clazz , String query, String paName) throws WmpException, WmpApiException;
	
	/**
	 * 위메프는 조회 시간이 24시간 미만이어야 함. 어제지금시간+10분 ~ 지금시간+9분
	 * @param targetDate
	 * @return
	 */
	public String makeDateTimeStart(String targetDate);
	
	/**
	 * 몇일전 날짜부터+지금시간+10분 ~ 지금시간+9분
	 * @param dayCnt
	 * @return
	 */
	public String makeDateTimeDayBefore(int dayCnt);

	/**
	 * 위메프는 조회 시간이 24시간 미만이어야 함. 어제지금시간+10분 ~ 지금시간+9분
	 * @param targetDate
	 * @return
	 */
	public String makeDateTimeEnd(String targetDate);
}
