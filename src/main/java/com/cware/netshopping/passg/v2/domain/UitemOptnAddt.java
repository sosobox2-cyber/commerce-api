package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UitemOptnAddt {
	String uitemOptnNm; // 단품옵션명
	String uitemOptnNo; // 단품옵션번호(uitem_attr.uitemOptnChoiTypeCdX)

	// 단품옵션입력유형코드: I461
	// 10 : 색상칩 20 : 옵션이미지
	String uitemOptnIptTypeCd;
	String dataFileNm; // 자료파일명
	String useYn; // 사용여부

	public String getUitemOptnNm() {
		return uitemOptnNm;
	}

	public void setUitemOptnNm(String uitemOptnNm) {
		this.uitemOptnNm = uitemOptnNm;
	}

	public String getUitemOptnNo() {
		return uitemOptnNo;
	}

	public void setUitemOptnNo(String uitemOptnNo) {
		this.uitemOptnNo = uitemOptnNo;
	}

	public String getUitemOptnIptTypeCd() {
		return uitemOptnIptTypeCd;
	}

	public void setUitemOptnIptTypeCd(String uitemOptnIptTypeCd) {
		this.uitemOptnIptTypeCd = uitemOptnIptTypeCd;
	}

	public String getDataFileNm() {
		return dataFileNm;
	}

	public void setDataFileNm(String dataFileNm) {
		this.dataFileNm = dataFileNm;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "UitemOptnAddt [uitemOptnNm=" + uitemOptnNm + ", uitemOptnNo=" + uitemOptnNo + ", uitemOptnIptTypeCd="
				+ uitemOptnIptTypeCd + ", dataFileNm=" + dataFileNm + ", useYn=" + useYn + "]";
	}

}
