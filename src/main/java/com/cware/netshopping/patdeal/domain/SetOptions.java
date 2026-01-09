package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetOptions {
	
	private String mallOptionNo ;
	private String productManagementCd ;
	private String optionUseYn ;
	private String optionValue ;
	private long count ;
	private long optionPrice ;
	private String sku ;
	private String optionName ;
	private String optionManagementCd ;
	private String mallProductNo ;
	private String stockNo ;
	private String productName ;
	
	public String getMallOptionNo() {
		return mallOptionNo;
	}
	public void setMallOptionNo(String mallOptionNo) {
		this.mallOptionNo = mallOptionNo;
	}
	public String getProductManagementCd() {
		return productManagementCd;
	}
	public void setProductManagementCd(String productManagementCd) {
		this.productManagementCd = productManagementCd;
	}
	public String getOptionUseYn() {
		return optionUseYn;
	}
	public void setOptionUseYn(String optionUseYn) {
		this.optionUseYn = optionUseYn;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(long optionPrice) {
		this.optionPrice = optionPrice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public String getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "SetOptions [mallOptionNo=" + mallOptionNo + ", productManagementCd=" + productManagementCd
				+ ", optionUseYn=" + optionUseYn + ", optionValue=" + optionValue + ", count=" + count
				+ ", optionPrice=" + optionPrice + ", sku=" + sku + ", optionName=" + optionName
				+ ", optionManagementCd=" + optionManagementCd + ", mallProductNo=" + mallProductNo + ", stockNo="
				+ stockNo + ", productName=" + productName + "]";
	}

	
	
}
