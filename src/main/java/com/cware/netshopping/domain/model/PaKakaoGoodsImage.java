package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaKakaoGoodsImage extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 
	
	private String paGroupCode;
	private String GoodsCode;
	private String imageGb;
	private String imageUrl;
	private String stoaImage;
	private String kakaoImage;	
	private Timestamp uploadDate;	
	private String uploadStatus;
	private String paCode;
	private String remark;
	
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getImageGb() {
		return imageGb;
	}
	public void setImageGb(String imageGb) {
		this.imageGb = imageGb;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getStoaImage() {
		return stoaImage;
	}
	public void setStoaImage(String stoaImage) {
		this.stoaImage = stoaImage;
	}
	public String getKakaoImage() {
		return kakaoImage;
	}
	public void setKakaoImage(String kakaoImage) {
		this.kakaoImage = kakaoImage;
	}
	public Timestamp getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}