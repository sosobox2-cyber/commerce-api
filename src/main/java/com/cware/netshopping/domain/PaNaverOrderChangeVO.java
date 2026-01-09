package com.cware.netshopping.domain;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimType;
import com.cware.api.panaver.order.seller.SellerServiceStub.GiftReceivingStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderChangeType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderStatusType;
import com.cware.netshopping.domain.model.PaNaverOrderChange;

public class PaNaverOrderChangeVO extends PaNaverOrderChange {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void setLastChangedStatus(String lastChangedStatus) {
		
		if(lastChangedStatus.equals("PAY_WAITING")) {
			this.lastChangedStatus = ProductOrderChangeType.PAY_WAITING;
		}
		else if(lastChangedStatus.equals("PAYED")) {
			this.lastChangedStatus = ProductOrderChangeType.PAYED;
		}
		else if(lastChangedStatus.equals("CANCEL_REQUESTED")) {
			this.lastChangedStatus = ProductOrderChangeType.CANCEL_REQUESTED;
		}
		else if(lastChangedStatus.equals("CANCELED")) {
			this.lastChangedStatus = ProductOrderChangeType.CANCELED;
		}
		else if(lastChangedStatus.equals("DISPATCHED")) {
			this.lastChangedStatus = ProductOrderChangeType.DISPATCHED;
		}
		else if(lastChangedStatus.equals("RETURN_REQUESTED")) {
			this.lastChangedStatus = ProductOrderChangeType.RETURN_REQUESTED;
		}
		else if(lastChangedStatus.equals("RETURNED")) {
			this.lastChangedStatus = ProductOrderChangeType.RETURNED;
		}
		else if(lastChangedStatus.equals("EXCHANGE_REQUESTED")) {
			this.lastChangedStatus = ProductOrderChangeType.EXCHANGE_REQUESTED;
		}
		else if(lastChangedStatus.equals("EXCHANGE_REDELIVERY_READY")) {
			this.lastChangedStatus = ProductOrderChangeType.EXCHANGE_REDELIVERY_READY;
		}
		else if(lastChangedStatus.equals("EXCHANGED")) {
			this.lastChangedStatus = ProductOrderChangeType.EXCHANGED;
		}
		else if(lastChangedStatus.equals("HOLDBACK_REQUESTED")) {
			this.lastChangedStatus = ProductOrderChangeType.HOLDBACK_REQUESTED;
		}
		else {
			this.lastChangedStatus = ProductOrderChangeType.PURCHASE_DECIDED;
		}
	}
	
