package com.cware.partner.common.util;

import java.sql.Timestamp;

/**
 * 날짜 관련 유틸리티
 *
 */
public class DateUtil {

	/**
	 * 타임스탬프 비교
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compare(Timestamp date1, Timestamp date2) {
	    return (date1 == null ? date2 == null : date1.equals(date2));
	}


}
