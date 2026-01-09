package com.cware.api.panaver.product.type;

import com.cware.framework.core.basic.AbstractModel;

public class PaDeliveryVO extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 
	
	private String shippingAddressId;	// 출고지 주소 번호
	private String returnAddressId;		// 회수지 주소 번호
	private String deliveryCompany;		// 택배사 정보
	
	public String getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(String shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public String getReturnAddressId() {
		return returnAddressId;
	}
	public void setReturnAddressId(String returnAddressId) {
		this.returnAddressId = returnAddressId;
	}
	public String getDeliveryCompany() {
		return deliveryCompany;
	}
	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}
	
}