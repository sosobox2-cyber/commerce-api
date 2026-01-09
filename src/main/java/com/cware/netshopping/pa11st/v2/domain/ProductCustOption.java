package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCustOption {

	// 구매자 작성형 옵션명 (필수)
	// 옵션명 최대 공백포함 한글10자/영문(숫자)20자)까지 입력가능하며 특수 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	private String colOptName;

	// 옵션 사용 여부 (필수)
	// Y : 사용함
	// N : 사용안함
	private String colOptUseYn;

	public String getColOptName() {
		return colOptName;
	}

	public void setColOptName(String colOptName) {
		this.colOptName = colOptName;
	}

	public String getColOptUseYn() {
		return colOptUseYn;
	}

	public void setColOptUseYn(String colOptUseYn) {
		this.colOptUseYn = colOptUseYn;
	}

	@Override
	public String toString() {
		return "ProductCustOption [colOptName=" + colOptName + ", colOptUseYn=" + colOptUseYn + "]";
	}

}
