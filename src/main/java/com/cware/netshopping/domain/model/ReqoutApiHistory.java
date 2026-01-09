package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class ReqoutApiHistory extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String delyType;
	private String ordNo;
	private String errorCode;
	private String errorReason;
	
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}	
	
	
}
