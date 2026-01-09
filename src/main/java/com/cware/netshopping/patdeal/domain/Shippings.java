package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shippings {

	private long shippingNo ;
	private long mallNo;
	private long partnerNo;
	private long deliveryTemplateGroupNo;
	private long deliveryTemplateNo;
	private long deliveryAmt;
	private long adjustedAmt;
	private long returnDeliveryAmt;
	private long remoteDeliveryAmt;
	private String deliveryType;
	private boolean prepaid;
	private boolean requiresShipping;
	private boolean combined;
	private boolean devided;
	private long originalShippingNo;
	private String invoiceNo;
	private String customsIdNumber;
	private String deliveryCompanyType;
	private boolean usesShippingInfoLaterInput;
	private String memo;
	private String deliveryCompanyTypeLabel;
	
	public long getShippingNo() {
		return shippingNo;
	}


	public void setShippingNo(long shippingNo) {
		this.shippingNo = shippingNo;
	}


	public long getMallNo() {
		return mallNo;
	}


	public void setMallNo(long mallNo) {
		this.mallNo = mallNo;
	}


	public long getPartnerNo() {
		return partnerNo;
	}


	public void setPartnerNo(long partnerNo) {
		this.partnerNo = partnerNo;
	}


	public long getDeliveryTemplateGroupNo() {
		return deliveryTemplateGroupNo;
	}


	public void setDeliveryTemplateGroupNo(long deliveryTemplateGroupNo) {
		this.deliveryTemplateGroupNo = deliveryTemplateGroupNo;
	}


	public long getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}


	public void setDeliveryTemplateNo(long deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}


	public long getDeliveryAmt() {
		return deliveryAmt;
	}


	public void setDeliveryAmt(long deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
	}


	public long getAdjustedAmt() {
		return adjustedAmt;
	}


	public void setAdjustedAmt(long adjustedAmt) {
		this.adjustedAmt = adjustedAmt;
	}


	public long getReturnDeliveryAmt() {
		return returnDeliveryAmt;
	}


	public void setReturnDeliveryAmt(long returnDeliveryAmt) {
		this.returnDeliveryAmt = returnDeliveryAmt;
	}


	public long getRemoteDeliveryAmt() {
		return remoteDeliveryAmt;
	}


	public void setRemoteDeliveryAmt(long remoteDeliveryAmt) {
		this.remoteDeliveryAmt = remoteDeliveryAmt;
	}


	public String getDeliveryType() {
		return deliveryType;
	}


	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}


	public boolean isPrepaid() {
		return prepaid;
	}


	public void setPrepaid(boolean prepaid) {
		this.prepaid = prepaid;
	}


	public boolean isRequiresShipping() {
		return requiresShipping;
	}


	public void setRequiresShipping(boolean requiresShipping) {
		this.requiresShipping = requiresShipping;
	}


	public boolean isCombined() {
		return combined;
	}


	public void setCombined(boolean combined) {
		this.combined = combined;
	}


	public boolean isDevided() {
		return devided;
	}


	public void setDevided(boolean devided) {
		this.devided = devided;
	}


	public long getOriginalShippingNo() {
		return originalShippingNo;
	}


	public void setOriginalShippingNo(long originalShippingNo) {
		this.originalShippingNo = originalShippingNo;
	}


	public String getInvoiceNo() {
		return invoiceNo;
	}


	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}


	public String getCustomsIdNumber() {
		return customsIdNumber;
	}


	public void setCustomsIdNumber(String customsIdNumber) {
		this.customsIdNumber = customsIdNumber;
	}


	public String getDeliveryCompanyType() {
		return deliveryCompanyType;
	}


	public void setDeliveryCompanyType(String deliveryCompanyType) {
		this.deliveryCompanyType = deliveryCompanyType;
	}


	public boolean isUsesShippingInfoLaterInput() {
		return usesShippingInfoLaterInput;
	}


	public void setUsesShippingInfoLaterInput(boolean usesShippingInfoLaterInput) {
		this.usesShippingInfoLaterInput = usesShippingInfoLaterInput;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getDeliveryCompanyTypeLabel() {
		return deliveryCompanyTypeLabel;
	}


	public void setDeliveryCompanyTypeLabel(String deliveryCompanyTypeLabel) {
		this.deliveryCompanyTypeLabel = deliveryCompanyTypeLabel;
	}




	@Override
	public String toString() {
		return "Shippings [shippingNo=" + shippingNo + ", mallNo=" + mallNo + ", partnerNo="
				+ partnerNo + ", deliveryTemplateGroupNo=" + deliveryTemplateGroupNo + ", deliveryTemplateNo=" + deliveryTemplateNo
				+ ", deliveryAmt=" + deliveryAmt + ", adjustedAmt=" + adjustedAmt + ", returnDeliveryAmt="
				+ returnDeliveryAmt + ", remoteDeliveryAmt=" + remoteDeliveryAmt
				+ ", deliveryType=" + deliveryType + ", prepaid=" + prepaid
				+ ", requiresShipping="+ requiresShipping + ", combined=" + combined
				+ ", devided="+ devided + ", originalShippingNo=" + originalShippingNo
				+ ", invoiceNo="+ invoiceNo + ", customsIdNumber=" + customsIdNumber
				+ ", deliveryCompanyType="+ deliveryCompanyType + ", usesShippingInfoLaterInput=" + usesShippingInfoLaterInput
				+ ", memo="+ memo + ", deliveryCompanyTypeLabel=" + deliveryCompanyTypeLabel
				+ "]";
	}
	

	
}
