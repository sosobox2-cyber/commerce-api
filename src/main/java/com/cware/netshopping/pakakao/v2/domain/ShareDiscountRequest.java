package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShareDiscountRequest {

	// 소문내면할인 설정여부
	boolean useShareDiscount;

	// 슬롯당 할인액
	Long value;

	// 공유 횟수 (default 3)
	Integer slotCount;

	// 기간설정 사용여부
	boolean useSalePeriod;

	// 적용 기간
	Period discountPeriod;

	public boolean isUseShareDiscount() {
		return useShareDiscount;
	}

	public void setUseShareDiscount(boolean useShareDiscount) {
		this.useShareDiscount = useShareDiscount;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Integer getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(Integer slotCount) {
		this.slotCount = slotCount;
	}

	public boolean isUseSalePeriod() {
		return useSalePeriod;
	}

	public void setUseSalePeriod(boolean useSalePeriod) {
		this.useSalePeriod = useSalePeriod;
	}

	public Period getDiscountPeriod() {
		return discountPeriod;
	}

	public void setDiscountPeriod(Period discountPeriod) {
		this.discountPeriod = discountPeriod;
	}

	@Override
	public String toString() {
		return "ShareDiscountRequest [useShareDiscount=" + useShareDiscount + ", value=" + value + ", slotCount="
				+ slotCount + ", useSalePeriod=" + useSalePeriod + ", discountPeriod=" + discountPeriod + "]";
	}

}
