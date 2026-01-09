package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DeliveryFee {
		
	// 배송비 타입
	private String deliveryFeeType;
	// 기본 배송비
	private int baseFee;
	// 무료 조건 금액
	private int freeConditionalAmount;
	// 기본 배송비 반복 부과 수량
	private int repeatQuantity;
	// 배송비 조건 2구간 수량
	private int secondBaseQuantity;	
	// 배송비 조건 2구간 수량 초과 시 추가 배송비
	private int secondExtraFee;	
	// 배송비 조건 3구간 수량
	private int thirdBaseQuantity;	
	// 배송비 조건 3구간 초과 시 추가 배송비
	private int thirdExtraFee;	
	// 배송비 결제 방식 코드
	private String deliveryFeePayType;	
	// 지역별 추가 배송비
	private DeliveryFeeByArea deliveryFeeByArea;	
	// 지역별 차등 배송비 정보
	private String differentialFeeByArea;	
		
	
	public String getDeliveryFeeType() {
		return deliveryFeeType;
	}

	public void setDeliveryFeeType(String deliveryFeeType) {
		this.deliveryFeeType = deliveryFeeType;
	}

	public int getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(int baseFee) {
		this.baseFee = baseFee;
	}

	public int getFreeConditionalAmount() {
		return freeConditionalAmount;
	}

	public void setFreeConditionalAmount(int freeConditionalAmount) {
		this.freeConditionalAmount = freeConditionalAmount;
	}

	public int getRepeatQuantity() {
		return repeatQuantity;
	}

	public void setRepeatQuantity(int repeatQuantity) {
		this.repeatQuantity = repeatQuantity;
	}

	public int getSecondBaseQuantity() {
		return secondBaseQuantity;
	}

	public void setSecondBaseQuantity(int secondBaseQuantity) {
		this.secondBaseQuantity = secondBaseQuantity;
	}

	public int getSecondExtraFee() {
		return secondExtraFee;
	}

	public void setSecondExtraFee(int secondExtraFee) {
		this.secondExtraFee = secondExtraFee;
	}

	public int getThirdBaseQuantity() {
		return thirdBaseQuantity;
	}

	public void setThirdBaseQuantity(int thirdBaseQuantity) {
		this.thirdBaseQuantity = thirdBaseQuantity;
	}

	public int getThirdExtraFee() {
		return thirdExtraFee;
	}

	public void setThirdExtraFee(int thirdExtraFee) {
		this.thirdExtraFee = thirdExtraFee;
	}

	public String getDeliveryFeePayType() {
		return deliveryFeePayType;
	}

	public void setDeliveryFeePayType(String deliveryFeePayType) {
		this.deliveryFeePayType = deliveryFeePayType;
	}

	public DeliveryFeeByArea getDeliveryFeeByArea() {
		return deliveryFeeByArea;
	}

	public void setDeliveryFeeByArea(DeliveryFeeByArea deliveryFeeByArea) {
		this.deliveryFeeByArea = deliveryFeeByArea;
	}

	public String getDifferentialFeeByArea() {
		return differentialFeeByArea;
	}

	public void setDifferentialFeeByArea(String differentialFeeByArea) {
		this.differentialFeeByArea = differentialFeeByArea;
	}

	@Override
	public String toString() {
		return "DeliveryFee [deliveryFeeType=" + deliveryFeeType +"baseFee=" + baseFee + "freeConditionalAmount=" + freeConditionalAmount + "repeatQuantity=" + repeatQuantity
				+ "secondBaseQuantity=" + secondBaseQuantity +"secondExtraFee=" + secondExtraFee +"thirdBaseQuantity=" + thirdBaseQuantity +"thirdExtraFee=" + thirdExtraFee
				+"deliveryFeeByArea=" + deliveryFeeByArea +"deliveryFeePayType=" + deliveryFeePayType+"differentialFeeByArea=" + differentialFeeByArea +"]";
	}

}
