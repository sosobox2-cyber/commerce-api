package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Images {
	// 상품 기본 이미지
	// 최소 600x600 / 권장 1000x1000
	private String basicImgURL;

	// 상품 추가 이미지 1
	private String addtionalImg1URL;

	// 상품 추가 이미지 2
	private String addtionalImg2URL;

	// 상품 추가 이미지 3
	private String addtionalImg3URL;

	public String getBasicImgURL() {
		return basicImgURL;
	}

	public void setBasicImgURL(String basicImgURL) {
		this.basicImgURL = basicImgURL;
	}

	public String getAddtionalImg1URL() {
		return addtionalImg1URL;
	}

	public void setAddtionalImg1URL(String addtionalImg1URL) {
		this.addtionalImg1URL = addtionalImg1URL;
	}

	public String getAddtionalImg2URL() {
		return addtionalImg2URL;
	}

	public void setAddtionalImg2URL(String addtionalImg2URL) {
		this.addtionalImg2URL = addtionalImg2URL;
	}

	public String getAddtionalImg3URL() {
		return addtionalImg3URL;
	}

	public void setAddtionalImg3URL(String addtionalImg3URL) {
		this.addtionalImg3URL = addtionalImg3URL;
	}

	@Override
	public String toString() {
		return "Images [basicImgURL=" + basicImgURL + ", addtionalImg1URL=" + addtionalImg1URL + ", addtionalImg2URL="
				+ addtionalImg2URL + ", addtionalImg3URL=" + addtionalImg3URL + "]";
	}

}
