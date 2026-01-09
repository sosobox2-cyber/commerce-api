package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import com.cware.framework.core.basic.AbstractModel;

public class PaNaverOrderList extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String productOrderId;
	private String productOrderStatus;
	private String claimType;
	private String claimStatus;
	private String productId;
	private String productName;
	private String productClass;
	private String productOption;
	private long quantity;
	private long unitPrice;
	private long optionPrice;
	private String optionCode;
	private long totalProductAmt;
	private long productDiscountAmt;
	private long productImediateDiscountAmt;
	private long productProductDiscountAmt;
	private long productMultipleDiscountAmt;
	private long totalPaymentAmt;
	private String sellerProductCode;
	private String mallId;
	private String shippingAddressSeq;
	private String expectedDeliveryMethod;
	private String packageNumber;
	private String shippingFeeType;
	private String deliveryPolicyType;
	private long deliveryFeeAmt;
	private long sectionDeliveryFee;
	private long deliveryDiscountAmt;
	private String shippingMemo;
	private String takingAddressSeq;
	private Timestamp shippingDueDate;
	private Timestamp decisionDate;
	private String freeGift;
	private String placeOrderStatus;
	private Timestamp placeOrderDate;
	private String delayedReason;
	private String delayedDetailedReason;
	private long sellerDiscountAmt;
	private long sellerImediateDiscountAmt;
	private long sellerProductDiscountAmt;
	private long sellerMultipleDiscountAmt;
	private String commisionRatingType;
	private String commisionPrePayStatus;
	private long paymentCommision;
	private long saleCommision;
	private long channelCommision;
	private long interlockCommision;
	private long expectedSettlementAmt;
	private String inflowPath;
	private String itemNo;
	private String optionManagerCode;
	private String purchaserSocialSecurityNo;
	private String sellerCustomCode1;
	private String sellerCustomCode2;
	private String claimId;
	private String individualCustomUniqueCode;
	private String giftReceivingStatus;
	private Timestamp insertDate;
	private Timestamp updateDate;

	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	public String getProductOrderId() { 
		return this.productOrderId;
	}
	public String getProductOrderStatus() { 
		return this.productOrderStatus;
	}
	public String getClaimType() { 
		return this.claimType;
	}
	public String getClaimStatus() { 
		return this.claimStatus;
	}
	public String getProductId() { 
		return this.productId;
	}
	public String getProductName() { 
		return this.productName;
	}
	public String getProductClass() { 
		return this.productClass;
	}
	public String getProductOption() { 
		return this.productOption;
	}
	public long getQuantity() { 
		return this.quantity;
	}
	public long getUnitPrice() { 
		return this.unitPrice;
	}
	public long getOptionPrice() { 
		return this.optionPrice;
	}
	public String getOptionCode() { 
		return this.optionCode;
	}
	public long getTotalProductAmt() { 
		return this.totalProductAmt;
	}
	public long getProductDiscountAmt() { 
		return this.productDiscountAmt;
	}
	public long getProductImediateDiscountAmt() { 
		return this.productImediateDiscountAmt;
	}
	public long getProductProductDiscountAmt() { 
		return this.productProductDiscountAmt;
	}
	public long getProductMultipleDiscountAmt() { 
		return this.productMultipleDiscountAmt;
	}
	public long getTotalPaymentAmt() { 
		return this.totalPaymentAmt;
	}
	public String getSellerProductCode() { 
		return this.sellerProductCode;
	}
	public String getMallId() { 
		return this.mallId;
	}
	public String getShippingAddressSeq() { 
		return this.shippingAddressSeq;
	}
	public String getExpectedDeliveryMethod() { 
		return this.expectedDeliveryMethod;
	}
	public String getPackageNumber() { 
		return this.packageNumber;
	}
	public String getShippingFeeType() { 
		return this.shippingFeeType;
	}
	public String getDeliveryPolicyType() { 
		return this.deliveryPolicyType;
	}
	public long getDeliveryFeeAmt() { 
		return this.deliveryFeeAmt;
	}
	public long getSectionDeliveryFee() { 
		return this.sectionDeliveryFee;
	}
	public long getDeliveryDiscountAmt() { 
		return this.deliveryDiscountAmt;
	}
	public String getShippingMemo() { 
		return this.shippingMemo;
	}
	public String getTakingAddressSeq() { 
		return this.takingAddressSeq;
	}
	public Timestamp getShippingDueDate() { 
		return this.shippingDueDate;
	}
	public Timestamp getDecisionDate() { 
		return this.decisionDate;
	}
	public String getFreeGift() { 
		return this.freeGift;
	}
	public String getPlaceOrderStatus() { 
		return this.placeOrderStatus;
	}
	public Timestamp getPlaceOrderDate() { 
		return this.placeOrderDate;
	}
	public String getDelayedReason() { 
		return this.delayedReason;
	}
	public String getDelayedDetailedReason() { 
		return this.delayedDetailedReason;
	}
	public long getSellerDiscountAmt() { 
		return this.sellerDiscountAmt;
	}
	public long getSellerImediateDiscountAmt() { 
		return this.sellerImediateDiscountAmt;
	}
	public long getSellerProductDiscountAmt() { 
		return this.sellerProductDiscountAmt;
	}
	public long getSellerMultipleDiscountAmt() { 
		return this.sellerMultipleDiscountAmt;
	}
	public String getCommisionRatingType() { 
		return this.commisionRatingType;
	}
	public String getCommisionPrePayStatus() { 
		return this.commisionPrePayStatus;
	}
	public long getPaymentCommision() { 
		return this.paymentCommision;
	}
	public long getSaleCommision() { 
		return this.saleCommision;
	}
	public long getChannelCommision() { 
		return this.channelCommision;
	}
	public long getInterlockCommision() { 
		return this.interlockCommision;
	}
	public long getExpectedSettlementAmt() { 
		return this.expectedSettlementAmt;
	}
	public String getInflowPath() { 
		return this.inflowPath;
	}
	public String getItemNo() { 
		return this.itemNo;
	}
	public String getOptionManagerCode() { 
		return this.optionManagerCode;
	}
	public String getPurchaserSocialSecurityNo() { 
		return this.purchaserSocialSecurityNo;
	}
	public String getSellerCustomCode1() { 
		return this.sellerCustomCode1;
	}
	public String getSellerCustomCode2() { 
		return this.sellerCustomCode2;
	}
	public String getClaimId() { 
		return this.claimId;
	}
	public String getIndividualCustomUniqueCode() { 
		return this.individualCustomUniqueCode;
	}
	public String getGiftReceivingStatus() { 
		return this.giftReceivingStatus;
	}

	public void setProductOrderId(String productOrderId) { 
		this.productOrderId = productOrderId;
	}
	public void setProductOrderStatus(String productOrderStatus) { 
		this.productOrderStatus = productOrderStatus;
	}
	public void setClaimType(String claimType) { 
		this.claimType = claimType;
	}
	public void setClaimStatus(String claimStatus) { 
		this.claimStatus = claimStatus;
	}
	public void setProductId(String productId) { 
		this.productId = productId;
	}
	public void setProductName(String productName) { 
		this.productName = productName;
	}
	public void setProductClass(String productClass) { 
		this.productClass = productClass;
	}
	public void setProductOption(String productOption) { 
		this.productOption = productOption;
	}
	public void setQuantity(long quantity) { 
		this.quantity = quantity;
	}
	public void setUnitPrice(long unitPrice) { 
		this.unitPrice = unitPrice;
	}
	public void setOptionPrice(long optionPrice) { 
		this.optionPrice = optionPrice;
	}
	public void setOptionCode(String optionCode) { 
		this.optionCode = optionCode;
	}
	public void setTotalProductAmt(long totalProductAmt) { 
		this.totalProductAmt = totalProductAmt;
	}
	public void setProductDiscountAmt(long productDiscountAmt) { 
		this.productDiscountAmt = productDiscountAmt;
	}
	public void setProductImediateDiscountAmt(long productImediateDiscountAmt) { 
		this.productImediateDiscountAmt = productImediateDiscountAmt;
	}
	public void setProductProductDiscountAmt(long productProductDiscountAmt) { 
		this.productProductDiscountAmt = productProductDiscountAmt;
	}
	public void setProductMultipleDiscountAmt(long productMultipleDiscountAmt) { 
		this.productMultipleDiscountAmt = productMultipleDiscountAmt;
	}
	public void setTotalPaymentAmt(long totalPaymentAmt) { 
		this.totalPaymentAmt = totalPaymentAmt;
	}
	public void setSellerProductCode(String sellerProductCode) { 
		this.sellerProductCode = sellerProductCode;
	}
	public void setMallId(String mallId) { 
		this.mallId = mallId;
	}
	public void setShippingAddressSeq(String shippingAddressSeq) { 
		this.shippingAddressSeq = shippingAddressSeq;
	}
	public void setExpectedDeliveryMethod(String expectedDeliveryMethod) { 
		this.expectedDeliveryMethod = expectedDeliveryMethod;
	}
	public void setPackageNumber(String packageNumber) { 
		this.packageNumber = packageNumber;
	}
	public void setShippingFeeType(String shippingFeeType) { 
		this.shippingFeeType = shippingFeeType;
	}
	public void setDeliveryPolicyType(String deliveryPolicyType) { 
		this.deliveryPolicyType = deliveryPolicyType;
	}
	public void setDeliveryFeeAmt(long deliveryFeeAmt) { 
		this.deliveryFeeAmt = deliveryFeeAmt;
	}
	public void setSectionDeliveryFee(long sectionDeliveryFee) { 
		this.sectionDeliveryFee = sectionDeliveryFee;
	}
	public void setDeliveryDiscountAmt(long deliveryDiscountAmt) { 
		this.deliveryDiscountAmt = deliveryDiscountAmt;
	}
	public void setShippingMemo(String shippingMemo) { 
		this.shippingMemo = shippingMemo;
	}
	public void setTakingAddressSeq(String takingAddressSeq) { 
		this.takingAddressSeq = takingAddressSeq;
	}
	public void setShippingDueDate(Timestamp shippingDueDate) { 
		this.shippingDueDate = shippingDueDate;
	}
	public void setDecisionDate(Timestamp decisionDate) { 
		this.decisionDate = decisionDate;
	}
	public void setFreeGift(String freeGift) { 
		this.freeGift = freeGift;
	}
	public void setPlaceOrderStatus(String placeOrderStatus) { 
		this.placeOrderStatus = placeOrderStatus;
	}
	public void setPlaceOrderDate(Timestamp placeOrderDate) { 
		this.placeOrderDate = placeOrderDate;
	}
	public void setDelayedReason(String delayedReason) { 
		this.delayedReason = delayedReason;
	}
	public void setDelayedDetailedReason(String delayedDetailedReason) { 
		this.delayedDetailedReason = delayedDetailedReason;
	}
	public void setSellerDiscountAmt(long sellerDiscountAmt) { 
		this.sellerDiscountAmt = sellerDiscountAmt;
	}
	public void setSellerImediateDiscountAmt(long sellerImediateDiscountAmt) { 
		this.sellerImediateDiscountAmt = sellerImediateDiscountAmt;
	}
	public void setSellerProductDiscountAmt(long sellerProductDiscountAmt) { 
		this.sellerProductDiscountAmt = sellerProductDiscountAmt;
	}
	public void setSellerMultipleDiscountAmt(long sellerMultipleDiscountAmt) { 
		this.sellerMultipleDiscountAmt = sellerMultipleDiscountAmt;
	}
	public void setCommisionRatingType(String commisionRatingType) { 
		this.commisionRatingType = commisionRatingType;
	}
	public void setCommisionPrePayStatus(String commisionPrePayStatus) { 
		this.commisionPrePayStatus = commisionPrePayStatus;
	}
	public void setPaymentCommision(long paymentCommision) { 
		this.paymentCommision = paymentCommision;
	}
	public void setSaleCommision(long saleCommision) { 
		this.saleCommision = saleCommision;
	}
	public void setChannelCommision(long channelCommision) { 
		this.channelCommision = channelCommision;
	}
	public void setInterlockCommision(long interlockCommision) { 
		this.interlockCommision = interlockCommision;
	}
	public void setExpectedSettlementAmt(long expectedSettlementAmt) { 
		this.expectedSettlementAmt = expectedSettlementAmt;
	}
	public void setInflowPath(String inflowPath) { 
		this.inflowPath = inflowPath;
	}
	public void setItemNo(String itemNo) { 
		this.itemNo = itemNo;
	}
	public void setOptionManagerCode(String optionManagerCode) { 
		this.optionManagerCode = optionManagerCode;
	}
	public void setPurchaserSocialSecurityNo(String purchaserSocialSecurityNo) { 
		this.purchaserSocialSecurityNo = purchaserSocialSecurityNo;
	}
	public void setSellerCustomCode1(String sellerCustomCode1) { 
		this.sellerCustomCode1 = sellerCustomCode1;
	}
	public void setSellerCustomCode2(String sellerCustomCode2) { 
		this.sellerCustomCode2 = sellerCustomCode2;
	}
	public void setClaimId(String claimId) { 
		this.claimId = claimId;
	}
	public void setIndividualCustomUniqueCode(String individualCustomUniqueCode) { 
		this.individualCustomUniqueCode = individualCustomUniqueCode;
	}
	public void setGiftReceivingStatus(String giftReceivingStatus) { 
		this.giftReceivingStatus = giftReceivingStatus;
	}
}
