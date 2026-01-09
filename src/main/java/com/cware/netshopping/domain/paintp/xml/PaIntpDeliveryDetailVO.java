package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 주문내역 - 배송비상세목록 - 상품별배송비 정보
 *
 */
@XmlRootElement(name = "PRD_DELV")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpDeliveryDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "ID")
	private long id;
	
	@XmlElement(name = "DELVSETL_SEQ")
	/** 배송비 번호 */
	private String delvsetlSeq;
	
	@XmlElement(name = "DELV_AMT")
	/** 배송비 */
	private double delvAmt;
	
	@XmlElement(name = "ADD_DELV_AMT")
	/** 추가배송비 */
	private double addDelvAmt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDelvsetlSeq() {
		return delvsetlSeq;
	}

	public void setDelvsetlSeq(String delvsetlSeq) {
		this.delvsetlSeq = delvsetlSeq;
	}

	public double getDelvAmt() {
		return delvAmt;
	}

	public void setDelvAmt(double delvAmt) {
		this.delvAmt = delvAmt;
	}

	public double getAddDelvAmt() {
		return addDelvAmt;
	}

	public void setAddDelvAmt(double addDelvAmt) {
		this.addDelvAmt = addDelvAmt;
	}
	
}
