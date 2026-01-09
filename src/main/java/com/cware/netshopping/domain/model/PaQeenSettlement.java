package com.cware.netshopping.domain.model;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PaQeenSettlement {
	private String   id;                                        
	private String   transactionType;                          
	private String   orderId;                                 
	private String   orderLineGroupId;                      
	private String   orderLineId;                            
	private long     sellerShare;                           
	private long     sellerDutyDiscount;                   
	private long     queenitShare;                           
	private long     queenitDutyDiscount;                   
	private long     amount;                                 
	private String   method;                                 
	private String   type;                                   
	private long     sellerCompensationAmount;             
	private String   lowestBaseDate;            
	private long     commissionPercentage;                  
	private String   includedBookId;                       
	private String   baseDate;                              
	private String   salesType;                             
	private String   memo;                                  
	private String   productId;                             
	private String   productCode;                           
	private String   optionTitle;                           
	private String   productTitle;                          
	private long     singleProductPrice;                   
	private long     quantity;                              
	private long     commissionTarget;                     
	private long     commission;                           
	private long     sellerProfit;                         
	private long     queenitProfit;                        
	private Date     eventAtMillis;                        
	private Date     deliveryCompletedAtMillis;           
	private long     sellerDirectCouponDiscountAmount;   
	private long     sellerIssuedCouponDiscountAmount;   
	private long     queenitDirectCouponDiscountAmount;  
	private long     queenitIssuedCouponDiscountAmount;  
	private long     queenitDiscountAmountByCoupon;      
	private long     queenitPointDiscountAmount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderLineGroupId() {
		return orderLineGroupId;
	}
	public void setOrderLineGroupId(String orderLineGroupId) {
		this.orderLineGroupId = orderLineGroupId;
	}
	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	public long getSellerShare() {
		return sellerShare;
	}
	public void setSellerShare(long sellerShare) {
		this.sellerShare = sellerShare;
	}
	public long getSellerDutyDiscount() {
		return sellerDutyDiscount;
	}
	public void setSellerDutyDiscount(long sellerDutyDiscount) {
		this.sellerDutyDiscount = sellerDutyDiscount;
	}
	public long getQueenitShare() {
		return queenitShare;
	}
	public void setQueenitShare(long queenitShare) {
		this.queenitShare = queenitShare;
	}
	public long getQueenitDutyDiscount() {
		return queenitDutyDiscount;
	}
	public void setQueenitDutyDiscount(long queenitDutyDiscount) {
		this.queenitDutyDiscount = queenitDutyDiscount;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSellerCompensationAmount() {
		return sellerCompensationAmount;
	}
	public void setSellerCompensationAmount(long sellerCompensationAmount) {
		this.sellerCompensationAmount = sellerCompensationAmount;
	}
	public String getLowestBaseDate() {
		return lowestBaseDate;
	}
	public void setLowestBaseDate(String lowestBaseDate) {
		this.lowestBaseDate = lowestBaseDate;
	}
	public long getCommissionPercentage() {
		return commissionPercentage;
	}
	public void setCommissionPercentage(long commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}
	public String getIncludedBookId() {
		return includedBookId;
	}
	public void setIncludedBookId(String includedBookId) {
		this.includedBookId = includedBookId;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public long getSingleProductPrice() {
		return singleProductPrice;
	}
	public void setSingleProductPrice(long singleProductPrice) {
		this.singleProductPrice = singleProductPrice;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getCommissionTarget() {
		return commissionTarget;
	}
	public void setCommissionTarget(long commissionTarget) {
		this.commissionTarget = commissionTarget;
	}
	public long getCommission() {
		return commission;
	}
	public void setCommission(long commission) {
		this.commission = commission;
	}
	public long getSellerProfit() {
		return sellerProfit;
	}
	public void setSellerProfit(long sellerProfit) {
		this.sellerProfit = sellerProfit;
	}
	public long getQueenitProfit() {
		return queenitProfit;
	}
	public void setQueenitProfit(long queenitProfit) {
		this.queenitProfit = queenitProfit;
	}
	public Date getEventAtMillis() {
		return eventAtMillis;
	}
	public void setEventAtMillis(Date eventAtMillis) {
		this.eventAtMillis = eventAtMillis;
	}
	public Date getDeliveryCompletedAtMillis() {
		return deliveryCompletedAtMillis;
	}
	public void setDeliveryCompletedAtMillis(Date deliveryCompletedAtMillis) {
		this.deliveryCompletedAtMillis = deliveryCompletedAtMillis;
	}
	public long getSellerDirectCouponDiscountAmount() {
		return sellerDirectCouponDiscountAmount;
	}
	public void setSellerDirectCouponDiscountAmount(long sellerDirectCouponDiscountAmount) {
		this.sellerDirectCouponDiscountAmount = sellerDirectCouponDiscountAmount;
	}
	public long getSellerIssuedCouponDiscountAmount() {
		return sellerIssuedCouponDiscountAmount;
	}
	public void setSellerIssuedCouponDiscountAmount(long sellerIssuedCouponDiscountAmount) {
		this.sellerIssuedCouponDiscountAmount = sellerIssuedCouponDiscountAmount;
	}
	public long getQueenitDirectCouponDiscountAmount() {
		return queenitDirectCouponDiscountAmount;
	}
	public void setQueenitDirectCouponDiscountAmount(long queenitDirectCouponDiscountAmount) {
		this.queenitDirectCouponDiscountAmount = queenitDirectCouponDiscountAmount;
	}
	public long getQueenitIssuedCouponDiscountAmount() {
		return queenitIssuedCouponDiscountAmount;
	}
	public void setQueenitIssuedCouponDiscountAmount(long queenitIssuedCouponDiscountAmount) {
		this.queenitIssuedCouponDiscountAmount = queenitIssuedCouponDiscountAmount;
	}
	public long getQueenitDiscountAmountByCoupon() {
		return queenitDiscountAmountByCoupon;
	}
	public void setQueenitDiscountAmountByCoupon(long queenitDiscountAmountByCoupon) {
		this.queenitDiscountAmountByCoupon = queenitDiscountAmountByCoupon;
	}
	public long getQueenitPointDiscountAmount() {
		return queenitPointDiscountAmount;
	}
	public void setQueenitPointDiscountAmount(long queenitPointDiscountAmount) {
		this.queenitPointDiscountAmount = queenitPointDiscountAmount;
	}
	
}