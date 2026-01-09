package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Pa11stclaimlist extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String ordNo;
	private String ordPrdSeq;
	private String clmReqSeq;
	private String paOrderGb;
	private String affliateBndlDlvSeq;
	private String clmReqCont;
	private int clmReqQty;
	private String clmReqRsn;
	private String clmStat;
	private String optName;
	private long prdNo;
	private Timestamp reqDt;
	private String ordNm;
	private String ordTlphnNo;
	private String ordPrtblTel;
	private String rcvrMailNo;
	private String rcvrMailNoSeq;
	private String rcvrBaseAddr;
	private String rcvrDtlsAddr;
	private String rcvrTypeAdd;
	private String rcvrTypeBilNo;
	private String twMthd;
	private String exchNm;
	private String exchTlphnNo;
	private String exchPrtbTel;
	private String exchMailNo;
	private String exchMailNoSeq;
	private String exchBaseAddr;
	private String exchDtlsAddr;
	private String exchTypeAdd;
	private String exchTypeBilNo;
	private int clmLstDlvCst;
	private int appmtDlvCst;
	private String clmDlvCstMthd;
	private String twPrdInvcNo;
	private String dlvEtprsCd;
	
	
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getOrdPrdSeq() {
		return ordPrdSeq;
	}
	public void setOrdPrdSeq(String ordPrdSeq) {
		this.ordPrdSeq = ordPrdSeq;
	}
	public String getClmReqSeq() {
		return clmReqSeq;
	}
	public void setClmReqSeq(String clmReqSeq) {
		this.clmReqSeq = clmReqSeq;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getAffliateBndlDlvSeq() {
		return affliateBndlDlvSeq;
	}
	public void setAffliateBndlDlvSeq(String affliateBndlDlvSeq) {
		this.affliateBndlDlvSeq = affliateBndlDlvSeq;
	}
	public String getClmReqCont() {
		return clmReqCont;
	}
	public void setClmReqCont(String clmReqCont) {
		this.clmReqCont = clmReqCont;
	}
	public int getClmReqQty() {
		return clmReqQty;
	}
	public void setClmReqQty(int clmReqQty) {
		this.clmReqQty = clmReqQty;
	}
	public String getClmReqRsn() {
		return clmReqRsn;
	}
	public void setClmReqRsn(String clmReqRsn) {
		this.clmReqRsn = clmReqRsn;
	}
	public String getClmStat() {
		return clmStat;
	}
	public void setClmStat(String clmStat) {
		this.clmStat = clmStat;
	}
	public long getPrdNo() {
		return prdNo;
	}
	public void setPrdNo(long prdNo) {
		this.prdNo = prdNo;
	}
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}
	public Timestamp getReqDt() {
		return reqDt;
	}
	public void setReqDt(Timestamp reqDt) {
		this.reqDt = reqDt;
	}
	public String getOrdNm() {
		return ordNm;
	}
	public void setOrdNm(String ordNm) {
		this.ordNm = ordNm;
	}
	public String getOrdTlphnNo() {
		return ordTlphnNo;
	}
	public void setOrdTlphnNo(String ordTlphnNo) {
		this.ordTlphnNo = ordTlphnNo;
	}
	public String getOrdPrtblTel() {
		return ordPrtblTel;
	}
	public void setOrdPrtblTel(String ordPrtblTel) {
		this.ordPrtblTel = ordPrtblTel;
	}
	public String getRcvrMailNo() {
		return rcvrMailNo;
	}
	public void setRcvrMailNo(String rcvrMailNo) {
		this.rcvrMailNo = rcvrMailNo;
	}
	public String getRcvrMailNoSeq() {
		return rcvrMailNoSeq;
	}
	public void setRcvrMailNoSeq(String rcvrMailNoSeq) {
		this.rcvrMailNoSeq = rcvrMailNoSeq;
	}
	public String getRcvrBaseAddr() {
		return rcvrBaseAddr;
	}
	public void setRcvrBaseAddr(String rcvrBaseAddr) {
		this.rcvrBaseAddr = rcvrBaseAddr;
	}
	public String getRcvrDtlsAddr() {
		return rcvrDtlsAddr;
	}
	public void setRcvrDtlsAddr(String rcvrDtlsAddr) {
		this.rcvrDtlsAddr = rcvrDtlsAddr;
	}
	public String getRcvrTypeAdd() {
		return rcvrTypeAdd;
	}
	public void setRcvrTypeAdd(String rcvrTypeAdd) {
		this.rcvrTypeAdd = rcvrTypeAdd;
	}
	public String getRcvrTypeBilNo() {
		return rcvrTypeBilNo;
	}
	public void setRcvrTypeBilNo(String rcvrTypeBilNo) {
		this.rcvrTypeBilNo = rcvrTypeBilNo;
	}
	public String getTwMthd() {
		return twMthd;
	}
	public void setTwMthd(String twMthd) {
		this.twMthd = twMthd;
	}
	public String getExchNm() {
		return exchNm;
	}
	public void setExchNm(String exchNm) {
		this.exchNm = exchNm;
	}
	public String getExchTlphnNo() {
		return exchTlphnNo;
	}
	public void setExchTlphnNo(String exchTlphnNo) {
		this.exchTlphnNo = exchTlphnNo;
	}
	public String getExchPrtbTel() {
		return exchPrtbTel;
	}
	public void setExchPrtbTel(String exchPrtbTel) {
		this.exchPrtbTel = exchPrtbTel;
	}
	public String getExchMailNo() {
		return exchMailNo;
	}
	public void setExchMailNo(String exchMailNo) {
		this.exchMailNo = exchMailNo;
	}
	public String getExchMailNoSeq() {
		return exchMailNoSeq;
	}
	public void setExchMailNoSeq(String exchMailNoSeq) {
		this.exchMailNoSeq = exchMailNoSeq;
	}
	public String getExchBaseAddr() {
		return exchBaseAddr;
	}
	public void setExchBaseAddr(String exchBaseAddr) {
		this.exchBaseAddr = exchBaseAddr;
	}
	public String getExchDtlsAddr() {
		return exchDtlsAddr;
	}
	public void setExchDtlsAddr(String exchDtlsAddr) {
		this.exchDtlsAddr = exchDtlsAddr;
	}
	public String getExchTypeAdd() {
		return exchTypeAdd;
	}
	public void setExchTypeAdd(String exchTypeAdd) {
		this.exchTypeAdd = exchTypeAdd;
	}
	public String getExchTypeBilNo() {
		return exchTypeBilNo;
	}
	public void setExchTypeBilNo(String exchTypeBilNo) {
		this.exchTypeBilNo = exchTypeBilNo;
	}
	public int getClmLstDlvCst() {
		return clmLstDlvCst;
	}
	public void setClmLstDlvCst(int clmLstDlvCst) {
		this.clmLstDlvCst = clmLstDlvCst;
	}
	public int getAppmtDlvCst() {
		return appmtDlvCst;
	}
	public void setAppmtDlvCst(int appmtDlvCst) {
		this.appmtDlvCst = appmtDlvCst;
	}
	public String getClmDlvCstMthd() {
		return clmDlvCstMthd;
	}
	public void setClmDlvCstMthd(String clmDlvCstMthd) {
		this.clmDlvCstMthd = clmDlvCstMthd;
	}
	public String getTwPrdInvcNo() {
		return twPrdInvcNo;
	}
	public void setTwPrdInvcNo(String twPrdInvcNo) {
		this.twPrdInvcNo = twPrdInvcNo;
	}
	public String getDlvEtprsCd() {
		return dlvEtprsCd;
	}
	public void setDlvEtprsCd(String dlvEtprsCd) {
		this.dlvEtprsCd = dlvEtprsCd;
	}
	
}
