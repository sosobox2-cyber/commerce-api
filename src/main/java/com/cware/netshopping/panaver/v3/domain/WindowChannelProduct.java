package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class WindowChannelProduct {
		
	// 채널 상품 전용 상품명
	private String channelProductName;
	// 콘텐츠 게시글 일련번호
	private long bbsSeq;
	// 알림받기 동의 회원 전용 상품 여부
	private String storeKeepExclusiveProduct;
	// 네이버쇼핑 등록 여부
	private String naverShoppingRegistration;
	// 윈도 채널 상품 채널번호
	private String channelNo;
	// 베스트 여부(윈도 채널 전용)
	private String best;

	
	public String getChannelProductName() {
		return channelProductName;
	}

	public void setChannelProductName(String channelProductName) {
		this.channelProductName = channelProductName;
	}

	public long getBbsSeq() {
		return bbsSeq;
	}

	public void setBbsSeq(long bbsSeq) {
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

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getBest() {
		return best;
	}

	public void setBest(String best) {
		this.best = best;
	}

	@Override
	public String toString() {
		return "SmartstoreChannelProduct [channelProductName=" + channelProductName +"bbsSeq=" + bbsSeq + "storeKeepExclusiveProduct=" + storeKeepExclusiveProduct + "naverShoppingRegistration=" + naverShoppingRegistration
				+ "channelNo=" + channelNo + "best=" + best +"]";
	}

}
