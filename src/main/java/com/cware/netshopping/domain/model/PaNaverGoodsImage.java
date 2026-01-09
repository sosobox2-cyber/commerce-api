package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverGoodsImage extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String goodsCode;	
	private String imageNaverP;
	private String imageNaverAp;
	private String imageNaverBp;
	private String imageNaverCp;
	private String imageNaverDp;
	private Timestamp lastSyncDate;	
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getImageNaverP() {
		return imageNaverP;
	}
	public void setImageNaverP(String imageNaverP) {
		this.imageNaverP = imageNaverP;
	}
	public String getImageNaverAp() {
		return imageNaverAp;
	}
	public void setImageNaverAp(String imageNaverAp) {
		this.imageNaverAp = imageNaverAp;
	}
	public String getImageNaverBp() {
		return imageNaverBp;
	}
	public void setImageNaverBp(String imageNaverBp) {
		this.imageNaverBp = imageNaverBp;
	}
	public String getImageNaverCp() {
		return imageNaverCp;
	}
	public void setImageNaverCp(String imageNaverCp) {
		this.imageNaverCp = imageNaverCp;
	}
	public String getImageNaverDp() {
		return imageNaverDp;
	}
	public void setImageNaverDp(String imageNaverDp) {
		this.imageNaverDp = imageNaverDp;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}	
}