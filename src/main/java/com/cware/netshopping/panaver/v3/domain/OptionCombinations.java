package com.cware.netshopping.panaver.v3.domain;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionCombinations {

	// 조합형 옵션 ID
	private BigInteger id;
	// 조합형 옵션값 1
	private String optionName1;
	// 조합형 옵션값 2
	private String optionName2;
	// 조합형 옵션값 3
	private String optionName3;
	// 조합형 옵션값 4
	private String optionName4;
	// 재고 수량
	private long stockQuantity;
	// 옵션가
	private double price;
	// 판매자 관리 코드
	private String sellerManagerCode;
	// 사용 여부
	private String usable;
		
	
	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
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

	public String getOptionName3() {
		return optionName3;
	}

	public void setOptionName3(String optionName3) {
		this.optionName3 = optionName3;
	}

	public String getOptionName4() {
		return optionName4;
	}

	public void setOptionName4(String optionName4) {
		this.optionName4 = optionName4;
	}

	public long getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(long stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
		return "OptionCombinations [id=" + id + ", optionName1=" + optionName1 + ", optionName2=" + optionName2 + ", optionName3=" + optionName3
				+ "optionName4=" + optionName4 + ", stockQuantity=" + stockQuantity + ", price=" + price + ", sellerManagerCode=" + sellerManagerCode
				+ "usable=" + usable + "]";
	}

}
