package com.cware.netshopping.patdeal.domain;

import java.util.List;


public class ProductNoMap {
	// 티딜 상품번호
	String mallProductNo; 
	// 티딜 옵션번호(등록한 순서대로)
	List<String> mallProductOptionNos;
	// 구매자작성 번호(등록한 순서대로)
	List<String> mallProductInputNos;
	public String getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public List<String> getMallProductOptionNos() {
		return mallProductOptionNos;
	}
	public void setMallProductOptionNos(List<String> mallProductOptionNos) {
		this.mallProductOptionNos = mallProductOptionNos;
	}
	public List<String> getMallProductInputNos() {
		return mallProductInputNos;
	}
	public void setMallProductInputNos(List<String> mallProductInputNos) {
		this.mallProductInputNos = mallProductInputNos;
	}
	
	
	
}
