package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpResultVO implements Serializable {

	/** 결과코드 (ex] 000:성공, xxx:실패코드) */
	@XmlElement(name = "CODE")
	private String code;
	
	/** 결과메시지 */
	@XmlElement(name = "MESSAGE")
	private String message;
	
	/** 로그번호 - API 요청내역 로그의 시퀀스 */
	@XmlElement(name = "LOG_SEQ")
	private String logSeq;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLogSeq() {
		return logSeq;
	}

	public void setLogSeq(String logSeq) {
		this.logSeq = logSeq;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
