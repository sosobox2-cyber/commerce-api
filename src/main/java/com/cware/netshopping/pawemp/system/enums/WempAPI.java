package com.cware.netshopping.pawemp.system.enums;

public enum WempAPI {
	
	PRODUCT_GET_CATEGORY("/product/out/getCategory","IF_WEMPAPI_00_001"),
	PRODUCT_GET_NOTICE("/product/out/getNotice","IF_WEMPAPI_00_002");
	
	private String apiUrl;
    private String apiCode;
	
	private WempAPI(String apiUrl,String apiCode){
		this.apiUrl = apiUrl;
		this.apiCode = apiCode;
	}
	
	public String URL(){
		return apiUrl;
	}
	
	public String CODE(){
		return apiCode;
	}
}
