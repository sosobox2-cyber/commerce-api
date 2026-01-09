package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeliveryFeeByArea {
		
	// 지역별 추가 배송비 권역 코드
	private String deliveryAreaType;
	// 2권역 추가 배송비
	private int area2extraFee;
	// 3권역 추가 배송비
	private int area3extraFee;
	
	
	public String getDeliveryAreaType() {
		return deliveryAreaType;
	}

	public void setDeliveryAreaType(String deliveryAreaType) {
		this.deliveryAreaType = deliveryAreaType;
	}

	public int getArea2extraFee() {
		return area2extraFee;
	}

	public void setArea2extraFee(int area2extraFee) {
		this.area2extraFee = area2extraFee;
	}

	public int getArea3extraFee() {
		return area3extraFee;
	}

	public void setArea3extraFee(int area3extraFee) {
		this.area3extraFee = area3extraFee;
	}

	@Override
	public String toString() {
		return "DeliveryFeeByArea [deliveryAreaType=" + deliveryAreaType +"area2extraFee=" + area2extraFee + "area3extraFee=" + area3extraFee +"]";
	}

}
