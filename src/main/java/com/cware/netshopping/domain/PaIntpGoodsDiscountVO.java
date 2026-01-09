package com.cware.netshopping.domain;


import com.cware.netshopping.domain.model.PaGoodsPrice;


public class PaIntpGoodsDiscountVO extends PaGoodsPrice {
	private static final long serialVersionUID = 1L;

	private String paCode;
	private int discountNo;
	private String discountName;
	private String discountType;
	private int discountPrice;
	private String issueStartDate;
	private String issueEndDate;
	private String isUse;
	
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	
	public int getDiscountNo() {
		return discountNo;
	}
	public void setDiscountNo(int discountNo) {
		this.discountNo = discountNo;
	}
	
	public String getDiscountName() {
		return discountName;
	}
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	
	public int getDiscountPrice() {
		return discountPrice;
	}	
	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}
	
	public String getIssueStartDate() {
		return issueStartDate;
	}
	public void setIssueStartDate(String issueStartDate) {
		this.issueStartDate = issueStartDate;
	}
	
	public String getIssueEndDate() {
		return issueEndDate;
	}
	public void setIssueEndDate(String issueEndDate) {
		this.issueEndDate = issueEndDate;
	}
	
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
}