	@Override
	public void setLastChangedDate(Timestamp lastChangedDate) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(lastChangedDate.getTime());
		this.lastChangedDate = c;
	}
	
	@Override
	public void setProductOrderStatus(String productOrderStatus) {
		
		if(productOrderStatus.equals("PAYMENT_WAITING")) {
			this.productOrderStatus = ProductOrderStatusType.PAYMENT_WAITING;
		}
		else if(productOrderStatus.equals("PAYED")) {
			this.productOrderStatus = ProductOrderStatusType.PAYED;
		}
		else if(productOrderStatus.equals("CANCELED")) {
			this.productOrderStatus = ProductOrderStatusType.CANCELED;
		}
		else if(productOrderStatus.equals("CANCELED_BY_NOPAYMENT")) {
			this.productOrderStatus = ProductOrderStatusType.CANCELED_BY_NOPAYMENT;
		}
		else if(productOrderStatus.equals("DELIVERING")) {
			this.productOrderStatus = ProductOrderStatusType.DELIVERING;
		}
		else if(productOrderStatus.equals("DELIVERED")) {
			this.productOrderStatus = ProductOrderStatusType.DELIVERED;
		}
		else if(productOrderStatus.equals("RETURNED")) {
			this.productOrderStatus = ProductOrderStatusType.RETURNED;
		}
		else if(productOrderStatus.equals("EXCHANGED")) {
			this.productOrderStatus = ProductOrderStatusType.EXCHANGED;
		}
		else {
			this.productOrderStatus = ProductOrderStatusType.PURCHASE_DECIDED;
		}
	}
	
	@Override
	public void setClaimType(String claimType) {
		
		if(claimType != null) {
			if(claimType.equals("CANCEL")) {
				this.claimType = ClaimType.CANCEL;
			}
			else if(claimType.equals("RETURN")) {
				this.claimType = ClaimType.RETURN;
			}
			else if(claimType.equals("EXCHANGE")) {
				this.claimType = ClaimType.EXCHANGE;
			}
			else if(claimType.equals("ADMIN_CANCEL")) {
				this.claimType = ClaimType.ADMIN_CANCEL;
			}
			else {
				this.claimType = ClaimType.PURCHASE_DECISION_HOLDBACK;
			}
		}
		else {
			this.claimType = null;
		}
	}
	
	@Override
	public void setClaimStatus(String claimStatus) {
		if(claimStatus != null) {
			if(claimStatus.equals("CANCEL_REQUEST")) {
				this.claimStatus = ClaimStatusType.CANCEL_REQUEST;
			}
			else if(claimStatus.equals("CANCELING")) {
				this.claimStatus = ClaimStatusType.CANCELING;
			}
			else if(claimStatus.equals("CANCEL_DONE")) {
				this.claimStatus = ClaimStatusType.CANCEL_DONE;
			}
			else if(claimStatus.equals("CANCEL_REJECT")) {
				this.claimStatus = ClaimStatusType.CANCEL_REJECT;
			}
			else if(claimStatus.equals("COLLECTING")) {
				this.claimStatus = ClaimStatusType.COLLECTING;
			}
			else if(claimStatus.equals("COLLECT_DONE")) {
				this.claimStatus = ClaimStatusType.COLLECT_DONE;
			}
			else if(claimStatus.equals("COLLECT_DONE")) {
				this.claimStatus = ClaimStatusType.RETURN_REQUEST;
			}
			else if(claimStatus.equals("RETURN_DONE")) {
				this.claimStatus = ClaimStatusType.RETURN_DONE;
			}
			else if(claimStatus.equals("RETURN_REJECT")) {
				this.claimStatus = ClaimStatusType.RETURN_REJECT;
			}
			else if(claimStatus.equals("EXCHANGE_REQUEST")) {
				this.claimStatus = ClaimStatusType.EXCHANGE_REQUEST;
			}
			else if(claimStatus.equals("EXCHANGE_REDELIVERING")) {
				this.claimStatus = ClaimStatusType.EXCHANGE_REDELIVERING;
			}
			else if(claimStatus.equals("EXCHANGE_DONE")) {
				this.claimStatus = ClaimStatusType.EXCHANGE_DONE;
			}
			else if(claimStatus.equals("EXCHANGE_REJECT")) {
				this.claimStatus = ClaimStatusType.EXCHANGE_REJECT;
			}
			else if(claimStatus.equals("ADMIN_CANCELING")) {
				this.claimStatus = ClaimStatusType.ADMIN_CANCELING;
			}
			else if(claimStatus.equals("ADMIN_CANCEL_DONE")) {
				this.claimStatus = ClaimStatusType.ADMIN_CANCEL_DONE;
			}
			else if(claimStatus.equals("PURCHASE_DECISION_REQUEST")) {
				this.claimStatus = ClaimStatusType.PURCHASE_DECISION_REQUEST;
			}
			else if(claimStatus.equals("PURCHASE_DECISION_HOLDBACK")) {
				this.claimStatus = ClaimStatusType.PURCHASE_DECISION_HOLDBACK;
			}
			else if(claimStatus.equals("PURCHASE_DECISION_HOLDBACK_REDELIVERING")) {
				this.claimStatus = ClaimStatusType.PURCHASE_DECISION_HOLDBACK_REDELIVERING;
			}
			else {
				this.claimStatus = ClaimStatusType.PURCHASE_DECISION_HOLDBACK_RELEASE;
			}
		}
		else {
			this.claimStatus = null;
		}
	}
	
	@Override
	public void setPaymentDate(Timestamp paymentDate) {
		if(paymentDate != null) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTimeInMillis(paymentDate.getTime());
			this.paymentDate = c;
		}
		else {
			this.paymentDate = null;
		}
	}
	
	@Override
	public void setGiftReceivingStatus(String giftReceivingStatus) {
		if(giftReceivingStatus != null) {
			if(giftReceivingStatus.equals("WAIT_FOR_RECEIVING")) {
				this.giftReceivingStatus = GiftReceivingStatusType.WAIT_FOR_RECEIVING;
			}
			else if(giftReceivingStatus.equals("RECEIVED")) {
				this.giftReceivingStatus = GiftReceivingStatusType.RECEIVED;
			}
			else {
				this.giftReceivingStatus = null;
			}
		}
		else {
			this.giftReceivingStatus = null;
		}
	}
	
	@Override
	public void setIsReceiverAddressChanged(String isReceiverAddressChanged) {
		if(isReceiverAddressChanged != null) {
			if(isReceiverAddressChanged.equals("0")) this.isReceiverAddressChanged = false;
			else this.isReceiverAddressChanged = true;
		}
		else {
			this.isReceiverAddressChanged = false;
		}
	}
}
