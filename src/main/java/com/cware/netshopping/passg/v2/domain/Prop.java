package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Prop {
	String itemAppePropClsId; // 상품 인증 ID
	String itemAppePropId; // 상품 인증 항목 ID
	String itemAppePropCntt; // 상품 인증 항목 값

	public String getItemAppePropClsId() {
		return itemAppePropClsId;
	}

	public void setItemAppePropClsId(String itemAppePropClsId) {
		this.itemAppePropClsId = itemAppePropClsId;
	}

	public String getItemAppePropId() {
		return itemAppePropId;
	}

	public void setItemAppePropId(String itemAppePropId) {
		this.itemAppePropId = itemAppePropId;
	}

	public String getItemAppePropCntt() {
		return itemAppePropCntt;
	}

	public void setItemAppePropCntt(String itemAppePropCntt) {
		this.itemAppePropCntt = itemAppePropCntt;
	}

	@Override
	public String toString() {
		return "Prop [itemAppePropClsId=" + itemAppePropClsId + ", itemAppePropId=" + itemAppePropId
				+ ", itemAppePropCntt=" + itemAppePropCntt + "]";
	}

}
