package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * abstract 주문취소요청(ORDER) xml mapping VO
 * @author FIC05202
 *
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class AbstractPaIntpCancelReqVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** id */
	@XmlAttribute(name = "ID")
	private Long id;

	/** 인터파크 주문번호 */
	@XmlElement(name = "ORD_NO")
	private String ordNo;
	
	/** 클레임요청순번 */
	@XmlElement(name = "CLMREQ_SEQ")
	private String clmreqSeq;
	
	/** 클레임요청일자(yyyyMMddHHmmss) */
	@XmlElement(name = "CLMREQ_DTS")
	private String clmreqDts;
	
	/** 클레임요청일자(yyyyMMdd) */
	@XmlElement(name = "CLMREQ_DT")
	private String clmreqDt;
	
	/** 클레임요청구분 */
	@XmlElement(name = "CLMREQ_TPNM")
	private String clmreqTpnm;
	
	/** 클레임요청구분코드(1: 출고전주문취소, 2:반품, 3:교환) - 2, 3 사용안함 */
	@XmlElement(name = "CLMREQ_TP")
	private String clmreqTp;
	
	/** 수거방법 */
	@XmlElement(name = "RETURN_MTHD_TPNM")
	private String returnMthdTpnm;
	
	/** 수거방법코드(1: 반품전담택배_인터파크, 2:공급업체, 3:고객) - 1 사용안함 */
	@XmlElement(name = "RETURN_MTHD_TP")
	private String returnMthdTp;
	
	/** 무료반품쿠폰사용여부 (Y: 무료반품쿠폰사용, N: 미사용) */
	@XmlElement(name = "FRTN_CP_TP")
	private String frtnCpTp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public String getClmreqSeq() {
		return clmreqSeq;
	}

	public void setClmreqSeq(String clmreqSeq) {
		this.clmreqSeq = clmreqSeq;
	}

	public String getClmreqDts() {
		return clmreqDts;
	}

	public void setClmreqDts(String clmreqDts) {
		this.clmreqDts = clmreqDts;
	}

	public String getClmreqTpnm() {
		return clmreqTpnm;
	}

	public void setClmreqTpnm(String clmreqTpnm) {
		this.clmreqTpnm = clmreqTpnm;
	}

	public String getClmreqTp() {
		return clmreqTp;
	}

	public void setClmreqTp(String clmreqTp) {
		this.clmreqTp = clmreqTp;
	}

	public String getReturnMthdTpnm() {
		return returnMthdTpnm;
	}

	public void setReturnMthdTpnm(String returnMthdTpnm) {
		this.returnMthdTpnm = returnMthdTpnm;
	}

	public String getReturnMthdTp() {
		return returnMthdTp;
	}

	public void setReturnMthdTp(String returnMthdTp) {
		this.returnMthdTp = returnMthdTp;
	}

	public String getFrtnCpTp() {
		return frtnCpTp;
	}

	public void setFrtnCpTp(String frtnCpTp) {
		this.frtnCpTp = frtnCpTp;
	}
	
	public String getClmreqDt() {
		return clmreqDt;
	}

	public void setClmreqDt(String clmreqDt) {
		this.clmreqDt = clmreqDt;
	}

}
