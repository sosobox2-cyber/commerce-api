package com.cware.netshopping.pawemp.goods.model;

/**
 * 위메프 제조사 조회 결과
 */
public class GetMakerResponse {
	
	private String makerNo;
	private String makerName;
	private String makerNameEnglish;
	
	public String getMakerNo() {
		return makerNo;
	}
	public void setMakerNo(String makerNo) {
		this.makerNo = makerNo;
	}
	public String getMakerName() {
		return makerName;
	}
	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}
	public String getMakerNameEnglish() {
		return makerNameEnglish;
	}
	public void setMakerNameEnglish(String makerNameEnglish) {
		this.makerNameEnglish = makerNameEnglish;
	}
	@Override
	public String toString() {
		return "GetMakerResponse [makerNo=" + makerNo + ", makerName="
				+ makerName + ", makerNameEnglish=" + makerNameEnglish + "]";
	}
}

