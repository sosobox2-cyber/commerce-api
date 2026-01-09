package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketItem {
	private String orderLineId;
	private String salesType;
	private String salesStatus;
	private String sellerCompanyCode;
	private String sellerCompanyName;
	private String brandName;
	private String productName;
	private String productId;
	private String mallProductCode;
	private PurchaseOption purchaseOption;
	private List<ExchangeOption> exchangeOptions;
	private Reason reason;
	private boolean freeExchangeTarget;
	private boolean freeReturnTarget;

	public String getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public String getSalesStatus() {
		return salesStatus;
	}

	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}

	public String getSellerCompanyCode() {
		return sellerCompanyCode;
	}

	public void setSellerCompanyCode(String sellerCompanyCode) {
		this.sellerCompanyCode = sellerCompanyCode;
	}

	public String getSellerCompanyName() {
		return sellerCompanyName;
	}

	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMallProductCode() {
		return mallProductCode;
	}

	public void setMallProductCode(String mallProductCode) {
		this.mallProductCode = mallProductCode;
	}

	public PurchaseOption getPurchaseOption() {
		return purchaseOption;
	}

	public void setPurchaseOption(PurchaseOption purchaseOption) {
		this.purchaseOption = purchaseOption;
	}

	public List<ExchangeOption> getExchangeOptions() {
		return exchangeOptions;
	}

	public void setExchangeOptions(List<ExchangeOption> exchangeOptions) {
		this.exchangeOptions = exchangeOptions;
	}

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public boolean isFreeExchangeTarget() {
		return freeExchangeTarget;
	}

	public void setFreeExchangeTarget(boolean freeExchangeTarget) {
		this.freeExchangeTarget = freeExchangeTarget;
	}

	public boolean isFreeReturnTarget() {
		return freeReturnTarget;
	}

	public void setFreeReturnTarget(boolean freeReturnTarget) {
		this.freeReturnTarget = freeReturnTarget;
	}

}
