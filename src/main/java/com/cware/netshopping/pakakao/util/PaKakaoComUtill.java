package com.cware.netshopping.pakakao.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import com.cware.framework.core.dataaccess.util.CamelUtil;
import com.cware.netshopping.common.util.DateUtil;

public class PaKakaoComUtill {
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
	
	//문자열로 된 날짜 형식을 TimeStamp로 변환한다.
	public static Timestamp kakaoDate2TimeStamp(String date ) throws Exception{
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
	 * @param  Class   -> ex) Map을 PakakaoOrderListVO에 넣고 싶으면  PakakaoOrderListVO.Class
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
	                	}else if("class java.sql.Timestamp".equals(param[0].getType().toString())) {
	                		method.invoke(obj,Timestamp.valueOf(map.get(keyVal).toString()));
	                	}else if("interface java.util.Map".equals(param[0].getType().toString())
		                	  || "class java.util.ArrayList".equals(param[0].getType().toString())) {
		                	method.invoke(obj,map.get(keyVal));
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
	 * DB컬럼 Map 데이터를 VO 객체로 바꿔주는 Util이다.
	 * @param  Map
	 * @param  Class   -> ex) Map을 PakakaoOrderListVO에 넣고 싶으면  PakakaoOrderListVO.Class
	 * @return VO
	 */
	public static Object dbMap2VO(Map<? , ?> map, Class<?> cls) throws Exception{ 
	    String keyVal 			= null ; 
	    String setterName 		= null; 
	    Iterator<?> itertator 	= map.keySet().iterator();    
	    Object obj 				= cls.newInstance();
	    String tempArr[]		= null;
	    String chgKeyVal		= "";
	    
	    while(itertator.hasNext()){
	    	chgKeyVal = "";
	    	keyVal = (String) itertator.next();
	    	tempArr = keyVal.toLowerCase().split("_");
	    	for(int i = 0 ; i < tempArr.length ; i++) {
	    		chgKeyVal += tempArr[i].substring(0,1).toUpperCase() + tempArr[i].substring(1);
	    	}
	    	
	        //=1) setter 이름을 가져온다  ex)setOrdNo 
	    	setterName = "set"+chgKeyVal; 
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
	                	}else if("class java.sql.Timestamp".equals(param[0].getType().toString())) {
	                		method.invoke(obj,Timestamp.valueOf(map.get(keyVal).toString()));
	                	}else if("interface java.util.Map".equals(param[0].getType().toString())
		                	  || "class java.util.ArrayList".equals(param[0].getType().toString())) {
		                	method.invoke(obj,map.get(keyVal));
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
	
	/**
	 * HashMap 2개를 합쳐준다 (중복되는 키의 경우 changeKeyName 붙이고 키의 첫글자 대분자로 변환) ex)키:id changeKeyName:order => ordererId
	 * @param map1
	 * @param map2
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> mergeMap(Map<String, Object> map1, HashMap<String, Object> map2, String changeKeyName) throws Exception{
		HashMap<String, Object> cloneMap = null;
   	    
   	    cloneMap = ((HashMap<String, Object>) map2.clone());
   	    
   	    cloneMap.forEach((key2, value2) -> { 
   	    	if(map1.containsKey(key2)) {
   	    		map2.put(changeKeyName+key2.toString().substring(0,1).toUpperCase()+key2.toString().substring(1), map2.remove(key2));
   	    	}
   	    });
   	       	    
   	    map1.putAll(map2); //putAll은 키 중복 시 덮어씌우니 주의
   	    //map2.forEach((key, value) -> map1.merge(key, value, (v1, v2) -> 1));
   	    //Map<String, Object> merged = Stream.of(map1, map2).flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey,  Map.Entry::getValue, (v1, v2) -> v1));   	    
   	       	    
   	    return map1;
	}
		
	
}
