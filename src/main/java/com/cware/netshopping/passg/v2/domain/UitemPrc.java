package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UitemPrc {
	String tempUitemId; // 단품 ID (임시 번호)
	String uitemId; // 단품 ID
	Integer splprc; // 공급가
	Integer sellprc; // 판매가
	Double mrgrt; // 마진율
	String aplStrtDts; // 적용 시작 일시(YYYYMMDDHH24MISS)

	public String getUitemId() {
		return uitemId;
	}

	public void setUitemId(String uitemId) {
		this.uitemId = uitemId;
	}

	public Integer getSplprc() {
		return splprc;
	}

	public void setSplprc(Integer splprc) {
		this.splprc = splprc;
	}

	public Integer getSellprc() {
		return sellprc;
	}

	public void setSellprc(Integer sellprc) {
		this.sellprc = sellprc;
	}

	public Double getMrgrt() {
		return mrgrt;
	}

	public void setMrgrt(Double mrgrt) {
		this.mrgrt = mrgrt;
	}

	public String getAplStrtDts() {
		return aplStrtDts;
	}

	public void setAplStrtDts(String aplStrtDts) {
		this.aplStrtDts = aplStrtDts;
	}

	public String getTempUitemId() {
		return tempUitemId;
	}

	public void setTempUitemId(String tempUitemId) {
		this.tempUitemId = tempUitemId;
	}

	@Override
	public String toString() {
		return "UitemPrc [uitemId=" + uitemId + ", splprc=" + splprc + ", sellprc=" + sellprc + ", mrgrt=" + mrgrt
				+ ", aplStrtDts=" + aplStrtDts + "]";
	}

}
