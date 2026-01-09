package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRootOption {

	// 옵션명 (필수)
	// 20Byte 까지만 입력가능하며 특수 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	// * 11번가 단일상품의 경우 표준옵션만 허용 가능합니다.
	// 허용 카테고리/옵션 참조 : http://image.11st.co.kr/product/sell/optionInfoByCategory.xls
	private String colTitle;

	// ProductOption (필수)
    @XmlElement(name = "ProductOption")
	private List<ProductOption> productOption;

	public String getColTitle() {
		return colTitle;
	}

	public void setColTitle(String colTitle) {
		this.colTitle = colTitle;
	}

	public List<ProductOption> getProductOption() {
		return productOption;
	}

	public void setProductOption(List<ProductOption> productOption) {
		this.productOption = productOption;
	}

	@Override
	public String toString() {
		return "ProductRootOption [colTitle=" + colTitle + ", ProductOption=" + productOption + "]";
	}

}
