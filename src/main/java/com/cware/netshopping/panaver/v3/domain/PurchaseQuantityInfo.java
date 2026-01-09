package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PurchaseQuantityInfo {
		
	// 최소 구매 수량
	@JsonInclude(Include.NON_DEFAULT)
	private long minPurchaseQuantity;
	// 1인 최대 구매 수량
	private long  maxPurchaseQuantityPerId;
	// 1회 최대 구매 수량
	private long  maxPurchaseQuantityPerOrder;
	
	
	public long getMinPurchaseQuantity() {
		return minPurchaseQuantity;
	}
	public void setMinPurchaseQuantity(long minPurchaseQuantity) {
		this.minPurchaseQuantity = minPurchaseQuantity;
	}
	public long getMaxPurchaseQuantityPerId() {
		return maxPurchaseQuantityPerId;
	}
	public void setMaxPurchaseQuantityPerId(long maxPurchaseQuantityPerId) {
		this.maxPurchaseQuantityPerId = maxPurchaseQuantityPerId;
	}
	public long getMaxPurchaseQuantityPerOrder() {
		return maxPurchaseQuantityPerOrder;
	}
	public void setMaxPurchaseQuantityPerOrder(long maxPurchaseQuantityPerOrder) {
		this.maxPurchaseQuantityPerOrder = maxPurchaseQuantityPerOrder;
	}

	@Override
	public String toString() {
		return "PurchaseQuantityInfo [minPurchaseQuantity=" + minPurchaseQuantity +"maxPurchaseQuantityPerId=" + maxPurchaseQuantityPerId +"maxPurchaseQuantityPerOrder=" + maxPurchaseQuantityPerOrder + "]";
	}

}
