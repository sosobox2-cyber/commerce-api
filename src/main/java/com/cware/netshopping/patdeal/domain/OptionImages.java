package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionImages {
	
	// 대표이미지 여부(옵션 이미지중 한개만 "Y")
	private String mainYn;
	// 이미지 주소
	private String imageUrl;
	// 전시순서
	private int order;
	
	public String getMainYn() {
		return mainYn;
	}
	public void setMainYn(String mainYn) {
		this.mainYn = mainYn;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "OptionImages [mainYn=" + mainYn + ", imageUrl=" + imageUrl + ", order=" + order + "]";
	}
	
}
