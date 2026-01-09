package com.cware.netshopping.paqeen.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cware.framework.core.dataaccess.util.CamelUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.paqeeen.domain.model.Brand;

public class PaQeenComUtil {

	/*
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
	 * @param  Class   -> ex) Map을 PassgOrderListVO에 넣고 싶으면  PassgOrderListVO.Class
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
		                }else if("class java.lang.String".equals(param[0].getType().toString())) {
	                		method.invoke(obj, map.get(keyVal).toString());	
	                	}else{
	                		method.invoke(obj, (Brand)map.get(keyVal));
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
}
