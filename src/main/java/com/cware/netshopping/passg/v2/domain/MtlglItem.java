package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MtlglItem {
	// 언어 구분코드
	// 20 : 영어
	// 30 : 중국어
	String langDivCd;
	String mtlglItemNm; // 다국어 상품명

	public String getLangDivCd() {
		return langDivCd;
	}

	public void setLangDivCd(String langDivCd) {
		this.langDivCd = langDivCd;
	}

	public String getMtlglItemNm() {
		return mtlglItemNm;
	}

	public void setMtlglItemNm(String mtlglItemNm) {
		this.mtlglItemNm = mtlglItemNm;
	}

	@Override
	public String toString() {
		return "MtlglItem [langDivCd=" + langDivCd + ", mtlglItemNm=" + mtlglItemNm + "]";
	}

}
