package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductImages {
	
	// 외부 이미지 사용 여부 (default: N)
	private boolean isExternal;
	// 대표이미지 여부(상품 이미지중 한개만 ‘Y’) 
	private String mainYn;
	// 이미지 URL
	private String imageUrl;
	// 이미지 url 타입 (IMAGE_URL: 이미지, VIDEO_URL: 비디오)
	private String imageUrlType;
	// 전시순서
	private int order;
	
	public boolean isExternal() {
		return isExternal;
	}
	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}
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
	public String getImageUrlType() {
		return imageUrlType;
	}
	public void setImageUrlType(String imageUrlType) {
		this.imageUrlType = imageUrlType;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "ProductImages [isExternal=" + isExternal + ", mainYn=" + mainYn + ", imageUrl=" + imageUrl
				+ ", imageUrlType=" + imageUrlType + ", order=" + order + "]";
	}
	
}
