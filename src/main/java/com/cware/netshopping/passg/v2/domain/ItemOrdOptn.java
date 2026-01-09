package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemOrdOptn {
	String addOrdOptnSeq; // 추가 주문 옵션 순번
	String addOrdOptnNm; // 추가 주문 옵션명
	String addOrdOptnMndtyYn; // 추가 주문 옵션 필수 여부

	public String getAddOrdOptnSeq() {
		return addOrdOptnSeq;
	}

	public void setAddOrdOptnSeq(String addOrdOptnSeq) {
		this.addOrdOptnSeq = addOrdOptnSeq;
	}

	public String getAddOrdOptnNm() {
		return addOrdOptnNm;
	}

	public void setAddOrdOptnNm(String addOrdOptnNm) {
		this.addOrdOptnNm = addOrdOptnNm;
	}

	public String getAddOrdOptnMndtyYn() {
		return addOrdOptnMndtyYn;
	}

	public void setAddOrdOptnMndtyYn(String addOrdOptnMndtyYn) {
		this.addOrdOptnMndtyYn = addOrdOptnMndtyYn;
	}

	@Override
	public String toString() {
		return "ItemOrdOptn [addOrdOptnSeq=" + addOrdOptnSeq + ", addOrdOptnNm=" + addOrdOptnNm + ", addOrdOptnMndtyYn="
				+ addOrdOptnMndtyYn + "]";
	}

}
