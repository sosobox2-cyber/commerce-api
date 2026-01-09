package com.cware.netshopping.patdeal.domain;

import java.sql.Timestamp;
import java.util.List;

import com.cware.netshopping.patdeal.util.PaTdealComUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderList {
	
	private Timestamp orderYmdt;//setter > string 형태 날짜형태 변환로직 
	private Timestamp firstPayYmdt;//setter > string 형태 날짜형태 변환로직 
	
	private String orderNo;
	private String mallNo;
	private String ordererName;
	private String ordererContact1;
	private String ordererContact2;
	private String orderMemo;
	private long firstCartCouponDiscountAmt;
	private long firstPayAmt;
	private long firstSubPayAmt;
	private long lastCartCouponDiscountAmt;
	private long lastPayAmt;
	private String platformType;
	private String ordererEmail;
	private String payType;
	private String pgType;
	private String channelType;
	private long firstImmediateDiscountAmt;
	private long firstAdditionalDiscountAmt;
	private long firstProductCouponDiscountAmt;
	private long lastImmediateDiscountAmt;
	private long lastAdditionalDiscountAmt;
	private long lastProductCouponDiscountAmt;
	private long firstMainPayAmt;
	private long lastMainPayAmt;
	private String memberId;
	private String oauthIdNo;
	private String cartCouponIssueNo;
	private long firstTotalDiscountAmt;
	private long lastTotalDiscountAmt;
	private String payTypeLabel;
	private String ip;
	private String cashReceiptIssueNo;
	private long cashReceiptIssueYmdt;
	private String cashReceiptStatus;
	private String taxInvoiceIssueNo;
	private Timestamp taxInvoiceIssueYmdt;
	private String taxInvoiceStatus;
	private boolean cashReceiptIssued;
	private boolean taxInvoiceIssued;
	
	private String currencyCode;
	
	private OrderSheetInfo orderSheetInfo;
	private List<DeliveryGroups> deliveryGroups;
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	private String memberNo;
	
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	
	public List<DeliveryGroups> getDeliveryGroups() {
		return deliveryGroups;
	}
	public void setDeliveryGroups(List<DeliveryGroups> deliveryGroups) {
		this.deliveryGroups = deliveryGroups;
	}
	public OrderSheetInfo getOrderSheetInfo() {
		return orderSheetInfo;
	}
	public void setOrderSheetInfo(OrderSheetInfo orderSheetInfo) {
		this.orderSheetInfo = orderSheetInfo;
	}
	public Timestamp getOrderYmdt() {
		return orderYmdt;
	}
	public void setOrderYmdt(String orderYmdt) {
		this.orderYmdt = PaTdealComUtil.convertStrToTimestamp(orderYmdt);//string 형태 날짜형태 변환
	}
	public Timestamp getFirstPayYmdt() {
		return firstPayYmdt;
	}
	public void setFirstPayYmdt(String firstPayYmdt) {
		this.firstPayYmdt = PaTdealComUtil.convertStrToTimestamp(firstPayYmdt);//string 형태 날짜형태 변환
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMallNo() {
		return mallNo;
	}
	public void setMallNo(String mallNo) {
		this.mallNo = mallNo;
	}
	public String getOrdererName() {
		return ordererName;
	}
	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}
	public String getOrdererContact1() {
		return ordererContact1;
	}
	public void setOrdererContact1(String ordererContact1) {
		this.ordererContact1 = ordererContact1;
	}
	public String getOrdererContact2() {
		return ordererContact2;
	}
	public void setOrdererContact2(String ordererContact2) {
		this.ordererContact2 = ordererContact2;
	}
	public String getOrderMemo() {
		return orderMemo;
	}
	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}
	public long getFirstCartCouponDiscountAmt() {
		return firstCartCouponDiscountAmt;
	}
	public void setFirstCartCouponDiscountAmt(long firstCartCouponDiscountAmt) {
		this.firstCartCouponDiscountAmt = firstCartCouponDiscountAmt;
	}
	public long getFirstPayAmt() {
		return firstPayAmt;
	}
	public void setFirstPayAmt(long firstPayAmt) {
		this.firstPayAmt = firstPayAmt;
	}
	public long getFirstSubPayAmt() {
		return firstSubPayAmt;
	}
	public void setFirstSubPayAmt(long firstSubPayAmt) {
		this.firstSubPayAmt = firstSubPayAmt;
	}
	public long getLastCartCouponDiscountAmt() {
		return lastCartCouponDiscountAmt;
	}
	public void setLastCartCouponDiscountAmt(long lastCartCouponDiscountAmt) {
		this.lastCartCouponDiscountAmt = lastCartCouponDiscountAmt;
	}
	public long getLastPayAmt() {
		return lastPayAmt;
	}
	public void setLastPayAmt(long lastPayAmt) {
		this.lastPayAmt = lastPayAmt;
	}
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getOrdererEmail() {
		return ordererEmail;
	}
	public void setOrdererEmail(String ordererEmail) {
		this.ordererEmail = ordererEmail;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPgType() {
		return pgType;
	}
	public void setPgType(String pgType) {
		this.pgType = pgType;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public long getFirstImmediateDiscountAmt() {
		return firstImmediateDiscountAmt;
	}
	public void setFirstImmediateDiscountAmt(long firstImmediateDiscountAmt) {
		this.firstImmediateDiscountAmt = firstImmediateDiscountAmt;
	}
	public long getFirstAdditionalDiscountAmt() {
		return firstAdditionalDiscountAmt;
	}
	public void setFirstAdditionalDiscountAmt(long firstAdditionalDiscountAmt) {
		this.firstAdditionalDiscountAmt = firstAdditionalDiscountAmt;
	}
	public long getFirstProductCouponDiscountAmt() {
		return firstProductCouponDiscountAmt;
	}
	public void setFirstProductCouponDiscountAmt(long firstProductCouponDiscountAmt) {
		this.firstProductCouponDiscountAmt = firstProductCouponDiscountAmt;
	}
	public long getLastImmediateDiscountAmt() {
		return lastImmediateDiscountAmt;
	}
	public void setLastImmediateDiscountAmt(long lastImmediateDiscountAmt) {
		this.lastImmediateDiscountAmt = lastImmediateDiscountAmt;
	}
	public long getLastAdditionalDiscountAmt() {
		return lastAdditionalDiscountAmt;
	}
	public void setLastAdditionalDiscountAmt(long lastAdditionalDiscountAmt) {
		this.lastAdditionalDiscountAmt = lastAdditionalDiscountAmt;
	}
	public long getLastProductCouponDiscountAmt() {
		return lastProductCouponDiscountAmt;
	}
	public void setLastProductCouponDiscountAmt(long lastProductCouponDiscountAmt) {
		this.lastProductCouponDiscountAmt = lastProductCouponDiscountAmt;
	}
	public long getFirstMainPayAmt() {
		return firstMainPayAmt;
	}
	public void setFirstMainPayAmt(long firstMainPayAmt) {
		this.firstMainPayAmt = firstMainPayAmt;
	}
	public long getLastMainPayAmt() {
		return lastMainPayAmt;
	}
	public void setLastMainPayAmt(long lastMainPayAmt) {
		this.lastMainPayAmt = lastMainPayAmt;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOauthIdNo() {
		return oauthIdNo;
	}
	public void setOauthIdNo(String oauthIdNo) {
		this.oauthIdNo = oauthIdNo;
	}
	public String getCartCouponIssueNo() {
		return cartCouponIssueNo;
	}
	public void setCartCouponIssueNo(String cartCouponIssueNo) {
		this.cartCouponIssueNo = cartCouponIssueNo;
	}
	public long getFirstTotalDiscountAmt() {
		return firstTotalDiscountAmt;
	}
	public void setFirstTotalDiscountAmt(long firstTotalDiscountAmt) {
		this.firstTotalDiscountAmt = firstTotalDiscountAmt;
	}
	public long getLastTotalDiscountAmt() {
		return lastTotalDiscountAmt;
	}
	public void setLastTotalDiscountAmt(long lastTotalDiscountAmt) {
		this.lastTotalDiscountAmt = lastTotalDiscountAmt;
	}
	public String getPayTypeLabel() {
		return payTypeLabel;
	}
	public void setPayTypeLabel(String payTypeLabel) {
		this.payTypeLabel = payTypeLabel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCashReceiptIssueNo() {
		return cashReceiptIssueNo;
	}
	public void setCashReceiptIssueNo(String cashReceiptIssueNo) {
		this.cashReceiptIssueNo = cashReceiptIssueNo;
	}
	public long getCashReceiptIssueYmdt() {
		return cashReceiptIssueYmdt;
	}
	public void setCashReceiptIssueYmdt(long cashReceiptIssueYmdt) {
		this.cashReceiptIssueYmdt = cashReceiptIssueYmdt;
	}
	public String getCashReceiptStatus() {
		return cashReceiptStatus;
	}
	public void setCashReceiptStatus(String cashReceiptStatus) {
		this.cashReceiptStatus = cashReceiptStatus;
	}
	public String getTaxInvoiceIssueNo() {
		return taxInvoiceIssueNo;
	}
	public void setTaxInvoiceIssueNo(String taxInvoiceIssueNo) {
		this.taxInvoiceIssueNo = taxInvoiceIssueNo;
	}
	public Timestamp getTaxInvoiceIssueYmdt() {
		return taxInvoiceIssueYmdt;
	}
	public void setTaxInvoiceIssueYmdt(String taxInvoiceIssueYmdt) {
		this.taxInvoiceIssueYmdt = PaTdealComUtil.convertStrToTimestamp(taxInvoiceIssueYmdt);
	}
	public String getTaxInvoiceStatus() {
		return taxInvoiceStatus;
	}
	public void setTaxInvoiceStatus(String taxInvoiceStatus) {
		this.taxInvoiceStatus = taxInvoiceStatus;
	}
	public boolean isCashReceiptIssued() {
		return cashReceiptIssued;
	}
	public void setCashReceiptIssued(boolean cashReceiptIssued) {
		this.cashReceiptIssued = cashReceiptIssued;
	}
	public boolean isTaxInvoiceIssued() {
		return taxInvoiceIssued;
	}
	public void setTaxInvoiceIssued(boolean taxInvoiceIssued) {
		this.taxInvoiceIssued = taxInvoiceIssued;
	}
	
	
}
