package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FstPriceInfo {
	Integer fstSellprc; // 최초출시가
	String dataFileNm; // 자료파일명

	public Integer getFstSellprc() {
		return fstSellprc;
	}

	public void setFstSellprc(Integer fstSellprc) {
		this.fstSellprc = fstSellprc;
	}

	public String getDataFileNm() {
		return dataFileNm;
	}

	public void setDataFileNm(String dataFileNm) {
		this.dataFileNm = dataFileNm;
	}

	@Override
	public String toString() {
		return "FstPriceInfo [fstSellprc=" + fstSellprc + ", dataFileNm=" + dataFileNm + "]";
	}

}
