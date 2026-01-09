package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

public class PaCopnGoodsAttri extends PaGoods {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String attributeSeq;
	private String attributeTypeName;
	private String attributeValueName;
	private String attributeTypeMapping;
	private String dataType;
	private String basicUnit;
	private String usableUnits;
	private String GroupNumber;
	private String exposed;   
	private String requiredYn;
	private Timestamp lastSyncDate;
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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
	public String getAttributeTypeMapping() {
		return attributeTypeMapping;
	}
	public void setAttributeTypeMapping(String attributeTypeMapping) {
		this.attributeTypeMapping = attributeTypeMapping;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	public String getGroupNumber() {
		return GroupNumber;
	}
	public void setGroupNumber(String groupNumber) {
		GroupNumber = groupNumber;
	}
	public String getExposed() {
		return exposed;
	}
	public void setExposed(String exposed) {
		this.exposed = exposed;
	}
	public String getRequiredYn() {
		return requiredYn;
	}
	public void setRequiredYn(String requiredYn) {
		this.requiredYn = requiredYn;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}

}
