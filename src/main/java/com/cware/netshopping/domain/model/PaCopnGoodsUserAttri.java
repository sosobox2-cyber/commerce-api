package com.cware.netshopping.domain.model;

public class PaCopnGoodsUserAttri extends PaGoods {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String goodsdtCode;
	private String attributeSeq;
	private String attributeTypeName;
	private String attributeValueName;
	private String usableUnit;
	private String exposedType;

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
	public String getAttributeSeq() {
		return attributeSeq;
	}
	public void setAttributeSeq(String attributeSeq) {
		this.attributeSeq = attributeSeq;
	}
	public String getAttributeTypeName() {
		return attributeTypeName;
	}
	public void setAttributeTypeName(String attributeTypeName) {
		this.attributeTypeName = attributeTypeName;
	}
	public String getAttributeValueName() {
		return attributeValueName;
	}
	public void setAttributeValueName(String attributeValueName) {
		this.attributeValueName = attributeValueName;
	}
	public String getUsableUnit() {
		return usableUnit;
	}
	public void setUsableUnit(String usableUnit) {
		this.usableUnit = usableUnit;
	}
	public String getExposedType() {
		return exposedType;
	}
	public void setExposedType(String exposedType) {
		this.exposedType = exposedType;
	}
	
}
