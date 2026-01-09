package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * 주문상품공통
 *
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpProductVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** id */
	@XmlAttribute(name = "ID")
	private long id;
	
	/** 주문순번 */
	@XmlElement(name = "ORD_SEQ")
	private String ordSeq;
	
	/**
	 * 옵션상품유형
	 * 01 일반단품상품
	 * 02 추가옵션이포함된부모상품
	 * 03 추가옵션상품
	 */
	@XmlElement(name = "OPT_PRD_TP")
	private String optPrdTp;
	
	/** 옵션부모상품순번 */
	@XmlElement(name = "OPT_PARENT_SEQ")
	private String optParentSeq;
	
	/** 인터파크 상품코드 */
	@XmlElement(name = "PRD_NO")
	private String prdNo;
	
	/** 인터파크 옵션상품코드 */
	@XmlElement(name = "OPT_PRD_NO")
	private String optPrdNo;
	
	/** 인터파크 업체번호 */
	@XmlElement(name = "SUPPLY_ENTR_NO")
	private String supplyEntrNo;
	
	/** 공급계약일련번호 */
	@XmlElement(name = "SUPPLY_CTRT_SEQ")
	private String supplyCtrtSeq;
	
	/** 제휴업체 상품 코드 */
	@XmlElement(name = "ENTR_PRD_NO")
	private String entrPrdNo;
	
	/** 제휴업체 옵션상품 코드 */
	@XmlElement(name = "OPT_NO")
	private String optNo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrdSeq() {
		return ordSeq;
	}

	public void setOrdSeq(String ordSeq) {
		this.ordSeq = ordSeq;
	}

	public String getOptPrdTp() {
		return optPrdTp;
	}

	public void setOptPrdTp(String optPrdTp) {
		this.optPrdTp = optPrdTp;
	}

	public String getOptParentSeq() {
		return optParentSeq;
	}

	public void setOptParentSeq(String optParentSeq) {
		this.optParentSeq = optParentSeq;
	}

	public String getPrdNo() {
		return prdNo;
	}

	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}

	public String getOptPrdNo() {
		return optPrdNo;
	}

	public void setOptPrdNo(String optPrdNo) {
		this.optPrdNo = optPrdNo;
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

	public String getEntrPrdNo() {
		return entrPrdNo;
	}

	public void setEntrPrdNo(String entrPrdNo) {
		this.entrPrdNo = entrPrdNo;
	}

	public String getOptNo() {
		return optNo;
	}

	public void setOptNo(String optNo) {
		this.optNo = optNo;
	}
	
}
