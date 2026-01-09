package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Contents {

	// 원상품번호
	private long originProductNo;
	// 채널 상품 목록
	private List<ChannelProducts> channelProducts;
	
	
	public long getOriginProductNo() {
		return originProductNo;
	}

	public void setOriginProductNo(long originProductNo) {
		this.originProductNo = originProductNo;
	}

	public List<ChannelProducts> getChannelProducts() {
		return channelProducts;
	}

	public void setChannelProducts(List<ChannelProducts> channelProducts) {
		this.channelProducts = channelProducts;
	}

	@Override
	public String toString() {
		return "Contents [originProductNo=" + originProductNo + ", channelProducts=" + channelProducts + "]";
	}

}
