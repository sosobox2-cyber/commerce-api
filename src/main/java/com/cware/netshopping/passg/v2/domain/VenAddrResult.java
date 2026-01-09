package com.cware.netshopping.passg.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class VenAddrResult {
	// 결과 코드
	// (00 정상, 나머지는 오류)
	String resultCode;
	String resultMessage; // 결과 값
	String resultDesc; // 오류 메시지

    @XmlElementWrapper
    @XmlElement(name="venAddrDelInfoDto")
	List<VenAddrDelInfo> venAddrDelInfo;

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

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public List<VenAddrDelInfo> getVenAddrDelInfo() {
		return venAddrDelInfo;
	}

	public void setVenAddrDelInfo(List<VenAddrDelInfo> venAddrDelInfo) {
		this.venAddrDelInfo = venAddrDelInfo;
	}

	@Override
	public String toString() {
		return "VenAddrResult [resultCode=" + resultCode + ", resultMessage=" + resultMessage + ", resultDesc="
				+ resultDesc + ", venAddrDelInfo=" + venAddrDelInfo + "]";
	}

}
