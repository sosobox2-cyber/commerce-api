package com.cware.netshopping.palton.v2.domain;

public class ResultData {
	String epdNo; // 업체상품번호
	String spdNo; // 판매자상품번호
	String resultCode; // 처리결과
	String resultMessage; // 처리메세지

	public String getEpdNo() {
		return epdNo;
	}

	public void setEpdNo(String epdNo) {
		this.epdNo = epdNo;
	}

	public String getSpdNo() {
		return spdNo;
	}

	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@Override
	public String toString() {
		return "ResultData [epdNo=" + epdNo + ", spdNo=" + spdNo + ", resultCode=" + resultCode + ", resultMessage="
				+ resultMessage + "]";
	}

}
