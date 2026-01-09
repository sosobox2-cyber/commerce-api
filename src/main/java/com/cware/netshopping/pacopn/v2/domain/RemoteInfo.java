package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RemoteInfo {
	
	// 택배사 코드
	// https://developers.coupangcorp.com/hc/ko/articles/360034156033
	// 택배사는 복수 등록이 가능하지만, 중복 등록은 불가
	String deliveryCode; 

	// 제주 지역 배송비(원) : 0 또는 최소 1,000원 이상 최대 
	// 아래 배송비 이하로 입력가능
	// HYUNDAI: 20,000원
	// CJGLS: 20,000원
	// HANJIN: 20,000원
	// 그 외: 70,000원
	long jeju;

	// 제주 외 지역 배송비(원)
	// 최대배송비는 제주 지역과 같음
	long notJeju;

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public long getJeju() {
		return jeju;
	}

	public void setJeju(long jeju) {
		this.jeju = jeju;
	}

	public long getNotJeju() {
		return notJeju;
	}

	public void setNotJeju(long notJeju) {
		this.notJeju = notJeju;
	}

	@Override
	public String toString() {
		return "RemoteInfo [deliveryCode=" + deliveryCode + ", jeju=" + jeju + ", notJeju=" + notJeju + "]";
	}
	
}
