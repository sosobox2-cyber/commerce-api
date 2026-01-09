package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaOfferCode extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String paGroupCode; 
	private String paOfferType; 
	private String paOfferCode;
	private String paOfferTypeName;
	private String paOfferCodeName;
	private String requiredYn;
	private String sortSeq;
	private String useYn;
	private String remark;
	private String unitRequiredYn; 
	private String iptMthdCd;
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
	public String getPaOfferTypeName() {
		return paOfferTypeName;
	}
	public void setPaOfferTypeName(String paOfferTypeName) {
		this.paOfferTypeName = paOfferTypeName;
	}
	public String getPaOfferCodeName() {
		return paOfferCodeName;
	}
	public void setPaOfferCodeName(String paOfferCodeName) {
		this.paOfferCodeName = paOfferCodeName;
	}
	public String getRequiredYn() {
		return requiredYn;
	}
	public void setRequiredYn(String requiredYn) {
		this.requiredYn = requiredYn;
	}
	public String getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(String sortSeq) {
		this.sortSeq = sortSeq;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnitRequiredYn() {
		return unitRequiredYn;
	}
	public void setUnitRequiredYn(String unitRequiredYn) {
		this.unitRequiredYn = unitRequiredYn;
	}
	public String getIptMthdCd() {
		return iptMthdCd;
	}
	public void setIptMthdCd(String iptMthdCd) {
		this.iptMthdCd = iptMthdCd;
	} 
	
	
}