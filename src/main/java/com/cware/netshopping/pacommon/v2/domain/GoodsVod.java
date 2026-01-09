package com.cware.netshopping.pacommon.v2.domain;

import java.sql.Timestamp;

public class GoodsVod  {

	private String goodsCode;
	private String goodsVodSeq;
	private String displayMedia;
	private String displayChannel;
	private String GoodsVodUrl;
	private String displayPriority;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	private String delyYn;
	private String delyTxt;
	private Timestamp delyDate;
	private String voiceTxtSmr;

	
	public String getGoodsCode() {
		return goodsCode;
	}


	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}


	public String getGoodsVodSeq() {
		return goodsVodSeq;
	}


	public void setGoodsVodSeq(String goodsVodSeq) {
		this.goodsVodSeq = goodsVodSeq;
	}


	public String getDisplayMedia() {
		return displayMedia;
	}


	public void setDisplayMedia(String displayMedia) {
		this.displayMedia = displayMedia;
	}


	public String getDisplayChannel() {
		return displayChannel;
	}


	public void setDisplayChannel(String displayChannel) {
		this.displayChannel = displayChannel;
	}


	public String getGoodsVodUrl() {
		return GoodsVodUrl;
	}


	public void setGoodsVodUrl(String goodsVodUrl) {
		GoodsVodUrl = goodsVodUrl;
	}


	public String getDisplayPriority() {
		return displayPriority;
	}


	public void setDisplayPriority(String displayPriority) {
		this.displayPriority = displayPriority;
	}


	public String getInsertId() {
		return insertId;
	}


	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}


	public Timestamp getInsertDate() {
		return insertDate;
	}


	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}


	public String getModifyId() {
		return modifyId;
	}


	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}


	public Timestamp getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}


	public String getDelyYn() {
		return delyYn;
	}


	public void setDelyYn(String delyYn) {
		this.delyYn = delyYn;
	}


	public String getDelyTxt() {
		return delyTxt;
	}


	public void setDelyTxt(String delyTxt) {
		this.delyTxt = delyTxt;
	}


	public Timestamp getDelyDate() {
		return delyDate;
	}


	public void setDelyDate(Timestamp delyDate) {
		this.delyDate = delyDate;
	}


	public String getVoiceTxtSmr() {
		return voiceTxtSmr;
	}


	public void setVoiceTxtSmr(String voiceTxtSmr) {
		this.voiceTxtSmr = voiceTxtSmr;
	}

	
	@Override
	public String toString() {
		return "PaGoodsPriceApply [goodsCode=" + goodsCode + ", goodsVodSeq=" + goodsVodSeq + ", displayMedia=" + displayMedia
				+ ", displayChannel=" + displayChannel + ", GoodsVodUrl=" + GoodsVodUrl + ", displayPriority=" + displayPriority
				+ ", insertId=" + insertId + ", insertDate=" + insertDate + ", modifyId=" + modifyId
				+ ", modifyDate=" + modifyDate + ", delyYn=" + delyYn + ", delyTxt="
				+ delyTxt + ", delyDate=" + delyDate + ", voiceTxtSmr=" + voiceTxtSmr + "]";
	}

}