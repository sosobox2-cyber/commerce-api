package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class CjOneDayToken extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String tokenSeq;
	private String resultCd;
	private String resultDetail;
	private String tokenNum;
	private String tokenExprtnDtm;
	private String notice;
	private String entpCode;
	private String cjSIdno;
	private String custId;

	public String getTokenSeq() {
		return tokenSeq;
	}

	public void setTokenSeq(String tokenSeq) {
		this.tokenSeq = tokenSeq;
	}

	public String getResultCd() {
		return resultCd;
	}

	public void setResultCd(String resultCd) {
		this.resultCd = resultCd;
	}

	public String getResultDetail() {
		return resultDetail;
	}

	public void setResultDetail(String resultDetail) {
		this.resultDetail = resultDetail;
	}
	
	public String getTokenNum() {
		return tokenNum;
	}

	public void setTokenNum(String tokenNum) {
		this.tokenNum = tokenNum;
	}
	
	public String getTokenExprtnDtm() {
		return tokenExprtnDtm;
	}

	public void setTokenExprtnDtm(String tokenExprtnDtm) {
		this.tokenExprtnDtm = tokenExprtnDtm;
	}
	
	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getCjSIdno() {
		return cjSIdno;
	}

	public void setCjSIdno(String cjSIdno) {
		this.cjSIdno = cjSIdno;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
}
