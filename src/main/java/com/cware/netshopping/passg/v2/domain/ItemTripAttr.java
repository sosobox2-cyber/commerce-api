package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemTripAttr {
	// 여행속성ID
	// 1000000000 : 대륙
	// 1000000001 : 국가
	// 1000000002 : 도시
	String itemTripPropId;
	String itemTripPropCntt; // 여행속성내용(여행속성 공통코드 조회API에서 확인가능)

	public String getItemTripPropId() {
		return itemTripPropId;
	}

	public void setItemTripPropId(String itemTripPropId) {
		this.itemTripPropId = itemTripPropId;
	}

	public String getItemTripPropCntt() {
		return itemTripPropCntt;
	}

	public void setItemTripPropCntt(String itemTripPropCntt) {
		this.itemTripPropCntt = itemTripPropCntt;
	}

	@Override
	public String toString() {
		return "ItemTripAttr [itemTripPropId=" + itemTripPropId + ", itemTripPropCntt=" + itemTripPropCntt + "]";
	}

}
