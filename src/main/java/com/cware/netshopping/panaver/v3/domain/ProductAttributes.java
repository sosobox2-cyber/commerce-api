package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductAttributes {
		
	// 속성 ID
	private long attributeSeq;
	// 속성값 ID
	private String attributeValueSeq;
	// 속성 실제 값
	private String attributeRealValue;
	// 속성 실제 값 단위 코드
	private String attributeRealValueUnitCode;
	
	
	public long getAttributeSeq() {
		return attributeSeq;
	}

	public void setAttributeSeq(long attributeSeq) {
		this.attributeSeq = attributeSeq;
	}

	public String getAttributeValueSeq() {
		return attributeValueSeq;
	}

	public void setAttributeValueSeq(String attributeValueSeq) {
		this.attributeValueSeq = attributeValueSeq;
	}

	public String getAttributeRealValue() {
		return attributeRealValue;
	}

	public void setAttributeRealValue(String attributeRealValue) {
		this.attributeRealValue = attributeRealValue;
	}

	public String getAttributeRealValueUnitCode() {
		return attributeRealValueUnitCode;
	}

	public void setAttributeRealValueUnitCode(String attributeRealValueUnitCode) {
		this.attributeRealValueUnitCode = attributeRealValueUnitCode;
	}

	@Override
	public String toString() {
		return "ProductAttributes [attributeSeq=" + attributeSeq + "attributeValueSeq="+ attributeValueSeq + "attributeRealValue="+ attributeRealValue + "attributeRealValueUnitCode="+ attributeRealValueUnitCode + "]";
	}
}
