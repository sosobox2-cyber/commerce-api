package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PaFapleShipSettlement {
	
	private String paFapleshipsettlementNo;
	private String senderId;			//			배송처코드
	private String senderName;			//			배송처명
	private String memberName;			//			고객명
	private String orderId;			//			주문번호
	private String deliveryDate;			//			배송완료(교환일자)
	private long chargeRatioOfHQ;			//			패플부담율(배송 프로모션 진행시 설정된값)
	private long NormalSenderFee;			//			정상배송료(배송 프로모션 진행시 고객결제배송료)
	private long feeHQ;			//			패플부담배송료(배송 프로모션 진행시 패플부담배송료)
	private long senderFee;			//			업체지급배송료
	private String contents;			//			배송료내역
	private String oldOrderId;			//			원주문번호
	private String orderGb;            // 클레임 
	
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public long getChargeRatioOfHQ() {
		return chargeRatioOfHQ;
	}
	public void setChargeRatioOfHQ(long chargeRatioOfHQ) {
		this.chargeRatioOfHQ = chargeRatioOfHQ;
	}
	public long getNormalSenderFee() {
		return NormalSenderFee;
	}
	public void setNormalSenderFee(long NormalSenderFee) {
		this.NormalSenderFee = NormalSenderFee;
	}
	public long getFeeHQ() {
		return feeHQ;
	}
	public void setFeeHQ(long feeHQ) {
		this.feeHQ = feeHQ;
	}
	public long getSenderFee() {
		return senderFee;
	}
	public void setSenderFee(long senderFee) {
		this.senderFee = senderFee;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getOrderGb() {
		return orderGb;
	}
	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	public String getPaFapleshipsettlementNo() {
		return paFapleshipsettlementNo;
	}
	public void setPaFapleshipsettlementNo(String paFapleshipsettlementNo) {
		this.paFapleshipsettlementNo = paFapleshipsettlementNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOldOrderId() {
		return oldOrderId;
	}
	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}
	
	
}