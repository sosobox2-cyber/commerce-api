package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 주문내역 - 배송비 정보 
 */
@XmlRootElement(name = "DELIV")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpDeliveryVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name = "ID")
	private long id;
	
	/** 인터파크 업체번호 */
	@XmlElement(name = "SUPPLY_ENTR_NO")
	private String supplyEntrNo;
	
	/** 곻급계약 일련번호 */
	@XmlElement(name = "SUPPLY_CTRT_SEQ")
	private String supplyCtrtSeq;
	
	/** 배송비 */
	@XmlElement(name = "DEL_AMT")
	private double delAmt;
	
	/** 추가 배송비 */
	@XmlElement(name = "ADD_DEL_AMT")
	private double addDelAmt;
	
	/** 초기 배송비 */
	@XmlElement(name = "INITIAL_DELV_AMT")
	private double initialDelvAmt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSupplyEntrNo() {
		return supplyEntrNo;
	}

	public void setSupplyEntrNo(String supplyEntrNo) {
		this.supplyEntrNo = supplyEntrNo;
	}

	public String getSupplyCtrtSeq() {
		return supplyCtrtSeq;
	}

	public void setSupplyCtrtSeq(String supplyCtrtSeq) {
		this.supplyCtrtSeq = supplyCtrtSeq;
	}

	public double getDelAmt() {
		return delAmt;
	}

	public void setDelAmt(double delAmt) {
		this.delAmt = delAmt;
	}

	public double getAddDelAmt() {
		return addDelAmt;
	}

	public void setAddDelAmt(double addDelAmt) {
		this.addDelAmt = addDelAmt;
	}

	public double getInitialDelvAmt() {
		return initialDelvAmt;
	}

	public void setInitialDelvAmt(double initialDelvAmt) {
		this.initialDelvAmt = initialDelvAmt;
	}
	
}
