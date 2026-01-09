package com.cware.netshopping.patmon.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.*;
import java.sql.Timestamp;

import org.apache.commons.beanutils.BeanUtils;

import com.cware.framework.core.dataaccess.util.CamelUtil;
import com.cware.netshopping.common.util.DateUtil;


public class PaTmonComUtill {
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 추후 수정예정 
	
	
	
	//문자열로 된 날짜 형식을 TimeStamp로 변환한다.
	public static Timestamp ltonDate2TimeStamp(String date ) throws Exception{
		String format = DateUtil.getFormatStringWithDate(date);
		return DateUtil.toTimestamp(date,format);
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
	 * @param  Class   -> ex) Map을 PaltonOrderListVO에 넣고 싶으면  PaltonOrderListVO.Class
	 * @return VO
	 */
	public static Object map2VO(Map<? , ?> map, Class<?> cls) throws Exception{ 
	    String keyVal 			= null ; 
	    String setterName 		= null; 
	    Iterator<?> itertator 	= map.keySet().iterator();    
	    Object obj 				= cls.newInstance();
	    
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
                	
	                	//4) 기본 자료형 파싱 #boolean #byte #short #int #long #float #double #char(String)
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
	                	}else if("class java.util.Date".equals(param[0].getType().toString())) {
	                		method.invoke(obj,DateUtil.toTimestamp(map.get(keyVal).toString()));
	                	}else if("interface java.util.Map".equals(param[0].getType().toString())
		                	  || "class java.util.ArrayList".equals(param[0].getType().toString())) {
		                	method.invoke(obj,map.get(keyVal));
		                }else if("class java.sql.Timestamp".equals(param[0].getType().toString())) {
		                	method.invoke(obj,Timestamp.valueOf(map.get(keyVal).toString()));
	                	}else {
	                		method.invoke(obj, map.get(keyVal).toString());	
	                	}
	                	
	                }catch(Exception e){ 
	                	System.out.println("ERROR:::::::::::::::" + keyVal);
	                	//e.printStackTrace(); 
	                } 
	            }
	        }
	    }
	    return obj; 
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
	public static Object[] list2VOs(List<?> list, Class<?> cls) throws Exception{   //TODO Object[] obj 의 CallbyReference 확인 필요
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
	
	//만드는중
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
			map = BeanUtils.describe(vo);	
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return map;
	}
	
	
	@SuppressWarnings("unused")
	private static String getLtonReturnMessgae(String code) {
		
		String rtnMsg;
		
		switch (code) {
		case "0000":
			rtnMsg = "정상 처리되었습니다.";
			break;
		case "2000":
			rtnMsg = "체크공통 잘못된 파라미터(코드값)입니다.";
			break;
		case "2001":
			rtnMsg = "체크공통 데이터(타입,사이즈)오류입니다.";
			break;
		case "2003":
			rtnMsg = "조회기간은 1일을 초과할 수 없습니다.";
			break;		
		case "2004":
			rtnMsg = "잘못된 OpenAPI 서비스 코드 입니다.";
			break;						
		case "2005":
			rtnMsg = "송장개수가 있을 경우, 송장번호는 필수 입니다.";
			break;						
		case "2006":
			rtnMsg = "송장번호가 있을 경우, 송장개수는 필수 입니다";
			break;					
		case "2008":
			rtnMsg = "체크공통 저장할 자료가 없습니다. 송신전문을 확인 하세요.";
			break;		
		case "9000":
			rtnMsg = "시스템공통 예상치 않은 오류가 발생하였습니다.";
			break;		
		case "9001":
			rtnMsg = "시스템공통 데이터베이스에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";
			break;		
		case "9002":
			rtnMsg = "시스템공통 시스템 내부에 오류가 있습니다. 잠시 후 다시 시도해 주세요.";
			break;		
		default:
			rtnMsg = code;
			break;
		}
		
		return rtnMsg;
		
	}
	
	/*

			
	@SuppressWarnings("unchecked")  
	//만들다 만거니 사용하지 말자..
	public static Object paDataMap2Object(Map<String,Object> map, Object obj){ 
		Iterator<?> itertator 	= map.keySet().iterator();
		String		keyVal	    = null;
		Object		tempObj 	= null;
		
		tempObj = map2Object(map, obj);
		
		while(itertator.hasNext()){
			keyVal = (String) itertator.next(); 
			
			if(map.get(keyVal) instanceof ArrayList<?> || map.get(keyVal) instanceof List<?>) {
				List<?> list = new ArrayList<>();
				list = (List<?>) map.get(keyVal);

				for(Object m : list) {
					tempObj = map2Object( (Map<String,Object>) m , obj);
				}
			}
			
		}		
		return tempObj;
	}
	*/
}
