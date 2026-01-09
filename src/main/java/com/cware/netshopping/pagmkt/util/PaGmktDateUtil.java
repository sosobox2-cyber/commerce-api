package com.cware.netshopping.pagmkt.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cware.netshopping.common.util.DateUtil;

public class PaGmktDateUtil {

    /**
    * G마켓 API 날짜 포맷(DATETIME)
    */
    public final static String GMARKET_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Timestamp toTimestamp(String date) throws Exception {
	if (date == null || "".equals(date))
	    return null;
	return new Timestamp(DateUtil.toDate(date, GMARKET_DATETIME_FORMAT).getTime());
    }
    
    
  
    public static String getFormattedDate(Timestamp date) {
        try{
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(GMARKET_DATETIME_FORMAT);
            return formatter.format(new Date(date.getTime()));
        } catch (Exception e) {
            return null;
        }
    }
    
}
