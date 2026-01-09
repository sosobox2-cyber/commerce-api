package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTmonOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String	tmonOrderNo;	//주문번호
	private String	userId;		//주문자아이디
	private String	userName;	//주문자명
	private String	userPhone;	//주문자연락처
	private String	receiverName;	//수취인이름
	private String	receiverPhone;	//수취인연락처
	private String	zipCode;
	private String	address;
	private String	streetAddress;
	private String	addressDetail;
	private String	deliveryMemo;	//배송메모
	private String	additionalMessage;	//구매추가메세지
	private String	orderDate;	//주문일시
	private String	depositDate;	//입금일
	private String	tmonDealNo;	//딜번호
	private String	dealTitle;	//딜명
	private String	deliveryNo;	//배송번호
	private String	deliveryStatus;	//배송상태
	private String	updatedDate;	//변경일
	private String	tmonDealOptionNo;	//딜옵션번호
	private String	dealOptionTitle;	//딜옵션명
	private double	salesPrice;	//판매가(단가)
	private long	qty;	//수량
	private double	purchasePrice;	//구매가(판매가 X 수량)
	private String	descriptions;	//파트너커스텀정보
	private String	optAdditionalMessage;	//구매추가메세지
	private String	safetyPhoneExpirationDate;	//안심번호유효일자
	private String	customsId;	//통관고유번호
	private String	deliveryScheduledDate;	//배송예정일
	private String	delayedReasonCode;	//배송지연사유
	private String	delayedReasonDetail;	//지연사유상세
	private double	amount;
	private double	tmonAmount;
	private double	partnerAmount;
	private double	userAmount;
	private double	longDistanceAmount;
	private double	longDistanceTmonAmount;
	private double	longDistancePartnerAmount;
	private double	longDistanceUserAmount;
	private double	discountAmount;
	private double	partnerDiscountAmount;
	private String	couponNo;
	private String	discountPolicyNo;
	
	public String getTmonOrderNo() {
		return tmonOrderNo;
	}
	public void setTmonOrderNo(String tmonOrderNo) {
		this.tmonOrderNo = tmonOrderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	public String getDeliveryMemo() {
		return deliveryMemo;
	}
	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}
	public String getAdditionalMessage() {
		return additionalMessage;
	}
	public void setAdditionalMessage(String additionalMessage) {
		this.additionalMessage = additionalMessage;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(String depositDate) {
		this.depositDate = depositDate;
	}
	public String getTmonDealNo() {
		return tmonDealNo;
	}
	public void setTmonDealNo(String tmonDealNo) {
		this.tmonDealNo = tmonDealNo;
	}
	public String getDealTitle() {
		return dealTitle;
	}
	public void setDealTitle(String dealTitle) {
		this.dealTitle = dealTitle;
	}
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getTmonDealOptionNo() {
		return tmonDealOptionNo;
	}
	public void setTmonDealOptionNo(String tmonDealOptionNo) {
		this.tmonDealOptionNo = tmonDealOptionNo;
	}
	public String getDealOptionTitle() {
		return dealOptionTitle;
	}
	public void setDealOptionTitle(String dealOptionTitle) {
		this.dealOptionTitle = dealOptionTitle;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getOptAdditionalMessage() {
		return optAdditionalMessage;
	}
	public void setOptAdditionalMessage(String optAdditionalMessage) {
		this.optAdditionalMessage = optAdditionalMessage;
	}
	public String getSafetyPhoneExpirationDate() {
		return safetyPhoneExpirationDate;
	}
	public void setSafetyPhoneExpirationDate(String safetyPhoneExpirationDate) {
		this.safetyPhoneExpirationDate = safetyPhoneExpirationDate;
	}
	public String getCustomsId() {
		return customsId;
	}
	public void setCustomsId(String customsId) {
		this.customsId = customsId;
	}
	public String getDeliveryScheduledDate() {
		return deliveryScheduledDate;
	}
	public void setDeliveryScheduledDate(String deliveryScheduledDate) {
		this.deliveryScheduledDate = deliveryScheduledDate;
	}
	public String getDelayedReasonCode() {
		return delayedReasonCode;
	}
	public void setDelayedReasonCode(String delayedReasonCode) {
		this.delayedReasonCode = delayedReasonCode;
	}
	public String getDelayedReasonDetail() {
		return delayedReasonDetail;
	}
	public void setDelayedReasonDetail(String delayedReasonDetail) {
		this.delayedReasonDetail = delayedReasonDetail;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTmonAmount() {
		return tmonAmount;
	}
	public void setTmonAmount(double tmonAmount) {
		this.tmonAmount = tmonAmount;
	}
	public double getPartnerAmount() {
		return partnerAmount;
	}
	public void setPartnerAmount(double partnerAmount) {
		this.partnerAmount = partnerAmount;
	}
	public double getUserAmount() {
		return userAmount;
	}
	public void setUserAmount(double userAmount) {
		this.userAmount = userAmount;
	}
	public double getLongDistanceAmount() {
		return longDistanceAmount;
	}
	public void setLongDistanceAmount(double longDistanceAmount) {
		this.longDistanceAmount = longDistanceAmount;
	}
	public double getLongDistanceTmonAmount() {
		return longDistanceTmonAmount;
	}
	public void setLongDistanceTmonAmount(double longDistanceTmonAmount) {
		this.longDistanceTmonAmount = longDistanceTmonAmount;
	}
	public double getLongDistancePartnerAmount() {
		return longDistancePartnerAmount;
	}
	public void setLongDistancePartnerAmount(double longDistancePartnerAmount) {
		this.longDistancePartnerAmount = longDistancePartnerAmount;
	}
	public double getLongDistanceUserAmount() {
		return longDistanceUserAmount;
	}
	public void setLongDistanceUserAmount(double longDistanceUserAmount) {
		this.longDistanceUserAmount = longDistanceUserAmount;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public double getPartnerDiscountAmount() {
		return partnerDiscountAmount;
	}
	public void setPartnerDiscountAmount(double partnerDiscountAmount) {
		this.partnerDiscountAmount = partnerDiscountAmount;
	}
	public String getCouponNo() {
		return couponNo;
	}
	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	public String getDiscountPolicyNo() {
		return discountPolicyNo;
	}
	public void setDiscountPolicyNo(String discountPolicyNo) {
		this.discountPolicyNo = discountPolicyNo;
	}
}
