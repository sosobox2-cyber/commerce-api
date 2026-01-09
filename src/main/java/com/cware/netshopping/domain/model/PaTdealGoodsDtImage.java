package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealGoodsDtImage extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String GoodsCode;
	private String GoodsDtCode;
	private String imagePath;
	private String imageFile;
	
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getGoodsDtCode() {
		return GoodsDtCode;
	}
	public void setGoodsDtCode(String goodsDtCode) {
		GoodsDtCode = goodsDtCode;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
		
}