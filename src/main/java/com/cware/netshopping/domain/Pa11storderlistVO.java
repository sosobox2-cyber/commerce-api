package com.cware.netshopping.domain;

import java.sql.Timestamp;
import com.cware.netshopping.domain.model.Pa11storderlist;

/**
 *
 * @author Commerceware
 *
 */
public class Pa11storderlistVO extends Pa11storderlist{
	private static final long serialVersionUID = 1L;

	private String paCode;
	//발송예정일 추가
	private Timestamp dlvSndDue; //발송마감일자 (기존 예정일)
	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public Timestamp getDlvSndDue() {
		return dlvSndDue;
	}

	public void setDlvSndDue(Timestamp dlvSndDue) {
		this.dlvSndDue = dlvSndDue;
	}

	
	
	
}