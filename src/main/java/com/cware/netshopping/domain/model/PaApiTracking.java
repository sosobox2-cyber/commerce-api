package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class PaApiTracking extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String linkCode;
	private String ipAddr;
	private String siteGb;
	private String entpCode;
	private String userId;
	private String apiCode;
	private String method;
	private String prm;
	private String resultCode;
	private String resultMsg;
	private String resultRow;
	private Timestamp startDate;
	private Timestamp endDate;
	private String remark1;
	private String remark2;
	private String apiType;


	
	
	public String getSiteGb() {
		return siteGb;
	}
	public void setSiteGb(String siteGb) {
		this.siteGb = siteGb;
	}
	public String getLinkCode() { 
		return this.linkCode;
	}
	public String getIpAddr() { 
		return this.ipAddr;
	}
	public String getEntpCode() { 
		return this.entpCode;
	}
	public String getUserId() { 
		return this.userId;
	}
	public String getApiCode() { 
		return this.apiCode;
	}
	public String getMethod() { 
		return this.method;
	}
	public String getPrm() { 
		return this.prm;
	}
	public String getResultCode() { 
		return this.resultCode;
	}
	public String getResultMsg() { 
		return this.resultMsg;
	}
	public String getResultRow() { 
		return this.resultRow;
	}
	public Timestamp getStartDate() { 
		return this.startDate;
	}
	public Timestamp getEndDate() { 
		return this.endDate;
	}
	public String getRemark1() { 
		return this.remark1;
	}
	public String getRemark2() { 
		return this.remark2;
	}
	public String getApiType() {
		return apiType;
	}
	public void setLinkCode(String linkCode) { 
		this.linkCode = linkCode;
	}
	public void setIpAddr(String ipAddr) { 
		this.ipAddr = ipAddr;
	}
	public void setEntpCode(String entpCode) { 
		this.entpCode = entpCode;
	}
	public void setUserId(String userId) { 
		this.userId = userId;
	}
	public void setApiCode(String apiCode) { 
		this.apiCode = apiCode;
	}
	public void setMethod(String method) { 
		this.method = method;
	}
	public void setPrm(String prm) { 
		this.prm = prm;
	}
	public void setResultCode(String resultCode) { 
		this.resultCode = resultCode;
	}
	public void setResultMsg(String resultMsg) { 
		this.resultMsg = resultMsg;
	}
	public void setResultRow(String resultRow) { 
		this.resultRow = resultRow;
	}
	public void setStartDate(Timestamp startDate) { 
		this.startDate = startDate;
	}
	public void setEndDate(Timestamp endDate) { 
		this.endDate = endDate;
	}
	public void setRemark1(String remark1) { 
		this.remark1 = remark1;
	}
	public void setRemark2(String remark2) { 
		this.remark2 = remark2;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
}
