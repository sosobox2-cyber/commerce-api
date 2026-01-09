package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImgInfo {
	Integer dataSeq; // 자료순번
	String dataFileNm; // 자료파일명
	String rplcTextNm; // 대체 텍스트 명

	public Integer getDataSeq() {
		return dataSeq;
	}

	public void setDataSeq(Integer dataSeq) {
		this.dataSeq = dataSeq;
	}

	public String getDataFileNm() {
		return dataFileNm;
	}

	public void setDataFileNm(String dataFileNm) {
		this.dataFileNm = dataFileNm;
	}

	public String getRplcTextNm() {
		return rplcTextNm;
	}

	public void setRplcTextNm(String rplcTextNm) {
		this.rplcTextNm = rplcTextNm;
	}

	@Override
	public String toString() {
		return "ImgInfo [dataSeq=" + dataSeq + ", dataFileNm=" + dataFileNm + ", rplcTextNm=" + rplcTextNm + "]";
	}

}
