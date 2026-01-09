package com.cware.netshopping.pafaple.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cware.framework.core.dataaccess.util.CamelUtil;

public class PaFapleComUtil {
	
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
}
