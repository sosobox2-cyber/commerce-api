package com.cware.netshopping.common.util;

import com.cware.framework.core.basic.ParamMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class FlexUtil implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static ParamMap getParamMap(ArrayList arrList) {
        if (arrList == null || arrList.size() == 0) return new ParamMap();
        Iterator i = arrList.iterator();
        ParamMap paramMap = null;
        while (i.hasNext()) {
            paramMap = new ParamMap();
            paramMap.setParamMap((HashMap) i.next());
            paramMap.put("SYSDATETIME", DateUtil.getLocalDateTime(DateUtil.CWARE_JAVA_DATETIME_FORMAT));
        }
        return paramMap;
    }

    public static ParamMap[] getParamMapArray(ArrayList arrList) {
        if (arrList == null || arrList.size() == 0) return new ParamMap[0];
        Collection collection = new ArrayList();
        Iterator i = arrList.iterator();
        ParamMap paramMap = null;
        while (i.hasNext()) {
            paramMap = new ParamMap();
            paramMap.setParamMap((HashMap) i.next());
            paramMap.put("SYSDATETIME", DateUtil.getLocalDateTime(DateUtil.CWARE_JAVA_DATETIME_FORMAT));
            collection.add(paramMap);
        }
        return (ParamMap[]) collection.toArray(new ParamMap[0]);
    }


}
