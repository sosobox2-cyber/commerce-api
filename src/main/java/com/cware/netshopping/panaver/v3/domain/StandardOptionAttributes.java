package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StandardOptionAttributes {

	// 속성 ID
	private long attributeId;
	// 속성값 ID
	private long attributeValueId;
	// 속성값 이름
	private String attributeValueName;
	// 표준형 옵션에서 사용할 이미지 URL 목록
	private String imageUrls;

	
	public long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public long getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(long attributeValueId) {
		this.attributeValueId = attributeValueId;
	}

	public String getAttributeValueName() {
		return attributeValueName;
	}

	public void setAttributeValueName(String attributeValueName) {
		this.attributeValueName = attributeValueName;
	}

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	@Override
	public String toString() {
		return "StandardOptionAttributes [attributeId=" + attributeId + ", attributeValueId=" + attributeValueId + ", attributeValueName=" + attributeValueName + ", imageUrls=" + imageUrls	+ "]";
	}

}
