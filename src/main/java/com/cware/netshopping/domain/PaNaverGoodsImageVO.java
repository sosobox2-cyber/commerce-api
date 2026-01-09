package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaNaverGoodsImage;

public class PaNaverGoodsImageVO extends PaNaverGoodsImage {
	
	private static final long serialVersionUID = 1L; 

	private String naverImageYn;

	public String getNaverImageYn() {
		return naverImageYn;
	}
	public void setNaverImageYn(String naverImageYn) {
		this.naverImageYn = naverImageYn;
	}
}