package com.cware.netshopping.common.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.collections.ExtendedProperties;

public class ConfigUtil {
	
	public static ExtendedProperties cwareProperties = null;
	
	/**
	 * boolean 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *             프로퍼티키
	 * @return boolean 타입의 값
	 */
	public static boolean getBoolean(String name) {
		return cwareProperties.getBoolean(name);
	}

	/**
	 * boolean 타입의 프로퍼티 값 얻기(디폴트값을 입력받음)
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return boolean 타입의 값
	 */
	public static boolean getBoolean(String name, boolean def) {
		return cwareProperties.getBoolean(name, def);
	}

	/**
	 * double 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return double 타입의 값
	 */
	public static double getDouble(String name) {
		return cwareProperties.getDouble(name);
	}

	/**
	 * double 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return double 타입의 값
	 */
	public static double getDouble(String name, double def) {
		return cwareProperties.getDouble(name, def);
	}

	/**
	 * float 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return Float 타입의 값
	 */
	public static float getFloat(String name) {
		return cwareProperties.getFloat(name);
	}

	/**
	 * float 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return float 타입의 값
	 */
	public static float getFloat(String name, float def) {
		return cwareProperties.getFloat(name, def);
	}

	/**
	 * int 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return int 타입의 값
	 */
	public static int getInt(String name) {
		return cwareProperties.getInt(name);
	}

	/**
	 * int 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return int 타입의 값
	 */
	public static int getInt(String name, int def) {
		return cwareProperties.getInt(name, def);
	}

	/**
	 * 프로퍼티 키 목록 읽기
	 * 
	 * @return Key를 위한 Iterator 
	 */
	public static Iterator<?> getKeys() {
		return cwareProperties.getKeys();
	}

	/**
	 * prefix를 이용한 키 목록 읽기
	 * 
	 * @param prefix
	 *             prefix
	 * @return prefix에 매칭되는 키목록
	 */
	public static Iterator<?> getKeys(String prefix) {
//		cwareProperties.values();
		return cwareProperties.getKeys(prefix);
	}

	/**
	 * long 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return long 타입의 값
	 */
	public static long getLong(String name) {
		return cwareProperties.getLong(name);
	}

	/**
	 * long 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return long 타입의 값
	 */
	public static long getLong(String name, long def) {
		return cwareProperties.getLong(name, def);
	}

	/**
	 * String 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return String 타입의 값
	 */
	public static String getString(String name) {
		return cwareProperties.getString(name);
	}

	/**
	 * String 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return String 타입의 값
	 */
	public static String getString(String name, String def) {
		return cwareProperties.getString(name, def);
	}

	/**
	 * String[] 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return String[] 타입의 값
	 */
	public static String[] getStringArray(String name) {
		return cwareProperties.getStringArray(name);
	}

	/**
	 * Vector 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @return Vector 타입의 값
	 */
	public static Vector<?> getVector(String name) {
		return cwareProperties.getVector(name);
	}

	/**
	 * Vector 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 
	 *            프로퍼티키
	 * @param def
	 *            디폴트 값
	 * @return Vector 타입의 값
	 */
	public static Vector<?> getVector(String name, Vector<?> def) {
		return cwareProperties.getVector(name, def);
	}
	
	/**
	 * 전체 키/값 쌍 얻기
	 * 
	 * @return Vector 타입의 값
	 */
	public static Collection<?> getAllKeyValue(){		
		return cwareProperties.values();
	}

	/**
	 * cwareProperties 얻기 
	 * 
	 * @return Properties of requested Service.
	 */
	@SuppressWarnings("unused")
	private ExtendedProperties getConfiguration() {
		return cwareProperties;
	}
	
	public static void setConfiguration(ExtendedProperties argProperties){
		cwareProperties = argProperties;
	}
}
