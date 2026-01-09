package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeliveryInfo {
		
	// 배송 방법 유형 코드
	private String deliveryType;
	// 배송 속성 타입 코드
	private String deliveryAttributeType;
	// 택배사
	private String deliveryCompany;
	// 묶음배송 가능 여부
	private String deliveryBundleGroupUsable;
	// 묶음배송 그룹 코드
	private String deliveryBundleGroupId;	
	// 방문주소
	@JsonInclude(Include.NON_DEFAULT)
	private long visitAddressId;	
	// 배송비 정보
	private DeliveryFee deliveryFee;
	// 클레임(반품/교환) 정보
	private ClaimDeliveryInfo claimDeliveryInfo;	
	// 별도 설치비 유무
	private String installationFee;	
	// 발송 예정일 직접 입력 값
	private String expectedDeliveryPeriodDirectInput;	
	// 오늘출발 상품 재고 수량
	private int todayStockQuantity;	
	// 주문 확인 후 제작 상품 여부
	private String customProductAfterOrderYn;	
	// 희망일배송 그룹 번호
	private long hopeDeliveryGroupId;
	//주문 제작 상품 발송 예정일 타입 코드
	private String expectedDeliveryPeriodType;
	
	public String getExpectedDeliveryPeriodType() {
		return expectedDeliveryPeriodType;
	}

	public void setExpectedDeliveryPeriodType(String expectedDeliveryPeriodType) {
		this.expectedDeliveryPeriodType = expectedDeliveryPeriodType;
	}
	
	public String getDeliveryType() {
		return deliveryType;
	}
	
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDeliveryAttributeType() {
		return deliveryAttributeType;
	}
	
	public void setDeliveryAttributeType(String deliveryAttributeType) {
		this.deliveryAttributeType = deliveryAttributeType;
	}
	
	public String getDeliveryCompany() {
		return deliveryCompany;
	}
	
	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}
	
	public String getDeliveryBundleGroupUsable() {
		return deliveryBundleGroupUsable;
	}
	
	public void setDeliveryBundleGroupUsable(String deliveryBundleGroupUsable) {
		this.deliveryBundleGroupUsable = deliveryBundleGroupUsable;
	}
	
	public String getDeliveryBundleGroupId() {
		return deliveryBundleGroupId;
	}
	
	public void setDeliveryBundleGroupId(String deliveryBundleGroupId) {
		this.deliveryBundleGroupId = deliveryBundleGroupId;
	}
	
	public long getVisitAddressId() {
		return visitAddressId;
	}
	
	public void setVisitAddressId(long visitAddressId) {
		this.visitAddressId = visitAddressId;
	}
	
	public String getInstallationFee() {
		return installationFee;
	}
	
	public void setInstallationFee(String installationFee) {
		this.installationFee = installationFee;
	}
	
	public String getExpectedDeliveryPeriodDirectInput() {
		return expectedDeliveryPeriodDirectInput;
	}
	
	public void setExpectedDeliveryPeriodDirectInput(String expectedDeliveryPeriodDirectInput) {
		this.expectedDeliveryPeriodDirectInput = expectedDeliveryPeriodDirectInput;
	}
	
	public int getTodayStockQuantity() {
		return todayStockQuantity;
	}
	
	public void setTodayStockQuantity(int todayStockQuantity) {
		this.todayStockQuantity = todayStockQuantity;
	}
	
	public String getCustomProductAfterOrderYn() {
		return customProductAfterOrderYn;
	}

	public void setCustomProductAfterOrderYn(String customProductAfterOrderYn) {
		this.customProductAfterOrderYn = customProductAfterOrderYn;
	}

	public long getHopeDeliveryGroupId() {
		return hopeDeliveryGroupId;
	}

	public void setHopeDeliveryGroupId(long hopeDeliveryGroupId) {
		this.hopeDeliveryGroupId = hopeDeliveryGroupId;
	}
	
	public DeliveryFee getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(DeliveryFee deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public ClaimDeliveryInfo getClaimDeliveryInfo() {
		return claimDeliveryInfo;
	}

	public void setClaimDeliveryInfo(ClaimDeliveryInfo claimDeliveryInfo) {
		this.claimDeliveryInfo = claimDeliveryInfo;
	}

	@Override
	public String toString() {
		return "DeliveryInfo [deliveryType=" + deliveryType + ", deliveryAttributeType=" + deliveryAttributeType
				+ ", deliveryCompany=" + deliveryCompany + ", deliveryBundleGroupUsable=" + deliveryBundleGroupUsable
				+ ", deliveryBundleGroupId=" + deliveryBundleGroupId + ", visitAddressId=" + visitAddressId
				+ ", deliveryFee=" + deliveryFee + ", claimDeliveryInfo=" + claimDeliveryInfo + ", installationFee="
				+ installationFee + ", expectedDeliveryPeriodDirectInput=" + expectedDeliveryPeriodDirectInput
				+ ", todayStockQuantity=" + todayStockQuantity + ", customProductAfterOrderYn="
				+ customProductAfterOrderYn + ", hopeDeliveryGroupId=" + hopeDeliveryGroupId
				+ ", expectedDeliveryPeriodType=" + expectedDeliveryPeriodType + "]";
	}

}
