package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddrBasiDlvCst {

	// 배송비 (필수)
	private String dlvCst;

	// 주문시작금액 (필수)
	// 이상금액
	private String ordBgnAmt;

	// 주문종료금액 (필수)
	// 미만금액
	private String ordEndAmt;

	// 국내외 구분 코드 (필수)
	// 01 : 국내
	// 02 : 해외
	private String mbAddrLocation;

	public String getDlvCst() {
		return dlvCst;
	}

	public void setDlvCst(String dlvCst) {
		this.dlvCst = dlvCst;
	}

	public String getOrdBgnAmt() {
		return ordBgnAmt;
	}

	public void setOrdBgnAmt(String ordBgnAmt) {
		this.ordBgnAmt = ordBgnAmt;
	}

	public String getOrdEndAmt() {
		return ordEndAmt;
	}

	public void setOrdEndAmt(String ordEndAmt) {
		this.ordEndAmt = ordEndAmt;
	}

	public String getMbAddrLocation() {
		return mbAddrLocation;
	}

	public void setMbAddrLocation(String mbAddrLocation) {
		this.mbAddrLocation = mbAddrLocation;
	}

	@Override
	public String toString() {
		return "AddrBasiDlvCst [dlvCst=" + dlvCst + ", ordBgnAmt=" + ordBgnAmt + ", ordEndAmt=" + ordEndAmt
				+ ", mbAddrLocation=" + mbAddrLocation + "]";
	}

}
