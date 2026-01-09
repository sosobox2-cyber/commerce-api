package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Success {

	private String prdNo;
	private String prdNm;
	private String originPrdNo;

	public String getPrdNo() {
		return prdNo;
	}

	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}

	public String getPrdNm() {
		return prdNm;
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}

	public String getOriginPrdNo() {
		return originPrdNo;
	}

	public void setOriginPrdNo(String originPrdNo) {
		this.originPrdNo = originPrdNo;
	}

	@Override
	public String toString() {
		return "Success [prdNo=" + prdNo + ", prdNm=" + prdNm + ", originPrdNo=" + originPrdNo + "]";
	}

}
