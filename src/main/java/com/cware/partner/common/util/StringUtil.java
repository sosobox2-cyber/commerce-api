package com.cware.partner.common.util;

import org.springframework.util.StringUtils;

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

}
