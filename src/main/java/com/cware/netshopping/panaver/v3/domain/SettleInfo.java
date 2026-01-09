package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettleInfo {
	
	// 정산 기준일(yyyy-MM-dd)
	private String settleBasisDate;
	// 정산 예정일(yyyy-MM-dd)
	private String settleExpectDate;
	// 정산 완료일(yyyy-MM-dd)
	private String settleCompleteDate;
	// 주문 번호
	private String orderId;
	// 상품 주문 번호, 배송비 번호, 기타 비용 번호
	private String productOrderId;
	// 정산 대상 구분(상품 주문, 배송비, 기타 비용)
	private String productOrderType;
	// 정산 상태 구분(정산, 정산 전 취소, 정산 후 취소)
	private String settleType;
	// 상품 번호
	private String productId;
	// 상품명
	private String productName;
	// 구매자명
	private String purchaserName;
	// 결제 정산 금액
	private double paySettleAmount;
	// 총 네이버페이 관리 수수료 금액
	private double totalPayCommissionAmount;
	// 판매자 부담 무이자 할부 수수료
	private double freeInstallmentCommissionAmount;
	// 매출 연동 수수료
	private double sellingInterlockCommissionAmount;
	// 혜택 정산 금액
	private double benefitSettleAmount;
	// 정산 예정 금액
	private double settleExpectAmount;
	// 가맹점 ID
	private String merchantId;
	// 가맹점명
	private String merchantName;
	// 계약 번호
	private String contractNo;
	
	public String getSettleBasisDate() {
		return settleBasisDate;
	}
	public void setSettleBasisDate(String settleBasisDate) {
		this.settleBasisDate = settleBasisDate;
	}
	public String getSettleExpectDate() {
		return settleExpectDate;
	}
	public void setSettleExpectDate(String settleExpectDate) {
		this.settleExpectDate = settleExpectDate;
	}
	public String getSettleCompleteDate() {
		return settleCompleteDate;
	}
	public void setSettleCompleteDate(String settleCompleteDate) {
		this.settleCompleteDate = settleCompleteDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductOrderId() {
		return productOrderId;
	}
	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}
	public String getProductOrderType() {
		return productOrderType;
	}
	public void setProductOrderType(String productOrderType) {
		this.productOrderType = productOrderType;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPurchaserName() {
		return purchaserName;
	}
	public void setPurchaserName(String purchaserName) {
		this.purchaserName = purchaserName;
	}
	public double getPaySettleAmount() {
		return paySettleAmount;
	}
	public void setPaySettleAmount(double paySettleAmount) {
		this.paySettleAmount = paySettleAmount;
	}
	public double getTotalPayCommissionAmount() {
		return totalPayCommissionAmount;
	}
	public void setTotalPayCommissionAmount(double totalPayCommissionAmount) {
		this.totalPayCommissionAmount = totalPayCommissionAmount;
	}
	public double getFreeInstallmentCommissionAmount() {
		return freeInstallmentCommissionAmount;
	}
	public void setFreeInstallmentCommissionAmount(double freeInstallmentCommissionAmount) {
		this.freeInstallmentCommissionAmount = freeInstallmentCommissionAmount;
	}
	public double getSellingInterlockCommissionAmount() {
		return sellingInterlockCommissionAmount;
	}
	public void setSellingInterlockCommissionAmount(double sellingInterlockCommissionAmount) {
		this.sellingInterlockCommissionAmount = sellingInterlockCommissionAmount;
	}
	public double getBenefitSettleAmount() {
		return benefitSettleAmount;
	}
	public void setBenefitSettleAmount(double benefitSettleAmount) {
		this.benefitSettleAmount = benefitSettleAmount;
	}
	public double getSettleExpectAmount() {
		return settleExpectAmount;
	}
	public void setSettleExpectAmount(double settleExpectAmount) {
		this.settleExpectAmount = settleExpectAmount;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	@Override
	public String toString() {
		return "SettleInfo [settleBasisDate=" + settleBasisDate + ", settleExpectDate=" + settleExpectDate
				+ ", settleCompleteDate=" + settleCompleteDate + ", orderId=" + orderId + ", productOrderId="
				+ productOrderId + ", productOrderType=" + productOrderType + ", settleType=" + settleType
				+ ", productId=" + productId + ", productName=" + productName + ", purchaserName=" + purchaserName
				+ ", paySettleAmount=" + paySettleAmount + ", totalPayCommissionAmount=" + totalPayCommissionAmount
				+ ", freeInstallmentCommissionAmount=" + freeInstallmentCommissionAmount
				+ ", sellingInterlockCommissionAmount=" + sellingInterlockCommissionAmount + ", benefitSettleAmount="
				+ benefitSettleAmount + ", settleExpectAmount=" + settleExpectAmount + ", merchantId=" + merchantId
				+ ", merchantName=" + merchantName + ", contractNo=" + contractNo + "]";
	}
}
