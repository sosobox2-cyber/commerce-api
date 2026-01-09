package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemMngAttr {
	String itemMngPropId; // 상품 관리 항목 ID
	String itemMngCntt; // 상품 관리 내용

	public String getItemMngPropId() {
		return itemMngPropId;
	}

	public void setItemMngPropId(String itemMngPropId) {
		this.itemMngPropId = itemMngPropId;
	}

	public String getItemMngCntt() {
		return itemMngCntt;
	}

	public void setItemMngCntt(String itemMngCntt) {
		this.itemMngCntt = itemMngCntt;
	}

	@Override
	public String toString() {
		return "ItemMngAttr [itemMngPropId=" + itemMngPropId + ", itemMngCntt=" + itemMngCntt + "]";
	}

}
