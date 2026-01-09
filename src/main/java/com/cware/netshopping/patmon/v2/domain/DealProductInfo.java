package com.cware.netshopping.patmon.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealProductInfo {
	String productType; // 상품타입
	List<DealProductInfoItem> productInfos; // 고시 정보들

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public List<DealProductInfoItem> getProductInfos() {
		return productInfos;
	}

	public void setProductInfos(List<DealProductInfoItem> productInfos) {
		this.productInfos = productInfos;
	}

	@Override
	public String toString() {
		return "DealProductInfo [productType=" + productType + ", productInfos=" + productInfos + "]";
	}

}
