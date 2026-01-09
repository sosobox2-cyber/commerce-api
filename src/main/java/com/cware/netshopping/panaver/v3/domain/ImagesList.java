package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ImagesList {
	
	// 이미지 url 목록
	private List<Images> images;	
		
	public List<Images> getImages() {
		return images;
	}
	
	public void setImages(List<Images> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "ImagesList [images=" + images + "]";
	}

}
