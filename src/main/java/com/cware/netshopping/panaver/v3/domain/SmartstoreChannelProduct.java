package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SmartstoreChannelProduct {
		
	// 채널 상품 전용 상품명
	private String channelProductName;
	// 콘텐츠 게시글 일련번호
	private Long bbsSeq;
	// 알림받기 동의 회원 전용 상품 여부
	private String storeKeepExclusiveProduct;
	// 네이버쇼핑 등록 여부
	private String naverShoppingRegistration;
	// 전시 상태 코드(스마트스토어 채널 전용)
	private String channelProductDisplayStatusType;
	
	
	public String getChannelProductName() {
		return channelProductName;
	}

	public void setChannelProductName(String channelProductName) {
		this.channelProductName = channelProductName;
	}

	public  Long getBbsSeq() {
		return bbsSeq;
	}

	public void setBbsSeq(Long bbsSeq) {
		this.bbsSeq = bbsSeq;
	}

	public String getStoreKeepExclusiveProduct() {
		return storeKeepExclusiveProduct;
	}

	public void setStoreKeepExclusiveProduct(String storeKeepExclusiveProduct) {
		this.storeKeepExclusiveProduct = storeKeepExclusiveProduct;
	}

	public String getNaverShoppingRegistration() {
		return naverShoppingRegistration;
	}

	public void setNaverShoppingRegistration(String naverShoppingRegistration) {
		this.naverShoppingRegistration = naverShoppingRegistration;
	}

	public String getChannelProductDisplayStatusType() {
		return channelProductDisplayStatusType;
	}

	public void setChannelProductDisplayStatusType(String channelProductDisplayStatusType) {
		this.channelProductDisplayStatusType = channelProductDisplayStatusType;
	}

	@Override
	public String toString() {
		return "SmartstoreChannelProduct [channelProductName=" + channelProductName +"bbsSeq=" + bbsSeq + "storeKeepExclusiveProduct=" + storeKeepExclusiveProduct + "naverShoppingRegistration=" + naverShoppingRegistration
				+ "channelProductDisplayStatusType=" + channelProductDisplayStatusType +"]";
	}

}
