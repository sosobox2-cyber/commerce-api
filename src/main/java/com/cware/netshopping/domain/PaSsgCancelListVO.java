package com.cware.netshopping.domain;

import java.util.ArrayList;
import java.util.Map;

import com.cware.netshopping.domain.model.PaSsgCancelList;

public class PaSsgCancelListVO extends PaSsgCancelList implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOrderGb;
	private String outBefClaimGb;
	private ArrayList<Object> cstInfo;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	public ArrayList<Object> getCstInfo() {
		return cstInfo;
	}
	public void setCstInfo(ArrayList<Object> cstInfo) {
		this.cstInfo = cstInfo;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
