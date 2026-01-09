package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsImage extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 
	
	private String goodsCode;
	private String imageUrl1;
	private String imageUrl2;
	private String imageP;
	private String imageAp;
	private String imageBp;
	private String imageCp;
	private String imageDp;
	private Timestamp lastSyncDate;
	private String naverImageYn;
	private String paGroupCode;
	private String imageUrl;
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getImageUrl1() {
		return imageUrl1;
	}
	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}
	public String getImageUrl2() {
		return imageUrl2;
	}
	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}
	public String getImageP() {
		return imageP;
	}
	public void setImageP(String imageP) {
		this.imageP = imageP;
	}
	public String getImageAp() {
		return imageAp;
	}
	public void setImageAp(String imageAp) {
		this.imageAp = imageAp;
	}
	public String getImageBp() {
		return imageBp;
	}
	public void setImageBp(String imageBp) {
		this.imageBp = imageBp;
	}
	public String getImageCp() {
		return imageCp;
	}
	public void setImageCp(String imageCp) {
		this.imageCp = imageCp;
	}
	public String getImageDp() {
		return imageDp;
	}
	public void setImageDp(String imageDp) {
		this.imageDp = imageDp;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getNaverImageYn() {
		return naverImageYn;
	}
	public void setNaverImageYn(String naverImageYn) {
		this.naverImageYn = naverImageYn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}