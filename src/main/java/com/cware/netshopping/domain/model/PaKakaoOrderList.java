package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaKakaoOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String orderBaseId;
	private String paymentId;
	private String groupDiscountRoomId;
	private String status;
	private String paidAt;
	private String decidedAt;
	private String refundedAt;
	private String canceledAt;
	private String createdAt;
	private String modifiedAt;
	
	private String phoneNumber;
	
	private String orderProductId;
	private String name;
	private String sellerItemNo;
	private String sellerItemOptionCode;
	private String optionContent;
	private long   quantity;
	private double productPrice;
	private double optionPrice;
	private double sellerDiscountPrice;
	private double settlementBasicPrice;
	private double baseFee;
	private double displayFee;
	private String refererCode;
	private String brandName;
	private String modelName;
	private String deliveryAmountOriginId;
	private String deliveryAmountPayPointTime;
	private String deliveryAmountType;
	private double deliveryAmount;
	private double areaAdditionalDeliveryAmount;
	
	private String confirmedAt;
	private String deliveredAt;
	private String deliveryCompanyCode;
	private String deliveryRequestAt;
	private String invoiceNumber;
	private String invoiceRegisteredAt;
	private String shippingMethod;
	
	private String receiverAddress;
	private String receiverMobileNumber;
	private String receiverName;
	private String receiverPhoneNumber;
	private String requirement;
	private String roadZipCode;
	private String zipcode;

	private String claimId;
	private String claimItemId;
	private String referType;
	private String orderClaimCancelCreatedAt;
	private String orderClaimCancelModifiedAt;
	private String reasonCodeName;
	private String reasonComment;
	
	private String withdrawYn;
    private Timestamp withdrawDate;
    private String procFlag;
    private String cancelProcNote;
    private String cancelProcId;
    private String procNote;
    
    private double returnShippingFee;
	private String orderClaimReturnCreatedAt;
	private String orderClaimReturnModifiedAt;
    
    private String orderClaimExchCreatedAt;
	private String orderClaimExchModifiedAt;
	private String completedAt;
	private String returnMethod;
	private String returnDeliveryCompanyId;
	private String returnInvoiceNumber;
	private String exchangeName;
	private String exchangeZipCode;
	private String exchangeRoadZipCode;
	private String exchangeAddress1;
	private String exchangeAddress2;
	private String exchangeMobileNumber;
	private String exchangePhoneNumber;
	private String exchangeMethod;
	private String exchangeDeliveryCompanyId;
	private String exchangeInvoiceNumber;
	private double exchangeShippingFee;
	private String pendingModifiedAt;
	private String pendingCodeName;
	private String pendingComment;
	
	private String pickupName;
	private String pickupZipCode;
	private String pickupRoadZipCode;
	private String pickupAddress1;
	private String pickupAddress2;
	private String pickupMobileNumber;
	private String pickupPhoneNumber;
	
	private String claimShippingFeeMethod;
	private String claimShippingFeeMethodLabel;
	
	private String	rejectYn;
	
	private double sellerCouponDiscountPrice;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderBaseId() {
		return orderBaseId;
	}
	public void setOrderBaseId(String orderBaseId) {
		this.orderBaseId = orderBaseId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getGroupDiscountRoomId() {
		return groupDiscountRoomId;
	}
	public void setGroupDiscountRoomId(String groupDiscountRoomId) {
		this.groupDiscountRoomId = groupDiscountRoomId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(String paidAt) {
		this.paidAt = paidAt;
	}
	public String getDecidedAt() {
		return decidedAt;
	}
	public void setDecidedAt(String decidedAt) {
		this.decidedAt = decidedAt;
	}
	public String getRefundedAt() {
		return refundedAt;
	}
	public void setRefundedAt(String refundedAt) {
		this.refundedAt = refundedAt;
	}
	public String getCanceledAt() {
		return canceledAt;
	}
	public void setCanceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getOrderProductId() {
		return orderProductId;
	}
	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSellerItemNo() {
		return sellerItemNo;
	}
	public void setSellerItemNo(String sellerItemNo) {
		this.sellerItemNo = sellerItemNo;
	}
	public String getSellerItemOptionCode() {
		return sellerItemOptionCode;
	}
	public void setSellerItemOptionCode(String sellerItemOptionCode) {
		this.sellerItemOptionCode = sellerItemOptionCode;
	}
	public String getOptionContent() {
		return optionContent;
	}
	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public double getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(double optionPrice) {
		this.optionPrice = optionPrice;
	}
	public double getSellerDiscountPrice() {
		return sellerDiscountPrice;
	}
	public void setSellerDiscountPrice(double sellerDiscountPrice) {
		this.sellerDiscountPrice = sellerDiscountPrice;
	}
	public double getSettlementBasicPrice() {
		return settlementBasicPrice;
	}
	public void setSettlementBasicPrice(double settlementBasicPrice) {
		this.settlementBasicPrice = settlementBasicPrice;
	}
	public double getBaseFee() {
		return baseFee;
	}
	public void setBaseFee(double baseFee) {
		this.baseFee = baseFee;
	}
	public double getDisplayFee() {
		return displayFee;
	}
	public void setDisplayFee(double displayFee) {
		this.displayFee = displayFee;
	}
	public String getRefererCode() {
		return refererCode;
	}
	public void setRefererCode(String refererCode) {
		this.refererCode = refererCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getDeliveryAmountOriginId() {
		return deliveryAmountOriginId;
	}
	public void setDeliveryAmountOriginId(String deliveryAmountOriginId) {
		this.deliveryAmountOriginId = deliveryAmountOriginId;
	}
	public String getDeliveryAmountPayPointTime() {
		return deliveryAmountPayPointTime;
	}
	public void setDeliveryAmountPayPointTime(String deliveryAmountPayPointTime) {
		this.deliveryAmountPayPointTime = deliveryAmountPayPointTime;
	}
	public String getDeliveryAmountType() {
		return deliveryAmountType;
	}
	public void setDeliveryAmountType(String deliveryAmountType) {
		this.deliveryAmountType = deliveryAmountType;
	}
	public double getDeliveryAmount() {
		return deliveryAmount;
	}
	public void setDeliveryAmount(double deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	public double getAreaAdditionalDeliveryAmount() {
		return areaAdditionalDeliveryAmount;
	}
	public void setAreaAdditionalDeliveryAmount(double areaAdditionalDeliveryAmount) {
		this.areaAdditionalDeliveryAmount = areaAdditionalDeliveryAmount;
	}
	public String getConfirmedAt() {
		return confirmedAt;
	}
	public void setConfirmedAt(String confirmedAt) {
		this.confirmedAt = confirmedAt;
	}
	public String getDeliveredAt() {
		return deliveredAt;
	}
	public void setDeliveredAt(String deliveredAt) {
		this.deliveredAt = deliveredAt;
	}
	public String getDeliveryCompanyCode() {
		return deliveryCompanyCode;
	}
	public void setDeliveryCompanyCode(String deliveryCompanyCode) {
		this.deliveryCompanyCode = deliveryCompanyCode;
	}
	public String getDeliveryRequestAt() {
		return deliveryRequestAt;
	}
	public void setDeliveryRequestAt(String deliveryRequestAt) {
		this.deliveryRequestAt = deliveryRequestAt;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceRegisteredAt() {
		return invoiceRegisteredAt;
	}
	public void setInvoiceRegisteredAt(String invoiceRegisteredAt) {
		this.invoiceRegisteredAt = invoiceRegisteredAt;
	}
	public String getShippingMethod() {
		return shippingMethod;
	}
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverMobileNumber() {
		return receiverMobileNumber;
	}
	public void setReceiverMobileNumber(String receiverMobileNumber) {
		this.receiverMobileNumber = receiverMobileNumber;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}
	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getRoadZipCode() {
		return roadZipCode;
	}
	public void setRoadZipCode(String roadZipCode) {
		this.roadZipCode = roadZipCode;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getClaimItemId() {
		return claimItemId;
	}
	public void setClaimItemId(String claimItemId) {
		this.claimItemId = claimItemId;
	}
	public String getReferType() {
		return referType;
	}
	public void setReferType(String referType) {
		this.referType = referType;
	}
	public String getOrderClaimCancelCreatedAt() {
		return orderClaimCancelCreatedAt;
	}
	public void setOrderClaimCancelCreatedAt(String orderClaimCancelCreatedAt) {
		this.orderClaimCancelCreatedAt = orderClaimCancelCreatedAt;
	}
	public String getOrderClaimCancelModifiedAt() {
		return orderClaimCancelModifiedAt;
	}
	public void setOrderClaimCancelModifiedAt(String orderClaimCancelModifiedAt) {
		this.orderClaimCancelModifiedAt = orderClaimCancelModifiedAt;
	}
	public String getReasonCodeName() {
		return reasonCodeName;
	}
	public void setReasonCodeName(String reasonCodeName) {
		this.reasonCodeName = reasonCodeName;
	}
	public String getReasonComment() {
		return reasonComment;
	}
	public void setReasonComment(String reasonComment) {
		this.reasonComment = reasonComment;
	}
	public String getWithdrawYn() {
		return withdrawYn;
	}
	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}
	public Timestamp getWithdrawDate() {
		return withdrawDate;
	}
	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getCancelProcNote() {
		return cancelProcNote;
	}
	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}
	public String getCancelProcId() {
		return cancelProcId;
	}
	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}
	public String getProcNote() {
		return procNote;
	}
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}
	public double getReturnShippingFee() {
		return returnShippingFee;
	}
	public void setReturnShippingFee(double returnShippingFee) {
		this.returnShippingFee = returnShippingFee;
	}
	public String getOrderClaimReturnCreatedAt() {
		return orderClaimReturnCreatedAt;
	}
	public void setOrderClaimReturnCreatedAt(String orderClaimReturnCreatedAt) {
		this.orderClaimReturnCreatedAt = orderClaimReturnCreatedAt;
	}
	public String getOrderClaimReturnModifiedAt() {
		return orderClaimReturnModifiedAt;
	}
	public void setOrderClaimReturnModifiedAt(String orderClaimReturnModifiedAt) {
		this.orderClaimReturnModifiedAt = orderClaimReturnModifiedAt;
	}
	public String getOrderClaimExchCreatedAt() {
		return orderClaimExchCreatedAt;
	}
	public void setOrderClaimExchCreatedAt(String orderClaimExchCreatedAt) {
		this.orderClaimExchCreatedAt = orderClaimExchCreatedAt;
	}
	public String getOrderClaimExchModifiedAt() {
		return orderClaimExchModifiedAt;
	}
	public void setOrderClaimExchModifiedAt(String orderClaimExchModifiedAt) {
		this.orderClaimExchModifiedAt = orderClaimExchModifiedAt;
	}
	public String getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}
	public String getReturnMethod() {
		return returnMethod;
	}
	public void setReturnMethod(String returnMethod) {
		this.returnMethod = returnMethod;
	}
	public String getReturnDeliveryCompanyId() {
		return returnDeliveryCompanyId;
	}
	public void setReturnDeliveryCompanyId(String returnDeliveryCompanyId) {
		this.returnDeliveryCompanyId = returnDeliveryCompanyId;
	}
	public String getReturnInvoiceNumber() {
		return returnInvoiceNumber;
	}
	public void setReturnInvoiceNumber(String returnInvoiceNumber) {
		this.returnInvoiceNumber = returnInvoiceNumber;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public String getExchangeZipCode() {
		return exchangeZipCode;
	}
	public void setExchangeZipCode(String exchangeZipCode) {
		this.exchangeZipCode = exchangeZipCode;
	}
	public String getExchangeRoadZipCode() {
		return exchangeRoadZipCode;
	}
	public void setExchangeRoadZipCode(String exchangeRoadZipCode) {
		this.exchangeRoadZipCode = exchangeRoadZipCode;
	}
	public String getExchangeAddress1() {
		return exchangeAddress1;
	}
	public void setExchangeAddress1(String exchangeAddress1) {
		this.exchangeAddress1 = exchangeAddress1;
	}
	public String getExchangeAddress2() {
		return exchangeAddress2;
	}
	public void setExchangeAddress2(String exchangeAddress2) {
		this.exchangeAddress2 = exchangeAddress2;
	}
	public String getExchangeMobileNumber() {
		return exchangeMobileNumber;
	}
	public void setExchangeMobileNumber(String exchangeMobileNumber) {
		this.exchangeMobileNumber = exchangeMobileNumber;
	}
	public String getExchangePhoneNumber() {
		return exchangePhoneNumber;
	}
	public void setExchangePhoneNumber(String exchangePhoneNumber) {
		this.exchangePhoneNumber = exchangePhoneNumber;
	}
	public String getExchangeMethod() {
		return exchangeMethod;
	}
	public void setExchangeMethod(String exchangeMethod) {
		this.exchangeMethod = exchangeMethod;
	}
	public String getExchangeDeliveryCompanyId() {
		return exchangeDeliveryCompanyId;
	}
	public void setExchangeDeliveryCompanyId(String exchangeDeliveryCompanyId) {
		this.exchangeDeliveryCompanyId = exchangeDeliveryCompanyId;
	}
	public String getExchangeInvoiceNumber() {
		return exchangeInvoiceNumber;
	}
	public void setExchangeInvoiceNumber(String exchangeInvoiceNumber) {
		this.exchangeInvoiceNumber = exchangeInvoiceNumber;
	}
	public double getExchangeShippingFee() {
		return exchangeShippingFee;
	}
	public void setExchangeShippingFee(double exchangeShippingFee) {
		this.exchangeShippingFee = exchangeShippingFee;
	}
	public String getPendingModifiedAt() {
		return pendingModifiedAt;
	}
	public void setPendingModifiedAt(String pendingModifiedAt) {
		this.pendingModifiedAt = pendingModifiedAt;
	}
	public String getPendingCodeName() {
		return pendingCodeName;
	}
	public void setPendingCodeName(String pendingCodeName) {
		this.pendingCodeName = pendingCodeName;
	}
	public String getPendingComment() {
		return pendingComment;
	}
	public void setPendingComment(String pendingComment) {
		this.pendingComment = pendingComment;
	}
	public String getPickupName() {
		return pickupName;
	}
	public void setPickupName(String pickupName) {
		this.pickupName = pickupName;
	}
	public String getPickupZipCode() {
		return pickupZipCode;
	}
	public void setPickupZipCode(String pickupZipCode) {
		this.pickupZipCode = pickupZipCode;
	}
	public String getPickupRoadZipCode() {
		return pickupRoadZipCode;
	}
	public void setPickupRoadZipCode(String pickupRoadZipCode) {
		this.pickupRoadZipCode = pickupRoadZipCode;
	}
	public String getPickupAddress1() {
		return pickupAddress1;
	}
	public void setPickupAddress1(String pickupAddress1) {
		this.pickupAddress1 = pickupAddress1;
	}
	public String getPickupAddress2() {
		return pickupAddress2;
	}
	public void setPickupAddress2(String pickupAddress2) {
		this.pickupAddress2 = pickupAddress2;
	}
	public String getPickupMobileNumber() {
		return pickupMobileNumber;
	}
	public void setPickupMobileNumber(String pickupMobileNumber) {
		this.pickupMobileNumber = pickupMobileNumber;
	}
	public String getPickupPhoneNumber() {
		return pickupPhoneNumber;
	}
	public void setPickupPhoneNumber(String pickupPhoneNumber) {
		this.pickupPhoneNumber = pickupPhoneNumber;
	}
	public String getRejectYn() {
		return rejectYn;
	}
	public void setRejectYn(String rejectYn) {
		this.rejectYn = rejectYn;
	}
	public String getClaimShippingFeeMethod() {
		return claimShippingFeeMethod;
	}
	public void setClaimShippingFeeMethod(String claimShippingFeeMethod) {
		this.claimShippingFeeMethod = claimShippingFeeMethod;
	}
	public String getClaimShippingFeeMethodLabel() {
		return claimShippingFeeMethodLabel;
	}
	public void setClaimShippingFeeMethodLabel(String claimShippingFeeMethodLabel) {
		this.claimShippingFeeMethodLabel = claimShippingFeeMethodLabel;
	}
	public double getSellerCouponDiscountPrice() {
		return sellerCouponDiscountPrice;
	}
	public void setSellerCouponDiscountPrice(double sellerCouponDiscountPrice) {
		this.sellerCouponDiscountPrice = sellerCouponDiscountPrice;
	}
		
	
}
