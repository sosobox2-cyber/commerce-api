package com.cware.netshopping.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.cware.framework.common.util.StringUtil;

public class DateUtil {

//	protected DateUtil() {}

    /**
    * Commerceware 날짜 포맷(DATE)
    */
    public final static String CWARE_DATE_FORMAT = "yyyy/MM/dd";

    /**
    * Commerceware 날짜 포맷(DATETIME) - java용 format
    */
    public final static String CWARE_JAVA_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
    * Commerceware 날짜 포맷(DATETIME) - db용 format
    */
    public final static String CWARE_DB_DATETIME_FORMAT = "yyyy/MM/dd HH24:mi:ss";

    /**
    * 기본 날짜 포맷 - java용 format
    */
    public final static String DEFAULT_JAVA_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
    * 기본 날짜 포맷 - db용 format
    */
    public final static String DEFAULT_DB_DATE_FORMAT = "yyyyMMddHH24miss";

    /**
    * 일반 날짜 포맷 (KICC 날짜 포맷)
    */
    public final static String GENERAL_DATE_FORMAT = "yyyyMMdd";

    /**
     * 네이버 질문 등록 기준 조회 날짜 포맷
     */
    public final static String NAVER_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * G마켓 날짜 포맷 
     */
    public final static String GMKT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    /**
     * 쿠팡 날짜 포맷(DATE)
     */
    public final static String COPN_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 쿠팡 날짜 포맷(DATETIME)
     */
    public final static String COPN_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 쿠팡 날짜 포맷(T/DATETIME)
     */
    public final static String COPN_T_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    /**
     * 위메프 날짜 포맷(DATE)
     */
    public final static String WEMP_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 위메프 날짜 포맷(DATETIME)
     */
    public final static String WEMP_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 티몬 날짜 포맷 (T/DATETIME)
     */
    public final static String TMON_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String TMON_DATEMIN_FORMAT = "yyyy-MM-dd'T'HH:mm";
    
    /**
     * 카카오 날짜 포맷(DATETIME)
     */
    public final static String KAKAO_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 카카오 날짜 포맷(DATE)
     */
    public final static String KAKAO_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 티딜 날짜 포맷(DATETIME)
     */
    public final static String TDEAL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String TDEAL_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 패션플러스 날짜 포맷
     */
    public final static String FAPLE_DATE_FORMAT = "yyyy-MM-dd";
    public final static String FAPLE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
    * 기본 TimeZone
    * korea : JST, uk:Europe/London fr:Europe/London
    */
    public final static String DEFAULT_TIMEZONE = "JST";
    
    public static final int TYPE_TO_MILLI_COUNT = 1; // yymmddhhmmssmmmmCOUNT

 	private static final int MAX_COUNT = 999999;

 	private static final int MIN_COUNT = 100001;

 	private static int count = MIN_COUNT;


    /**
    * <PRE>
    * Desc      : 현재일을 기준으로 (요청한 format)를 받아 날을 더한뒤 다시 String date (요청한 format)로 return
    * </PRE>
    */
    public static String addDay(int day, String format) {
        String dateAdd = getFormattedDate(addDay(getDate(getLocalDateTime(format), format), day), format);
        return dateAdd;
    }

    /**
    * <PRE>
    * Desc      : 현재일을 기준으로 (요청한 format)를 받아 달을 더한뒤 다시 String date (요청한 format)로 return
    * </PRE>
    */
    public static String addMonth(int month, String format) {
        String dateAdd = getFormattedDate(addMonth(getDate(getLocalDateTime(format), format), month), format);
        return dateAdd;
    }

    /**
    * <PRE>
    * Desc     : String date (요청한 format)를 받아 날을 더한뒤 다시 String date (요청한 format)로 return
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static String addDay(String date, int day, String format) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        String dateAdd = getFormattedDate(addDay(getDate(date, format), day), format);
        return dateAdd;
    }

     /**
    * <PRE>
    * Desc     :String date (요청한 format)를 받아 달을 더한뒤 다시 String date (요청한 format)로 return
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static String addMonth(String date, int month, String format) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        String dateAdd = getFormattedDate(addMonth(getDate(date, format), month), format);
        return dateAdd;
    }
    
    /**
     * <PRE>
     * Desc     :String date (요청한 format)를 받아 달을 더한뒤 다시 String date (요청한 format)로 return
     * </PRE>
     * @exception IllegalArgumentException date가 null일 경우 발생
     */
     public static String addHour(String date, int month, String format) {
         if (date == null) throw new java.lang.IllegalArgumentException();
         String dateAdd = getFormattedDate(addHour(getDate(date, format), month), format);
         return dateAdd;
     }

