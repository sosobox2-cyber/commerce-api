package com.cware.netshopping.pa11st.v2.domain.ns2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cware.netshopping.pa11st.v2.domain.InOutAddress;

@XmlRootElement(name = "inOutAddresss", namespace = "http://skt.tmall.business.openapi.spring.service.client.domain/")
@XmlAccessorType(XmlAccessType.FIELD)
public class InOutAddresss {

	@XmlElement(name = "inOutAddress")
	private InOutAddress intOutAddress;

	// SUCCESS
	// result_message를 체크하여 주소 순번을 가져온다.
	private String result_message;

	public InOutAddress getIntOutAddress() {
		return intOutAddress;
	}

	public void setIntOutAddress(InOutAddress intOutAddress) {
		this.intOutAddress = intOutAddress;
	}

	public String getResult_message() {
		return result_message;
	}

	public void setResult_message(String result_message) {
		this.result_message = result_message;
	}

	@Override
	public String toString() {
		return "InOutAddressRes [intOutAddress=" + intOutAddress + ", result_message=" + result_message + "]";
	}

}
