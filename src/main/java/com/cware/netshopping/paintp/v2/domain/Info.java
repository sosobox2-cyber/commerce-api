package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 정보고시
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Info {

	// 항목코드 - 상품군+필수입력항목 코드값을 입력
	private String infoSubNo;

	// 입력타입 - 입력항목Type에 따른 필수 선택값을 입력
	private String infoCd;

	// 입력텍스트 - 직접입력이 필요한경우 직접입력값을 입력
	private String infoTx;

	public String getInfoSubNo() {
		return infoSubNo;
	}

	public void setInfoSubNo(String infoSubNo) {
		this.infoSubNo = infoSubNo;
	}

	public String getInfoCd() {
		return infoCd;
	}

	public void setInfoCd(String infoCd) {
		this.infoCd = infoCd;
	}

	public String getInfoTx() {
		return infoTx;
	}

	public void setInfoTx(String infoTx) {
		this.infoTx = infoTx;
	}

	@Override
	public String toString() {
		return "Info [infoSubNo=" + infoSubNo + ", infoCd=" + infoCd + ", infoTx=" + infoTx + "]";
	}

}
