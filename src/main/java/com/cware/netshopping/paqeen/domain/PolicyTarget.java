package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PolicyTarget {
	
	private int id;
    private String name;
    private boolean assorted;
    private String sellerCompanyCode;
    private List<String> brandCodes;
    private ShippingPolicy shippingPolicy;
    private boolean deliveryContractSigned;
    private boolean autoReturnConfirmation;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAssorted() {
		return assorted;
	}
	public void setAssorted(boolean assorted) {
		this.assorted = assorted;
	}
	public String getSellerCompanyCode() {
		return sellerCompanyCode;
	}
	public void setSellerCompanyCode(String sellerCompanyCode) {
		this.sellerCompanyCode = sellerCompanyCode;
	}
	public List<String> getBrandCodes() {
		return brandCodes;
	}
	public void setBrandCodes(List<String> brandCodes) {
		this.brandCodes = brandCodes;
	}
	public ShippingPolicy getShippingPolicy() {
		return shippingPolicy;
	}
	public void setShippingPolicy(ShippingPolicy shippingPolicy) {
		this.shippingPolicy = shippingPolicy;
	}
	public boolean isDeliveryContractSigned() {
		return deliveryContractSigned;
	}
	public void setDeliveryContractSigned(boolean deliveryContractSigned) {
		this.deliveryContractSigned = deliveryContractSigned;
	}
	public boolean isAutoReturnConfirmation() {
		return autoReturnConfirmation;
	}
	public void setAutoReturnConfirmation(boolean autoReturnConfirmation) {
		this.autoReturnConfirmation = autoReturnConfirmation;
	}

	
	
}
