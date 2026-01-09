package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOptionExt {

	// ProductOption
    @XmlElement(name = "ProductOption")
	private List<ProductOption> productOption;

	public List<ProductOption> getProductOption() {
		return productOption;
	}

	public void setProductOption(List<ProductOption> productOption) {
		this.productOption = productOption;
	}

	@Override
	public String toString() {
		return "ProductOptionExt [ProductOption=" + productOption + "]";
	}

}
