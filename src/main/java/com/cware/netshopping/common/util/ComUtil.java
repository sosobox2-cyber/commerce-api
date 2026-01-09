package com.cware.netshopping.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.Security;
import java.security.SignatureException;
import java.sql.Clob;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.coupang.openapi.sdk.Hmac;
import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.product.type.AccessCredentialsType;
import com.cware.framework.core.basic.ParamMap;
import com.cware.framework.core.dataaccess.util.CamelUtil;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;
import com.vdurmont.emoji.EmojiParser;

  
/**
 * Common method
 *
 * @version 1.0, 2005/02/03
 * @author  kim Sungtaek <webzest@commerceware.co.kr>
 */
public class ComUtil implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	private static String PANAVER_ACCOUNT_PATH = "/naverpaysettle-order/naverpaysettle/v1/settlements/by-case?periodType=SETTLE_BASIS_DATE&";
	
	static { //ComUtil 오류없이 실행
		Security.addProvider(new BouncyCastleProvider());
	}
	
  /**
   * <PRE>
   * Desc     : Splits the provided text into an array, separators specified. This is an alternative to using StringTokenizer.
   * </PRE>
   * @param   str       : 원본
   * @param   separator : 구분자
   * @return  String[]
   */
    public static String[] split(String str, String separator){
        return StringUtils.split(str, separator);
    }

  /**
   * <PRE>
   * Desc     : Replaces multiple characters in a String in one go.
   * </PRE>
   * @param   str     : 바꾸려는 문자열을 가진 원본
   * @param   pattern : 찾을 문자열
   * @param   replace : 바꿔줄 문자열
   * @return  String
   */
  public static String replaceChars(String text, String repl, String with)
  {
    return StringUtils.replaceChars(text, repl, with);
  }

  /** GET ROUND
  // ROUND_CEILING : 올림
  // ROUND_HALF_UP : 반올림
  // ROUND_DOWN    : 버림
  **/
/* 미사용 - 2007.03.23 주석처리
  public static double getRound( double orgVal, int decimalPlace){
	  double     chVal = 0;
      BigDecimal bd    = new BigDecimal("0");

      if(orgVal == 0 ) return 0;

      bd    = BigDecimal.valueOf(orgVal).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
      chVal = bd.doubleValue();

//
//      BigDecimal bd = new BigDecimal(orgVal);
//      //bd = bd.setScale(decimalPlace,BigDecimal.ROUND_CEILING);
//      bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//      chVal = Double.parseDouble(bd.toString());
//
      return  chVal;
  }
*/
    /** GET RATE
    // ROUND_CEILING : 올림
    // ROUND_HALF_UP : 반올림
    // ROUND_DOWN    : 버림
    // 버림(버림(insAmt / orgAmt, 10) * 100, 2)
    **/
/* 미사용 - 2007.03.23 주석처리
  public static double getDcRate(double orgAmt, double insAmt){
        BigDecimal bd = new BigDecimal("0");

        if(insAmt == 0 || orgAmt == 0) return 0;

        bd = BigDecimal.valueOf(insAmt)
             .divide(BigDecimal.valueOf(orgAmt), ConfigUtil.getString("CALCULATE_SCALE"), ConfigUtil.getString("roundMode"))
             .multiply(BigDecimal.valueOf(100))
             .setScale(ConfigUtil.getString("DEFAULT_SCALE_RATE"), ConfigUtil.getString("roundMode"));

        return  bd.doubleValue();
    }
*/
    /** GET AMT
    // ROUND_CEILING : 올림
    // ROUND_HALF_UP : 반올림
    // ROUND_DOWN    : 버림
    // 버림(버림( (amt * rate) / 100, 10) * qty, 2)
    **/
/* 미사용 - 2007.03.23 주석처리
    public static double getDcAmt( double amt, double rate, long qty){
        BigDecimal bd = new BigDecimal("0");

        if(rate < 0 || amt == 0 || qty == 0) return 0;

        bd    = BigDecimal.valueOf(amt)
                .multiply(BigDecimal.valueOf(rate))
                .divide(BigDecimal.valueOf(100), ConfigUtil.getString("CALCULATE_SCALE"), ConfigUtil.getString("roundMode"))
                .multiply(BigDecimal.valueOf(qty))
                .setScale(ConfigUtil.getString("DEFAULT_SCALE_AMT"), ConfigUtil.getString("roundMode"));

        return  bd.doubleValue();
    }
*/

//= START ---------------- 부동 소수점 연산 오류 보정 및 반올림 정책 적용
	/**
	 * 부동 소수점 연산 오류 보정 및 반올림 정책 적용
	 * @param argAmt : 최종 금액
	 * @return
	 */
	public static double modAmt ( double argAmt){
		return modAmt ( argAmt, ConfigUtil.getInt("DEFAULT_MODE"), ConfigUtil.getInt("DEFAULT_FINAL_SIZE") );
	}

	/**
	 * 원단위 처리 (절사처리)
	 * @param argAmt
	 * @return
	 */
	public static double modAmtRemoveWon ( double argAmt){
		//= -1 인 경우만 원단위 절사(한국) 이외의 경우  resource.properties 의 DEFAULT_FINAL_SIZE 에 의한 절사처리.
		return (ConfigUtil.getInt("FINAL_D_POINT") == -1)? modAmt( modAmt ( argAmt / 10, ConfigUtil.getInt("MODE_FLOOR"), 0) * 10, ConfigUtil.getInt("DEFAULT_MODE"), 0) :
			modAmt(argAmt, ConfigUtil.getInt("MODE_FLOOR"));
	}

	/**
	 * 부동 소수점 연산 오류 보정 및 반올림 정책 적용
	 * @param argAmt : 최종 금액
	 * @param argMod : 반올림 정책
	 * @return
	 */
	public static double modAmt ( double argAmt, int argMod ){
		return modAmt ( argAmt, argMod, ConfigUtil.getInt("DEFAULT_FINAL_SIZE"));
	}

	/**
	 * 부동 소수점 연산 오류 보정 및 반올림 정책 적용
   * 모든 연산의 반올림을 사용한다. default
   * 최종 할인금액과 적립금을 계산하는 순간만 버림으로 사용
   *
   * 고려대상
   * 금액 할인의 금액 분배         ; 금액 할인 상품단위로 분배되는 경우        >> 반올림
   * 상품 할인의 금액 분배         ; Set상품일 경우 구성상품으로 분배되는 경우  >> 반올림
   * 부분반품, 부분취소 접수받을 때  ;  반품, 취소되어 분배되는 경우            >> 반올림
   * 적립금 분배                  ; Set상품일 경우 구성상품으로 분배되는 경우  >> 반올림
	 * @param argAmt       : 최종 금액
	 * @param argMod       : 반올림 정책
	 * @param argFinalSize : 최종 소수점 유효 자리수
	 * @return double
	 */
	public static double modAmt ( double argAmt, int argMod, int argFinalSize ){
		BigDecimal decimalValue = null;
		try{
			if(argAmt == 0) return 0;

			decimalValue = BigDecimal.valueOf(argAmt);

			//= Const.VALID_DECIMAL 까지 넘는 size 일 경우  Const.VALID_DECIMAL, Const.DEFAULT_MODE 로 세팅
			decimalValue = decimalValue.setScale(ConfigUtil.getInt("VALID_DECIMAL"), ConfigUtil.getInt("DEFAULT_MODE"));

			//= argMod, argFinalSize 로 재조정.
			decimalValue = decimalValue.setScale(argFinalSize, argMod);

		}catch(ArithmeticException e){ return 0;}

		return decimalValue.doubleValue();
	}

