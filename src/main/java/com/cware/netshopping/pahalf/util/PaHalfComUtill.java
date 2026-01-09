package com.cware.netshopping.pahalf.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import com.cware.framework.core.basic.ParamMap;
import com.cware.framework.core.dataaccess.util.CamelUtil;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaHalfGoods;


public class PaHalfComUtill {
	
	//문자열로 된 날짜 형식을 TimeStamp로 변환한다.
	public static Timestamp date2TimeStamp(String date ) throws Exception{
		String format = DateUtil.getFormatStringWithDate(date);
		return DateUtil.toTimestamp(date,format);
	}
	
	public static String getErrorMessage(Exception e) {
		String errMsg = "";
		
		if(e.getMessage() != null) {
			errMsg = e.getMessage();
		}else {
			errMsg = e.toString();
		}
		errMsg = errMsg.length() > 3950 ? errMsg.substring(0, 3950) : errMsg;   //insertApiTrackingTx 안에서 subString 함
		return errMsg;
	}
	
	public static String getErrorMessage(Exception e, int length) {
		String errMsg = "";
		
		if(e.getMessage() != null) {
			errMsg = e.getMessage();
		}else {
			errMsg = e.toString();
		}
		errMsg = errMsg.length() > length ? errMsg.substring(0, length) : errMsg;   //insertApiTrackingTx 안에서 subString 함
		return errMsg;
	}
	
	/**
	 * ArrayList 안의 Map을 Cammel형식으로 수정한다.
	 * @param  ArrayList
	 */
	@SuppressWarnings("unchecked")
	public static void replaceCamelList(List<Map<String, Object>> arrList) {
		int index = 0;
		
		for(Map< ?, ?> map : arrList ) {
			arrList.set( index ,(Map<String, Object>)replaceCamel(map));
			index++;
		}
	}
	
	public static Map<?, ?> replaceCamel(Map<?,?> map) {
        Iterator<?> iterator 			= map.entrySet().iterator();
        Entry<?, ?> entry 	 			= null;
        HashMap<String, Object> rtnMap 	= new HashMap<String, Object>();

        while (iterator.hasNext()) {
         	entry = (Entry<?, ?>) iterator.next();
            rtnMap.put(CamelUtil.convert2CamelCase(entry.getKey().toString().toUpperCase()), entry.getValue());
        }
        
        return rtnMap;
    }
	
