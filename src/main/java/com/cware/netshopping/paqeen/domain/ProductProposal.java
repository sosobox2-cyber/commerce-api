package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ProductProposal {
	
	private Product data;
	@JsonProperty("isModifying")
    private boolean isModifying;
	@JsonProperty("isReified")
    private boolean isReified;
    private long reifiedAtMillis;
    
	public Product getData() {
		return data;
	}
	public void setData(Product data) {
		this.data = data;
	}
	public boolean isModifying() {
		return isModifying;
	}
	public void setModifying(boolean isModifying) {
		this.isModifying = isModifying;
	}
	public boolean isReified() {
		return isReified;
	}
	public void setReified(boolean isReified) {
		this.isReified = isReified;
	}
	public long getReifiedAtMillis() {
		return reifiedAtMillis;
	}
	public void setReifiedAtMillis(long reifiedAtMillis) {
		this.reifiedAtMillis = reifiedAtMillis;
	}
    
    
    
	
}
