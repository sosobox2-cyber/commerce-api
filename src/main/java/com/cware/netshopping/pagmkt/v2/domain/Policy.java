package com.cware.netshopping.pagmkt.v2.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Policy {

	// 출하지 번호
	private String placeNo;
	
	// 묶음배송비 설정, 상품별 배송비 여부 설정
	// 1 : 묶음배송비
	// 2 : 상품별배송비
	private int feeType;

	// 묶음 배송비 정책
	private Bundle bundle;

	// 상품별 배송비 정책
	private Map<String, Object> each;

	// Document에 없는 속성
	private Map<String, Object> is3plDeliveryFeeFree;

	public String getPlaceNo() {
		return placeNo;
	}

	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}

	public int getFeeType() {
		return feeType;
	}

	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public Map<String, Object> getEach() {
		return each;
	}

	public void setEach(Map<String, Object> each) {
		this.each = each;
	}

	public Map<String, Object> getIs3plDeliveryFeeFree() {
		return is3plDeliveryFeeFree;
	}

	public void setIs3plDeliveryFeeFree(Map<String, Object> is3plDeliveryFeeFree) {
		this.is3plDeliveryFeeFree = is3plDeliveryFeeFree;
	}

	@Override
	public String toString() {
		return "Policy [placeNo=" + placeNo + ", feeType=" + feeType + ", bundle=" + bundle + ", each=" + each
				+ ", is3plDeliveryFeeFree=" + is3plDeliveryFeeFree + "]";
	}

}
