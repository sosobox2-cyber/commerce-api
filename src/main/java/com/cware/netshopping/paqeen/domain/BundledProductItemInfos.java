package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BundledProductItemInfos {
	private String productId;
	private String productItemId;
	private int bundledQuantity;
	private String mallProductCode;
	private String optionName;
	private String productItemCode;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductItemId() {
		return productItemId;
	}
	public void setProductItemId(String productItemId) {
		this.productItemId = productItemId;
	}
	public int getBundledQuantity() {
		return bundledQuantity;
	}
	public void setBundledQuantity(int bundledQuantity) {
		this.bundledQuantity = bundledQuantity;
	}
	public String getMallProductCode() {
		return mallProductCode;
	}
	public void setMallProductCode(String mallProductCode) {
		this.mallProductCode = mallProductCode;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getProductItemCode() {
		return productItemCode;
	}
	public void setProductItemCode(String productItemCode) {
		this.productItemCode = productItemCode;
	}
	
}
