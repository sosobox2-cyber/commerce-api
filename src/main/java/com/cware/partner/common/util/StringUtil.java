package com.cware.partner.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 문자열 관련 유틸리티
 *
 */
@Slf4j
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

		if (str == null || str.length() == 0 || bytes < 1)
			return "";

		int len = str.length();

		int endIndex = 0;

		int curBytes = 0;
		String ch = null;
		for (int i = 0; i < len; i++) {
			ch = str.substring(i, i + 1);
			curBytes += ch.getBytes().length;
			if (curBytes > bytes) break;
			endIndex = i + 1;
		}

		return str.substring(0, endIndex);
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
			log.error("{} {}", jsonString, ex.getMessage());
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
}
