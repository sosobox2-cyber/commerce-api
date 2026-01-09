package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaCopnOption extends AbstractModel{

	private static final long serialVersionUID = 1L;
	
	private String paLmsdKey; 
	private String attrSeq; 
	private String attrTypeNm;
	private String basicUnit;
	private String usableUnits;
	private String requiredYn;
	private String dataType;
	private String groupNum;
	private String exposedType;
	private String noticeCategoryNm;
	private String insertSeq;
	
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getAttrSeq() {
		return attrSeq;
	}
	public void setAttrSeq(String attrSeq) {
		this.attrSeq = attrSeq;
	}
	public String getAttrTypeNm() {
		return attrTypeNm;
	}
	public void setAttrTypeNm(String attrTypeNm) {
		this.attrTypeNm = attrTypeNm;
	}
	public String getBasicUnit() {
		return basicUnit;
	}
	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
	}
	public String getUsableUnits() {
		return usableUnits;
	}
	public void setUsableUnits(String usableUnits) {
		this.usableUnits = usableUnits;
	}
	public String getRequiredYn() {
		return requiredYn;
	}
	public void setRequiredYn(String requiredYn) {
		this.requiredYn = requiredYn;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}
	public String getExposedType() {
		return exposedType;
	}
	public void setExposedType(String exposedType) {
		this.exposedType = exposedType;
	}
	public String getNoticeCategoryNm() {
		return noticeCategoryNm;
	}
	public void setNoticeCategoryNm(String noticeCategoryNm) {
		this.noticeCategoryNm = noticeCategoryNm;
	}
	public String getInsertSeq() {
		return insertSeq;
	}
	public void setInsertSeq(String insertSeq) {
		this.insertSeq = insertSeq;
	}
	
	
}
