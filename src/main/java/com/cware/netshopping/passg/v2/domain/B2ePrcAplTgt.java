package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class B2ePrcAplTgt {
	Integer b2eSplprc; // B2E 공급가
	Integer b2eSellprc; // B2E 판매가
	Double b2eMrgrt; // B2E 마진율

	public Integer getB2eSplprc() {
		return b2eSplprc;
	}

	public void setB2eSplprc(Integer b2eSplprc) {
		this.b2eSplprc = b2eSplprc;
	}

	public Integer getB2eSellprc() {
		return b2eSellprc;
	}

	public void setB2eSellprc(Integer b2eSellprc) {
		this.b2eSellprc = b2eSellprc;
	}

	public Double getB2eMrgrt() {
		return b2eMrgrt;
	}

	public void setB2eMrgrt(Double b2eMrgrt) {
		this.b2eMrgrt = b2eMrgrt;
	}

	@Override
	public String toString() {
		return "B2ePrcAplTgt [b2eSplprc=" + b2eSplprc + ", b2eSellprc=" + b2eSellprc + ", b2eMrgrt=" + b2eMrgrt + "]";
	}

}
