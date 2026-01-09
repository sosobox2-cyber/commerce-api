package com.cware.netshopping.common.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 문자열 관련 유틸리티
 *
 */
public class StringUtil {

	/**
	 * 바이트단위로 한글 자르기
	 *
	 * @param str
	 * @param beginBytes
	 * @param endBytes
	 * @return
	 */
	public static String truncate(String str, int bytes) {

		if (str == null || str.isEmpty()  || bytes < 1)
			return "";

		int len = str.length();

		int endIndex = 0;
		int curBytes = 0;
		
		for (int i = 0; i < len; i++) {
			String ch = str.substring(i, i + 1);
			
			curBytes += ch.getBytes().length;
			if (curBytes > bytes) break;
			endIndex = i + 1;
		}

		return str.substring(0, endIndex);
	}

	/**
	 * 문자열 길이로 자르기
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String cut(String str, int length) {

		if (str == null) return null;
		if (str.length() <= length) return str;

		return str.substring(0, length);
	}

	/**
	 * 문자열 길이 초과 생랴
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String ellipsis(String str, int length) {

		if (str == null) return null;
		if (str.length() <= length)	return str;

		return str.substring(0, length - 3) + "...";
	}
	
	/**
	 * 문자열비교
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compare(String str1, String str2) {
	    return (str1 == null ? !StringUtils.hasLength(str2) : str1.equals(str2));
	}

	/**
	 * json 문자열을 Map으로 변환
	 *
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = null;

		try {
			result = mapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
		} catch (Exception ex) {
			result = new HashMap<String, Object>();
			result.put("code", "ERROR");
		}
		return result;
	}

	/**
	 * 오브젝트를 json 문자열로 반환
	 *
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		if (object == null) return "";
		
		ObjectMapper mapper = new ObjectMapper();
		String result;
		try {
			result = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return object.toString();
		} 
		return result;
	}

	/**
	 * 오브젝트를 xml 문자열로 반환
	 *
	 * @param object
	 * @return
	 */
	public static String objectToXml(Object object) {
		if (object == null) return "";
		StringWriter sw = new StringWriter();
		JAXB.marshal(object, sw);
		String xmlString = sw.toString();
		return xmlString;
	}

	public static String objectToXml(JAXBContext context, Object object, String encoding) {

		java.io.StringWriter sw = new StringWriter();

		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(object, sw);
		} catch (JAXBException e) {
			return object.toString();
		}

		return sw.toString();
	}

	/**
	 * xml문자열을 오브젝트로 반환
	 *
	 * @param <T>
	 * @param xmlString
	 * @param objectType
	 * @return
	 */
	public static <T> T xmlToObject(String xmlString, Class<T> objectType) {
		if (xmlString == null) return null;
		StringReader reader = new StringReader(xmlString);
		return JAXB.unmarshal(reader, objectType);
	}
}
