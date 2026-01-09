package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ProductItem {
    private String id;
    private String title;
    private List<String> optionElementIds;
    private int price;
    private boolean display;
    private String salesStatus;
    private String mallProductItemCode;
    private String releaseAtEstimate;
    private int maxQuantity;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getOptionElementIds() {
		return optionElementIds;
	}
	public void setOptionElementIds(List<String> optionElementIds) {
		this.optionElementIds = optionElementIds;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getMallProductItemCode() {
		return mallProductItemCode;
	}
	public void setMallProductItemCode(String mallProductItemCode) {
		this.mallProductItemCode = mallProductItemCode;
	}
	public String getReleaseAtEstimate() {
		return releaseAtEstimate;
	}
	public void setReleaseAtEstimate(String releaseAtEstimate) {
		this.releaseAtEstimate = releaseAtEstimate;
	}
	public int getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	
    
    
}
