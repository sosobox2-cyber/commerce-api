package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Categories {
		
	// 수수료
	private double commissionRate;
	// 2depth 카테고리 전시순서
	private int depth2DisplayOrder;
	// 1depth 카테고리 전시순서
	private int depth1DisplayOrder;
	// 1depth 카테고리 전시 코드
	private String depth1DisplayCd;
	// 2depth 카테고리 명
	private String depth2CategoryName;
	// 2depth 카테고리 전시 코드
	private String depth2DisplayCd;
	// 3depth 카테고리 번호
	private String depth3CategoryNo;
	// 성인전용 여부 (Y: 성인전용, N: 일반)
	private String adultYn;
	// 1depth 카테고리 명
	private String depth1CategoryName;
	// 2depth 카테고리 번호
	private String depth2CategoryNo;
	// 1depth 카테고리 번호
	private String depth1CategoryNo;
	// 3depth 카테고리 전시 코드
	private String depth3DisplayCd;
	// 카테고리 명
	private String fullCategoryName;
	// 4depth 카테고리 전시순서
	private int depth4DisplayOrder;
	// 4depth 카테고리 번호
	private String depth4CategoryNo;
	// 3depth 카테고리 명
	private String depth3CategoryName;
	// 4depth 카테고리 전시 코드
	private String depth4DisplayCd;
	// 3depth 카테고리 전시순서
	private int depth3DisplayOrder;
	// 4depth 카테고리 명
	private String depth4CategoryName;
	
	public double getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(double commissionRate) {
		this.commissionRate = commissionRate;
	}
	public int getDepth2DisplayOrder() {
		return depth2DisplayOrder;
	}
	public void setDepth2DisplayOrder(int depth2DisplayOrder) {
		this.depth2DisplayOrder = depth2DisplayOrder;
	}
	public int getDepth1DisplayOrder() {
		return depth1DisplayOrder;
	}
	public void setDepth1DisplayOrder(int depth1DisplayOrder) {
		this.depth1DisplayOrder = depth1DisplayOrder;
	}
	public String getDepth1DisplayCd() {
		return depth1DisplayCd;
	}
	public void setDepth1DisplayCd(String depth1DisplayCd) {
		this.depth1DisplayCd = depth1DisplayCd;
	}
	public String getDepth2CategoryName() {
		return depth2CategoryName;
	}
	public void setDepth2CategoryName(String depth2CategoryName) {
		this.depth2CategoryName = depth2CategoryName;
	}
	public String getDepth2DisplayCd() {
		return depth2DisplayCd;
	}
	public void setDepth2DisplayCd(String depth2DisplayCd) {
		this.depth2DisplayCd = depth2DisplayCd;
	}
	public String getDepth3CategoryNo() {
		return depth3CategoryNo;
	}
	public void setDepth3CategoryNo(String depth3CategoryNo) {
		this.depth3CategoryNo = depth3CategoryNo;
	}
	public String getAdultYn() {
		return adultYn;
	}
	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}
	public String getDepth1CategoryName() {
		return depth1CategoryName;
	}
	public void setDepth1CategoryName(String depth1CategoryName) {
		this.depth1CategoryName = depth1CategoryName;
	}
	public String getDepth2CategoryNo() {
		return depth2CategoryNo;
	}
	public void setDepth2CategoryNo(String depth2CategoryNo) {
		this.depth2CategoryNo = depth2CategoryNo;
	}
	public String getDepth1CategoryNo() {
		return depth1CategoryNo;
	}
	public void setDepth1CategoryNo(String depth1CategoryNo) {
		this.depth1CategoryNo = depth1CategoryNo;
	}
	public String getDepth3DisplayCd() {
		return depth3DisplayCd;
	}
	public void setDepth3DisplayCd(String depth3DisplayCd) {
		this.depth3DisplayCd = depth3DisplayCd;
	}
	public String getFullCategoryName() {
		return fullCategoryName;
	}
	public void setFullCategoryName(String fullCategoryName) {
		this.fullCategoryName = fullCategoryName;
	}
	public int getDepth4DisplayOrder() {
		return depth4DisplayOrder;
	}
	public void setDepth4DisplayOrder(int depth4DisplayOrder) {
		this.depth4DisplayOrder = depth4DisplayOrder;
	}
	public String getDepth4CategoryNo() {
		return depth4CategoryNo;
	}
	public void setDepth4CategoryNo(String depth4CategoryNo) {
		this.depth4CategoryNo = depth4CategoryNo;
	}
	public String getDepth3CategoryName() {
		return depth3CategoryName;
	}
	public void setDepth3CategoryName(String depth3CategoryName) {
		this.depth3CategoryName = depth3CategoryName;
	}
	public String getDepth4DisplayCd() {
		return depth4DisplayCd;
	}
	public void setDepth4DisplayCd(String depth4DisplayCd) {
		this.depth4DisplayCd = depth4DisplayCd;
	}
	public int getDepth3DisplayOrder() {
		return depth3DisplayOrder;
	}
	public void setDepth3DisplayOrder(int depth3DisplayOrder) {
		this.depth3DisplayOrder = depth3DisplayOrder;
	}
	public String getDepth4CategoryName() {
		return depth4CategoryName;
	}
	public void setDepth4CategoryName(String depth4CategoryName) {
		this.depth4CategoryName = depth4CategoryName;
	}
	
	@Override
	public String toString() {
		return "Categories [commissionRate=" + commissionRate + ", depth2DisplayOrder=" + depth2DisplayOrder
				+ ", depth1DisplayOrder=" + depth1DisplayOrder + ", depth1DisplayCd=" + depth1DisplayCd
				+ ", depth2CategoryName=" + depth2CategoryName + ", depth2DisplayCd=" + depth2DisplayCd
				+ ", depth3CategoryNo=" + depth3CategoryNo + ", adultYn=" + adultYn + ", depth1CategoryName="
				+ depth1CategoryName + ", depth2CategoryNo=" + depth2CategoryNo + ", depth1CategoryNo="
				+ depth1CategoryNo + ", depth3DisplayCd=" + depth3DisplayCd + ", fullCategoryName=" + fullCategoryName
				+ ", depth4DisplayOrder=" + depth4DisplayOrder + ", depth4CategoryNo=" + depth4CategoryNo
				+ ", depth3CategoryName=" + depth3CategoryName + ", depth4DisplayCd=" + depth4DisplayCd
				+ ", depth3DisplayOrder=" + depth3DisplayOrder + ", depth4CategoryName=" + depth4CategoryName + "]";
	}
}