    /**
     * <PRE>
     * Desc     : String date (요청한 format)를 받아 분을 더한뒤 다시 String date (요청한 format)로 return
     * </PRE>
     * @exception IllegalArgumentException date가 null일 경우 발생
     */
     public static String addMinute(String date, int minute, String format) {
         if (date == null) throw new java.lang.IllegalArgumentException();
         String dateAdd = getFormattedDate(addMinute(getDate(date, format), minute), format);
         return dateAdd;
     }


    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date와 비교 check
    * </PRE>
    * src_date가 시간상 target_date와 비교 check
    * @param     format : 날짜 포맷
    * @return    src_date = target_date ; 0
    *            src_date < target_date ; -
    *            src_date > target_date ; +
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static int compareTo(String src_date, String target_date, String format) {
        Date src    = getDate(src_date, format);
        Date target = getDate(target_date, format);

        if ((src == null) || (target == null)){
            throw new java.lang.IllegalArgumentException();
        }
        return src.compareTo(target);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date와 비교 check (DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * src_date가 시간상 target_date와 비교 check
    * @param     format : 날짜 포맷
    * @return    src_date = target_date ; 0
    *            src_date < target_date ; -
    *            src_date > target_date ; +
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static int compareTo(String src_date, String target_date){
        return compareTo(src_date, target_date, DEFAULT_JAVA_DATE_FORMAT);
    }



    /**
    * <PRE>
    * Desc     : Date를 timezone과 format에 따른 날짜 String으로 변환해서 리턴
    * </PRE>
    * @param   date : 날짜(String)
    * @return  날짜 String을 리턴, 변환도중 error발생시 null을 리턴
    */
    public static String getYearMD(String date) {
        try{
            if (date.equals("") || date.equals(" ") || date.equals("null")  || date.equals(null)) {
                return " ";
            }
            java.util.TimeZone homeTz = java.util.TimeZone.getTimeZone(DEFAULT_TIMEZONE);
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy년 MM월 dd일");
            formatter.setTimeZone(homeTz);
            return formatter.format(getDate(date,"yyyyMMdd"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * <PRE>
    * Desc     : Date를 timezone과 format에 따른 날짜 String으로 변환해서 리턴
    * </PRE>
    * @param   date : 날짜(String)
    * @return  날짜 String을 리턴, 변환도중 error발생시 null을 리턴
    */
    public static String getYearMD(String date, String format) {
        try{
            String from = DEFAULT_JAVA_DATE_FORMAT.substring(0, date.length());
            if (date.equals("") || date.equals(" ") || date.equals("null")  || date.equals(null)) {
                return " ";
            }
            java.util.TimeZone homeTz = java.util.TimeZone.getTimeZone(DEFAULT_TIMEZONE);
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
            formatter.setTimeZone(homeTz);
            return formatter.format(getDate(date, from));
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * <PRE>
    * Desc     : 일자에 요일 상수값 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static int getDayNumber(String date) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(getDate(date, "yyyy/MM/dd"));
        int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
        return dayNumber;
    }

    /**
    * <PRE>
    * Desc     : 월에 마지막 일 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static int getLastDay(String date) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(getDate(date, "yyyy/MM/dd"));
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    /**
    * <PRE>
    * Desc     : Date를 timezone과 format에 따른 날짜 String으로 변환해서 리턴
    * </PRE>
    * @param   date     : 날짜
    * @param   format   : 날짜 포맷 (예) yyyyMMddHHmmss
    * @param   timezone : Timezone (예) JST
    * @return  날짜 String을 리턴, 변환도중 error발생시 null을 리턴
    */
    public static String getFormattedDate(Date date, String format, String timezone) {
        try{
            java.util.TimeZone homeTz = java.util.TimeZone.getTimeZone(timezone);
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
            formatter.setTimeZone(homeTz);
            return formatter.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * <PRE>
    * Desc     : Date를 format에 따른 날짜 String으로 변환해서 리턴 (DEFAULT_TIMEZONE을 사용)
    * </PRE>
    * @param   date   : 날짜
    * @param   format : 날짜 포맷 (예) yyyyMMddHHmmss
    * @return  날짜 String을 리턴, 변환도중 error발생시 null을 리턴
    */
    public static String getFormattedDate(Date date, String format) {
        return getFormattedDate(date, format, DEFAULT_TIMEZONE);
    }

    /**
    * <PRE>
    * Desc     : Date를 날짜 String으로 변환해서 리턴 (DEFAULT_TIMEZONE과 DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * @param   date : 날짜
    * @return  날짜 String을 리턴, 변환도중 error발생시 null을 리턴
    */
    public static String getFormattedDate(Date date) {
        return getFormattedDate(date, DEFAULT_JAVA_DATE_FORMAT);
    }


    /**
    * 날짜 String을 Date로 변환해서 리턴한다.
    * @param   date   : 날짜 String
    * @param   format : 날짜 포맷
    * @return  날짜 String을 Date로 변환해서 리턴, 변환도중 error발생시 null을 리턴
    */
    public static Date getDate(String date, String format) {
        if ((date == null) || (format == null)) return null;
        try{
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
            return formatter.parse(date, new java.text.ParsePosition(0));
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * <PRE>
    * Desc     : 날짜 String을 Date로 변환해서 리턴 (DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * @return  날짜 String을 Date로 변환해서 리턴, 변환도중 error발생시 null을 리턴
    */
    public static Date getDate(String date) {
        return getDate(date, DEFAULT_JAVA_DATE_FORMAT);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date 이전인지를 check
    * </PRE>
    * @param     format : 날짜 포맷
    * @return    src_date가 target_date이전이면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean before(String src_date, String target_date, String sformat) {
        Date src    = getDate(src_date, sformat);
        Date target = getDate(target_date, sformat);

        if ((src == null) || (target == null)) {
            throw new java.lang.IllegalArgumentException();
        }

        return src.before(target);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date 이전인지를 check (DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * @return    src_date가 target_date이전이면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean before(String src_date, String target_date) {
        return before(src_date, target_date, DEFAULT_JAVA_DATE_FORMAT);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date 이후인지를 check
    * </PRE>
    * @param     format : 날짜 포맷
    * @return    src_date가 target_date이후이면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean after(String src_date, String target_date, String format){
        Date src    = getDate(src_date, format);
        Date target = getDate(target_date, format);

        if ((src == null) || (target == null)){
            throw new java.lang.IllegalArgumentException();
        }

        return src.after(target);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date 이후인지를 check (DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * @return    src_date가 target_date이후이면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean after(String src_date, String target_date){
        return after(src_date, target_date, DEFAULT_JAVA_DATE_FORMAT);
    }

    /**
    * src_date가 시간상 target_date와 같은지 check
    * @param     format : 날짜 포맷
    * @return    src_date가 target_date와 같으면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean equals(String src_date, String target_date, String format) {
        Date src    = getDate(src_date, format);
        Date target = getDate(target_date, format);

        if ((src == null) || (target == null)){
            throw new java.lang.IllegalArgumentException();
        }

        return (src.compareTo(target) == 0) ? true:false;
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 target_date와 같은지 check
    * </PRE>
    * @return  src_date가 target_date와 같으면 true, 그렇지 않으면 false를 리턴
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean equals(String src_date, String target_date) {
        return equals(src_date, target_date, DEFAULT_JAVA_DATE_FORMAT);
    }





    /**
    * <PRE>
    * Desc     : start_date와 end_date 사이의 날짜 간격을 리턴
    * </PRE>
    * @param     format : 날짜 포맷
    * @exception IllegalArgumentException Parameter가 null이거나 날짜 포맷과 맞지 않는 경우 발생
    */
    public static int difference(String start_date, String end_date, String format){
        Date start = getDate(start_date, format);
        Date end   = getDate(end_date, format);

        if ((start == null) || (end == null)) {
            throw new java.lang.IllegalArgumentException();
        }

        long lStart = (long) (start.getTime()/(1000*60*60*24));
        long lEnd = (long) (end.getTime()/(1000*60*60*24));

        return (int) Math.abs(lStart - lEnd);
    }

    /**
    * <PRE>
    * Desc     : start_date와 end_date 사이의 날짜 간격을 리턴 (DEFAULT_JAVA_DATE_FORMAT 사용)
    * </PRE>
    * @exception IllegalArgumentException Parameter가 null이거나 날짜 포맷과 맞지 않는 경우 발생
    */
    public static int difference(String start_date, String end_date){
        return difference(start_date, end_date, DEFAULT_JAVA_DATE_FORMAT);
    }


    /**
    * <PRE>
    * Desc     : src_date가 시간상 start_date와 end_date 사이에 있는지 check
    * </PRE>
    * @param     format : 날짜 포맷
    * @return    true if start_data <= src_date <= end_date, otherwise false
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생
    */
    public static boolean between(String src_date, String start_date, String end_date, String format) {
        Date src   = getDate(src_date, format);
        Date start = getDate(start_date, format);
        Date end   = getDate(end_date, format);

        if ((src == null) || (start == null) || (end == null)) {
            throw new java.lang.IllegalArgumentException();
        }
        return ((src.compareTo(start) < 0) || (src.compareTo(end) > 0)) ? false:true;
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 start_date와 end_date 사이에 있는지 check (DEFAULT_JAVA_DATE_FORMAT을 사용)
    * </PRE>
    * @return    true if start_data <= src_date <= end_date, otherwise false
    * @exception IllegalArgumentException 날짜 String이 포맷과 맞지 않을 경우 발생한다.
    */
    public static boolean between(String src_date, String start_date, String end_date){
        return between(src_date, start_date, end_date, DEFAULT_JAVA_DATE_FORMAT);
    }

    /**
    * <PRE>
    * Desc     : src_date가 시간상 start_date와 end_date 사이에 있는지 check
    * </PRE>
    * @return    true if start_data <= src_date <= end_date, otherwise false
    * @exception IllegalArgumentException Parameter가 null이거나 날짜 포맷과 맞지 않는 경우 발생
    */
    public static boolean between(Date src_date, Date start_date, Date end_date) {
        if ((src_date == null) || (start_date == null) || (end_date == null)) {
            throw new java.lang.IllegalArgumentException();
        }
        return ((src_date.compareTo(start_date) < 0) || (src_date.compareTo(end_date) > 0)) ? false:true;
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n 분을 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null이면 발생
    */
    public static Date addMinute(Date date, int minute) {
        if (date == null) throw new java.lang.IllegalArgumentException();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.MINUTE , minute);
        return cal.getTime();
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n 시간을 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null이면 발생
    */
    public static Date addHour(Date date, int hour) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.HOUR , hour);
        return cal.getTime();
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n날을 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null이면 발생
    */
    public static Date addDay(Date date, int day) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR , day);
        return cal.getTime();
    }
    /**
     * <PRE>
     * Desc     : date를 기준으로 n날을 더한 날짜를 리턴
     * </PRE>
     * @exception IllegalArgumentException date가 null이면 발생
     */
     public static Timestamp addDay(Timestamp date, int day) {
         if (date == null) throw new java.lang.IllegalArgumentException();
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
         cal.setTime(date);
         cal.add(Calendar.DAY_OF_YEAR , day);
         return new Timestamp( cal.getTimeInMillis());
     }

    /**
    * <PRE>
    * Desc     : String date (yyyy/MM/dd)를 받아 날을 더한뒤 다시 String date (yyyy/MM/dd)로 return
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static String addDay(String date, int day) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        String dateAdd = getFormattedDate(addDay(getDate(date, "yyyy/MM/dd"), day), "yyyy/MM/dd");
        return dateAdd;
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n주를 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static Date addWeek(Date date, int week) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, week);
        return cal.getTime();
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n달을 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static Date addMonth(Date date, int month) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

     /**
    * <PRE>
    * Desc     : String date (yyyy/MM/dd)를 받아 달을 더한뒤 다시 String date (yyyy/MM/dd)로 return
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static String addMonth(String date, int month) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        String dateAdd = getFormattedDate(addMonth(getDate(date, "yyyy/MM/dd"), month), "yyyy/MM/dd");
        return dateAdd;
    }

    /**
    * <PRE>
    * Desc     : date를 기준으로 n년을 더한 날짜를 리턴
    * </PRE>
    * @exception IllegalArgumentException date가 null일 경우 발생
    */
    public static Date addYear(Date date, int year) {
        if (date == null) throw new java.lang.IllegalArgumentException();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
    * <PRE>
    * 현재(System TimeZone 및 Locale 기준 ) 날짜/시간정보를 얻는다.
    * </PRE>
    * @return   String "yyyyMMddHHmmss" 형태의 스트링을 반환한다.
    */
    public static String getLocalDateTime() {
        Calendar calLocal = Calendar.getInstance();
        return "" + calLocal.get(Calendar.YEAR)
                    + makeTowDigit(calLocal.get(Calendar.MONTH) + 1)
                    + makeTowDigit(calLocal.get(Calendar.DATE))
                    + makeTowDigit(calLocal.get(Calendar.HOUR_OF_DAY))
                    + makeTowDigit(calLocal.get(Calendar.MINUTE))
                    + makeTowDigit(calLocal.get(Calendar.SECOND));
    }

    /**
    * <PRE>
    * 현재(한국기준) 시간정보를 얻는다.
    * 	(ex1) format string "yyyyMMddhh" : return value is 1998121011 (0~23 hour type)
    * 	(ex2) format string "yyyyMMddHHmmss" : return value is 19990114232121
    * </PRE>
    * @param    format      time format
    * @return   formatted current korean time
    */
    public static String getLocalDateTime(String format) {
        SimpleDateFormat fmt= new SimpleDateFormat(format);
        long time = System.currentTimeMillis();
        String strTime = fmt.format(new java.util.Date(time));
        return strTime;
    }

    /**
    * <PRE>
    * 숫자를 문자열로 변환하는데, 2자리수 미민이면 두자리수로 맞춘다.
    * </PRE>
    * @return   String "00" 형태의 스트링을 반환한다.
    */
    protected static String makeTowDigit(int num){
        return (num < 10 ? "0" : "") + num;
    }

    /**
    * FunctionName    : getCurrentLongTime
    * @param          : none
    * Description     : current time format = YYYYMMDDHHMMSS
    * @return         : time String
    * @exception      : E2321
    **/
    public static String getCurrentLongTime() {
        String strCurDate = new String();
        strCurDate = getLocalDateTime();
        return strCurDate;
    }

	/**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어진 기간만큼 추가한 날을 계산하여 문자열로 리턴한다.
     * </p>
     *
     * <pre>
     * String result=DateHelper.getCalcDateAsString("2004","10","30",2,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 "20041101"의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return "년+월+일"
     */
    public static String getCalcDateAsString(String sYearPara, String sMonthPara,
                                             String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return getFormalYear(cd) + getFormalMonth(cd) + getFormalDay(cd);
    }


    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어진 기간만큼 추가한 날을 계산하여 년을 리턴한다.
     * </p>
     *
     * <pre>
     * String result=DateHelper.getCalcYearAsString("2004","12","30",2,"day");
     * <pre>
     *
     * <p>
     * <code>result</code>는 "2005"의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 년(年)
     */
    public static String getCalcYearAsString(String sYearPara, String sMonthPara,
                                             String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }
        return getFormalYear(cd);
    }

    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어진 기간만큼 추가한 날을 계산하여 월을 리턴한다.
     * </p>
     *
     * <pre>
     * String result=DateHelper.getCalcMonthAsString("2004","12","30",2,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 "01"의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 월(月)
     */
    public static String getCalcMonthAsString(String sYearPara, String sMonthPara,
                                              String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return getFormalMonth(cd);
    }

    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어진 기간만큼 추가한 날을 계산하여 일을 리턴한다.
     * </p>
     *
     * <pre>
     * String result=DateHelper.getCalcDayAsString("2004","12","30",3,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 "02"의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 일(日)
     */
    public static String getCalcDayAsString(String sYearPara, String sMonthPara,
                                            String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return getFormalDay(cd);
    }


    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어진 기간만큼 추가한 날을 계산하여 년을 리턴한다.
     * </p>
     *
     * <pre>
     * int result=DateHelper.getCalcYearAsInt("2004","12","30",3,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 2005의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 년(年)
     */
    public static int getCalcYearAsInt(String sYearPara, String sMonthPara,
                                       String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return cd.get(Calendar.YEAR);
    }

    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어일 기간만큼 추가한 날을 계산하여 월을 리턴한다.
     * </p>
     *
     * <pre>
     * int result=DateHelper.getCalcMonthAsInt("2004","12","30",3,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 1의 값을 갖는다.
     * </p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 월(月)
     */
    public static int getCalcMonthAsInt(String sYearPara, String sMonthPara,
                                        String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * <p>
     * 특정 날짜를 인자로 받아 그 일자로부터 주어일 기간만큼 추가한 날을 계산하여 일을 리턴한다.
     * </p>
     *
     * <pre>
     * int result=DateHelper.getCalcDayAsInt("2004","12","30",3,"day");
     * </pre>
     *
     * <p>
     * <code>result</code>는 2의 값을 갖는다.
     * <p>
     *
     * @param sYearPara  년도
     * @param sMonthPara 월
     * @param sDayPara   일
     * @param iTerm      기간
     * @param sGuBun     구분("day":일에 기간을 더함,"month":월에 기간을 더함,"year":년에 기간을 더함.)
     * @return 일(日)
     */
    public static int getCalcDayAsInt(String sYearPara, String sMonthPara,
                                      String sDayPara, int iTerm, String sGuBun) {

        Calendar cd = new GregorianCalendar(Integer.parseInt(sYearPara),
                Integer.parseInt(sMonthPara) - 1,
                Integer.parseInt(sDayPara));

        if (StringUtil.equals(sGuBun, "day")) {
            cd.add(Calendar.DATE, iTerm);
        } else if (StringUtil.equals(sGuBun, "month")) {
            cd.add(Calendar.MONTH, iTerm);
        } else if (StringUtil.equals(sGuBun, "year")) {
            cd.add(Calendar.YEAR, iTerm);
        }

        return cd.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * <p>
     * 현재 연도값을 리턴
     * </p>
     *
     * @return 년(年)
     */
    public static int getCurrentYearAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.YEAR);
    }

    /**
     * <p>
     * 현재 월을 리턴
     * </p>
     *
     * @return 월(月)
     */
    public static int getCurrentMonthAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * <p>
     * 현재 일을 리턴
     * </p>
     *
     * @return 일(日)
     */
    public static int getCurrentDayAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * <p>
     * 현재 시간을 리턴
     * </p>
     *
     * @return 시(時)
     */
    public static int getCurrentHourAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * <p>
     * 현재 분을 리턴
     * </p>
     *
     * @return 분(分)
     */
    public static int getCurrentMinuteAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.MINUTE);
    }

    /**
     * <p>
     * 현재 초를 리턴
     * </p>
     *
     * @return 밀리초
     */
    public static int getCurrentMilliSecondAsInt() {

        Calendar cd = new GregorianCalendar();

        return cd.get(Calendar.MILLISECOND);
    }

    /**
     * <p>
     * 현재 년도를 YYYY 형태로 리턴
     * </p>
     *
     * @return 년도(YYYY)
     */
    public static String getCurrentYearAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalYear(cd);
    }

    /**
     * <P>
     * 현재 월을 MM 형태로 리턴
     * </p>
     *
     * @return 월(MM)
     */
    public static String getCurrentMonthAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalMonth(cd);
    }

    /**
     * <p>
     * 현재 일을 DD 형태로 리턴
     * </p>
     *
     * @return 일(DD)
     */
    public static String getCurrentDayAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalDay(cd);
    }

    /**
     * <p>
     * 현재 시간을 HH 형태로 리턴
     * </p>
     *
     * @return 시간(HH)
     */
    public static String getCurrentHourAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalHour(cd);
    }

    /**
     * <p>
     * 현재 분을 mm 형태로 리턴
     * </p>
     *
     * @return 분(mm)
     */
    public static String getCurrentMinuteAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalMin(cd);
    }

    /**
     * <p>
     * 현재 초를 ss 형태로 리턴
     * </p>
     *
     * @return 초(ss)
     */
    public static String getCurrentSecondAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalSec(cd);
    }

    /**
     * <p>
     * 현재 밀리초를 sss 형태로 리턴
     * </p>
     *
     * @return 밀리초(sss)
     */
    public static String getCurrentMilliSecondAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalMSec(cd);
    }

    /**
     * <p>
     * 현재 날짜를 년월일을 합쳐서 String으로 리턴하는 메소드
     * </p>
     *
     * @return 년+월+일 값
     */
    public static String getCurrentDateAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalYear(cd) + getFormalMonth(cd) + getFormalDay(cd);
    }
    /**
     * <p>
     * 현재 날짜를 년월일을 합쳐서 String으로 리턴하는 메소드
     * </p>
     *
     * @return 년+월+일 값
     */
    public static String getCurrentNaverDateAsString() {
    	
    	Calendar cd = new GregorianCalendar();
    	
    	return getFormalYear(cd) + "-" + getFormalMonth(cd) + "-" + getFormalDay(cd);
    }
    
    /**
     * <p>
     * 현재 날짜를 년월일을 합쳐서 String으로 리턴하는 메소드
     * </p>
     *
     * @return 년-월-일
     */
    public static String getCurrentDateWithHyphen(String date) {
    	
    	if(null != date && date.length() == 8 && date.matches("\\d{8}")) {
    		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    	} else {
    		Calendar cd = new GregorianCalendar();
    		return getFormalYear(cd) + "-" + getFormalMonth(cd) + "-" + getFormalDay(cd);
    	}
    }

    /**
     * <p>
     * 현재 시간을 시분초를 합쳐서 String으로 리턴하는 메소드
     * </p>
     *
     * @return 시+분+초 값
     */
    public static String getCurrentTimeAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalHour(cd) + getFormalMin(cd) + getFormalSec(cd);
    }

    /**
     * <p>
     * 현재 날짜와 시간을 년월일시분초를 합쳐서 String으로 리턴하는 메소드
     * </p>
     *
     * @return 년+월+일+시+분+초 값
     */
    public static String getCurrentDateTimeAsString() {

        Calendar cd = new GregorianCalendar();

        return getFormalYear(cd) + getFormalMonth(cd) + getFormalDay(cd) + getFormalHour(cd) + getFormalMin(cd) + getFormalSec(cd);
    }

//    /**
//     * <p>
//     * 해당 년,월,일을 받아 요일을 리턴하는 메소드
//     * </p>
//     *
//     * @param sYear  년도
//     * @param sMonth 월
//     * @param sDay   일
//     * @return 요일(한글)
//     */
//    public static String getDayOfWeekAsString(String sYear, String sMonth, String sDay) {
//
//        Calendar cd = new GregorianCalendar(Integer.parseInt(sYear),
//                Integer.parseInt(sMonth) - 1,
//                Integer.parseInt(sDay));
//
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE"); // "EEE" - Day in Week
//
//        Date d1 = cd.getTime();
//
//        return sdf.format(d1);
//    }


    /**
     * <p>
     * 해당 대상자에 대해 기준일자에서의 만 나이를 구한다.
     * </p>
     *
     * <pre>
     * int age = DateHelper.getFullAge("7701011234567","20041021");
     * </pre>
     *
     * <p>
     * <code>age</code>는 27의 값을 갖는다.
     * </p>
     *
     * @param socialNo 주민번호 13자리
     * @param keyDate  기준일자 8자리
     * @return 만 나이
     */
    public static int getFullAge(String socialNo, String keyDate) {

        String birthDate = null;

        // 주민번호 7번째 자리가 0 또는 9 라면 1800년도 출생이다.
        if (StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "0") ||
                StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "9")) {
            birthDate = "18" + socialNo.substring(0, 6);
        }

        // 주민번호 7번째 자리가 1 또는 2 라면 1900년도 출생이다.
        else if (StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "1") ||
                StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "2")) {
            birthDate = "19" + socialNo.substring(0, 6);
        }

        // 주민번호 7번째 자리가 3 또는 4 라면 2000년도 출생이다.
        else if (StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "3") ||
                StringUtil.equals(StringUtil.toSubString(socialNo, 6, 7), "4")) {
            birthDate = "20" + socialNo.substring(0, 6);
        }

        //생일이 안지났을때 기준일자 년에서 생일년을 빼고 1년을 더뺀다.
        if (Integer.parseInt(keyDate.substring(4, 8)) <
                Integer.parseInt(birthDate.substring(4, 8))) {

            return Integer.parseInt(keyDate.substring(0, 4)) -
                    Integer.parseInt(birthDate.substring(0, 4)) - 1;
        }

        //생일이 지났을때 기준일자 년에서 생일년을 뺀다.
        else {

            return Integer.parseInt(keyDate.substring(0, 4)) -
                    Integer.parseInt(birthDate.substring(0, 4));
        }
    }

    /**
     * <p>
     * 주민번호를 넘겨 현재 시점의 만 나이를 구한다.
     * </p>
     *
     * @param socialNo 주민번호 6자리
     * @return 만 나이
     */
    public static int getCurrentFullAge(String socialNo) {

        //현재일자를 구한다.
        String sCurrentDate = getCurrentYearAsString() + getCurrentMonthAsString() +
                getCurrentDayAsString();

        return getFullAge(socialNo, sCurrentDate);
    }

    /**
     * <p>
     * 해당 년의 특정월의 일자를 구한다.
     * </p>
     *
     * @param year  년도4자리
     * @param month 월 1자리 또는 2자리
     * @return 특정월의 일자
     */
    public static int getDayCountForMonth(int year, int month) {

        int[] DOMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};   // 평년
        int[] lDOMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};  // 윤년

        if ((year % 4) == 0) {

            if ((year % 100) == 0 && (year % 400) != 0) {
                return DOMonth[month - 1];
            }

            return lDOMonth[month - 1];

        } else {
            return DOMonth[month - 1];
        }
    }

    //****** 시작일자와 종료일자 사이의 일자를 구하는 메소드군 *******//

    /**
     * <p>
     * 8자리로된(yyyyMMdd) 시작일자와 종료일자 사이의 일수를 구함.
     * </p>
     *
     * @param from 8자리로된(yyyyMMdd)시작일자
     * @param to   8자리로된(yyyyMMdd)종료일자
     * @return 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일수 리턴
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     */
    public static int getDayCount(String from, String to) throws ParseException {

        return getDayCountWithFormatter(from, to, "yyyyMMdd");
    }

    /**
     * <p>
     * 해당 문자열이 "yyyyMMdd" 형식에 합당한지 여부를 판단하여 합당하면 Date 객체를 리턴한다.
     * </p>
     *
     * @param source 대상 문자열
     * @return "yyyyMMdd" 형식에 맞는 Date 객체를 리턴한다.
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     */
    public static Date dateFormatCheck(String source) throws ParseException {

        return dateFormatCheck(source, "yyyyMMdd");
    }

    /**
     * <p>
     * 해당 문자열이 주어진 일자 형식을 준수하는지 여부를 검사한다.
     * </p>
     *
     * @param source 검사할 대상 문자열
     * @param format Date 형식의 표현. 예) "yyyy-MM-dd".
     * @return 형식에 합당하는 경우 Date 객체를 리턴한다.
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     */
    public static Date dateFormatCheck(String source, String format) throws ParseException {

        if (source == null) {
            throw new ParseException("date string to check is null", 0);
        }

        if (format == null) {
            throw new ParseException("format string to check date is null", 0);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date date = null;

        try {
            date = formatter.parse(source);
        } catch (ParseException e) {
            throw new ParseException(" wrong date:\"" + source +
                    "\" with format \"" + format + "\"", 0);
        }

        if (!formatter.format(date).equals(source)) {
            throw new ParseException("Out of bound date:\"" + source +
                    "\" with format \"" + format + "\"", 0);
        }

        return date;
    }

    /**
     * <p>
     * 정해진 일자 형식을 기반으로 시작일자와 종료일자 사이의 일자를 구한다.
     * <p/>
     *
     * @param from 시작 일자
     * @param to   종료 일자
     * @return 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일수를 리턴
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     * @see #getTimeCount(String, String, String)
     */
    public static int getDayCountWithFormatter(String from, String to, String format) throws ParseException {

        long duration = getTimeCount(from, to, format);

        return (int) (duration / (1000 * 60 * 60 * 24));
    }

    /**
     * <p>
     * DATE 문자열을 이용한 format string을 생성
     * </p>
     *
     * @param date Date 문자열
     * @return Java.text.DateFormat 부분의 정규 표현 문자열
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     */
    public static String getFormatStringWithDate(String date) throws ParseException {
        String format = null;

        if (date.length() == 4) {
            format = "HHmm";
        } else if (date.length() == 8) {
            format = "yyyyMMdd";
        } else if (date.length() == 12) {
            format = "yyyyMMddHHmm";
        } else if (date.length() == 14) {
            format = "yyyyMMddHHmmss";
        } else if (date.length() == 17) {
            format = "yyyyMMddHHmmssSSS";
        } else {
            throw new ParseException(" wrong date format!:\"" + format + "\"", 0);
        }

        return format;
    }

    /**
     * <p>
     * <code>yyyyMMddHHmmssSSS</code> 와 같은 Format 문자열 없이 시작 일자 시간, 끝 일자 시간을
     * </p>
     *
     * @param from 시작일자
     * @param to   끝일자
     * @return 두 일자 간의 차의 밀리초(long)값
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     * @see #getFormatStringWithDate(String)
     */
    public static long getTimeCount(String from, String to) throws ParseException {

        String format = getFormatStringWithDate(from);

        return getTimeCount(from, to, format);
    }

    /**
     * <p>
     * 정해진 일자 형식을 기반으로 시작일자와 종료일자 사이의 일자를 구한다.
     * <p/>
     *
     * <pre>
     * Symbol   Meaning                 Presentation        Example
     * ------   -------                 ------------        -------
     * G        era designator          (Text)              AD
     * y        year                    (Number)            1996
     * M        month in year           (Text & Number)     July & 07
     * d        day in month            (Number)            10
     * h        hour in am/pm (1~12)    w(Number)            12
     * H        hour in day (0~23)      (Number)            0
     * m        minute in hour          (Number)            30
     * s        second in minute        (Number)            55
     * S        millisecond             (Number)            978
     * E        day in week             (Text)              Tuesday
     * D        day in year             (Number)            189
     * F        day of week in month    (Number)            2 (2nd Wed in July)
     * w        week in year            (Number)            27
     * W        week in month           (Number)            2
     * a        am/pm marker            (Text)              PM
     * k        hour in day (1~24)      (Number)            24
     * K        hour in am/pm (0~11)    (Number)            0
     * z        time zone               (Text)              Pacific Standard Time
     * '        escape for text         (Delimiter)
     * ''       single quote            (Literal)           '
     * </pre>
     *
     * @param from   시작 일자
     * @param to     종료 일자
     * @param format
     * @return 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일수를 리턴
     * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜인 경우 발생함
     */
    public static long getTimeCount(String from, String to, String format) throws ParseException {

        Date d1 = dateFormatCheck(from, format);
        Date d2 = dateFormatCheck(to, format);

        long duration = d2.getTime() - d1.getTime();

        return duration;
    }

    /**
     * <p>
     * 시작일자와 종료일자 사이의 해당 요일이 몇번 있는지 계산한다.
     * </p>
     *
     * @param from 시작 일자
     * @param to   종료 일자
     * @param yoil 요일
     * @return 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일자 리턴
     * @throws ParseException 발생. 형식이 잘못 되었거나 존재하지 않는 날짜
     */
    public static int getDayOfWeekCount(String from, String to, String yoil) throws ParseException {

        int first = 0; // from 날짜로 부터 며칠 후에 해당 요일인지에 대한 변수
        int count = 0; // 해당 요일이 반복된 횟수
        String[] sYoil = {"일", "월", "화", "수", "목", "금", "토"};

        // 두 일자 사이의 날 수
        int betweenDays = getDayCount(from, to);

        // 첫번째 일자에 대한 요일
        Calendar cd = new GregorianCalendar(Integer.parseInt(from.substring(0, 4)),
                Integer.parseInt(from.substring(4, 6)) - 1,
                Integer.parseInt(from.substring(6, 8)));
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);

        // 요일이 3자리이면 첫자리만 취한다.
        if (yoil.length() == 3) {
            yoil = yoil.substring(0, 1);

            // 첫번째 해당 요일을 찾는다.
        }

        // bug fixed 2009.03.23
        //while (!sYoil[dayOfWeek - 1].equals(yoil)) {
        while (!sYoil[(dayOfWeek - 1) % 7].equals(yoil)) {
            dayOfWeek += 1;
            first++;
        }

        if ((betweenDays - first) < 0) {

            return 0;

        } else {

            count++;

        }
        count += (betweenDays - first) / 7;

        return count;
    }


    /**
     * <p>
     * 년도 표시를 네자리로 형식화 한다.
     * </p>
     *
     * @param cd 년도를 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 네자리로 형식화된 년도
     */
    private static String getFormalYear(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.YEAR)), 4, "0");
    }

