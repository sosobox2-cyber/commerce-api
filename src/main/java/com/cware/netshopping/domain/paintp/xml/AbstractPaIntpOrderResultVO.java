package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * abstract 인터파크 주문 API 호출 결과 xml mapping VO
 * @author FIC05202
 *
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public abstract class AbstractPaIntpOrderResultVO implements Serializable {
	
	@XmlElement(name = "RESULT")
	private PaIntpResultVO result;

	public void setResult(PaIntpResultVO result) {
		this.result = result;
	}

	public PaIntpResultVO getResult() {
		return result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
