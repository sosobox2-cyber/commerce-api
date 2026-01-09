package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaCopnCertification extends AbstractModel{

	private static final long serialVersionUID = 1L;
	
	private String paLmsdKey;
	private String sertiSeq;
	private String certiType;
	private String names;
	private String dataType;
	private String requiredYn;
	
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getSertiSeq() {
		return sertiSeq;
	}
	public void setSertiSeq(String sertiSeq) {
		this.sertiSeq = sertiSeq;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRequiredYn() {
		return requiredYn;
	}
	public void setRequiredYn(String requiredYn) {
		this.requiredYn = requiredYn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
