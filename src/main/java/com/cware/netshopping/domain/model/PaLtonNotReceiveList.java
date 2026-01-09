package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonNotReceiveList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String odNo;
	private String odSeq;
	private String procSeq;
	private String spdNo;
	private String spdNm;
	private String sitmNo;
	private String sitmNm;
	private String odAccpDttm;
	private String dvCmptDttm;
	private String dvCoCd;
	private String invcNo;
	private String nrcptDeclAccpDttm;
	private String nrcptDeclAccpNo;
	private String nrcptDeclCnts;
	private String nrcptDeclWhdrReqCnts;
	private String thdyDvProcTypCd;
	private String thdyPdYn;
	private String procUser;
	private Timestamp procDate;
	private Timestamp transDate;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getOdNo() {
		return odNo;
	}
	public void setOdNo(String odNo) {
		this.odNo = odNo;
	}
	public String getOdSeq() {
		return odSeq;
	}
	public void setOdSeq(String odSeq) {
		this.odSeq = odSeq;
	}
	public String getProcSeq() {
		return procSeq;
	}
	public void setProcSeq(String procSeq) {
		this.procSeq = procSeq;
	}
	public String getSpdNo() {
		return spdNo;
	}
	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}
	public String getSpdNm() {
		return spdNm;
	}
	public void setSpdNm(String spdNm) {
		this.spdNm = spdNm;
	}
	public String getSitmNo() {
		return sitmNo;
	}
	public void setSitmNo(String sitmNo) {
		this.sitmNo = sitmNo;
	}
	public String getSitmNm() {
		return sitmNm;
	}
	public void setSitmNm(String sitmNm) {
		this.sitmNm = sitmNm;
	}
	public String getOdAccpDttm() {
		return odAccpDttm;
	}
	public void setOdAccpDttm(String odAccpDttm) {
		this.odAccpDttm = odAccpDttm;
	}
	public String getDvCmptDttm() {
		return dvCmptDttm;
	}
	public void setDvCmptDttm(String dvCmptDttm) {
		this.dvCmptDttm = dvCmptDttm;
	}
	public String getDvCoCd() {
		return dvCoCd;
	}
	public void setDvCoCd(String dvCoCd) {
		this.dvCoCd = dvCoCd;
	}
	public String getInvcNo() {
		return invcNo;
	}
	public void setInvcNo(String invcNo) {
		this.invcNo = invcNo;
	}
	public String getNrcptDeclAccpDttm() {
		return nrcptDeclAccpDttm;
	}
	public void setNrcptDeclAccpDttm(String nrcptDeclAccpDttm) {
		this.nrcptDeclAccpDttm = nrcptDeclAccpDttm;
	}
	public String getNrcptDeclAccpNo() {
		return nrcptDeclAccpNo;
	}
	public void setNrcptDeclAccpNo(String nrcptDeclAccpNo) {
		this.nrcptDeclAccpNo = nrcptDeclAccpNo;
	}
	public String getNrcptDeclCnts() {
		return nrcptDeclCnts;
	}
	public void setNrcptDeclCnts(String nrcptDeclCnts) {
		this.nrcptDeclCnts = nrcptDeclCnts;
	}
	public String getNrcptDeclWhdrReqCnts() {
		return nrcptDeclWhdrReqCnts;
	}
	public void setNrcptDeclWhdrReqCnts(String nrcptDeclWhdrReqCnts) {
		this.nrcptDeclWhdrReqCnts = nrcptDeclWhdrReqCnts;
	}
	public String getThdyDvProcTypCd() {
		return thdyDvProcTypCd;
	}
	public void setThdyDvProcTypCd(String thdyDvProcTypCd) {
		this.thdyDvProcTypCd = thdyDvProcTypCd;
	}
	public String getThdyPdYn() {
		return thdyPdYn;
	}
	public void setThdyPdYn(String thdyPdYn) {
		this.thdyPdYn = thdyPdYn;
	}
	public String getProcUser() {
		return procUser;
	}
	public void setProcUser(String procUser) {
		this.procUser = procUser;
	}
	public Timestamp getProcDate() {
		return procDate;
	}
	public void setProcDate(Timestamp procDate) {
		this.procDate = procDate;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}	
}