	/**
	 * Map 데이터를 VO 객체로 바꿔주는 Util이다.
	 * @param  Map
	 * @param  Class   -> ex) Map을 PaHalfOrderListVO에 넣고 싶으면  PaHalfOrderListVO.Class
	 * @return VO
	 */
	public static Object map2VO(Map<? , ?> map, Class<?> cls) throws Exception{ 
	    String keyVal 			= null ; 
	    String setterName 		= null; 
	    Iterator<?> itertator 	= map.keySet().iterator();    
	    Object obj 				= cls.newInstance();
	    String		format		= "";
	    
	    while(itertator.hasNext()){
	    	keyVal = (String) itertator.next(); 
	        //=1) setter 이름을 가져온다  ex)setOrdNo  
	    	setterName = "set"+keyVal.substring(0,1).toUpperCase()+keyVal.substring(1); 
	    	//=2) VO class의 Method들의 목록을 가져온다 (getter, setter, toString 정도가 되겠지)
	    	Method[] methods = obj.getClass().getMethods(); 
	    	//=3) 루프를 돌려서 Map의 value 로 만든 kayVal과 메소드 이름을 비교해서 같으면 Map의 값을 Vo Setter에 세팅해준다 
	    	if(map.get(keyVal) == null) continue;
	    	
	    	for(Method method : methods){ 
	            if(setterName.equals(method.getName())){ 
	                try{
	                	Parameter[] param = method.getParameters();
	                	String ourType = (param[0].toString()).replace("java.sql.", "").split(" ")[0] ;
	                	ourType = ourType.replace("java.lang.", "").split(" ")[0];
	                	
	                	//4) 기본 자료형 파싱 #boolean #byte #short #int #long #float #double #char(String)
	                	if("double".equals(ourType)) {
	                		method.invoke(obj, Double.parseDouble(String.valueOf(map.get(keyVal))) ); 
	                	}else if("int".equals(ourType)){
	                		method.invoke(obj, Integer.parseInt(String.valueOf(map.get(keyVal))) ); 
	                	}else if("long".equals(ourType)) {
	                		method.invoke(obj, Long.parseLong(String.valueOf(map.get(keyVal))) ); 
	                	}else if("float".equals(ourType)) {
	                		method.invoke(obj, Float.parseFloat(String.valueOf(map.get(keyVal))) ); 
	                	}else if("short".equals(ourType)) {
	                		method.invoke(obj, Short.parseShort(String.valueOf(map.get(keyVal))) ); 
	                	}else if("byte".equals(ourType)) {
	                		method.invoke(obj, Byte.parseByte(String.valueOf(map.get(keyVal))));
	                	}else if("boolean".equals(ourType)) {
	                		method.invoke(obj, Boolean.parseBoolean(String.valueOf(map.get(keyVal))));
	                	}else if("String".equals(ourType)) {
	                		method.invoke(obj, String.valueOf(map.get(keyVal)));
	                	}else if("Timestamp".equals(ourType)) {
	                		
	                		
	                		if(String.valueOf(map.get(keyVal)).contains("-") ) {
	                			format = DateUtil.COPN_DATETIME_FORMAT;	
	                		}else {
	                			format = DateUtil.CWARE_JAVA_DATETIME_FORMAT;	
	                		}
	                		
	                		method.invoke(obj, DateUtil.toTimestamp(String.valueOf(map.get(keyVal)), format));
	                	
	                	
	                	
	                	}
	                	/*
	                	if("long".equals(param[0].getType().toString())) {
	                		method.invoke(obj, (long)checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("int".equals(param[0].getType().toString())) {
	                		method.invoke(obj, checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("double".equals(param[0].getType().toString())) {
		                	method.invoke(obj, Double.parseDouble(map.get(keyVal).toString()));
		                }else if("float".equals(param[0].getType().toString())) {
	                		method.invoke(obj, Float.parseFloat(map.get(keyVal).toString()));
	                	}else if ("short" .equals(param[0].getType().toString())) {
	                		method.invoke(obj, (short)checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("byte".equals(param[0].getType().toString())) {
	                		method.invoke(obj, Byte.parseByte(map.get(keyVal).toString()));
	                	}else if("boolean".equals(param[0].getType().toString())) {
	                		method.invoke(obj,Boolean.parseBoolean(map.get(keyVal).toString()));
	                	}else {
	                		method.invoke(obj, map.get(keyVal).toString());	
	                	}*/
	                	
	                }catch(Exception e){ 
	                	System.out.println("ERROR:::::::::::::::" + keyVal);
	                	//e.printStackTrace(); 
	                } 
	            }
	        }
	    }
	    return obj; 
	}
	
