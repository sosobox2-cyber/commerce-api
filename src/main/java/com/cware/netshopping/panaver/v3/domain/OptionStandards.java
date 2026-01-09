package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionStandards {

	// 속성 ID
	private long id;
	// 표준형 옵션값 1
	private String optionName1;
	// 표준형 옵션값 2
	private String optionName2;
	// 재고 수량
	private int stockQuantity;
	// 판매자 관리 코드
	private String sellerManagerCode;
	// 사용 여부
	private String usable;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOptionName1() {
		return optionName1;
	}

	public void setOptionName1(String optionName1) {
		this.optionName1 = optionName1;
	}

	public String getOptionName2() {
		return optionName2;
	}

	public void setOptionName2(String optionName2) {
		this.optionName2 = optionName2;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getSellerManagerCode() {
		return sellerManagerCode;
	}

	public void setSellerManagerCode(String sellerManagerCode) {
		this.sellerManagerCode = sellerManagerCode;
	}

	public String getUsable() {
		return usable;
	}

	public void setUsable(String usable) {
		this.usable = usable;
	}

	@Override
	public String toString() {
		return "OptionStandards [id=" + id + ", optionName1=" + optionName1 + ", optionName2=" + optionName2 + ", stockQuantity=" + stockQuantity	
				+ "sellerManagerCode" + sellerManagerCode + ", usable=" + usable + "]";
	}

}
