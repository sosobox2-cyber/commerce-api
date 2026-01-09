package com.cware.netshopping.pawemp.system.constants;

import com.cware.netshopping.common.util.ConfigUtil;

public class WempConstants {

	/**
	 * API METHOD
	 */
	public static final String POST			= "POST";
	public static final String GET			= "GET";

	/**
	 * HTTP CODE
	 */
	public static final int OK				= 200;
	public static final int CREATED			= 201;
	public static final int UNAUTHORIZED	= 401;
	public static final int FORBIDDE		= 403;
	public static final int NOT_FOUND		= 404;
	public static final int VALIDITY_ERROR	= 422;
	public static final int SYSTEM_ERROR	= 500;
	
	/**
	 * API KEY
	 */
	public static final String API_KEY = ConfigUtil.getString("API_KEY");
	
	/**
	 * 임시 API 정의
	 */
	
	public static final String PRODUCT_OUT_GET_CATEGORY = "/product/out/getCategory";
	
}
