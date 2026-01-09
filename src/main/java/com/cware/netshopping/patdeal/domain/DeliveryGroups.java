package com.cware.netshopping.patdeal.domain;


import java.sql.Timestamp;
import java.util.List;

import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.patdeal.util.PaTdealComUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryGroups {
	
	private String deliveryNo;
	private String invoiceNo;
	

	private Timestamp invoiceRegisterYmdt;//string 형태 날짜형태 변환
	
	private String deliveryTemplateGroupNo;
	private String deliveryTemplateNo;
	private String deliveryConditionNo;
	
	private long deliveryAmt;
	private long returnDeliveryAmt;
	private long remoteDeliveryAmt;
	private long adjustedAmt;
	
	private String deliveryCompanyType;
	private String receiverName;
	private String countryCd;
	private String receiverZipCd;
	private String receiverAddress;
	private String receiverJibunAddress;
	private String receiverDetailAddress;
	private String receiverContact1;
	private String receiverContact2;
	private String deliveryMemo;
	private String deliveryType;
	private String shippingMethodType;
	private String customsIdNumber;
	private String deliveryAmtInAdvanceYn;
	private String deliveryYn;
	private String combineDeliveryYn;
	private String divideDeliveryYn;
	private String deliveryCombinationYn;
	private String originalDeliveryNo;
	
	private boolean usesShippingInfoLaterInput;
	
	private String encryptedShippingNo;
	
	private List<OrderProducts> orderProducts;

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Timestamp getInvoiceRegisterYmdt() {
		return invoiceRegisterYmdt;
	}

	public void setInvoiceRegisterYmdt(String invoiceRegisterYmdt) {
		this.invoiceRegisterYmdt = PaTdealComUtil.convertStrToTimestamp(invoiceRegisterYmdt);
	}

	public String getDeliveryTemplateGroupNo() {
		return deliveryTemplateGroupNo;
	}

	public void setDeliveryTemplateGroupNo(String deliveryTemplateGroupNo) {
		this.deliveryTemplateGroupNo = deliveryTemplateGroupNo;
	}

	public String getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}

	public void setDeliveryTemplateNo(String deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}

	public String getDeliveryConditionNo() {
		return deliveryConditionNo;
	}

	public void setDeliveryConditionNo(String deliveryConditionNo) {
		this.deliveryConditionNo = deliveryConditionNo;
	}

	public long getDeliveryAmt() {
		return deliveryAmt;
	}

	public void setDeliveryAmt(long deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
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

	public long getAdjustedAmt() {
		return adjustedAmt;
	}

	public void setAdjustedAmt(long adjustedAmt) {
		this.adjustedAmt = adjustedAmt;
	}

	public String getDeliveryCompanyType() {
		return deliveryCompanyType;
	}

	public void setDeliveryCompanyType(String deliveryCompanyType) {
		this.deliveryCompanyType = deliveryCompanyType;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getCountryCd() {
		return countryCd;
	}

	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}

	public String getReceiverZipCd() {
		return receiverZipCd;
	}

	public void setReceiverZipCd(String receiverZipCd) {
		this.receiverZipCd = receiverZipCd;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverJibunAddress() {
		return receiverJibunAddress;
	}

	public void setReceiverJibunAddress(String receiverJibunAddress) {
		this.receiverJibunAddress = receiverJibunAddress;
	}

	public String getReceiverDetailAddress() {
		return receiverDetailAddress;
	}

	public void setReceiverDetailAddress(String receiverDetailAddress) {
		this.receiverDetailAddress = receiverDetailAddress;
	}

	public String getReceiverContact1() {
		return receiverContact1;
	}

	public void setReceiverContact1(String receiverContact1) {
		this.receiverContact1 = receiverContact1;
	}

	public String getReceiverContact2() {
		return receiverContact2;
	}

	public void setReceiverContact2(String receiverContact2) {
		this.receiverContact2 = receiverContact2;
	}

	public String getDeliveryMemo() {
		return deliveryMemo;
	}

	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getShippingMethodType() {
		return shippingMethodType;
	}

	public void setShippingMethodType(String shippingMethodType) {
		this.shippingMethodType = shippingMethodType;
	}

	public String getCustomsIdNumber() {
		return customsIdNumber;
	}

	public void setCustomsIdNumber(String customsIdNumber) {
		this.customsIdNumber = customsIdNumber;
	}

	public String getDeliveryAmtInAdvanceYn() {
		return deliveryAmtInAdvanceYn;
	}

	public void setDeliveryAmtInAdvanceYn(String deliveryAmtInAdvanceYn) {
		this.deliveryAmtInAdvanceYn = deliveryAmtInAdvanceYn;
	}

	public String getDeliveryYn() {
		return deliveryYn;
	}

	public void setDeliveryYn(String deliveryYn) {
		this.deliveryYn = deliveryYn;
	}

	public String getCombineDeliveryYn() {
		return combineDeliveryYn;
	}

	public void setCombineDeliveryYn(String combineDeliveryYn) {
		this.combineDeliveryYn = combineDeliveryYn;
	}

	public String getDivideDeliveryYn() {
		return divideDeliveryYn;
	}

	public void setDivideDeliveryYn(String divideDeliveryYn) {
		this.divideDeliveryYn = divideDeliveryYn;
	}

	public String getDeliveryCombinationYn() {
		return deliveryCombinationYn;
	}

	public void setDeliveryCombinationYn(String deliveryCombinationYn) {
		this.deliveryCombinationYn = deliveryCombinationYn;
	}

	public String getOriginalDeliveryNo() {
		return originalDeliveryNo;
	}

	public void setOriginalDeliveryNo(String originalDeliveryNo) {
		this.originalDeliveryNo = originalDeliveryNo;
	}

	public boolean isUsesShippingInfoLaterInput() {
		return usesShippingInfoLaterInput;
	}

	public void setUsesShippingInfoLaterInput(boolean usesShippingInfoLaterInput) {
		this.usesShippingInfoLaterInput = usesShippingInfoLaterInput;
	}

	public String getEncryptedShippingNo() {
		return encryptedShippingNo;
	}

	public void setEncryptedShippingNo(String encryptedShippingNo) {
		this.encryptedShippingNo = encryptedShippingNo;
	}

	public List<OrderProducts> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProducts> orderProducts) {
		this.orderProducts = orderProducts;
	}

	@Override
	public String toString() {
		return "DeliveryGroups [deliveryNo=" + deliveryNo + ", invoiceNo=" + invoiceNo + ", invoiceRegisterYmdt="
				+ invoiceRegisterYmdt + ", deliveryTemplateGroupNo=" + deliveryTemplateGroupNo + ", deliveryTemplateNo="
				+ deliveryTemplateNo + ", deliveryConditionNo=" + deliveryConditionNo + ", deliveryAmt=" + deliveryAmt
				+ ", returnDeliveryAmt=" + returnDeliveryAmt + ", remoteDeliveryAmt=" + remoteDeliveryAmt
				+ ", adjustedAmt=" + adjustedAmt + ", deliveryCompanyType=" + deliveryCompanyType + ", receiverName="
				+ receiverName + ", countryCd=" + countryCd + ", receiverZipCd=" + receiverZipCd + ", receiverAddress="
				+ receiverAddress + ", receiverJibunAddress=" + receiverJibunAddress + ", receiverDetailAddress="
				+ receiverDetailAddress + ", receiverContact1=" + receiverContact1 + ", receiverContact2="
				+ receiverContact2 + ", deliveryMemo=" + deliveryMemo + ", deliveryType=" + deliveryType
				+ ", shippingMethodType=" + shippingMethodType + ", customsIdNumber=" + customsIdNumber
				+ ", deliveryAmtInAdvanceYn=" + deliveryAmtInAdvanceYn + ", deliveryYn=" + deliveryYn
				+ ", combineDeliveryYn=" + combineDeliveryYn + ", divideDeliveryYn=" + divideDeliveryYn
				+ ", deliveryCombinationYn=" + deliveryCombinationYn + ", originalDeliveryNo=" + originalDeliveryNo
				+ ", usesShippingInfoLaterInput=" + usesShippingInfoLaterInput + ", encryptedShippingNo="
				+ encryptedShippingNo + ", orderProducts=" + orderProducts + "]";
	}


	
	
}
