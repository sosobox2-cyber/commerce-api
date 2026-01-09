package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductListImageInfo {
	
	// 외부 이미지 여부(개별 적용)
	private boolean isExternal;
	// 이미지 url 타입 (IMAGE_URL: 이미지, VIDEO_URL: 비디오)
	private String imageUrlType;
	// 리스트 이미지 URL
	private String url;
	
	public boolean isExternal() {
		return isExternal;
	}
	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}
	public String getImageUrlType() {
		return imageUrlType;
	}
	public void setImageUrlType(String imageUrlType) {
		this.imageUrlType = imageUrlType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "ProductListImageInfo [isExternal=" + isExternal + ", imageUrlType=" + imageUrlType + ", url=" + url
				+ "]";
	}
	
}
