package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerReviewPointRequest {

	// 리뷰 포인트 적립 설정 여부
	boolean usePoint;

	// 리뷰 포인트 적립 정책 아이디
	Long pointId;

	// 텍스트 리뷰 포인트 적립액
	Integer value;

	// 포토 리뷰 포인트 적립액
	Integer photoValue;

	// 포인트 적립기간
	Period period;

	public boolean isUsePoint() {
		return usePoint;
	}

	public void setUsePoint(boolean usePoint) {
		this.usePoint = usePoint;
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getPhotoValue() {
		return photoValue;
	}

	public void setPhotoValue(Integer photoValue) {
		this.photoValue = photoValue;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "SellerReviewPointRequest [usePoint=" + usePoint + ", pointId=" + pointId + ", value=" + value
				+ ", photoValue=" + photoValue + ", period=" + period + "]";
	}

}
