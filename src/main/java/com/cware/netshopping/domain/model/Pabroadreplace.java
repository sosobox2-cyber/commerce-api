package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractVO;

public class Pabroadreplace extends AbstractVO{

	private static final long serialVersionUID = 1L;
	
	private String paGroupCode;
	private String goodsCode;
	private int sumQty;
	private int sumQtyRank;
	private int cycleSeq;
	private String goodsName;
	private String paGoodsCode;
	private String vodUrl;
	private String vodImage;
	private String shManName;
	private String targetYn;

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
	public int getSumQty() {
		return sumQty;
	}
	public void setSumQty(int sumQty) {
		this.sumQty = sumQty;
	}
	public int getSumQtyRank() {
		return sumQtyRank;
	}
	public void setSumQtyRank(int sumQtyRank) {
		this.sumQtyRank = sumQtyRank;
	}
	public int getCycleSeq() {
		return cycleSeq;
	}
	public void setCycleSeq(int cycleSeq) {
		this.cycleSeq = cycleSeq;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getVodUrl() {
		return vodUrl;
	}
	public void setVodUrl(String vodUrl) {
		this.vodUrl = vodUrl;
	}
	public String getVodImage() {
		return vodImage;
	}
	public void setVodImage(String vodImage) {
		this.vodImage = vodImage;
	}
	public String getShManName() {
		return shManName;
	}
	public void setShManName(String shManName) {
		this.shManName = shManName;
	}
	public String getTargetYn() {
		return targetYn;
	}
	public void setTargetYn(String targetYn) {
		this.targetYn = targetYn;
	}
}