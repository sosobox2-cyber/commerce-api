package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnexchangelist extends AbstractModel {

    private static final long serialVersionUID = 1L;

	private String paOrderGb;
	private String exchangeId;
	private String orderId;
	private String vendorId;
	private String orderDeliveryStatusCode;
	private String exchangeStatus;
	private String referType;
	private String faultType;
	private String exchangeAmount;
	private String reasonCode;
	private String reasonCodeText;
	private String reasonEtcDetail;
	private String cancelReason;
	private String createdByType;
	private Timestamp createdAt;
	private String modifiedByType;
	private Timestamp modifiedAt;
	private Pacopnexchangeitemlist[] exchangeItemDtoV1s;
	private String eadExchangeAddressId;
	private String eadReturnCustomerName;
	private String eadReturnAddressZipCode;
	private String eadReturnAddress;
	private String eadReturnAddressDetail;
	private String eadReturnPhone;
	private String eadReturnMobile;
	private String eadReturnMemo;
	private String eadDeliveryCustomerName;
	private String eadDeliveryAddressZipCode;
	private String eadDeliveryAddress;
	private String eadDeliveryAddressDetail;
	private String eadDeliveryPhone;
	private String eadDeliveryMobile;
	private String eadDeliveryMemo;
	private Timestamp eadCreatedAt;
	private Timestamp eadModifiedAt;
	private String eadExchangeId;
	private String deliveryInvoiceGroupDtos;
	private String deliveryStatus;
	private String collectStatus;
	private Timestamp collectCompleteDate;
	private String collectInformationsDto;
	private String returnDeliveryDtos;
	private String orderDeliveryStatusLabel;
	private String exchangeStatusLabel;
	private String referTypeLabel;
	private String faultTypeLabel;
	private String createdByTypeLabel;
	private String rejectable;
	private String modifiedByTypeLabel;
	private String deliveryInvoiceModifiable;
	private String successable;
	private Timestamp insertDate;
	private Timestamp modifyDate;
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getOrderDeliveryStatusCode() {
		return orderDeliveryStatusCode;
	}
	public void setOrderDeliveryStatusCode(String orderDeliveryStatusCode) {
		this.orderDeliveryStatusCode = orderDeliveryStatusCode;
	}
	public String getExchangeStatus() {
		return exchangeStatus;
	}
	public void setExchangeStatus(String exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}
	public String getReferType() {
		return referType;
	}
	public void setReferType(String referType) {
		this.referType = referType;
	}
	public String getFaultType() {
		return faultType;
	}
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
	public String getExchangeAmount() {
		return exchangeAmount;
	}
	public void setExchangeAmount(String exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonCodeText() {
		return reasonCodeText;
	}
	public void setReasonCodeText(String reasonCodeText) {
		this.reasonCodeText = reasonCodeText;
	}
	public String getReasonEtcDetail() {
		return reasonEtcDetail;
	}
	public void setReasonEtcDetail(String reasonEtcDetail) {
		this.reasonEtcDetail = reasonEtcDetail;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getCreatedByType() {
		return createdByType;
	}
	public void setCreatedByType(String createdByType) {
		this.createdByType = createdByType;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getModifiedByType() {
		return modifiedByType;
	}
	public void setModifiedByType(String modifiedByType) {
		this.modifiedByType = modifiedByType;
	}
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public Pacopnexchangeitemlist[] getExchangeItemDtoV1s() {
		return exchangeItemDtoV1s;
	}
	public void setExchangeItemDtoV1s(Pacopnexchangeitemlist[] exchangeItemDtoV1s) {
		this.exchangeItemDtoV1s = exchangeItemDtoV1s;
	}
	public String getEadExchangeAddressId() {
		return eadExchangeAddressId;
	}
	public void setEadExchangeAddressId(String eadExchangeAddressId) {
		this.eadExchangeAddressId = eadExchangeAddressId;
	}
	public String getEadReturnCustomerName() {
		return eadReturnCustomerName;
	}
	public void setEadReturnCustomerName(String eadReturnCustomerName) {
		this.eadReturnCustomerName = eadReturnCustomerName;
	}
	public String getEadReturnAddressZipCode() {
		return eadReturnAddressZipCode;
	}
	public void setEadReturnAddressZipCode(String eadReturnAddressZipCode) {
		this.eadReturnAddressZipCode = eadReturnAddressZipCode;
	}
	public String getEadReturnAddress() {
		return eadReturnAddress;
	}
	public void setEadReturnAddress(String eadReturnAddress) {
		this.eadReturnAddress = eadReturnAddress;
	}
	public String getEadReturnAddressDetail() {
		return eadReturnAddressDetail;
	}
	public void setEadReturnAddressDetail(String eadReturnAddressDetail) {
		this.eadReturnAddressDetail = eadReturnAddressDetail;
	}
	public String getEadReturnPhone() {
		return eadReturnPhone;
	}
	public void setEadReturnPhone(String eadReturnPhone) {
		this.eadReturnPhone = eadReturnPhone;
	}
	public String getEadReturnMobile() {
		return eadReturnMobile;
	}
	public void setEadReturnMobile(String eadReturnMobile) {
		this.eadReturnMobile = eadReturnMobile;
	}
	public String getEadReturnMemo() {
		return eadReturnMemo;
	}
	public void setEadReturnMemo(String eadReturnMemo) {
		this.eadReturnMemo = eadReturnMemo;
	}
	public String getEadDeliveryCustomerName() {
		return eadDeliveryCustomerName;
	}
	public void setEadDeliveryCustomerName(String eadDeliveryCustomerName) {
		this.eadDeliveryCustomerName = eadDeliveryCustomerName;
	}
	public String getEadDeliveryAddressZipCode() {
		return eadDeliveryAddressZipCode;
	}
	public void setEadDeliveryAddressZipCode(String eadDeliveryAddressZipCode) {
		this.eadDeliveryAddressZipCode = eadDeliveryAddressZipCode;
	}
	public String getEadDeliveryAddress() {
		return eadDeliveryAddress;
	}
	public void setEadDeliveryAddress(String eadDeliveryAddress) {
		this.eadDeliveryAddress = eadDeliveryAddress;
	}
	public String getEadDeliveryAddressDetail() {
		return eadDeliveryAddressDetail;
	}
	public void setEadDeliveryAddressDetail(String eadDeliveryAddressDetail) {
		this.eadDeliveryAddressDetail = eadDeliveryAddressDetail;
	}
	public String getEadDeliveryPhone() {
		return eadDeliveryPhone;
	}
	public void setEadDeliveryPhone(String eadDeliveryPhone) {
		this.eadDeliveryPhone = eadDeliveryPhone;
	}
	public String getEadDeliveryMobile() {
		return eadDeliveryMobile;
	}
	public void setEadDeliveryMobile(String eadDeliveryMobile) {
		this.eadDeliveryMobile = eadDeliveryMobile;
	}
	public String getEadDeliveryMemo() {
		return eadDeliveryMemo;
	}
	public void setEadDeliveryMemo(String eadDeliveryMemo) {
		this.eadDeliveryMemo = eadDeliveryMemo;
	}
	public Timestamp getEadCreatedAt() {
		return eadCreatedAt;
	}
	public void setEadCreatedAt(Timestamp eadCreatedAt) {
		this.eadCreatedAt = eadCreatedAt;
	}
	public Timestamp getEadModifiedAt() {
		return eadModifiedAt;
	}
	public void setEadModifiedAt(Timestamp eadModifiedAt) {
		this.eadModifiedAt = eadModifiedAt;
	}
	public String getEadExchangeId() {
		return eadExchangeId;
	}
	public void setEadExchangeId(String eadExchangeId) {
		this.eadExchangeId = eadExchangeId;
	}
	public String getDeliveryInvoiceGroupDtos() {
		return deliveryInvoiceGroupDtos;
	}
	public void setDeliveryInvoiceGroupDtos(String deliveryInvoiceGroupDtos) {
		this.deliveryInvoiceGroupDtos = deliveryInvoiceGroupDtos;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public Timestamp getCollectCompleteDate() {
		return collectCompleteDate;
	}
	public void setCollectCompleteDate(Timestamp collectCompleteDate) {
		this.collectCompleteDate = collectCompleteDate;
	}
	public String getCollectInformationsDto() {
		return collectInformationsDto;
	}
	public void setCollectInformationsDto(String collectInformationsDto) {
		this.collectInformationsDto = collectInformationsDto;
	}
	public String getReturnDeliveryDtos() {
		return returnDeliveryDtos;
	}
	public void setReturnDeliveryDtos(String returnDeliveryDtos) {
		this.returnDeliveryDtos = returnDeliveryDtos;
	}
	public String getOrderDeliveryStatusLabel() {
		return orderDeliveryStatusLabel;
	}
	public void setOrderDeliveryStatusLabel(String orderDeliveryStatusLabel) {
		this.orderDeliveryStatusLabel = orderDeliveryStatusLabel;
	}
	public String getExchangeStatusLabel() {
		return exchangeStatusLabel;
	}
	public void setExchangeStatusLabel(String exchangeStatusLabel) {
		this.exchangeStatusLabel = exchangeStatusLabel;
	}
	public String getReferTypeLabel() {
		return referTypeLabel;
	}
	public void setReferTypeLabel(String referTypeLabel) {
		this.referTypeLabel = referTypeLabel;
	}
	public String getFaultTypeLabel() {
		return faultTypeLabel;
	}
	public void setFaultTypeLabel(String faultTypeLabel) {
		this.faultTypeLabel = faultTypeLabel;
	}
	public String getCreatedByTypeLabel() {
		return createdByTypeLabel;
	}
	public void setCreatedByTypeLabel(String createdByTypeLabel) {
		this.createdByTypeLabel = createdByTypeLabel;
	}
	public String getRejectable() {
		return rejectable;
	}
	public void setRejectable(String rejectable) {
		this.rejectable = rejectable;
	}
	public String getModifiedByTypeLabel() {
		return modifiedByTypeLabel;
	}
	public void setModifiedByTypeLabel(String modifiedByTypeLabel) {
		this.modifiedByTypeLabel = modifiedByTypeLabel;
	}
	public String getDeliveryInvoiceModifiable() {
		return deliveryInvoiceModifiable;
	}
	public void setDeliveryInvoiceModifiable(String deliveryInvoiceModifiable) {
		this.deliveryInvoiceModifiable = deliveryInvoiceModifiable;
	}
	public String getSuccessable() {
		return successable;
	}
	public void setSuccessable(String successable) {
		this.successable = successable;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
}
