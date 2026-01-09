package com.cware.netshopping.domain.model;
import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsdt extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtInfo;
	private String goodsdtInfoKind;
	private String sortType;
	private String transTargetYn;
	private Timestamp lastSyncDate;
	private String saleGb;
	
	private String colorName;
	private String sizeName;
	private String patternName;
	private String formName;
	private String otherText;	
	
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getPatternName() {
		return patternName;
	}
	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getOtherText() {
		return otherText;
	}
	public void setOtherText(String otherText) {
		this.otherText = otherText;
	}
	public String getSaleGb() {
		return saleGb;
	}
	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getGoodsdtInfoKind() {
		return goodsdtInfoKind;
	}
	public void setGoodsdtInfoKind(String goodsdtInfoKind) {
		this.goodsdtInfoKind = goodsdtInfoKind;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	
}