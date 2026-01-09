package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class CjSlipError extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String apiGubun;
	private String apiParam;
	private String errorCode;
	private String errorReason;
		
	public String getApiGubun() {
		return apiGubun;
	}
	public void setApiGubun(String apiGubun) {
		this.apiGubun = apiGubun;
	}
	
	public String getApiParam() {
		return apiParam;
	}
	public void setApiParam(String apiParam) {
		this.apiParam = apiParam;
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
