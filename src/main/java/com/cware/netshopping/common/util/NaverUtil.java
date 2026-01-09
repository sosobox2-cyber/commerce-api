package com.cware.netshopping.common.util;

public class NaverUtil {
	
	public static String getErrorCodeDesc(String errorCode) {
		
		String errorDescribe = "";
		
		switch(errorCode) {
			case "ERR-PRD-000101":
				errorDescribe = "error occured while processing request (look for error details)";
				break;
			case "ERR-PRD-000102":
				errorDescribe = "validation error (look for error details)";
				break;
			case "ERR-PRD-000201":
				errorDescribe = "nbp internal error";
				break;
			case "ERR-PRD-000301":
				errorDescribe = "sellerId error";
				break;
			case "ERR-PRD-000302":
				errorDescribe = "productId null";
				break;
			case "ERR-PRD-000303":
				errorDescribe = "the productId is not exists";
				break;
			case "ERR-PRD-000304":
				errorDescribe = "license error";
				break;
			case "ERR-PRD-000305":
				errorDescribe = "session key expired";
				break;
			case "ERR-PRD-000306":
				errorDescribe = "tried to update an event product(goods)";
				break;
			case "ERR-PRD-000307":
				errorDescribe = "sellerNoticeId null";
				break;
			case "ERR-PRD-000308":
				errorDescribe = "sellerNoticeId is not exists";
				break;
			case "ERR-PRD-000309":
				errorDescribe = "unauthorized to access the requested data";
				break;
			case "ERR-PRD-000202":
				errorDescribe = "unauthorized seller";
				break;
			default:
				errorDescribe = "unknown error : " + errorCode;
				break;
		}
		
		return errorDescribe;
	}

}
