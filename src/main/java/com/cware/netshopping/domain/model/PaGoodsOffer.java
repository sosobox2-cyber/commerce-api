package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsOffer extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	private String paGroupCode;
	private String paOfferType;
	private String paOfferTypeName;
	private String paOfferCode;
	private String paOfferExt;
	private String paOfferCodeName;
	private String remark1;
	private String remark2;
	private String useYn;
	private String transTargetYn;
	private Timestamp lastSyncDate;
	private String iptMthdCd;
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaOfferType() {
		return paOfferType;
	}
	public void setPaOfferType(String paOfferType) {
		this.paOfferType = paOfferType;
	}
	public String getPaOfferCode() {
		return paOfferCode;
	}
	public void setPaOfferCode(String paOfferCode) {
		this.paOfferCode = paOfferCode;
	}
	public String getPaOfferExt() {
		return paOfferExt;
	}
	public void setPaOfferExt(String paOfferExt) {
		this.paOfferExt = paOfferExt;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	public String getIptMthdCd() {
		return iptMthdCd;
	}
	public void setIptMthdCd(String iptMthdCd) {
		this.iptMthdCd = iptMthdCd;
	}
	public String getPaOfferCodeName() {
		return paOfferCodeName;
	}
	public void setPaOfferCodeName(String paOfferCodeName) {
		this.paOfferCodeName = paOfferCodeName;
	}
	public String getPaOfferTypeName() {
		return paOfferTypeName;
	}
	public void setPaOfferTypeName(String paOfferTypeName) {
		this.paOfferTypeName = paOfferTypeName;
	}
}