//= END ---------------- 부동 소수점 연산 오류 보정 및 반올림 정책 적용


    // get Parse Pay Month
    public static String getParsePayMonth(String norest_allot_months){
        String convertStr = StringUtils.leftPad(norest_allot_months,36,"0");
        String rtnValue   = "";
        String sep        = "";

        for(int i=0 ; i < 36 ; i++){
            if( convertStr.substring(i,i+1).equals("1") ){
                if(!sep.equals("-")){
                    rtnValue += ",";
                    if( i == 0 ) {
                        rtnValue += "일시불";
                    }else{
                        rtnValue += ""+(i+1);
                    }
                    if( i < 34 ){
                        if( convertStr.substring(i+1,i+2).equals("1") && convertStr.substring(i+2,i+3).equals("1") ){
                            sep = "-";
                            rtnValue += sep;
                        }
                    }
                }else{
                    if( i < 35 ){
                        if( !convertStr.substring(i+1,i+2).equals("1")){
                            rtnValue += ""+(i+1);
                            sep = "";
                        }
                    }else{
                        rtnValue += ""+(i+1);
                    }
                }
            }
        }

        if(!rtnValue.equals("")) rtnValue = rtnValue.substring(1);
        if(rtnValue.equals(""))  rtnValue = "불가";

        return rtnValue;
    }

    // Right Left a String with a specified character.
    public static String lpad(String s, int n , String replace )
    {
        return  StringUtils.leftPad(s, n, replace);
    }
    // Right pad a String with a specified character.
    public static String rpad(String s, int n , String replace )
    {
        return  StringUtils.rightPad(s, n, replace);
    }
    // +1 => Left pad a String with a specified character.
    public static String increaseLpad(String s, int n , String replace )
    {
        long tempValue = Long.parseLong(s);
        tempValue = tempValue + 1;
        s = Long.toString(tempValue);
        return  lpad(s, n, replace);
    }

    // CheckBox Value Mapping
    // i)   0  >> 1
    // ii) any >> 0
    public static String checkBoxValueSet(String inputValue){
        String rtnValue = "0";
        if( inputValue == null ) return rtnValue;
        if( inputValue.equals("") ) return rtnValue;
        if( !inputValue.equals("0") ) rtnValue = "1";
        return rtnValue;
    }

    // Object - > String
    public static String objToStr(Object objVal){
        return objToStr(objVal, "");
    }
    // Object - > String
    public static String objToStr(Object objVal, String emptyValue){
        if(objVal == null)		return emptyValue;
        if(objVal.equals(""))	return emptyValue;
        emptyValue = objVal.toString();
        return emptyValue;
    }

    // Object - > int
    public static int objToInt(Object objVal){
        return objToInt(objVal, 0);
    }
    // Object - > int
    public static int objToInt(Object objVal, int emptyValue){
        if(objVal == null) return emptyValue;
        if(objVal.equals("")) return emptyValue;
        emptyValue = Integer.parseInt(objVal.toString());
        return emptyValue;
    }

    // Object - > long
    public static long objToLong(Object objVal){
        return objToLong(objVal, 0);
    }
    // Object - > long
    public static long objToLong(Object objVal, long emptyValue){
        if(objVal == null) return emptyValue;
        if(objVal.equals("")) return emptyValue;
        emptyValue = (long)Double.parseDouble(objVal.toString());
        return emptyValue;
    }

    // Object - > long
    public static long objToDoubleToLong(Object objVal){
    	long emptyValue = 0;
        if(objVal == null)		return emptyValue;
        if(objVal.equals(""))	return emptyValue;
        emptyValue = (long)objToDouble(objVal);
        return emptyValue;
    }

    // Object - > double
    public static double objToDouble(Object objVal){
        return objToDouble(objVal, 0);
    }
    // Object - > double
    public static double objToDouble(Object objVal, double emptyValue){
        if(objVal == null) return emptyValue;
        if(objVal.equals("")) return emptyValue;
        emptyValue = Double.parseDouble(objVal.toString());
        return emptyValue;
    }
    // Double - > String
    public static String objToDoubleToStr(Object objVal){
    	String emptyValue = "";
        if(objVal == null)		return emptyValue;
        emptyValue = objToStr(objToDoubleToLong(objVal));//objVal.toString();
        return emptyValue;
    }

    /** String 을 구분자에 따라 잘라서 배열에 저장 **/
  public static String[] getStringToArray(String strVale, String CheckValue, int ArrayCount){
    String [] tempArray = new String[ArrayCount];
    if(strVale == null)    return tempArray;
    if(strVale.equals("")) return tempArray;

    int s = 0;
    int e = 0;
    int i = 0;

    while ((e = strVale.indexOf(CheckValue, s)) >= 0)
    {
      tempArray[i] = strVale.substring(s, e);
      s = e+CheckValue.length();
      i++;
    }
    return tempArray;
  }



  /**
   * <PRE>
   * Desc     : 123,456 -> int type convert.
   * </PRE>
   * @param   paramMoney : \123,456 Format
   * @return  int
   */
  public static int setCurrencyToInt(String paramMoney){
    int money = 0;
    StringBuffer strMoney = new StringBuffer(paramMoney);

    for (int i=0; i<strMoney.length(); i++)
    {
      if (strMoney.charAt(i) == ',')
      {
        strMoney.deleteCharAt(i);
      }
    }

    try
    {
      money = Integer.parseInt(strMoney.toString());
    } catch (NumberFormatException nfe) {
      money = -1; // error -1 return
    }
    return money;
  }

  /**
   * <PRE>
   * Desc     : 123,456 -> double type convert.
   * </PRE>
   * @param   paramMoney : \123,456 Format
   * @return  double
   */
  public static double setCurrencyToDouble(String paramMoney){
    double money = 0;
    StringBuffer strMoney = new StringBuffer(paramMoney);
    for (int i=0; i<strMoney.length(); i++)
    {
      if (strMoney.charAt(i) == ',')
      {
          strMoney.deleteCharAt(i);
      }
    }

    try
    {
      money = Double.parseDouble(strMoney.toString());
    } catch (NumberFormatException nfe) {
      money = -1; // error -1 return
    }
    return money;
  }

  /**
   * <PRE>
   * Desc     : 123,456 -> String type convert.
   * </PRE>
   * @param   paramMoney : \123,456 Format
   * @return  String
   */
  public static String setCurrencyToStr(String paramMoney)
  {
    if (paramMoney == null)
    {
      paramMoney = "";
    }

    StringBuffer strMoney = new StringBuffer(paramMoney);
    for (int i=0; i<strMoney.length(); i++)
    {
      if (strMoney.charAt(i) == ',' || strMoney.charAt(i) == '-' || strMoney.charAt(i) == '/')
      {
        strMoney.deleteCharAt(i);
      }
    }

    if (isNumber(strMoney.toString()))
    {
      return strMoney.toString();
    } else {
      return null;
    }
  }

  /**
   * <PRE>
   * Desc     : int -> 123,456 type convert.
   * </PRE>
   * @param   paramMoney : int
   * @return  String
   */
  public static String setIntToCurrency(int paramMoney)
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(paramMoney);
  }

  /**
   * <PRE>
   * Desc     : double -> 123,456 type convert.
   * </PRE>
   * @param   paramMoney : double
   * @return  String
   */
  public static String setIntToCurrency(double paramMoney)
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(paramMoney);
  }

  /**
   * <PRE>
   * Desc     : long -> 123,456 type convert.
   * </PRE>
   * @param   paramMoney : double
   * @return  String
   */
  public static String setIntToCurrency(long paramMoney)
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(paramMoney);
  }

  /**
   * <PRE>
   * Desc     : long -> 123,456 type convert.
   * </PRE>
   * @param   paramMoney : double
   * @return  String
   */
  public static String setLongToCurrency(long paramMoney)
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(paramMoney);
  }

  /**
   * <PRE>
   * Desc     : String -> 123,456 type convert.
   * </PRE>
   * @param   paramMoney : String
   * @return  String
   */
  public static String setStrToCurrency(String paramMoney)
  {
    if (paramMoney.equals(""))
      return "0";
    if (!isNumber(paramMoney))
      return null;

    Long paramLong = new Long(paramMoney);
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(paramLong.longValue());
  }

  /**
   * <PRE>
   * Desc     : number check
   * </PRE>
   * @param   strNumber : String
   * @return  boolean
   */
  public static boolean isNumber(String strNumber)
  {
    boolean isSuccess = true;

    try
    {
      new Long(strNumber);
    } catch (NumberFormatException nfe) {
      isSuccess = false;
    }
    return isSuccess;
  }

  /**
   * <PRE>
   * Desc     : 숫자에 대해(금액) TRUNC
   * </PRE>
   * @param   param : 숫자
   * @param   param : 숫자
   * @return  float
   */
  public static float Truncate(float param, float tunc)
  {
    param = param / tunc;
    Float floatTrunc = new Float(param);
    Integer IntTrunc = new Integer(floatTrunc.intValue());
    param = IntTrunc.floatValue();
    param = param  * tunc;
    return param;
  }

  /**
   * <PRE>
   * Desc     : 정수를 받아서 반올림(5이하 버림, 5이상 올림)
   * </PRE>
   * @param   number : 정수
   * @return  long
   */
  public static long Round(int number)
  {
    Integer i = new Integer(number);
    double d_number = i.doubleValue();
    long round_number = Math.round(d_number/10);
    long exchange_number = round_number * 10;
    return exchange_number;
  }

  /**
   * <PRE>
   * Desc     : double를 받아서 반올림(5이하 버림, 5이상 올림)
   * </PRE>
   * @param   number : double
   * @return  long
   */
  public static long Round(double number)
  {
    long round_number = Math.round(number/10);
    long exchange_number = round_number * 10;
    return exchange_number;
  }

  /**
   * <PRE>
   * Desc     : String 형식을 받아서 Html 형식으로 변환
   * </PRE>
   * @param   comment : String
   * @return  String
   */
  public static String convertHtmlBr(String comment)
  {
    int length = comment.length();
    StringBuffer buffer = new StringBuffer();

    if (comment.equals(null))
    {
      buffer.append("");
      return buffer.toString();
    }

    for (int i = 0; i < length; ++i)
    {
      String comp = comment.substring(i, i+1);
      if ("\r".compareTo(comp) == 0)
      {
        comp = comment.substring(++i, i+1);
        if ("\n".compareTo(comp) == 0)
          buffer.append("<BR>\r");
        else
          buffer.append("\r");
      }
      buffer.append(comp);
    }
    return buffer.toString();
  }

	/**
     * db에서 사용할수 없는 값들을 변환(&,',^)   web --> db
     * @param dbstring 바꿀 문자열
     * @return temp 바꾼 문자열
     */
    public static String script2web(String dbstring){
        int index=0;
        String temp = dbstring;

        while((index=temp.indexOf("~&"))>=0) {
            temp = temp.substring(0,index)+"{"+temp.substring(index+2);
        }
        while((index=temp.indexOf("~`"))>=0) {
            temp = temp.substring(0,index)+"\\"+temp.substring(index+2);
        }
        while((index=temp.indexOf("~^"))>=0) {
            //temp = temp.substring(0,index)+"\r\n"+temp.substring(index+2);
			temp = temp.substring(0,index)+"<br>"+temp.substring(index+2);
        }
        return temp;
    }

	/**
     * db에서 사용할수 없는 값들을 변환()   text --> db
     * @param dbstring 바꿀 문자열
     * @return temp 바꾼 문자열
     */
    public static String text2db(String desc){
        if(desc==null || desc.length()==0 ) return "";
        if(desc.length() == 1){
            if(desc.equals("\\")){
                return " ";
            }else if(desc.equals("\"")){
                return "'";
            }else{
                return desc;
            }
        }

        String temp = desc;
        int index=0;

        while((index=temp.indexOf('"'))>=0) {
            temp = temp.substring(0,index)+"'"+temp.substring(index+1);
        }

        String lastStr1 = temp.substring(temp.length()-1, temp.length());

        if (lastStr1.equals("\\") ){
		    return temp.substring(0, temp.length()-1) + " ";
        }

        return temp;

    }

	/**
     * web에서 사용할수 없는 값들을 변환   db --> web
     * @param desc 바꿀 문자열
     * @return temp 바꾼 문자열
     */
    public static String db2script(String desc){
        if(desc==null || desc.length()==0 )
			return "";

        String temp = desc;
        int index=-2;

/*        while((index=temp.indexOf('\'',index+2))>=0) {
            temp = temp.substring(0,index)+"\'\'"+temp.substring(index+1);
        }
*/
        while((index=temp.indexOf('{'))>=0) {
            temp = temp.substring(0,index)+"~&"+temp.substring(index+1);
        }
        while((index=temp.indexOf('\\'))>=0) {
            temp = temp.substring(0,index)+"~`"+temp.substring(index+1);
        }
        while((index=temp.indexOf("\r\n"))>=0) {
            temp = temp.substring(0,index)+"~^"+temp.substring(index+2);
        }
        while((index=temp.indexOf('\n'))>=0) {
            temp = temp.substring(0,index)+"~^"+temp.substring(index+1);
        }
        while((index=temp.indexOf('"'))>=0) {
            temp = temp.substring(0,index)+"`"+temp.substring(index+1);
        }
/*
        while((index=temp.indexOf("'",index+2))>=0) {
            temp = temp.substring(0,index)+"`"+temp.substring(index+2);
        }
*/
        return temp;
    }



  /**
   * <PRE>
   * Desc     : 변수가 한글이 포함되어 있는지 Check
   * </PRE>
   * @param   argStr : 문자열
   * @return  boolean
   */
  public static boolean isString(String argStr)
  {
    // 문자열의 길이와 문자열의 바이트배열의 길이를 비교해서 체크
    if (argStr.length() == argStr.getBytes().length)
      return false;
    else
      return true;
  }

  /**
   * <PRE>
   * Desc     : 특정문자 변환 Check
   * </PRE>
   * @param   str     : 바꾸려는 문자열을 가진 원본
   * @param   pattern : 찾을 문자열
   * @param   replace : 바꿔줄 문자열
   * @return  String
   */
  public static String replace(String str, String pattern, String replace)
  {
    int s = 0;
    int e = 0;
    StringBuffer result = new StringBuffer();

    while ((e = str.indexOf(pattern, s)) >= 0)
    {
      result.append(str.substring(s, e));
      result.append(replace);
      s = e+pattern.length();
    }

    result.append(str.substring(s));
    return result.toString();
  }

  /**
   * <PRE>
   * Desc     : 특수 char(& , " ) 를 ( , , ' ) 로 변환
   * </PRE>
   * @param   str : 특수 char(& , " )
   * @return  String
   */
  public static String charReplace(String str)
  {
    str = str.replace('&',',');
    str = str.replace('"', ' ');
    return str;
  }

  /**
   * Desc     : 좌측버튼 URL
   */
  public static final String RIGHT_BTN_URL = "/common/images/button/btn_list_forward.gif";
  /**
   * Desc     : 우측버튼 URL
   */
  public static final String LEFT_BTN_URL  = "/common/images/button/btn_list_previous.gif";

  /**
   * <PRE>
   * Desc     : GET 방식의 게시판의 counter
   * </PRE>
   * @param   int    : current_page
   * @param   int    : total_page
   * @param   Stirng : callee_url
   * @return  myIndexList(true, 10, current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, callee_url, null) call
   */
  public static String myIndexList(int current_page, int total_page, String callee_url)
  {
    return myIndexList(true, 10, current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, callee_url, null);
  }

  /**
   * <PRE>
   * Desc     : POST 방식의 게시판의 counter(1)
   * </PRE>
   * @param   int : current_page
   * @param   int : total_page
   * @return  myIndexList(false, 10,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", null) call
   */
  public static String myIndexList(int current_page, int total_page)
  {
    return myIndexList(false, 10,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", null);
  }

  /**
   * <PRE>
   * Desc     : POST 방식의 게시판의 counter(2)
   * </PRE>
   * @param   int : list_limit
   * @param   int : current_page
   * @param   int : total_page
   * @return  myIndexList(false ,list_limit,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", null) call
   */
  public static String myIndexList(int list_limit, int current_page, int total_page)
  {
    return myIndexList(false ,list_limit,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", null);
  }

  /**
   * <PRE>
   * Desc     : POST 방식의 게시판의 counter(3)
   * </PRE>
   * @param   int    : list_limit
   * @param   int    : current_page
   * @param   int    : total_page
   * @param   String : font_color
   * @return  myIndexList(false ,list_limit,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", font_color) call
   */
  public static String myIndexList(int list_limit, int current_page, int total_page, String font_color)
  {
    return myIndexList(false ,list_limit,current_page,total_page, LEFT_BTN_URL, RIGHT_BTN_URL, "goToPage", font_color);
  }

  /**
   * <PRE>
   * Desc     : Navigation Index List
   * </PRE>
   * @param   method_type T/F : get/post
   * @param   list_limit      : setting number (displayed number)
   * @param   current_page
   * @param   total_page
   * @param   left_image_url
   * @param   right_image_url
   * @param   callee_url
   * @return  html로 변환
   */
  public static String myIndexList(boolean method_type, int list_limit, int current_page, int total_page,
                                   String left_image_url, String right_image_url, String callee_url, String font_color)
  {
    int startpage;
    int endpage;
    int curpage;
    StringBuffer returnList = new StringBuffer();

    if (list_limit    < 0)       list_limit      = 0;
    if (current_page  < 0)       current_page    = 0;
    if (total_page    < 0)       total_page      = 0;
    if (left_image_url  == null) left_image_url  = "";
    if (right_image_url == null) right_image_url = "";
    if (callee_url      == null) callee_url      = "";
    if (font_color      == null) font_color      = "";


    startpage = ((current_page - 1) / list_limit) * list_limit + 1;

    endpage = (((startpage - 1) +  list_limit) / list_limit) * list_limit;

    if (total_page <= endpage)
    {
      endpage = total_page;
    }

    /**
    if (current_page > list_limit){
        curpage = startpage - 1;
        if (method_type)
            returnList.append("<a href='"+callee_url+"?page="+curpage+"'>");
        else
            returnList.append("<a href='javascript:"+callee_url+"("+curpage+");'>");

        returnList.append("<img src='"+left_image_url+"' border='0' align='absmiddle'></a>");
        returnList.append("... ");
        returnList.append("\n");
    }
    **/

    curpage = startpage;
    while (curpage <= endpage)
    {
      if (curpage == current_page)
      {
        returnList.append("<u>"+current_page+"</u>&nbsp;\n");
      }
      else
      {
        if (method_type)
          returnList.append("<u><a href='"+callee_url+"?page="+curpage+"'>"+curpage+"</a></u>&nbsp;\n");
        else
          returnList.append("<u><a href='javascript:"+callee_url+"("+curpage+");'>"+curpage+"</a></u>&nbsp;\n");
      }
      curpage++;
    }

    /**
    if (total_page > endpage){
        returnList.append(" ...");
        if (method_type)
            returnList.append("<a href='"+callee_url+"?page="+curpage+"'>");
        else
            returnList.append("<a href='javascript:"+callee_url+"("+curpage+");'>");

        returnList.append("<img src='"+right_image_url+"' border='0' align='absmiddle'></a>");
        returnList.append("\n");
    }
    **/

    return returnList.toString();
  }

  /**
   * <PRE>
   * Desc     : String[] => Integer[]으로 변환
   * </PRE>
   * @param   strs : String[]
   * @return  Integer[]
   */
  public static Integer[] setStrToInteger(String[] strs)
  {
    if ( strs == null ) return null;
    Integer[] ints = new Integer[strs.length];
    for (int i=0 ; i< ints.length ; i++)
    {
      try
      {
        ints[i] = new Integer(strs[i]);
      } catch (NumberFormatException e) {
        ints[i] = new Integer(0);
      }
    }
    return ints;
  }

  /**
   * <PRE>
   * Desc     : 원하는 값의 존재 유무 (해당번째)
   * </PRE>
   * @param   strs : 해당문자열
   * @param   comp : 비교값
   * @return  int
   */
  public static int getArrayCompare(String strs, String comp)
  {
    return strs.indexOf(comp, 0);
  }

  /**
   * <PRE>
   * Desc     : 원하는 값의 존재 유무
   * </PRE>
   * @param   strs[] : 해당문자열
   * @param   comp   : 비교값
   * @return  boolean
   */
  public static boolean getArrayCompare(String[] strs, String comp)
  {
    if ( strs == null ) return false;

    for (int i=0 ; i< strs.length ; i++)
    {
      if (strs[i].equals(comp)) return true;
    }
    return false;
  }

  /**
   * <PRE>
   * Desc     : 원하는 위치값의 존재 유무
   * </PRE>
   * @param   strs[]   : 해당문자열
   * @param   position : 위치값
   * @param   comp     : 비교값
   * @return  boolean
   */
  public static boolean getArrayCompare(String[] strs, int position, String comp)
  {
    if ( strs == null ) return false;
    if ( strs.length < position ) return false;
    if ( strs[position].equals(comp) ) return true;

    return false;
  }

  /**
   * <PRE>
   * Desc     : 유효한 값의 갯수
   * </PRE>
   * @param   strs[] : 문자열
   * @return  int
   */
  public static int getArrayCount(String[] strs)
  {
    int count = 0;
    try
    {
      if ( strs == null ) return count;
      for (int i=0 ; i< strs.length ; i++)
      {
        if (!strs[i].equals("") && strs[i] != null)
          count++;
      }
    } catch (Exception e) {
    }

    return count;
  }

  /**
   * <PRE>
   * Desc     : String[] => Double[]으로 변환
   * </PRE>
   * @param   strs[]
   * @return  Double[]
   */
  public static Double[] setStrToDouble(String[] strs)
  {
    if ( strs == null ) return null;
    Double[] doubles = new Double[strs.length];

    for (int i=0 ; i< doubles.length ; i++)
    {
      try
      {
        doubles[i] = new Double(strs[i]);
      } catch (NumberFormatException e) {
        doubles[i] = new Double(0);
      }
    }
    return doubles;
  }

  /**
   * <PRE>
   * Desc     : String 으로 받은 인수를 size 1 씩 짤라서 지정된 size 의 배열에 저장
   * </PRE>
   * @param   Amt   : 문자열
   * @param   Count : size
   * @return  String[] : 문자열을 배열에 setting
   */
  public static String[] arrayAmtSetting(String Amt, int Count)
  {
    String [] AmtArray = new String[Count];
    int AmtLength = Amt.length();
    int compLength = Count - AmtLength;

    for (int i = 0 ; i < Count ; i++)
    {
      if (compLength > i)
      {
        AmtArray[i] = "";
      } else {
        AmtArray[i] = Amt.substring(i-compLength,i-compLength+1);
      }
    }
    return AmtArray;
  }


  /**
   * <PRE>
   * Desc     : KOREA 로 변경
   * </PRE>
   * @param   en 문자열
   * @return  String
   */
  public static String enToKo(String en)
  {
    String korean=null;
    try
    {
      korean = new String(en.getBytes("8859_1"),"KSC5601");
    } catch(Exception e) {
      //e.printStackTrace();
      return korean;
    }
    return korean;
  }

  /**
   * <PRE>
   * Desc     : DB에 Data를 저장할때
   * </PRE>
   * @param   ko : korea 문자열
   * @return  String
   */
  public static String koToEn(String ko)
  {
    String english=null;
    try
    {
      english = new String(ko.getBytes("KSC5601"),"8859_1");
    } catch(Exception e) {
      e.printStackTrace();
      //return english;
    }
    return english;
  }

    // get norest allot months
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector getAllotMonthCal(String norest_allot_months){
        String convertStr = StringUtils.leftPad(norest_allot_months,36,"0");
        Vector vtRtn = new Vector();

        for(int i=0 ; i < 36 ; i++){
            if( convertStr.substring(i,i+1).equals("1") ){
                vtRtn.add(new Integer(i+1));
            }
        }
        return vtRtn;

/**
        String arrayVal[] = new String[36];
        int cnt = 0;
        for ( int i = 0 ; i < 36 ; i++ ){
            if(convertStr.substring(i,i+1).equals("1")){;
                arrayVal[cnt] = Integer.toString(i+1);
                cnt++;
            }
        }

        String rtnArray[] = new String[cnt];
        for( int j = 0 ; j < cnt; j++){
            rtnArray[j] = arrayVal[j];
        }
        return rtnArray;
**/
    }

    /**
     * 무이자 할부 개월 수 구하기 (숫자로 변환)
     */
    public static long getNorestAllotMonthsNumber(String norest_allot_months){
        long NorestAllotMonthsNumber = 0;

    	if(norest_allot_months != null && norest_allot_months.length() == 36){
        	for(int i=0; i<36; i++){
        		if( "1".equals(norest_allot_months.substring(i, i+1)) ){
        			NorestAllotMonthsNumber ++;
        		} else {
        			break;
        		}
        	}
        }
    	return NorestAllotMonthsNumber;
    }

//	/**
//	 *  주어진 스트링의 쿠키 값 리턴
//	 */
//    public static String getCookie(HttpServletRequest request, String name)
//			throws ServletException, IOException
//	{
//
//		String value = null;
//		Cookie[] cookies = request.getCookies();
//
//		if (cookies != null && cookies.length > 0) {
//			for (int i = 0; i < cookies.length; i++)
//			{
//				if ( cookies[i].getName().equals(name) )
//				{
//					value = cookies[i].getValue();
//					return value;
//				}
//			}
//		}
//
//		return null;
//	}

	/**
	 * <PRE>
	 * Desc     : Null String을 "" String으로 바꿔준다.
	 * </PRE>
	 * @param   String  문자열
	 * @return  String  문자열 or ""
	 */
	public static String NVL(String str){
		if(str == null || str.length() <= 0)
			return "";
		else
			return str;
	}
	
	public static Object NVL(Object str){
		if(str == null )
			return "";
		else
			return str;
	}

	/**
	 * <PRE>
	 * Desc     : Null String을 replace String으로 바꿔준다.
	 * </PRE>
	 * @param   String  검사 문자열
	 * @param   String  바뀔 문자열
	 * @return  String  문자열
	 */
	public static String NVL(String str, String replace){
		if(str == null || str.length() <= 0)
			return replace;
		else
			return str;
	}
	
	public static Object NVL(Object str, Object replace){
		if(str == null )
			return replace;
		else
			return str;
	}

	/**
	 * <PRE>
	 * Desc     : Null String을 replace String으로 바꿔준다.
	 *            Null 인 경우만 check 해서 replace 해주도록 변경
	 * </PRE>
	 * @param   String  검사 문자열
	 * @param   String  바뀔 문자열
	 * @return  String  문자열
	 */
	public static String isNull(String str, String replace){
		if(str == null)
			return replace;
		else
			return str;
	}



   	public static String replaceOne(String str) {

		if ( str == null ) return null;

		StringBuffer buff = new StringBuffer();
		char charArray[] = str.toCharArray();
		for (int i = 0; i<charArray.length; i++) {
			buff.append(charArray[i]);
			if (charArray[i]==39) {
				buff.append("\'");
			}
		}
		str = new String(buff);
		return str;
	}

   	public static String replaceQue(String str) {

		if ( str == null ) return null;

		StringBuffer buff = new StringBuffer();
		char charArray[] = str.toCharArray();
		for (int i = 0; i<charArray.length; i++) {
			if (charArray[i] != 39 && charArray[i] != 34) {
				buff.append(charArray[i]);
			}
		}
		str = new String(buff);
		return str;
	}

    /** GET AMT
    // ROUND_CEILING : 올림
    // ROUND_HALF_UP : 반올림
    // ROUND_DOWN    : 버림
    **/
/* 미사용 2007.03.23 주석처리
    public static String getCalAmt( String orgAmt, String insRate){
        int decimalPlace = 0;
        if(insRate.equals("0")) return "0";
        double     rtnAmt = ( Double.parseDouble(orgAmt) * Double.parseDouble(insRate) / 100 );
        BigDecimal bd     = new BigDecimal(rtnAmt);
        bd     = bd.setScale(decimalPlace,BigDecimal.ROUND_CEILING);
        //bd     = bd.setScale(decimalPlace,BigDecimal.ROUND_DOWN);
        return  bd.toString();
    }

    public static double getSetCalDcAmt( double inSetGoodsAmt, double setGoodsAmt, double setGoodsDcAmt, long inSetGoodsCount){
        try{
            double rtnVal = 0;
            if(inSetGoodsAmt == 0 || setGoodsAmt == 0 || setGoodsDcAmt == 0) return 0;
            BigDecimal bd     = new BigDecimal( (double)inSetGoodsAmt / (double)setGoodsAmt * (double)setGoodsDcAmt );
            bd     = bd.setScale(0,BigDecimal.ROUND_DOWN);
            rtnVal = ( Double.parseDouble(bd.toString()) / inSetGoodsCount * inSetGoodsCount);
            return  rtnVal;
        }catch(Exception e){return 0;}
    }
*/


     /**
     * 문자 하나가 알파벳인지 검사
     *
     * @param   str 검사 하고자 하는 문자
     * @return  알파벳인지의 여부에 따라 'true' or 'false'
     */
    public static boolean isAlpha(char c) {
        if ((c < 'a' || c > 'z') &&
            (c < 'A' || c > 'Z') 
//            && c != '_' && c != ' '
            )
            return false;
        return true;
    }


    /**
     * 문자열이 알파벳인지 검사
     *
     * @param   str 검사 하고자 하는 문자열
     * @return  알파벳인지의 여부에 따라 'true' or 'false'
     */
    public static boolean isAlpha(String str) {
        if (str == null) return false;

        str = str.trim();
        int len = str.length();
        if (len == 0)
            return false;

        for (int i = 0; i < len; i++) {
            if (!isAlpha(str.charAt(i)))
                return false;
        }
        return true;
    }


    /**
     * 상품 코드를 Barcode로 변환
     *
     * @param   str1 상품코드
     * @param   str2 단품코드
     * @return  Barcode
     */
    public static String getBarcode(String str1, String str2) {
        //= CODE 39
        String[] code = {   "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
    						"B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L",
    						"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
    						"X", "Y", "Z", "-", ",", " ", "$", "/", "+", "%"};
        String barcode = (str1 + str2).toUpperCase();
        int lencode = barcode.length();
        int sumcode = 0;
        int k = 0;
        String cutcode = "";

        for(int i=0; i<lencode; i++){
        	cutcode = barcode.substring(i, i+1);
        	k=0;
        	for(int j=0; j<43; j++){
        		//= CODE39에 대응되는 값의 위치를 찾는다.
        		k=j;
        		if( cutcode.equals(code[j]) ) break;
        	}
        	//= 대응되는 위치값을 더한다.
        	sumcode = sumcode + k;
        }

        //= BarCode를 생성한다.
        barcode = barcode + code[(sumcode % 43)];

        return barcode;
    }


    /**
     * ArrayList를 Message 배열로 변환
     *
     * @param   ArrayList
     * @return  Message 배열
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ParamMap[] getHashMapArr(ArrayList arrList){
    	if(arrList == null || arrList.size() == 0) return null;
    	Collection collection = new ArrayList();
    	Iterator i=arrList.iterator();
    	ParamMap msg = null;
        while(i.hasNext()){
        	msg = new ParamMap();
        	msg.setParamMap((HashMap)i.next());
        	collection.add( msg );
        }
        return (ParamMap[])collection.toArray(new ParamMap[0]);
    }

	public static String toXml(List<?> list){
		StringBuffer xml = new StringBuffer();
		for(int i=0;i<list.size();i++){
			xml.append(((String)list.get(i)).replaceAll("\\<(\\?)[xml](\\w+)(.*)(\\?)\\>", ""));
			// 특수문자는 앞에 \\
			// [xml] xml 세글자
			// [a-z] a부터 z까지 한개의 문자
			// [a-z]+ 한개 이상의 문자
			// [a-z]* 0개 이상의 문자
			// \\w 공백 문자 한개 이상
			// .* 모든 문자 0개 이상
			// () 그룹
		}
		return xml.toString();
	}

	/**
	 * form에서 전송된 Request Parameter를 HashMap으로 변환
	 *
	 * @param HttpServletRequest
	 * @return HashMap<String, Object>
	 */
	public static HashMap<String, Object> requestToHashMap(HttpServletRequest request) {
		HashMap<String, Object> commandMap = new HashMap<String, Object>();
		Enumeration<?> enumeration = request.getParameterNames();

		while(enumeration.hasMoreElements()){
			String key = (String) enumeration.nextElement();
			String[] values = request.getParameterValues(key);
			if(values!=null){
				commandMap.put(key, (values.length > 1) ? values:values[0] );
			}
		}

		return commandMap;
	}
	
	/**
	 * Map에 있는 key값을 String[]로 변환
	 *
	 * @param Map map
	 * @param String key
	 * @return String[]
	 */
	public static String[] mapToParamArray(Map<?,?> map, String key) {
		if(map == null) return null;
		
		String[] arr = null;
		if (map.containsKey(key)) {
			if (map.get(key) instanceof String) {
				arr = new String[1];
				arr[0] = (String) map.get(key);
			} else if (map.get(key) instanceof String[]) {
				arr = (String[]) map.get(key);
			} else {
				arr = new String[0];
			}
		} else {
			arr = new String[0];
		}
		return arr;
	}

	/**
	 * 접속서버와 컨테이너 정보 출력
	 *
	 * @return String
	 */
	public static String getMachineName() {
		return System.getProperty("hnsp.machine.name");
	}
	
	/**
	 * String 을 byte 길이 만큼 자르기
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public static String subStringBytes(String str, int byteLength) {
    	int length = str.length();
    	int retLength = 0;
    	int tempSize = 0;
    	int asc;
    	for (int i = 1; i <= length; i++) {
    		asc = (int) str.charAt(i - 1);
    		if (asc > 127) {
    			if (byteLength >= tempSize + 2) {
    				tempSize += 2;
    				retLength++;
    			} else {
    				return str.substring(0, retLength);
    			}
    		} else {
    			if (byteLength > tempSize) {
    				tempSize++;
    				retLength++;
    				}
    			}
    		}

    	return str.substring(0, retLength);
    }
	
	/**
	 * 회원세션값
	 * @param session
	 * @return Integer (0:비회원 1:회원 -1:세션없음)
	 * @throws Exception
	 */
	public static int getLoginStatus(HttpSession session) throws Exception {
		if (session.getAttribute(Constants.USER_KEY) != null) {
			if ("1".equals(session.getAttribute(Constants.USER_NONMEMBER_KEY))) { return 0; }
			else { return 1; }
		}
		else { return -1; }
	}
	
	/**
	 * 최근본상품 map list 로 만들기
	 * 
	 * @param goodscookie
	 * @return
	 */
	public static List<Map<String, Object>> getRecentlyGoodsList(String goodscookie){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(goodscookie == null || goodscookie.length() == 0) return list;
		try {
			
			String[] str_array = split(goodscookie, ":");
			
			for(int i = 0; i < str_array.length; i++){
				String[] goods_array = split(str_array[i], "$$");
				Map<String, Object> map = new HashMap<String, Object>();
				
				if(goods_array.length > 4){
					map.put("goodsCode", goods_array[0]);
					map.put("goodsName", goods_array[1]);
					map.put("salePrice", goods_array[2]);
					map.put("imageUrl", goods_array[3]);
					map.put("imageS", goods_array[4]);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
		return list;
	}
	
	/**
	 * WAS SERVER HOST NAME 가져오기
	 * @return
	 */
	public static String getServerHostName(){
		String hostname = "";
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			return "";
		}
	}

    public static String getPostData(HttpServletRequest request) throws IOException{
        StringBuffer jb = new StringBuffer();
        String line = null;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);
        return jb.toString();
    }
    
    /**
	 * 공백 및 null 체크
	 * @param s
	 * @return
	 */
	public static boolean isEmptyOrWhitespace(String s) {
		s = makeSafe(s);
		for (int i = 0, n = s.length(); i < n; i++) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * null일 경우 공백으로 치환
	 * @param s
	 * @return
	 */
	public static String makeSafe(String s) {
		return (s == null) ? "" : s;
	}

	/**
	 * string을 object로 mapping한다.
	 * @param json : 변환할 json형태의 string
	 * @param clazz : 변환된 object
	 * @return
	 */
	public static <T> Object jsonToObject(String json, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * object를 json(String)으로 변환한다.
	 * @param o : 변환할 object
	 * @return jsonString
	 */
	public static String objectToJson(Object o) {
		JsonGenerator generator = null;
		StringWriter writer = new StringWriter();
		try {
			generator = JSON_FACTORY.createJsonGenerator(writer);
			OBJECT_MAPPER.writeValue(generator, o);
			generator.flush();
			return writer.toString();
		} catch (Exception e) {
			return null;
		} finally {
			if (generator != null) {
				try {
					generator.close();
				} catch (Exception e) {

				}
			}
		}
	}
	
	/**
	 * 슷자 입력여부 검출
	 * @param String
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean numberCheck(String str) throws Exception {
		char c;
		if (str.equals(""))
			return true;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c < 48 || c > 57) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * form에서 전송된 Request Parameter를 로그 남기기
	 *
	 * @param HttpServletRequest
	 * @return void
	 */
	public static void requestToLog(HttpServletRequest request, Logger log, Class<?> a) {
		log = LoggerFactory.getLogger(a);
		//System.out.println("class2: "+a.toString());
		log.info("==== RequestToLog ===");
		log.info("reqURL: "+request.getRequestURI());
		Enumeration<?> names = request.getParameterNames();
		String name="";
		while(names.hasMoreElements()){ 
			name = (String)names.nextElement();
			if(!name.equals("entpPass")){
			    log.info(name+": "+request.getParameter(name));
			} 			
		}
		log.info("=== RequestToLog ===");
	}

	/**
	 * form에서 전송된 Response log를 로그 남기기
	 *
	 * @param HttpServletRequest
	 * @return void
	 */
	public static void responseToLog(HttpServletRequest request, Logger log, Class<?> a, ResponseMsg resMsg) {
		log = LoggerFactory.getLogger(a);
		//System.out.println("class2: "+a.toString());
		log.info("=== ResponseToLog ===");
		log.info("resURL: "+request.getRequestURI());
		/*
		Enumeration<?> names = request.getParameterNames();
		String name="";
		while(names.hasMoreElements()){ 
			name = (String)names.nextElement();
			log.info(name+": "+request.getParameter(name));
		}
		*/
		log.info("HTTP STATUS CODE = "+resMsg.getStatus());
		log.info("CODE = "+resMsg.getCode());
		log.info("RESPONSE MESSAGE = "+resMsg.getMessage());
		//log.info("RESPONSE MEDIA_CODE = "+resMsg.getMediaCode());
		log.info("=== ResponseToLog ===");
	}

	/**
	 * Response log를 로그 남기기
	 * @param request
	 * @param log
	 * @param targetClass
	 * @param status
	 * @param code
	 * @param message
	 */
	public static void responseToLog(HttpServletRequest request, Logger log, Class<?> targetClass, int status, String code, String message) {
		ResponseMsg tvResMsg = new ResponseMsg(status ,code, message);
		responseToLog(request,log,targetClass,tvResMsg);
	}
	
	/**
	 * InputStream to Document
	 * @param InputStream
	 * @return Document
	 */
	public static Document parseXML(InputStream stream) throws Exception{
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
 
        try{        	
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            
            // XXE 취약점 예방
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            objDocumentBuilderFactory.setXIncludeAware(false);
            objDocumentBuilderFactory.setExpandEntityReferences(false);
                        
            //objDocumentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);            
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
 
            doc = (Document) objDocumentBuilder.parse(stream);
        }catch(Exception ex){
            throw ex;
        }       
        return doc;
    }
	
	/**
	 * 11번가 Connection Setting(connectTime 설정)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection pa11stConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, int connectTimeOut, int readTimeOut) throws Exception{
		return pa11stConnectionProc(apiInfo,paCode,requestType,parameter,connectTimeOut,readTimeOut, ConfigUtil.getString("PA11ST_COM_BASE_URL"));
	}
	
	/**
	 * 11번가 Connection Setting(URL TYPE 설정)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection pa11stConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, String urlType) throws Exception{
		return pa11stConnectionProc(apiInfo,paCode,requestType,parameter,ConfigUtil.getInt("URL_CONNECT_TIMEOUT"),ConfigUtil.getInt("URL_READ_TIMEOUT"), urlType);
	}
	
	/**
	 * 11번가 Connection Setting(default)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection pa11stConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter) throws Exception{
		return pa11stConnectionProc(apiInfo,paCode,requestType,parameter,ConfigUtil.getInt("URL_CONNECT_TIMEOUT"),ConfigUtil.getInt("URL_READ_TIMEOUT"), ConfigUtil.getString("PA11ST_COM_BASE_URL"));
	}
	
	public static HttpURLConnection pa11stConnectionProc(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, int connectTimeOut, int readTimeOut, String urlType) throws Exception{
		HttpURLConnection conn = null;
		URL url = new URL(urlType+apiInfo.get("API_URL")+parameter);
		conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(readTimeOut);
		conn.setRequestMethod(requestType);
		conn.setRequestProperty("openapikey", apiInfo.get(paCode));
		conn.setRequestProperty("Content-Type", apiInfo.get("contentType")==null?"application/xml":apiInfo.get("contentType").toString());
		conn.setRequestProperty("Accept", "application/xml");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		return conn;
	}
	

	/**
	 * SOAP Connection Call
	 * @param requestMassge
	 * @param soapEndpointUrl
	 * @return SOAPMessage
	 * @throws Exception
	 */
	public static SOAPMessage callSoapWebService(SOAPMessage requestMassge, String soapEndpointUrl) throws Exception {
		SOAPMessage responseMessage = null;
		SOAPConnectionFactory soapConnectionFactory = null;
		SOAPConnection soapConnection = null;

		try {
		    soapConnectionFactory = SOAPConnectionFactory.newInstance();
		    soapConnection =  soapConnectionFactory.createConnection();
		    //인터페이스 CALL
		    responseMessage = soapConnection.call(requestMassge, soapEndpointUrl);
		}catch (Exception e) {
		    // TODO: Exception 처리
		    throw e;
		}finally  {
		    if(soapConnection != null){
			soapConnection.close();
		    }
		}

		return responseMessage;
	}


	public static Map<String,Object> ConverttObjectToMap(Object obj){
		Field[] field = obj.getClass().getDeclaredFields();

		Map<String,Object> map = new HashMap<String,Object>();

		for(int i = 0; i < field.length; i++){
			field[i].setAccessible(true);		

			try{
				map.put(field[i].getName(),field[i].get(obj));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return map;
	}
	
	
	/**
	 * 함수명 : splitJson
	 * @param  String
	 * @return Map<String, Object>
	 * @throws Exception
	 * Spec			Response 데이터를  MAP으로 변환
	 * Param  Info  G마켓 통신 결과로 받은 response.getbody()
	 */
	public static Map<String, Object> splitJson(String JSONString) throws  Exception{
		if(JSONString == null || JSONString.equals("")) return null;	
	
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isMapYn = true;
		int arrayType     = JSONString.indexOf("["); 
		int mapType   = JSONString.indexOf("{");
		
		if(mapType > arrayType ){  //= map의 인덱스가 더 뒤에있다 -> arrayList가 Map을 감싸는 형태
			isMapYn = false;
		}else{
			isMapYn = true;
		}
		
		if(arrayType == -1) isMapYn = true;   //{} 가 아예 없음
			
		if(mapType  ==  -1 ) isMapYn = false; // [] 가 아예 없음
			
		ObjectMapper mapper = new ObjectMapper();
		if(!isMapYn){
			List<Object> list = mapper.readValue(JSONString, new TypeReference<List<Object>>(){});
			map.put("Data", list);
			JSONString = mapper.writeValueAsString(map);
		}
		
		try {
			map = mapper.readValue(JSONString, new TypeReference<Map<String, Object>>(){});
		} catch (JsonParseException e) {
			return null;
		} catch (JsonMappingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return map;	
	}
	
	/**
	 * 함수명 : splitJson
	 * @param  Map<String, Object>, String ,Int 
	 * @return Map<String, Object>
	 * @throws Exception
	 * Spec			Map [R1{A =a, B=b, C=c}, R2{A =a, B=b, C=c}] -> Map[{A =a}, {B=b}, {C=c}로 리턴 , *호출부에서 for문을 통해 호출하세요
	 * Param  Info  key = Json 상위 Name , count = map의 size 
	 */
	public static Map<String, Object> map2map(Map<String, Object> map , String key, int count) throws Exception{
		if(map ==null || map.size() < 1) return null;
		
		ArrayList<Object> ar = (ArrayList<Object>) map.get(key);
		Map<String, Object> map2 = new HashMap<String, Object>();
		
		if(ar ==null) return map2;
		
		if(ar.size() > count){
			map2 = (Map<String, Object>) ar.get(count);
		}
		
		return map2;
	}
	
	public static List<Map<String, Object>> map2List(Map<String, Object> map , String key) throws Exception{
		if(map ==null || map.size() < 1 ) return null;
		
		List<Map<String, Object>> ar = new ArrayList<Map<String, Object>>();
		
		for(int i = 0 ; i < map.size() ;  i++){
			
			ar = (List<Map<String, Object>>) map.get(key);
		}
		
		if(ar == null){
			ar = new ArrayList<Map<String, Object>>();
		}
		
		return ar;
	}

	
	public static Map<String, Object> subMap2map(Map<String, Object> map, String key ){
		if(map.size() < 1) return null;
		Map<String, Object> tempMap = (Map<String, Object>) map.get(key);
		if(tempMap == null || tempMap.size() < 1) return null;
		return tempMap;
	}
	
	/**
	 * 네이버 API 인증정보 생성
	 * @param serviceName
	 * @param operationName
	 * @return
	 * @throws SignatureException
	 */
	public static AccessCredentialsType paNaverCreateAccessCredentials(String serviceName, String operationName) throws SignatureException {
		String panaver_access_license = ConfigUtil.getString("PANAVER_ACCESS_LICENSE");
		String panaver_secret_key = ConfigUtil.getString("PANAVER_SECRET_KEY");
		
		String timestamp = SimpleCryptLib.getTimestamp();
		String signature = SimpleCryptLib.generateSign(timestamp + serviceName + operationName, panaver_secret_key);
		AccessCredentialsType accessCredentials = new AccessCredentialsType();
		accessCredentials.setAccessLicense(panaver_access_license);
		accessCredentials.setTimestamp(timestamp);
		accessCredentials.setSignature(signature);
		return accessCredentials;
	}
	
	public static NaverSignature paNaverGenerateSignature(String operationName) throws SignatureException {
		return paNaverGenerateSignature(operationName,ConfigUtil.getString("PANAVER_SELLER_SERVICE_NAME"));
	}
	
	public static NaverSignature paNaverGenerateSignature(String operationName,String serviceName) throws SignatureException {
		String accessLicense = ConfigUtil.getString("PANAVER_ACCESS_LICENSE");
		String secretKey = ConfigUtil.getString("PANAVER_SECRET_KEY");
		NaverSignature naverSignature = new NaverSignature();
		
		naverSignature.setAccessLicense(accessLicense);
		naverSignature.setTimeStamp(SimpleCryptLib.getTimestamp());
		String data = naverSignature.getTimeStamp() + serviceName + operationName;
		naverSignature.setSignature(SimpleCryptLib.generateSign(data, secretKey));
		naverSignature.setEncryptKey(SimpleCryptLib.generateKey(naverSignature.getTimeStamp(), secretKey));
		
		return naverSignature;
	}	
	
	public static String dateFormater(String yyyyMMddDate) throws ParseException {

		Date date = new SimpleDateFormat("yyyyMMdd").parse(yyyyMMddDate);
		String resDate = new SimpleDateFormat("yyyyMMdd").format(date);

		return resDate ;
	}
	
	public static String dateFormater(String yyyyMMddDate, String format) throws ParseException {

		Date date = new SimpleDateFormat("yyyyMMdd").parse(yyyyMMddDate);
		String resDate = new SimpleDateFormat(format).format(date);

		return resDate ;
	}
	
	/**
	 * HttpQueryString parser
	 * @param map
	 * @return
	 */
	public static String makeHTTPQuery(Map map) {
		StringBuilder sb = new StringBuilder();
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			Entry thisEntry = (Entry) entries.next();
			String key = (String) thisEntry.getKey();
			String value = (String) thisEntry.getValue();
			sb.append(String.format("%s=%s", key, value));
		}
		return sb.toString();
	}
	
	/**
	 * 네이버 Connection Setting(get Method)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static String paNaverHttpGetConnection(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter) throws Exception{
		return paNaverGetConnectionProc(apiInfo,paCode,requestType,parameter,ConfigUtil.getInt("URL_CONNECT_TIMEOUT"),ConfigUtil.getInt("URL_READ_TIMEOUT"));
	}
	
	/**
	 *
	 * @param apiInfo
	 * @param paCode
	 * @param requestType
	 * @param parameter
	 * @param connectTimeOut
	 * @param readTimeOut
	 * @param urlType
	 * @return
	 * @throws Exception
	 */
	public static String paNaverGetConnectionProc(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, int connectTimeOut, int readTimeOut) throws Exception{

//		 String domain = ConfigUtil.getString("PANAVR_ACCOUNT_URL"); dev.apis.naver.com
		 String domain = "apis.naver.com";
//		 int PORT = 443;
		 String SCHEMA = "https";
		 String resStr = "";

		 String path = parameter;
//		 String path = "/naverpaysettle-order/naverpaysettle/v1/settlements/by-case?periodType=SETTLE_COMPLETE_DATE&startDate=20191101&endDate=20191110&pageNumber=1";
		 System.out.println(path);
		 CloseableHttpClient client = null;
		 HttpEntity entity = null ;

	     try {
	         //create client
	         client = HttpClients.createDefault();
	         //build uri
	         URIBuilder uriBuilder = new URIBuilder();

	         uriBuilder.setScheme(SCHEMA).setHost(domain);
	         HttpGet get = new HttpGet(uriBuilder.build().toString().replace("%3F", "?")+PANAVER_ACCOUNT_PATH+path);
	         System.out.println("URL INFO : "+ uriBuilder.build().toString().replace("%3F", "?")+PANAVER_ACCOUNT_PATH+path);
	         get.addHeader("Content-Type", "application/json");
//	         get.addHeader("X-Naver-API-Key", ConfigUtil.getString("PANAVR_ACCOUNT_KEY")); 2acacb76516877a57a6b85cf749cd726ba9b43150d2eb21ef7a396b2cb4bf311
//	         get.addHeader("X-Naver-API-Key", "2acacb76516877a57a6b85cf749cd726ba9b43150d2eb21ef7a396b2cb4bf311");
	         get.addHeader("X-Naver-API-Key", ConfigUtil.getString("PANAVER_ACCOUNT_LICENSE"));
	         get.addHeader("charset", "UTF-8");
	         CloseableHttpResponse response = null;

	         try {
	             //execute get request
	             response = client.execute(get);
	             //print result
	             System.out.println("status code:" + response.getStatusLine().getStatusCode());
	             System.out.println("status message:" + response.getStatusLine().getReasonPhrase());

	             entity = response.getEntity();
	             resStr = EntityUtils.toString(entity) ;
	             System.out.println("result:" + resStr);


	         } catch (Exception e) {
	             e.printStackTrace();

	         } finally {
	             if (response != null) {
	                 try {
	                     response.close();
	                 } catch (IOException e) {
	                     e.printStackTrace();
	                 }
	             }
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	     } finally {
	         if (client != null) {
	             try {
	                 client.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	         }
	     }


		return resStr;
	}
	
	
	/**
	 * 쿠팡 API 호출:  apiData is null, apiMethod: GET
	 * @param apiInfo
	 * @param paCode
	 * @param uriBuilder
	 * @return
	 */
	public static JsonObject callPaCopnAPI(HashMap<String, String> apiInfo, String paName, URIBuilder uriBuilder) {
		return callPaCopnAPI(apiInfo, paName, uriBuilder, "GET", "");
	}

	/**
	 * 쿠팡 API 호출: apiData is null
	 * @param apiInfo
	 * @param paCode
	 * @param uriBuilder
	 * @param apiMethod
	 * @return
	 */
	public static JsonObject callPaCopnAPI(HashMap<String, String> apiInfo, String paName, URIBuilder uriBuilder, String apiMethod) {
		return callPaCopnAPI(apiInfo, paName, uriBuilder, apiMethod, "");
	}

	/**
	 * 쿠팡 API 호출
	 * @param apiInfo
	 * @param paName
	 * @param uriBuilder
	 * @param apiMethod
	 * @param apiData
	 * @return
	 */
	public static JsonObject callPaCopnAPI(HashMap<String, String> apiInfo, String paName, URIBuilder uriBuilder, String apiMethod, String apiData) {
		RequestConfig requestConfig = null;
		CloseableHttpClient client = null;
		HttpGet requestGet = null;
		HttpPost requestPost = null;
		HttpPut requestPut = null;
		HttpPatch requestPatch = null;
		HttpDelete requestDelete = null;
		CloseableHttpResponse response = null;
		String[] apiKeys = null;
		JsonObject resultObject = new JsonObject();
		String	   resultMsg	= "";
		String requestHeader = "";
		Header[] headers = null;

		try {
			// 0: VENDOR_ID , 1: SECRET_KEY , 2: ACCESS_KEY
			apiKeys = apiInfo.get(paName).split(";");

			requestConfig = RequestConfig.custom()
					.setSocketTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT"))
					.setConnectTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT"))
					.setConnectionRequestTimeout(ConfigUtil.getInt("URL_READ_TIMEOUT")).build();

			client = HttpClientBuilder.create().build();

			String authorization = Hmac.generate(apiMethod, uriBuilder.build().toString(), apiKeys[1], apiKeys[2]);

			uriBuilder.setScheme(ConfigUtil.getString("PACOUP_SCHEMA")).setHost(ConfigUtil.getString("PACOUP_HOST"));

			if ("GET".equals(apiMethod)) {

				requestGet = new HttpGet(uriBuilder.build().toString());
				requestGet.setConfig(requestConfig);
				requestGet.addHeader("Authorization"     , authorization);
				requestGet.addHeader("content-type"      , "application/json");
				requestGet.addHeader("X-EXTENDED-TIMEOUT", "30000");
				response = client.execute(requestGet);
				
				headers = requestGet.getAllHeaders();

			} else if ("POST".equals(apiMethod)) {

				requestPost = new HttpPost(uriBuilder.build().toString());
				requestPost.setConfig(requestConfig);
				StringEntity params = new StringEntity(apiData, "UTF-8");
				requestPost.addHeader("Authorization", authorization);
				requestPost.addHeader("content-type", "application/json");
				requestPost.addHeader("X-EXTENDED-TIMEOUT", "30000");
				requestPost.setEntity(params);
				response = client.execute(requestPost);
				
				headers = requestPost.getAllHeaders();

			} else if ("PUT".equals(apiMethod)) {

				requestPut = new HttpPut(uriBuilder.build().toString());
				requestPut.setConfig(requestConfig);
				StringEntity params = new StringEntity(apiData, "UTF-8");
				requestPut.addHeader("Authorization", authorization);
				requestPut.addHeader("content-type", "application/json");
				requestPut.addHeader("X-EXTENDED-TIMEOUT", "30000");
				requestPut.setEntity(params);
				response = client.execute(requestPut);
				
				headers = requestPut.getAllHeaders();

			} else if ("PATCH".equals(apiMethod)) {

				requestPatch = new HttpPatch(uriBuilder.build().toString());
				requestPatch.setConfig(requestConfig);
				StringEntity params = new StringEntity(apiData, "UTF-8");
				requestPatch.addHeader("Authorization", authorization);
				requestPatch.addHeader("content-type", "application/json");
				requestPatch.addHeader("X-EXTENDED-TIMEOUT", "30000");
				requestPatch.setEntity(params);
				response = client.execute(requestPatch);
				
				headers = requestPatch.getAllHeaders();

			} else if ("DELETE".equals(apiMethod)) {

				requestDelete = new HttpDelete(uriBuilder.build().toString());
				requestDelete.setConfig(requestConfig);
				requestDelete.addHeader("Authorization", authorization);
				requestDelete.addHeader("content-type", "application/json");
				requestDelete.addHeader("X-EXTENDED-TIMEOUT", "30000");
				response = client.execute(requestDelete);
				
				headers = requestDelete.getAllHeaders();
			}

			HttpEntity entity = response.getEntity();
			Gson gson = new Gson();
			resultObject = (JsonObject) gson.fromJson(EntityUtils.toString(entity), JsonObject.class);

			int headerCnt = 0;
			for(Header h : headers) {
				if(headerCnt == 0) {
					requestHeader += "{";
				}
				else {
					if(headerCnt <= headers.length-1) requestHeader += ", ";
				}
				requestHeader += h.getName() + "=";
				requestHeader += "["+h.getValue()+"]";
				if(headerCnt++ == headers.length-1) requestHeader += "}";
			}
		
			resultMsg = resultObject.toString();

		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.toString();
		} finally {
			
			try {
				PaRequestMap paRequestMap = new PaRequestMap();
				if(paName.equals(Constants.PA_BROAD)) {
					paRequestMap.setPaCode(Constants.PA_COPN_BROAD_CODE);
				}
				else {
					paRequestMap.setPaCode(Constants.PA_COPN_ONLINE_CODE);
				}
				paRequestMap.setReqApiCode(apiInfo.get("apiInfo").toString());
				//paRequestMap.setReqUrl("["+apiMethod+"]"+apiInfo.get("API_URL").toString());
				paRequestMap.setReqUrl(uriBuilder.build().toString());
				if(paRequestMap.getReqUrl().length() > 300) {
					paRequestMap.setReqUrl(paRequestMap.getReqUrl().substring(0,300));
				}
				paRequestMap.setReqHeader(requestHeader);
				paRequestMap.setRequestMap(apiData);
				paRequestMap.setResponseMap(resultMsg);
				paRequestMap.setRemark("");
				ApiHistoryUtil.insertPaRequestMap(paRequestMap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultObject;
	}
	
	public static String cleanHtmlFragment(String htmlFragment, String attributesToRemove) {
        return htmlFragment.replaceAll("\\s+(?:" + attributesToRemove + ")\\s*=\\s*\"[^\"]*\"","");    
    }
	
	public static String parse(String testString) {
		Matcher m;
		Pattern p;
		 
		String widthPattern = "width\\s*=\\s*('|\")[0-9]*('|\")";
		String heightPattern = "height\\s*=\\s*('|\")[0-9]*('|\")";
		String stylePattern = "style\\s*=\\s*('|\").*?('|\")";
		String EmbedPattern = "</?embed\\s*(\\s*\\w*\\s*=\\s*('|\").*?('|\"))*\\s*>";
		String reg[] = { 
							widthPattern, 
							heightPattern, 
							stylePattern, 
							EmbedPattern 
						};
		
		for (String s : reg) {
			p = Pattern.compile(s);
			m = p.matcher(testString);
			testString = m.replaceAll("");
		}
		
		return testString;
	}
	
	/**
	 * 인터파크 Connection Setting(connectTime 설정)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection paIntpConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, int connectTimeOut, int readTimeOut) throws Exception{
		return paIntpConnectionProc(apiInfo,paCode,requestType,parameter,connectTimeOut,readTimeOut, ConfigUtil.getString("PAINTP_COM_BASE_URL"));
	}
	
	/**
	 * 인터파크 Connection Setting(URL TYPE 설정)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection paIntpConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, String urlType) throws Exception{
		return paIntpConnectionProc(apiInfo,paCode,requestType,parameter,ConfigUtil.getInt("URL_CONNECT_TIMEOUT"),ConfigUtil.getInt("URL_READ_TIMEOUT"), urlType);
	}
	
	/**
	 * 인터파크 Connection Setting(default)
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection paIntpConnectionSetting(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter) throws Exception{
		return paIntpConnectionProc(apiInfo,paCode,requestType,parameter,ConfigUtil.getInt("URL_CONNECT_TIMEOUT"),ConfigUtil.getInt("URL_READ_TIMEOUT"), ConfigUtil.getString("PAINTP_GOODS_COM_BASE_URL"));
	}
	
	public static HttpURLConnection paIntpConnectionProc(HashMap<String, String> apiInfo, String paCode, String requestType, String parameter, int connectTimeOut, int readTimeOut, String urlType) throws Exception{
		HttpURLConnection conn = null;
		URL url = new URL(urlType+apiInfo.get("API_URL")+parameter);
		conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(readTimeOut);
		conn.setRequestMethod(requestType);
		conn.setRequestProperty("openapikey", apiInfo.get(paCode));
		conn.setRequestProperty("Content-Type", apiInfo.get("contentType")==null?"application/xml":apiInfo.get("contentType").toString());
		conn.setRequestProperty("Accept", "application/xml");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		return conn;
	}
	
	/**
	 * 인터파크 API 호출
	 */
	public static HttpResponse paIntpConnectionSetting(HashMap<String, String> apiInfo, Map<String, String> param) throws Exception{
		
		HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
		String[] apiKeys = null;
		apiKeys = apiInfo.get(apiInfo.get("paName")).split(";");
		String urlType = "";
		if("IF_PAINTPAPI_00_009".equals(apiInfo.get("apiInfo"))) {
			urlType = ConfigUtil.getString("PAINTP_COM_BASE_URL");
		}else {
			urlType = ConfigUtil.getString("PAINTP_GOODS_COM_BASE_URL");			
		}
		String citeKey = apiKeys[0];
		String secretKey = apiKeys[1];
		
		StringBuilder sb = new StringBuilder(apiInfo.get("API_URL"));
		
		sb.append("&");
		
		if (param != null && param.size() > 0) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if(!"dataUrl".equals(entry.getKey()))
				{
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "EUC-KR")).append("&");
				}
				else
				{
					sb.append("dataUrl").append("=").append(URLEncoder.encode(ConfigUtil.getString("PAINTP_GOODS_XML_URL")+entry.getValue(), "EUC-KR")).append("&");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		
		String url = sb.toString();
		url = url.replace("{citeKey}", URLEncoder.encode(citeKey, "EUC-KR"));
		url = url.replace("{secretKey}", URLEncoder.encode(secretKey, "EUC-KR"));
		
		HttpGet getRequest = new HttpGet(urlType+url); //GET 메소드 URL 생성
		HttpResponse response = client.execute(getRequest);
		System.out.println("url ===> " + url);
		
		return response;

	}
	
	// 응답 JSON 파싱하여 ParamMap으로 반환
	public static ParamMap parseIntpCommonResponse(HttpResponse response) throws Exception {

		ParamMap returnMap = null;
		try {

			if (response != null) {
				returnMap = new ParamMap();

				HttpEntity entity = response.getEntity(); 
				InputStream body = entity.getContent(); 
				
				returnMap.put("code", response.getStatusLine().getStatusCode());
				returnMap.put("data", ComUtil.parseXML(body));
			}
		} catch (Exception e) {
			throw e;
		}

		return returnMap;
	}
	
	public static JSONArray parseIntpCommonResponseToJson(HttpResponse response) throws Exception {
		JSONArray result = null;
		try {
			if (response != null) {

				HttpEntity entity = response.getEntity();
				if(entity != null) {
					String retSrc = EntityUtils.toString(entity);
					result = new JSONArray(retSrc);
				}
			}
		} catch (Exception e) {
			
		} finally {
			
		}

		return result;
	}
	
	public static String subStringBytes(String str, int byteLength, int sizePerLetter) {

		int retLength = 0;
		int tempSize = 0;
		int asc;

		if(str == null || "".equals(str) || "null".equals(str)){
			str = "";

		}

		int length = str.length();

		for(int i = 1; i <= length; i++){
			asc = (int) str.charAt(i-1);
			if(asc > 127){
				if(byteLength >= tempSize + sizePerLetter){
					tempSize += sizePerLetter;
					retLength++;     
				}
			}else{
				if(byteLength > tempSize){
					tempSize ++;
					retLength++;
				}
			}
		}

		return str.substring(0,retLength);
	}
	
	/**
	 * 인터파크 xml data to map
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public static ParamMap paIntpXmlToMap(final ParamMap data)
	{
		ParamMap map = new ParamMap();
		Document doc = null;
		doc = (Document) data.get("data");
		String errorCheck = "";
		NodeList childeList = doc.getFirstChild().getChildNodes();
		
		for(int j=0; j<childeList.getLength();j++){
			if("error".equals(childeList.item(j).getNodeName()))
			{
				errorCheck = "error";
			}
			for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
				for(int i=0; i<node.getChildNodes().getLength(); i++){
        			Node directionList = node.getChildNodes().item(i);
        			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
    			}
			}
		}
		
		if("error".equals(errorCheck))
		{
			map.put("ERROR", "ERR");
		}else{
			map.put("ERROR", "SUCC");
		}
		
		return map;
	}
	
	public static void makeCdataNode(Document doc, Element item, String nodeName, String nodeValue) throws Exception
	{
		Element node = doc.createElement(nodeName);
		node.appendChild(doc.createCDATASection(ComUtil.isNull(nodeValue, "")));
		item.appendChild(node);
	}
	
	public static void makeNode(Document doc, Element item, String nodeName, String nodeValue) throws Exception
	{
		
		Element node = doc.createElement(nodeName);
		node.appendChild(doc.createTextNode(ComUtil.isNull(nodeValue, "")));
		item.appendChild(node);
		
	}
	
	//이건 굳이?????
	public static String nullToString(String str, String replace){
		if(str == null || str.length() <= 0 || str.toLowerCase() == "null")
			return replace;
		else
			return str;
	}
	
	public static String getArrayToString(String[] array) {
		String result = "";
		
		for(int i = 0; i < array.length; i++) {
			if(i != array.length - 1) {
				result += array[i] +",";
			}else {
				result += array[i];
			}
		}
		
		return result;
	}
	
	public static String getListToString(List list) {
		String result = "";
		
		for(int i = 0; i < list.size(); i++) {
			if(i != list.size() - 1) {
				result += list.get(i) + ",";
			}else {
				result += list.get(i);
			}
		}
		
		return result;
	}

	public static String getClobToString(Object objVal) throws Exception {
		BufferedReader stringReader = new BufferedReader( ((Clob) objVal).getCharacterStream() );
		String singleLine = null;
		StringBuffer strBuff = new StringBuffer();
		while ((singleLine = stringReader.readLine()) != null) {
		    strBuff.append(singleLine);
		}
		return strBuff.toString();
	}
	
	/**
	 * 11번가 InputStream to Document (유니코드 제거)
	 * @param InputStream
	 * @return Document
	 */
	public static Document pa11stParseXML(InputStream stream) throws Exception{
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
 
        try{        	
        	//XXE 취약점 예방
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            objDocumentBuilderFactory.setXIncludeAware(false);
            objDocumentBuilderFactory.setExpandEntityReferences(false);
                        
            //objDocumentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
			
			String str = CharStreams.toString(new InputStreamReader(stream, "euc-kr")); // InputStream to String
            str = str.replaceAll("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFF]+", ""); // 유니코드 제거
            
            stream = new ByteArrayInputStream(str.getBytes("euc-kr")); // String to InputStream
 
            doc = (Document) objDocumentBuilder.parse(stream);
        }catch(Exception ex){
            throw ex;
        }       
        return doc;
    }
	
	/**
	 * String 을 byte 길이 만큼 자르기 (UTF-8)
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public static String subStringUTFBytes(String str, int byteLength) {
    	int length = str.length();
    	int retLength = 0;
    	int tempSize = 0;
    	int asc;
    	for (int i = 1; i <= length; i++) {
    		asc = (int) str.charAt(i - 1);
    		if (asc > 127) {
    			if (byteLength >= tempSize + 3) {
    				tempSize += 3;
    				retLength++;
    			} else {
    				return str.substring(0, retLength);
    			}
    		} else {
    			if (byteLength > tempSize) {
    				tempSize++;
    				retLength++;
    				}
    			}
    		}

    	return str.substring(0, retLength);
    }
	
	/**
	 * 이모지 제거
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String removeEmoji(String str) throws Exception {
		
		String emojiRemoved = "";
		
		emojiRemoved = EmojiParser.removeAllEmojis(str);
		
		return emojiRemoved;
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

	                	
	                }catch(Exception e){ 
	                	System.out.println("ERROR:::::::::::::::" + keyVal);
	                	//e.printStackTrace(); 
	                } 
	            }
	        }
	    }
	    return obj; 
	}

} // end of class
