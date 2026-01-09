package com.cware.netshopping.pakakao.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionImage {
	// 대표 이미지 정보
	ImageInfo representImage;

	// 추가 이미지
	List<ImageInfo> optionalImages;

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
		return "OptionImage [representImage=" + representImage + ", optionalImages=" + optionalImages + "]";
	}
}
