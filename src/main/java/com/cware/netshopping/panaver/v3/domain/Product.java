package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Product {
		
	//원상품
	private OriginProduct originProduct;
	
	
	public OriginProduct getOriginProduct() {
		return originProduct;
	}
	
	public void setOriginProduct(OriginProduct originProduct) {
		this.originProduct = originProduct;
	}	
	
	@Override
	public String toString() {
		return "Product [OriginProduct=" + originProduct + "]";
	}
		
}
