package com.cware.netshopping.domain.paintp.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 클레임주문상품공통
 *
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "PRD")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpClaimProductVO extends PaIntpProductVO {
		
	/** 클레임수량 */
	@XmlElement(name = "CLM_QTY")
	private Integer clmQty;
	
	/** 클레임상태 */
	@XmlElement(name = "CURRENT_CLMPRD_STATNM")
	private String currentClmPrdStatNm;
	
	/** 클레임상태코드 */
	@XmlElement(name = "CURRENT_CLMPRD_STAT")
	private String currentClmPrdStat;
	
	/** 클레임상태일자(yyyyMMddHHmiss) */
	@XmlElement(name = "CLM_DT")
	private String clmDt;

	/** 클레임사유코드 */
	@XmlElement(name = "CLM_RSN_TP")
	private String clmRsnTp;
	
	/** 클레임사유 */
	@XmlElement(name = "CLM_RSN_TPNM")
	private String clmRsnTpNm;
	
	/** 클레임상세사유 */
	@XmlElement(name = "CLM_RSN_DTL")
	private String clmRsnDtl;
	
	/** 비용책임구분코드 */
	@XmlElement(name = "COST_RESP_TP")
	private String costRespTp;
	
	/** 비용책임구분 */
	@XmlElement(name = "COST_RESP_TPNM")
	private String costRespTpNm;
	
	/** 고객비용결제방법코드 */
	@XmlElement(name = "COST_PAY_MTHD_TP")
	private String costPayMthdTp;
	
	/** 고객비용결제방법 */
	@XmlElement(name = "COST_PAY_MTHD_TPNM")
	private String costPayMthdTpNm;

	public Integer getClmQty() {
		return clmQty;
	}

	public void setClmQty(Integer clmQty) {
		this.clmQty = clmQty;
	}
	
	public String getCurrentClmPrdStatNm() {
		return currentClmPrdStatNm;
	}

	public void setCurrentClmPrdStatNm(String currentClmPrdStatNm) {
		this.currentClmPrdStatNm = currentClmPrdStatNm;
	}

	public String getCurrentClmPrdStat() {
		return currentClmPrdStat;
	}

	public void setCurrentClmPrdStat(String currentClmPrdStat) {
		this.currentClmPrdStatNm = currentClmPrdStat;
	}
	
	public String getClmDt() {
		return clmDt;
	}

	public void setClmDt(String clmDt) {
		this.currentClmPrdStatNm = clmDt;
	}
	
	public String getClmRsnTp() {
		return clmRsnTp;
	}

	public void setClmRsnTp(String clmRsnTp) {
		this.clmRsnTp = clmRsnTp;
	}
	
	public String getClmRsnTpNm() {
		return clmRsnTpNm;
	}

	public void setClmRsnTpNm(String clmRsnTpNm) {
		this.clmRsnTpNm = clmRsnTpNm;
	}

	public String getClmRsnDtl() {
		return clmRsnDtl;
	}

	public void setClmRsnDtl(String clmRsnDtl) {
		this.clmRsnDtl = clmRsnDtl;
	}

	public String getCostRespTp() {
		return costRespTp;
	}

	public void setCostRespTp(String costRespTp) {
		this.costRespTp = costRespTp;
	}

	public String getCostRespTpNm() {
		return costRespTpNm;
	}

	public void setCostRespTpNm(String costRespTpNm) {
		this.costRespTpNm = costRespTpNm;
	}

	public String getCostPayMthdTp() {
		return costPayMthdTp;
	}

	public void setCostPayMthdTp(String costPayMthdTp) {
		this.costPayMthdTp = costPayMthdTp;
	}

	public String getCostPayMthdTpNm() {
		return costPayMthdTpNm;
	}

	public void setCostPayMthdTpNm(String costPayMthdTpNm) {
		this.costPayMthdTpNm = costPayMthdTpNm;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
