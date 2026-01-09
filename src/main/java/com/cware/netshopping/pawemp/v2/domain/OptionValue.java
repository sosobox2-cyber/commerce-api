package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionValue {
	// 옵션값1 (최대 70자) 필수
	String optionValue1;
	// 옵션값2 (최대 70자)
	String optionValue2;
	// 옵션값3 (최대 70자)
	String optionValue3;
	// 옵션값4 (최대 70자)
	String optionValue4;
	// 옵션값5 (최대 70자)
	String optionValue5;
	// 옵션 판매상태 (A:판매중, S:품절) 필수
	String saleStatus;
	// 옵션 재고수량 (0~99999) 필수
	int stockCount;
	// 옵션 노출여부(Y:노출, N:비노출) 필수
	String displayYn;
	// 업체옵션코드 (최대 50자)
	String sellerOptionCode;

	public String getOptionValue1() {
		return optionValue1;
	}

	public void setOptionValue1(String optionValue1) {
		this.optionValue1 = optionValue1;
	}

	public String getOptionValue2() {
		return optionValue2;
	}

	public void setOptionValue2(String optionValue2) {
		this.optionValue2 = optionValue2;
	}

	public String getOptionValue3() {
		return optionValue3;
	}

	public void setOptionValue3(String optionValue3) {
		this.optionValue3 = optionValue3;
	}

	public String getOptionValue4() {
		return optionValue4;
	}

	public void setOptionValue4(String optionValue4) {
		this.optionValue4 = optionValue4;
	}

	public String getOptionValue5() {
		return optionValue5;
	}

	public void setOptionValue5(String optionValue5) {
		this.optionValue5 = optionValue5;
	}

	public String getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public String getDisplayYn() {
		return displayYn;
	}

	public void setDisplayYn(String displayYn) {
		this.displayYn = displayYn;
	}

	public String getSellerOptionCode() {
		return sellerOptionCode;
	}

	public void setSellerOptionCode(String sellerOptionCode) {
		this.sellerOptionCode = sellerOptionCode;
	}

	@Override
	public String toString() {
		return "OptionValue [optionValue1=" + optionValue1 + ", optionValue2=" + optionValue2 + ", optionValue3="
				+ optionValue3 + ", optionValue4=" + optionValue4 + ", optionValue5=" + optionValue5 + ", saleStatus="
				+ saleStatus + ", stockCount=" + stockCount + ", displayYn=" + displayYn + ", sellerOptionCode="
				+ sellerOptionCode + "]";
	}

}
