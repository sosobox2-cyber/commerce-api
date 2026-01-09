package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProducts {
	
	private String orderProductNo;
	private String mallProductNo;
	private String productName;
	private String productNameEn;
	private String productManagementCd;
	private String imageUrl;
	private String hsCode;
	
	private long firstProductCouponDiscountAmt;
	private long lastProductCouponDiscountAmt;
	private long productCouponIssueNo;
	private String reservationDeliveryStartYmdt;
	
	private List<OrderProductOptions> orderProductOptions;
	private List<OrderOptions> orderOptions;
	
	public String getOrderProductNo() {
		return orderProductNo;
	}

	public void setOrderProductNo(String orderProductNo) {
		this.orderProductNo = orderProductNo;
	}

	public String getMallProductNo() {
		return mallProductNo;
	}

	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNameEn() {
		return productNameEn;
	}

	public void setProductNameEn(String productNameEn) {
		this.productNameEn = productNameEn;
	}

	public String getProductManagementCd() {
		return productManagementCd;
	}

	public void setProductManagementCd(String productManagementCd) {
		this.productManagementCd = productManagementCd;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public long getFirstProductCouponDiscountAmt() {
		return firstProductCouponDiscountAmt;
	}

	public void setFirstProductCouponDiscountAmt(long firstProductCouponDiscountAmt) {
		this.firstProductCouponDiscountAmt = firstProductCouponDiscountAmt;
	}

	public long getLastProductCouponDiscountAmt() {
		return lastProductCouponDiscountAmt;
	}

	public void setLastProductCouponDiscountAmt(long lastProductCouponDiscountAmt) {
		this.lastProductCouponDiscountAmt = lastProductCouponDiscountAmt;
	}

	public long getProductCouponIssueNo() {
		return productCouponIssueNo;
	}

	public void setProductCouponIssueNo(long productCouponIssueNo) {
		this.productCouponIssueNo = productCouponIssueNo;
	}

	public List<OrderProductOptions> getOrderProductOptions() {
		return orderProductOptions;
	}

	public void setOrderProductOptions(List<OrderProductOptions> orderProductOptions) {
		this.orderProductOptions = orderProductOptions;
	}

	public List<OrderOptions> getOrderOptions() {
		return orderOptions;
	}

	public void setOrderOptions(List<OrderOptions> orderOptions) {
		this.orderOptions = orderOptions;
	}
	
	public String getReservationDeliveryStartYmdt() {
		return reservationDeliveryStartYmdt;
	}

	public void setReservationDeliveryStartYmdt(String reservationDeliveryStartYmdt) {
		this.reservationDeliveryStartYmdt = reservationDeliveryStartYmdt;
	}
	
	@Override
	public String toString() {
		return "OrderProducts [orderProductNo=" + orderProductNo + ", mallProductNo=" + mallProductNo + ", productName="
				+ productName + ", productNameEn=" + productNameEn + ", productManagementCd=" + productManagementCd
				+ ", imageUrl=" + imageUrl + ", hsCode=" + hsCode + ", firstProductCouponDiscountAmt="
				+ firstProductCouponDiscountAmt + ", lastProductCouponDiscountAmt=" + lastProductCouponDiscountAmt
				+ ", productCouponIssueNo=" + productCouponIssueNo + ", orderProductOptions=" + orderProductOptions
				+ ", orderOptions="+ orderOptions + ", reservationDeliveryStartYmdt=" + reservationDeliveryStartYmdt
				+ "]";
	}
	

	
}
