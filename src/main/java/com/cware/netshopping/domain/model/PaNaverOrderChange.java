package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimType;
import com.cware.api.panaver.order.seller.SellerServiceStub.GiftReceivingStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderChangeType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderStatusType;
import com.cware.framework.core.basic.AbstractModel;

public class PaNaverOrderChange extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String productOrderId;
	protected ProductOrderChangeType lastChangedStatus;
	protected GregorianCalendar lastChangedDate;
	protected ProductOrderStatusType productOrderStatus;
	protected ClaimType claimType;
	protected ClaimStatusType claimStatus;
	protected GregorianCalendar paymentDate;
	protected boolean isReceiverAddressChanged;
	protected GiftReceivingStatusType giftReceivingStatus;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductOrderId() {
		return productOrderId;
	}
	public GiftReceivingStatusType getGiftReceivingStatus() {
		return giftReceivingStatus;
	}
	public void setGiftReceivingStatus(String giftReceivingStatus) {
		//
	}
	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}
	public ProductOrderChangeType getLastChangedStatus() {
		return lastChangedStatus;
	}
	public void setLastChangedStatus(String lastChangedStatus) {
		//
	}
	public GregorianCalendar getLastChangedDate() {
		return lastChangedDate;
	}
	public void setLastChangedDate(Timestamp lastChangedDate) {
		//
	}
	public ProductOrderStatusType getProductOrderStatus() {
		return productOrderStatus;
	}
	public void setProductOrderStatus(String productOrderStatus) {
		//
	}
	public ClaimType getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		//
	}
	public ClaimStatusType getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		//
	}
	public GregorianCalendar getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Timestamp paymentDate) {
		//
	}
	public boolean getIsReceiverAddressChanged() {
		return isReceiverAddressChanged;
	}
	public void setIsReceiverAddressChanged(String isReceiverAddressChanged) {
		//
	}

}
