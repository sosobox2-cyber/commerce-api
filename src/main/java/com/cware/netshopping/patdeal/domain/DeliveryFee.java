package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeliveryFee {
	
	// 배송비 유형 [ FREE: 무료, CONDITIONAL: 조건부 무료, FIXED_FEE: 유료(고정배송비), QUANTITY_PROPOSITIONAL_FEE: 수량 비례, PRICE_FEE: 금액별 차등, QUANTITY_FEE: 수량별 차등 ]
	private String deliveryConditionType;
	// 배송비(조건부 무료, 유료일 때)
	private double deliveryAmt;
	// 지역별 추가배송비 조건부 무료 체크 여부(현재 false 고정)
	private boolean remoteAreaFeeConditionCheck;
	// 수량 비례 조건에서 수량
	private String perOrderCnt;
	// 조건부 무료 조건
	private double criteria;
	// 반품 배송비
	private double returnDeliveryAmt;
	// 차등 조건 구간
	private DeliveryFeeRanges deliveryFeeRanges;
	// 배송비 유형 라벨
	private String deliveryConditionTypeLabel;
	
	public String getDeliveryConditionType() {
		return deliveryConditionType;
	}
	public void setDeliveryConditionType(String deliveryConditionType) {
		this.deliveryConditionType = deliveryConditionType;
	}
	public double getDeliveryAmt() {
		return deliveryAmt;
	}
	public void setDeliveryAmt(double deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
	}
	public boolean isRemoteAreaFeeConditionCheck() {
		return remoteAreaFeeConditionCheck;
	}
	public void setRemoteAreaFeeConditionCheck(boolean remoteAreaFeeConditionCheck) {
		this.remoteAreaFeeConditionCheck = remoteAreaFeeConditionCheck;
	}
	public String getPerOrderCnt() {
		return perOrderCnt;
	}
	public void setPerOrderCnt(String perOrderCnt) {
		this.perOrderCnt = perOrderCnt;
	}
	public double getCriteria() {
		return criteria;
	}
	public void setCriteria(double criteria) {
		this.criteria = criteria;
	}
	public double getReturnDeliveryAmt() {
		return returnDeliveryAmt;
	}
	public void setReturnDeliveryAmt(double returnDeliveryAmt) {
		this.returnDeliveryAmt = returnDeliveryAmt;
	}
	public DeliveryFeeRanges getDeliveryFeeRanges() {
		return deliveryFeeRanges;
	}
	public void setDeliveryFeeRanges(DeliveryFeeRanges deliveryFeeRanges) {
		this.deliveryFeeRanges = deliveryFeeRanges;
	}
	public String getDeliveryConditionTypeLabel() {
		return deliveryConditionTypeLabel;
	}
	public void setDeliveryConditionTypeLabel(String deliveryConditionTypeLabel) {
		this.deliveryConditionTypeLabel = deliveryConditionTypeLabel;
	}
	
	@Override
	public String toString() {
		return "DeliveryFee [deliveryConditionType=" + deliveryConditionType + ", deliveryAmt=" + deliveryAmt
				+ ", remoteAreaFeeConditionCheck=" + remoteAreaFeeConditionCheck + ", perOrderCnt=" + perOrderCnt
				+ ", criteria=" + criteria + ", returnDeliveryAmt=" + returnDeliveryAmt + ", deliveryFeeRanges="
				+ deliveryFeeRanges + ", deliveryConditionTypeLabel=" + deliveryConditionTypeLabel + "]";
	}
	
}
