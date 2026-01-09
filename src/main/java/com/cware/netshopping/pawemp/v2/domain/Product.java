package com.cware.netshopping.pawemp.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
	// 상품번호 (입력안하면 등록, 입력하면 수정)
	String productNo;
	// 기본정보 (필수)
	Basic basic;
	// 판매정보 (필수)
	Sale sale;
	// 상세정보 (필수)
	Detail detail;
	// 옵션정보 (필수)
	Option option;
	// 상품정보고시 (필수)
	// (최대 10개)
	List<NoticeGroup> noticeList;
	// 기타정보 (필수)
	Etc etc;
	// 지점정보 (최대 200개)
	List<Branch> branchList;

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public Basic getBasic() {
		return basic;
	}

	public void setBasic(Basic basic) {
		this.basic = basic;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

	public List<NoticeGroup> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<NoticeGroup> noticeList) {
		this.noticeList = noticeList;
	}

	public Etc getEtc() {
		return etc;
	}

	public void setEtc(Etc etc) {
		this.etc = etc;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<Branch> branchList) {
		this.branchList = branchList;
	}

	@Override
	public String toString() {
		return "Product [productNo=" + productNo + ", basic=" + basic + ", sale=" + sale + ", detail=" + detail
				+ ", option=" + option + ", noticeList=" + noticeList + ", etc=" + etc + ", branchList=" + branchList
				+ "]";
	}

}
