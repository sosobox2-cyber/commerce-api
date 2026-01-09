package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Template {
	
	// 기본 템플릿 여부
	@JsonProperty("default")
	private boolean defaultYn;
	// 배송비 템플릿 번호
	private String templateNo;
	// 배송비 설정
	private DeliveryFee deliveryFee;
	// 배송 방법 [ PARCEL_DELIVERY: 택배/등기/소포, DIRECT_DELIVERY: 직접배송(화물배달) ]
	private String deliveryType;
	// 배송비 템플릿 명
	private String name;
	// 반품지 번호
	private String returnWarehouseNo;
	// 배송 회사
	private String deliveryCompanyType;
	// 출고지 번호
	private String releaseWarehouseNo;
	
	
	public boolean isDefaultYn() {
		return defaultYn;
	}
	public void setDefaultYn(boolean defaultYn) {
		this.defaultYn = defaultYn;
	}
	public String getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}
	public DeliveryFee getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(DeliveryFee deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReturnWarehouseNo() {
		return returnWarehouseNo;
	}
	public void setReturnWarehouseNo(String returnWarehouseNo) {
		this.returnWarehouseNo = returnWarehouseNo;
	}
	public String getDeliveryCompanyType() {
		return deliveryCompanyType;
	}
	public void setDeliveryCompanyType(String deliveryCompanyType) {
		this.deliveryCompanyType = deliveryCompanyType;
	}
	public String getReleaseWarehouseNo() {
		return releaseWarehouseNo;
	}
	public void setReleaseWarehouseNo(String releaseWarehouseNo) {
		this.releaseWarehouseNo = releaseWarehouseNo;
	}
	
	@Override
	public String toString() {
		return "Template [defaultYn=" + defaultYn + ", templateNo=" + templateNo + ", deliveryFee=" + deliveryFee
				+ ", deliveryType=" + deliveryType + ", name=" + name + ", returnWarehouseNo=" + returnWarehouseNo
				+ ", deliveryCompanyType=" + deliveryCompanyType + ", releaseWarehouseNo=" + releaseWarehouseNo + "]";
	}
	
}
