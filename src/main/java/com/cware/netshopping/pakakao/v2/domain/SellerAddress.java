package com.cware.netshopping.pakakao.v2.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerAddress {
	String id; // 주소지 ID

	// 배송지 명
	// 동일한 배송지명으로 중복 등록 불가
	// 50자 이내로 입력 가능
	String name;

	// 우편번호
	String postNo;

	// 연락처
	String contact;

	// 제조사명
	String addressPost;

	// 상세 주소
	String addressDetail;

	// 반품 배송비
	// 0원 이상 50만원 이하 금액 입력 가능
	BigDecimal returnFeeAmount;

	// 교환 배송비
	// 0원이상 100만원이하 등록 가능
	BigDecimal exchangeFeeAmount;

	// 기본 반품지 여부
	boolean basicReturnAddress;

	// 기본 출고지 여부
	boolean basicSenderAddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostNo() {
		return postNo;
	}

	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddressPost() {
		return addressPost;
	}

	public void setAddressPost(String addressPost) {
		this.addressPost = addressPost;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public BigDecimal getReturnFeeAmount() {
		return returnFeeAmount;
	}

	public void setReturnFeeAmount(BigDecimal returnFeeAmount) {
		this.returnFeeAmount = returnFeeAmount;
	}

	public BigDecimal getExchangeFeeAmount() {
		return exchangeFeeAmount;
	}

	public void setExchangeFeeAmount(BigDecimal exchangeFeeAmount) {
		this.exchangeFeeAmount = exchangeFeeAmount;
	}

	public boolean isBasicReturnAddress() {
		return basicReturnAddress;
	}

	public void setBasicReturnAddress(boolean basicReturnAddress) {
		this.basicReturnAddress = basicReturnAddress;
	}

	public boolean isBasicSenderAddress() {
		return basicSenderAddress;
	}

	public void setBasicSenderAddress(boolean basicSenderAddress) {
		this.basicSenderAddress = basicSenderAddress;
	}

	@Override
	public String toString() {
		return "SellerAddress [id=" + id + ", name=" + name + ", postNo=" + postNo + ", contact=" + contact
				+ ", addressPost=" + addressPost + ", addressDetail=" + addressDetail + ", returnFeeAmount="
				+ returnFeeAmount + ", exchangeFeeAmount=" + exchangeFeeAmount + ", basicReturnAddress="
				+ basicReturnAddress + ", basicSenderAddress=" + basicSenderAddress + "]";
	}

}