	//오버로딩 :: 이미 생성되어있는 VO객체에 추가로 MAP의 데이터를 넣고을때 사용
	public static void map2VO(Map<? , ?> map, Object obj) throws Exception{ 
	    String keyVal 			= null ; 
	    String setterName 		= null; 
	    Iterator<?> itertator 	= map.keySet().iterator();    
	    String format 			= "";
	    
	    while(itertator.hasNext()){
	    	keyVal = (String) itertator.next(); 
	        //=1) setter 이름을 가져온다  ex)setOrdNo  
	    	setterName = "set"+keyVal.substring(0,1).toUpperCase()+keyVal.substring(1); 
	    	//=2) VO class의 Method들의 목록을 가져온다 (getter, setter, toString 정도가 되겠지)
	    	Method[] methods = obj.getClass().getMethods(); 
	    	//=3) 루프를 돌려서 Map의 value 로 만든 kayVal과 메소드 이름을 비교해서 같으면 Map의 값을 Vo Setter에 세팅해준다 
	    	if(map.get(keyVal) == null) continue;
	    	
	    	for(Method method : methods){ 
	            if(setterName.equals(method.getName())){ 
	                try{
	                	Parameter[] param = method.getParameters();
	                	String ourType = (param[0].toString()).replace("java.sql.", "").split(" ")[0] ;
	                	ourType = ourType.replace("java.lang.", "").split(" ")[0];
	                	
	                	if("double".equals(ourType)) {
	                		method.invoke(obj, Double.parseDouble(String.valueOf(map.get(keyVal))) ); 
	                	}else if("int".equals(ourType)){
	                		method.invoke(obj, Integer.parseInt(String.valueOf(map.get(keyVal))) ); 
	                	}else if("long".equals(ourType)) {
	                		method.invoke(obj, Long.parseLong(String.valueOf(map.get(keyVal))) ); 
	                	}else if("float".equals(ourType)) {
	                		method.invoke(obj, Float.parseFloat(String.valueOf(map.get(keyVal))) ); 
	                	}else if("short".equals(ourType)) {
	                		method.invoke(obj, Short.parseShort(String.valueOf(map.get(keyVal))) ); 
	                	}else if("byte".equals(ourType)) {
	                		method.invoke(obj, Byte.parseByte(String.valueOf(map.get(keyVal))));
	                	}else if("boolean".equals(ourType)) {
	                		method.invoke(obj, Boolean.parseBoolean(String.valueOf(map.get(keyVal))));
	                	}else if("String".equals(ourType)) {
	                		method.invoke(obj, String.valueOf(map.get(keyVal)));
	                	}else if("Timestamp".equals(ourType)) {
	                		if(String.valueOf(map.get(keyVal)).contains("-") ) {
	                			format = DateUtil.COPN_DATETIME_FORMAT;	
	                		}else {
	                			format = DateUtil.CWARE_JAVA_DATETIME_FORMAT;	
	                		}
	                		method.invoke(obj, DateUtil.toTimestamp(String.valueOf(map.get(keyVal)), format ));
	                	}
	                	
	                	
	                	/*
	                	if("long".equals(param[0].getType().toString())) {
	                		method.invoke(obj, (long)checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("int".equals(param[0].getType().toString())) {
	                		method.invoke(obj, checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("double".equals(param[0].getType().toString())) {
		                	method.invoke(obj, Double.parseDouble(map.get(keyVal).toString()));
		                }else if("float".equals(param[0].getType().toString())) {
	                		method.invoke(obj, Float.parseFloat(map.get(keyVal).toString()));
	                	}else if ("short" .equals(param[0].getType().toString())) {
	                		method.invoke(obj, (short)checkDoubleFloatValue(map.get(keyVal)));
	                	}else if("byte".equals(param[0].getType().toString())) {
	                		method.invoke(obj, Byte.parseByte(map.get(keyVal).toString()));
	                	}else if("boolean".equals(param[0].getType().toString())) {
	                		method.invoke(obj,Boolean.parseBoolean(map.get(keyVal).toString()));
	                	}else {
	                		method.invoke(obj, map.get(keyVal).toString());		                			
	                	}
	                	*/
	                }catch(Exception e){ 
	                	System.out.println("ERROR:::::::::::::::" + keyVal);
	                	//e.printStackTrace(); 
	                } 
	            }
	        }
	    }
	}
	
	private static int checkDoubleFloatValue(Object val) {
		int returnVal = 0;
		
		if(val instanceof Double ) {
			returnVal = (int)Double.parseDouble(val.toString());
		}else if (val instanceof Float) {
			returnVal =  (int)Float.parseFloat(val.toString());
		}else {
			returnVal = Integer.parseInt(val.toString());
		}
		return returnVal;
	}
	