    /**
     * <p>
     * 월(Month) 표시를 두자리로 형식화 한다.
     * </p>
     *
     * @param cd 월을 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 두자리로 형식화된 월
     */
    private static String getFormalMonth(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.MONTH) + 1), 2, "0");
    }

    /**
     * <p>
     * 일(Day) 표시를 두자리로 형식화 한다.
     * </p>
     *
     * @param cd 일자를 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 두자리로 형식화된 일
     */
    private static String getFormalDay(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.DAY_OF_MONTH)), 2, "0");
    }

    /**
     * <p>
     * 시간(Hour) 표시를 두자리로 형식화 한다.
     * </p>
     *
     * @param cd 시간을 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 두자리로 형식화된 시간
     */
    private static String getFormalHour(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.HOUR_OF_DAY)), 2, "0");

    }

    /**
     * <p>
     * 분(Minute) 표시를 두자리로 형식화 한다.
     * </p>
     *
     * @param cd 분을 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 두자리로 형식화된 분
     */
    private static String getFormalMin(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.MINUTE)), 2, "0");

    }

    /**
     * <p>
     * 초(sec) 표시를 두자리로 형식화 한다.
     * </p>
     *
     * @param cd 초를 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 두자리로 형식화된 초
     */
    private static String getFormalSec(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.SECOND)), 2, "0");

    }

    /**
     * <p>
     * 밀리초(millisec) 표시를 세자리로 형식화 한다.
     * </p>
     *
     * @param cd 밀리초를 포함하는 <strong>Calendar</strong> 오브젝트
     * @return 세자리로 형식화된 밀리초
     */
    private static String getFormalMSec(Calendar cd) {
        return StringUtil.lPad(Integer.toString(cd.get(Calendar.MILLISECOND)), 3, "0");

    }

    /**
     * <p>
     * Date -> String
     * </p>
     *
     * @param date Date which you want to change.
     * @return String The Date string. Type,  yyyyMMdd HH:mm:ss.
     */
    public static String toString(Date date, String format) {

        if (StringUtil.isNull(format)) {
            format = DEFAULT_JAVA_DATE_FORMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String tmp = sdf.format(date);

        return tmp;
    }

    /**
     * <p>
     * String -> Date
     * fromat : 일자 => yyyy/MM/dd (default)
     *          일시 => yyyy/MM/dd HH:mm:ss
     * </p>
     *
     * @param date 문자열, 문자열의 date format
     * @return date
     */
    public static Date toDate(String date, String format) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date toDate(String date) throws Exception {
       return toDate(date, CWARE_DATE_FORMAT);
    }

	public static Timestamp toTime(String date) throws Exception {
		if (date == null || "".equals(date))
			return null;
		return new Timestamp(toDate(date, CWARE_DATE_FORMAT).getTime());
	}

	public static Timestamp toTimestamp(String date) throws Exception {
		if (date == null || "".equals(date))
			return null;
		return new Timestamp(toDate(date, CWARE_JAVA_DATETIME_FORMAT).getTime());
	}

	public static Timestamp toTimestamp(String date, String format) throws Exception {
		if (date == null || "".equals(date))
			return null;
		if (format == null)
			format = CWARE_JAVA_DATETIME_FORMAT;
		return new Timestamp(toDate(date, format).getTime());
	}

	public static String timestampToString(Timestamp date) throws Exception {
		return timestampToString(date, CWARE_JAVA_DATETIME_FORMAT);
	}

	public static String timestampToString(Timestamp date, String format) throws Exception {
		if (date == null)
			return null;
		if (format == null)
			format = CWARE_JAVA_DATETIME_FORMAT;

		return getFormattedDate(new Date(date.getTime()), format);
	}

	public static String formatConvertStringToGmarketDate(String sysdate, int term) throws Exception{
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    SimpleDateFormat formatToGmarket = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    
	    Date date = format.parse(sysdate);		    
	    Calendar cal = Calendar.getInstance();	    
	    
	    cal.setTime(date);
	    cal.add(Calendar.DATE, term);	    
	    return formatToGmarket.format(cal.getTime());
	}
	
	/**
	 * 현재시간을 "yyyy-MM-dd HH:mm:ss" Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(str);
	 * </pre>
	 * 
	 * @return String.
	 */
	public static String toDateString() {
		return toDateString(false, 0, "yyyy-MM-dd HH:mm:ss", new Date());
	}

	/**
	 * 현재시간을 원하는 Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(&quot;yyyy-MM-dd HH:mm:ss&quot;, Date);
	 * </pre>
	 * 
	 * @param fmt Format.
	 * @return String.
	 */
	public static String toDateString(String fmt, Date date) {
		return toDateString(false, 0, fmt, date);
	}

	/**
	 * 현재시간을 원하는 Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(&quot;yyyy-MM-dd HH:mm:ss&quot;, str);
	 * </pre>
	 * 
	 * @param fmt Format.
	 * @return String.
	 */
	public static String toDateString(String fmt) {
		return toDateString(false, 0, fmt, new Date());
	}

	/**
	 * 현재시간을 선택해서 "yyyy-MM-dd HH:mm:ss" Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(false);
	 * </pre>
	 * 
	 * @param sync Synchronized.
	 * @return String.
	 */
	public static String toDateString(boolean sync) {
		return toDateString(sync, 0, "yyyy-MM-dd HH:mm:ss", new Date());
	}

	/**
	 * 현재시간을 "yyyyMMddHHmmssmmmm" Format으로 변환해서 COUNT값을 더해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(DateUtil.TYPE_TO_MILLI_COUNT);
	 * </pre>
	 * 
	 * @param type TYPE_TO_MILLI_COUNT값
	 * @return String.
	 */
	public static String toDateString(int type) {
		if (type != TYPE_TO_MILLI_COUNT) {
			throw new NullPointerException("Type값은 [" + TYPE_TO_MILLI_COUNT
					+ "]만 올수 있습니다.");
		}
		return toDateString(true, type, "yyyyMMddHHmmssmmmm", new Date());
	}

	/**
	 * 현재시간을 synchronized를 선택 후 원하는 Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(false, &quot;yyyy/MM/dd&quot;);
	 * </pre>
	 * 
	 * @param sync Synchronized.
	 * @param fmt Format.
	 * @return String.
	 */
	public static String toDateString(boolean sync, String fmt) {
		return toDateString(sync, 0, fmt, new Date());
	}

	/**
	 * 원하는 시간을 synchronized를 선택 후 원하는 Format으로 변환해서 반환한다.
	 * 
	 * @param sync Synchronized.
	 * @param fmt Format.
	 * @param date Date.
	 * @return String.
	 */
	public static String toDateString(boolean sync, String fmt, Date date) {
		return toDateString(sync, 0, fmt, date);
	}

	/**
	 * 시간을 원하는 String Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(true, &quot;yyyy-MM-dd HH:mm:ss mmmm&quot;, str, new Date());
	 * </pre>
	 * 
	 * @param sync synchronized를 이용할지 선택 (true / false)
	 * @param fmt Format.
	 * @param date Date
	 * @param type int
	 * @return String
	 */
	public static String toDateString(boolean sync, int type, String fmt,
			Date date) {
		if (sync) {
			synchronized (DateUtil.class) {
				if (type == TYPE_TO_MILLI_COUNT) {
					SimpleDateFormat sdf = new SimpleDateFormat(fmt);
					String ret = sdf.format(date) + count++;
					if (count == MAX_COUNT) {
						count = MIN_COUNT;
					}
					return ret;
				}
				else {
					return new SimpleDateFormat(fmt).format(date);
				}
			}
		}
		else {
			return new SimpleDateFormat(fmt).format(date);
		}
	}

	public static String formatConvertStringToDateTime(String strDate) throws Exception{
	    
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
	
		Date formatDate = dtFormat.parse(strDate);

	    return newDtFormat.format(formatDate);
	}
}
