package com.cware.netshopping.pakakao.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductImage {

	// 기본 이미지 비율 코드
	// SQUARE 1:1 비율
	// VERTICAL_LONG_RECTANGLE 3:4 비율
	String imageRatio;

	// 대표 이미지 정보
	ImageInfo representImage;

	// 추가 이미지
	List<ImageInfo> optionalImages;

	public String getImageRatio() {
		return imageRatio;
	}

	public void setImageRatio(String imageRatio) {
		this.imageRatio = imageRatio;
	}

	public ImageInfo getRepresentImage() {
		return representImage;
	}

	public void setRepresentImage(ImageInfo representImage) {
		this.representImage = representImage;
	}

	public List<ImageInfo> getOptionalImages() {
		return optionalImages;
	}

	public void setOptionalImages(List<ImageInfo> optionalImages) {
		this.optionalImages = optionalImages;
	}

	@Override
	public String toString() {
		return "ProductImage [imageRatio=" + imageRatio + ", representImage=" + representImage + ", optionalImages="
				+ optionalImages + "]";
	}
}
