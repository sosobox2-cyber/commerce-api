package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductNotification {

	// 유형코드 (필수)
	private String type;

	// 항목정보 (필수)
	// 유형에 해당하는 항목정보
	private List<Item> item;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "ProductNotification [type=" + type + ", item=" + item + "]";
	}

}
