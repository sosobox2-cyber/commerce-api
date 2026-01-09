package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerCodeInfo {
		
	// 판매자 관리 코드
	private String sellerManagementCode;
	// 판매자 바코드
	private String sellerBarcode;
	// 판매자 내부 코드 1
	private String sellerCustomCode1;
	// 판매자 내부 코드 2)
	private String sellerCustomCode2;

	
	public String getSellerManagementCode() {
		return sellerManagementCode;
	}
	public void setSellerManagementCode(String sellerManagementCode) {
		this.sellerManagementCode = sellerManagementCode;
	}
	public String getSellerBarcode() {
		return sellerBarcode;
	}
	public void setSellerBarcode(String sellerBarcode) {
		this.sellerBarcode = sellerBarcode;
	}
	public String getSellerCustomCode1() {
		return sellerCustomCode1;
	}
	public void setSellerCustomCode1(String sellerCustomCode1) {
		this.sellerCustomCode1 = sellerCustomCode1;
	}
	public String getSellerCustomCode2() {
		return sellerCustomCode2;
	}
	public void setSellerCustomCode2(String sellerCustomCode2) {
		this.sellerCustomCode2 = sellerCustomCode2;
	}

	@Override
	public String toString() {
		return "SellerCodeInfo [sellerManagementCode=" + sellerManagementCode +"sellerBarcode=" + sellerBarcode + "sellerCustomCode1=" + sellerCustomCode1 + "sellerCustomCode2=" + sellerCustomCode2	+"]";
	}

}
