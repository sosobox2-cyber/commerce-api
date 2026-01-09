package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmkOrderBackUp extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    
    private String paCode;
    private String packNo;
    private String contrNo;
    private String contrWSEQ;
    private String gmktItemNo;
    private String buyerName;
    private String buyerPhone1;
    private String buyerPhone2;
    private String receiverName;
    private String receiverPhone1;
    private String receiverPhone2;
    private String receiverZipcode;
    private String receiverAddress;
    private String receiverAddress1;
    private String receiverAddress2;
    private String payment;
    private Timestamp contrDate;    
    private String outItemNo;
    private String itemName;
    private String itemOptionSelectionInfo;
    private String itemOptionAdditionInfo;
    private String freeGift;
    private long salePrice;
    private long costPrice;
    private long quantity;
    private long paymentPrice;
    private long couponDiscountPrice;
    private long addDiscountsPrice;
    private long itemOptionSelectionPrice;
    private long itemOptionAdditionPrice;
    private long referencePrice;
    private String buyerMemo;
    private long shippingFee;
    private String shippingFeeCondition;
    private long shippingFeeGroupCode;
    private String outOrderNo;
    private String inventoryNo;
    private Timestamp transDueDate;
    private String transPolicyType;
    private String itemAddOptionValue;
    private long itemAddOptionOrderCnt;
    private String itemAddOptionCode;
    private String itemOptionValue;
    private long itemOptionOrderCnt;
    private String itemOptionCode;
    
    
	public String getPaCode() {
        return paCode;
    }
    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }
	public String getPackNo() {
		return packNo;
	}
	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}
	public String getContrNo() {
		return contrNo;
	}
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getContrWSEQ() {
		return contrWSEQ;
	}
	public void setContrWSEQ(String contrWSEQ) {
		this.contrWSEQ = contrWSEQ;
	}
	public String getGmktItemNo() {
		return gmktItemNo;
	}
	public void setGmktItemNo(String gmktItemNo) {
		this.gmktItemNo = gmktItemNo;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerPhone1() {
		return buyerPhone1;
	}
	public void setBuyerPhone1(String buyerPhone1) {
		this.buyerPhone1 = buyerPhone1;
	}
	public String getBuyerPhone2() {
		return buyerPhone2;
	}
	public void setBuyerPhone2(String buyerPhone2) {
		this.buyerPhone2 = buyerPhone2;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone1() {
		return receiverPhone1;
	}
	public void setReceiverPhone1(String receiverPhone1) {
		this.receiverPhone1 = receiverPhone1;
	}
	public String getReceiverPhone2() {
		return receiverPhone2;
	}
	public void setReceiverPhone2(String receiverPhone2) {
		this.receiverPhone2 = receiverPhone2;
	}
	public String getReceiverZipcode() {
		return receiverZipcode;
	}
	public void setReceiverZipcode(String receiverZipcode) {
		this.receiverZipcode = receiverZipcode;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverAddress1() {
		return receiverAddress1;
	}
	public void setReceiverAddress1(String receiverAddress1) {
		this.receiverAddress1 = receiverAddress1;
	}
	public String getReceiverAddress2() {
		return receiverAddress2;
	}
	public void setReceiverAddress2(String receiverAddress2) {
		this.receiverAddress2 = receiverAddress2;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public Timestamp getContrDate() {
		return contrDate;
	}
	public void setContrDate(Timestamp contrDate) {
		this.contrDate = contrDate;
	}
	public String getOutItemNo() {
		return outItemNo;
	}
	public void setOutItemNo(String outItemNo) {
		this.outItemNo = outItemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemOptionSelectionInfo() {
		return itemOptionSelectionInfo;
	}
	public void setItemOptionSelectionInfo(String itemOptionSelectionInfo) {
		this.itemOptionSelectionInfo = itemOptionSelectionInfo;
	}
	public String getItemOptionAdditionInfo() {
		return itemOptionAdditionInfo;
	}
	public void setItemOptionAdditionInfo(String itemOptionAdditionInfo) {
		this.itemOptionAdditionInfo = itemOptionAdditionInfo;
	}
	public String getFreeGift() {
		return freeGift;
	}
	public void setFreeGift(String freeGift) {
		this.freeGift = freeGift;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public long getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(long costPrice) {
		this.costPrice = costPrice;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getPaymentPrice() {
		return paymentPrice;
	}
	public void setPaymentPrice(long paymentPrice) {
		this.paymentPrice = paymentPrice;
	}
	public long getCouponDiscountPrice() {
		return couponDiscountPrice;
	}
	public void setCouponDiscountPrice(long couponDiscountPrice) {
		this.couponDiscountPrice = couponDiscountPrice;
	}
	public long getAddDiscountsPrice() {
		return addDiscountsPrice;
	}
	public void setAddDiscountsPrice(long addDiscountsPrice) {
		this.addDiscountsPrice = addDiscountsPrice;
	}
	public long getItemOptionSelectionPrice() {
		return itemOptionSelectionPrice;
	}
	public void setItemOptionSelectionPrice(long itemOptionSelectionPrice) {
		this.itemOptionSelectionPrice = itemOptionSelectionPrice;
	}
	public long getItemOptionAdditionPrice() {
		return itemOptionAdditionPrice;
	}
	public void setItemOptionAdditionPrice(long itemOptionAdditionPrice) {
		this.itemOptionAdditionPrice = itemOptionAdditionPrice;
	}
	public long getReferencePrice() {
		return referencePrice;
	}
	public void setReferencePrice(long referencePrice) {
		this.referencePrice = referencePrice;
	}
	public String getBuyerMemo() {
		return buyerMemo;
	}
	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}
	public long getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(long shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getShippingFeeCondition() {
		return shippingFeeCondition;
	}
	public void setShippingFeeCondition(String shippingFeeCondition) {
		this.shippingFeeCondition = shippingFeeCondition;
	}
	public long getShippingFeeGroupCode() {
		return shippingFeeGroupCode;
	}
	public void setShippingFeeGroupCode(long shippingFeeGroupCode) {
		this.shippingFeeGroupCode = shippingFeeGroupCode;
	}
	public String getOutOrderNo() {
		return outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	public String getInventoryNo() {
		return inventoryNo;
	}
	public void setInventoryNo(String inventoryNo) {
		this.inventoryNo = inventoryNo;
	}
	public Timestamp getTransDueDate() {
		return transDueDate;
	}
	public void setTransDueDate(Timestamp transDueDate) {
		this.transDueDate = transDueDate;
	}
	public String getTransPolicyType() {
		return transPolicyType;
	}
	public void setTransPolicyType(String transPolicyType) {
		this.transPolicyType = transPolicyType;
	}
	public String getItemAddOptionValue() {
		return itemAddOptionValue;
	}
	public void setItemAddOptionValue(String itemAddOptionValue) {
		this.itemAddOptionValue = itemAddOptionValue;
	}
	public long getItemAddOptionOrderCnt() {
		return itemAddOptionOrderCnt;
	}
	public void setItemAddOptionOrderCnt(long itemAddOptionOrderCnt) {
		this.itemAddOptionOrderCnt = itemAddOptionOrderCnt;
	}
	public String getItemAddOptionCode() {
		return itemAddOptionCode;
	}
	public void setItemAddOptionCode(String itemAddOptionCode) {
		this.itemAddOptionCode = itemAddOptionCode;
	}
	public String getItemOptionValue() {
		return itemOptionValue;
	}
	public void setItemOptionValue(String itemOptionValue) {
		this.itemOptionValue = itemOptionValue;
	}
	public long getItemOptionOrderCnt() {
		return itemOptionOrderCnt;
	}
	public void setItemOptionOrderCnt(long itemOptionOrderCnt) {
		this.itemOptionOrderCnt = itemOptionOrderCnt;
	}
	public String getItemOptionCode() {
		return itemOptionCode;
	}
	public void setItemOptionCode(String itemOptionCode) {
		this.itemOptionCode = itemOptionCode;
	}
    
}
