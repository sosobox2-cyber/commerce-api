package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductImages {
	
	// 이미지 URL
	private RepresentativeImage representativeImage;	
	// 추가 이미지
	private List<OptionalImages> optionalImages;	
	
	
	public RepresentativeImage getRepresentativeImage() {
		return representativeImage;
	}

	public void setRepresentativeImage(RepresentativeImage representativeImage) {
		this.representativeImage = representativeImage;
	}	
	
	public List<OptionalImages> getOptionalImages() {
		return optionalImages;
	}

	public void setOptionalImages(List<OptionalImages> optionalImages) {
		this.optionalImages = optionalImages;
	}

	@Override
	public String toString() {
		return "ProductImages [RepresentativeImage=" + representativeImage +  "OptionalImages=" + optionalImages + "]";
	}

}
