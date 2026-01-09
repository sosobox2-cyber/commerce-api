package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class BundledProductItemInfo {
	
    private String productId;
    private String productItemId;
    private Integer bundledQuantity;
    
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
	public Integer getBundledQuantity() {
		return bundledQuantity;
	}
	public void setBundledQuantity(Integer bundledQuantity) {
		this.bundledQuantity = bundledQuantity;
	}
    
    
    
	
}