	/**
	 * List 데이터를  Map[] 으로 바꿔주는 Utill이다.
	 * @param VO
	 * @param map
	 * @return
	 */	
	public static Object[] list2VOs(List<?> list, Class<?> cls) throws Exception{
		Object   ob = cls.newInstance();
		int size    = list.size(); 
		int index   = 0;
		
		Object[] obs =  new Object[size];
		
		for(Object map : list) {
			ob = map2VO( (Map<?,?>)map  , cls);
			obs[index] = ob;
			index++;
		}
		
		return obs;
	}
	
	public static void list2VOList(List<Object> fromList, List<Object> toList) throws Exception{
		Class<?> cls 	= toList.get(0).getClass();
		Object   ob 	= cls.getClass().newInstance();
		
		for(Object map : fromList) {
			ob = map2VO( (Map<?,?>)map  , cls);
			toList.add(ob);
		}
	}
	
	/**
	 * VO 데이터를  Map으로 바꿔주는 Utill이다.
	 * @param VO
	 * @param map
	 * @return
	 */	
	public static Map<?,?> vo2Map(Object vo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<?,?> map = new HashMap<>();
		try{ 			
			if(vo != null) {
				map = BeanUtils.describe(vo);					
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return map;
	}
	
	@SuppressWarnings({ "unchecked" })
	public static String makeQuryString(Object params) throws Exception{
		Map<String, Object> tempMap = null;
		
		Iterator<Map.Entry<String, Object>> itertator 	= null;    
	    if(params instanceof ParamMap){
	    	tempMap = ((ParamMap) params).get();
	    }else if(params instanceof HashMap<?, ?>) {
	    	tempMap = (Map<String, Object>)params;
	    	//itertator = ((HashMap<?,?>)params).keySet().iterator();
	    }
	    
	    itertator = tempMap.entrySet().iterator();
		StringBuffer sb    = new StringBuffer();

		if(itertator.hasNext()) sb.append("?");

		while (itertator.hasNext() ) {
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) itertator.next();
			sb.append(entry.getKey() + "=" + entry.getValue());			
			if (itertator.hasNext()) sb.append("&");
		}

		return sb.toString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getApiResult( Map<?, ?> paramMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, Object> tempMap   = null;
		
		try {
			if(paramMap ==null || paramMap.isEmpty() || paramMap.size() < 1 ) throw new Exception();
			tempMap = (Map<String, Object>) paramMap.get("resultStatus") ;
			
			resultMap.put("status"	, String.valueOf(tempMap.get("status")));
			resultMap.put("code"	, String.valueOf(tempMap.get("code")));
			resultMap.put("message"	, String.valueOf(tempMap.get("message")));
			
			if(resultMap ==null || resultMap.isEmpty() || resultMap.size() < 1 ) throw new Exception();
		}catch (Exception e) {
			resultMap =  new HashMap<String, String>();
			resultMap.put("status"	, "false");
			resultMap.put("code"	, "500");
			resultMap.put("message"	, "No-Data");
			return resultMap;
		}
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getApiData( Map<?, ?> paramMap) {
		Map<String, String> resultMap = null;
		
		try {
			if(paramMap ==null || paramMap.isEmpty() || paramMap.size() < 1 ) throw new Exception();
			resultMap = (Map<String, String>) paramMap.get("data") ;
			if(resultMap ==null || resultMap.isEmpty() || resultMap.size() < 1 ) throw new Exception();
		}catch (Exception e) {
			resultMap =  new HashMap<String, String>();
			return resultMap;
		}
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Object getApiData( Map<?, ?> paramMap, String key) {
		Map<String, Object> resultMap = null;
		Object resultOb 			  = null;
		try {
			if(paramMap ==null || paramMap.isEmpty() || paramMap.size() < 1 ) throw new Exception("NULL-PARAM");
			resultMap = (Map<String, Object>) paramMap.get("data") ;
			if(resultMap ==null || resultMap.isEmpty() || resultMap.size() < 1 ) throw new Exception("NO-DATA");
		
			if(key.equals("result") && resultMap.get("result") instanceof String) {
				return (String)resultMap.get("result");
			}else if (resultMap.get("result") instanceof List) {
				resultOb =  (List<Map<String, Object>>) resultMap.get("result") ;
				return resultOb;
			}
				
			resultMap =  (Map<String, Object>) resultMap.get("result") ;	
			if(resultMap ==null || resultMap.isEmpty() || resultMap.size() < 1 ) throw new Exception("NO-DATA-RESULT");
			
			Iterator<Map.Entry<String, Object>> itertator = resultMap.entrySet().iterator();
			
			while (itertator.hasNext() ) {
				Map.Entry<String,Object> entry = (Map.Entry<String,Object>) itertator.next();
				if(key.equals(entry.getKey())) {
					resultOb = resultMap.get(key);
					break;
				}
			}
			
			if(resultOb == null) throw new Exception("NO-DATA-RESULT" + key);
			
		}catch (Exception e) {
			Object ob = new Object();
			return ob;
		}
		
		return resultOb;
	}
	
	public static List<Map<String, Object>> map2List(Map<String, Object> map) throws Exception{
		return map2List(map, "data", "result");
	}	
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> map2List(Map<String, Object> map , String key ,String key2) throws Exception{
		if(map ==null || map.size() < 1 ) return null;
		
		Map<String, Object> dataMap = (Map<String, Object>) map.get(key);
	
		List<Map<String, Object>> ar = new ArrayList<Map<String, Object>>();
		ar = (List<Map<String, Object>>) dataMap.get(key2);
		return ar;
	}
	
	public static PaGoodsTransLog setGoodsTransLog(String prg_id, String goodsCode, String paCode, String itemNo , Map<String, String> apiResultMap) {
		PaHalfGoods paHalfGoods = new PaHalfGoods();
		paHalfGoods.setGoodsCode(goodsCode);
		paHalfGoods.setPaCode(paCode);
		paHalfGoods.setProductNo(itemNo);
		
		return setGoodsTransLog(prg_id,paHalfGoods , apiResultMap);
	}
	
	public static  Map<String,String> checkResultMap(Map<String,String> apiResultMap , Exception e) {
		String message = "";
			
		if(apiResultMap == null) {
			message = getErrorMessage(e);		
			apiResultMap = new HashMap<String, String>();
			apiResultMap.put("code"		, "500");
			apiResultMap.put("message" 	, message);
		}
			
		return apiResultMap;	
	}
	
	
	public static PaGoodsTransLog setGoodsTransLog(String prg_id, PaHalfGoods paHalfGoods , Map<String, String> apiResultMap) {
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();

		try {
			
			String successYn = "1";
			String productNo = "";
			
			if(!"200".equals(apiResultMap.get("code"))) successYn = "0";	
			
			if(paHalfGoods.getProductNo() != null && !"".equals(paHalfGoods.getProductNo())) productNo = paHalfGoods.getProductNo();
				
			paGoodsTransLog.setGoodsCode	(paHalfGoods.getGoodsCode());
			paGoodsTransLog.setPaCode		(paHalfGoods.getPaCode());
			paGoodsTransLog.setItemNo		(productNo);
			paGoodsTransLog.setSuccessYn	(successYn);
			paGoodsTransLog.setRtnCode		(String.valueOf(apiResultMap.get("code")));
			paGoodsTransLog.setRtnMsg		(prg_id+" || "+apiResultMap.get("message"));
			paGoodsTransLog.setProcId		(Constants.PA_HALF_PROC_ID);
		}catch (Exception e) {
			
		}
		return paGoodsTransLog;	
	}
	
	public static String makeHalfDateFormat(String targetDate ) {
		if(ComUtil.NVL(targetDate).length() == 8 || ComUtil.NVL(targetDate).length() == 14) {
			return targetDate.substring(0, 4) + targetDate.substring(4, 6) +targetDate.substring(6, 8);
		} else {
			return DateUtil.toString(new Date() , DateUtil.GENERAL_DATE_FORMAT); 
		}
	}
		
}
