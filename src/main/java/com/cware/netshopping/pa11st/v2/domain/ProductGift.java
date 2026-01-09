package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductGift {

	// 사은품 정보 (필수)
	// 특수문자 등이 포함되어 있을 경우, <![CDATA[]]>로 묶어주세요.
	// 글자수는 200byte로 제한됩니다.
	// 한글100자, 영문/숫자 200자 이내로 입력해 주십시오.
	private String giftInfo;

	// 사은품명 입력 (필수)
	// 특수문자 등이 포함되어 있을 경우, <![CDATA[]]>로 묶어주세요.
	// 글자수는 30byte로 제한됩니다.
	// 한글15자, 영문/숫자 30자 이내로 입력해 주십시오.
	private String giftNm;

	// 사은품 지급 시작일 (필수)
	private String aplBgnDt;

	// 사은품 지급 종료일 (필수)
	private String aplEndDt;

	public String getGiftInfo() {
		return giftInfo;
	}

	public void setGiftInfo(String giftInfo) {
		this.giftInfo = giftInfo;
	}

	public String getGiftNm() {
		return giftNm;
	}

	public void setGiftNm(String giftNm) {
		this.giftNm = giftNm;
	}

	public String getAplBgnDt() {
		return aplBgnDt;
	}

	public void setAplBgnDt(String aplBgnDt) {
		this.aplBgnDt = aplBgnDt;
	}

	public String getAplEndDt() {
		return aplEndDt;
	}

	public void setAplEndDt(String aplEndDt) {
		this.aplEndDt = aplEndDt;
	}

	@Override
	public String toString() {
		return "ProductGift [giftInfo=" + giftInfo + ", giftNm=" + giftNm + ", aplBgnDt=" + aplBgnDt + ", aplEndDt="
				+ aplEndDt + "]";
	}

}
