package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaSlipInfo extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String paGroupCode; 
	private String mappingSeq;
	private String seq;
	private String transPaDelyGb;
	private String transSlipNo;
	private String paDelyGb;
	private String slipNo;
	private String lastYn;
	private String transYn;
	private String remark1V;
		
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}	
	public String getTransPaDelyGb() {
		return transPaDelyGb;
	}
	public void setTransPaDelyGb(String transPaDelyGb) {
		this.transPaDelyGb = transPaDelyGb;
	}
	public String getTransSlipNo() {
		return transSlipNo;
	}
	public void setTransSlipNo(String transSlipNo) {
		this.transSlipNo = transSlipNo;
	}
	public String getPaDelyGb() {
		return paDelyGb;
	}
	public void setPaDelyGb(String paDelyGb) {
		this.paDelyGb = paDelyGb;
	}
	public String getSlipNo() {
		return slipNo;
	}
	public void setSlipNo(String slipNo) {
		this.slipNo = slipNo;
	}	
	public String getLastYn() {
		return lastYn;
	}
	public void setLastYn(String lastYn) {
		this.lastYn = lastYn;
	}
	public String getTransYn() {
		return transYn;
	}
	public void setTransYn(String transYn) {
		this.transYn = transYn;
	}
	public String getRemark1V() {
		return remark1V;
	}
	public void setRemark1V(String remark1v) {
		remark1V = remark1v;
	}
}
