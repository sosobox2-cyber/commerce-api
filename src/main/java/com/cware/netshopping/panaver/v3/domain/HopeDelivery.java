package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class HopeDelivery {
		
	//지역
	private String region;
	//배송 희망 지역 설정 배송비
	private double additionalFee;
	//배송 희망일. yyyymmdd 형식의 연월일
	private Timestamp hopeDeliveryYmd;
	//배송 희망 시간. HHmm 형식의 시간
	private Timestamp hopeDeliveryHm;
	//변경 사유
	private String changeReason;
	//변경한 사용자(구매자/판매자/판매자 API)
	private String changer;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public double getAdditionalFee() {
		return additionalFee;
	}
	public void setAdditionalFee(double additionalFee) {
		this.additionalFee = additionalFee;
	}
	public Timestamp getHopeDeliveryYmd() {
		return hopeDeliveryYmd;
	}
	public void setHopeDeliveryYmd(Timestamp hopeDeliveryYmd) {
		this.hopeDeliveryYmd = hopeDeliveryYmd;
	}
	public Timestamp getHopeDeliveryHm() {
		return hopeDeliveryHm;
	}
	public void setHopeDeliveryHm(Timestamp hopeDeliveryHm) {
		this.hopeDeliveryHm = hopeDeliveryHm;
	}
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	public String getChanger() {
		return changer;
	}
	public void setChanger(String changer) {
		this.changer = changer;
	}
	@Override
	public String toString() {
		return "HopeDelivery [region=" + region + ", additionalFee=" + additionalFee + ", hopeDeliveryYmd="
				+ hopeDeliveryYmd + ", hopeDeliveryHm=" + hopeDeliveryHm + ", changeReason=" + changeReason
				+ ", changer=" + changer + "]";
	}
	
	
}
