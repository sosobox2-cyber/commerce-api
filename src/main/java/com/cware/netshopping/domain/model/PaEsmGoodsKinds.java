package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaEsmGoodsKinds extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String lgroup;
	private String mgroup;
	private String sgroup;
	private String dgroup;
	private String ngroup;
	private String lgroupName;
	private String mgroupName;
	private String sgroupName;
	private String dgroupName;
	private String ngroupName;
	private String lmsdnCode;
	private String doNotLeafYn;
	
	public String getLgroup() {
		return lgroup;
	}
	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}
	public String getMgroup() {
		return mgroup;
	}
	public void setMgroup(String mgroup) {
		this.mgroup = mgroup;
	}
	public String getSgroup() {
		return sgroup;
	}
	public void setSgroup(String sgroup) {
		this.sgroup = sgroup;
	}
	public String getDgroup() {
		return dgroup;
	}
	public void setDgroup(String dgroup) {
		this.dgroup = dgroup;
	}
	public String getNgroup() {
		return ngroup;
	}
	public void setNgroup(String ngroup) {
		this.ngroup = ngroup;
	}
	public String getLgroupName() {
		return lgroupName;
	}
	public void setLgroupName(String lgroupName) {
		this.lgroupName = lgroupName;
	}
	public String getMgroupName() {
		return mgroupName;
	}
	public void setMgroupName(String mgroupName) {
		this.mgroupName = mgroupName;
	}
	public String getSgroupName() {
		return sgroupName;
	}
	public void setSgroupName(String sgroupName) {
		this.sgroupName = sgroupName;
	}
	public String getDgroupName() {
		return dgroupName;
	}
	public void setDgroupName(String dgroupName) {
		this.dgroupName = dgroupName;
	}
	public String getNgroupName() {
		return ngroupName;
	}
	public void setNgroupName(String ngroupName) {
		this.ngroupName = ngroupName;
	}
	public String getLmsdnCode() {
		return lmsdnCode;
	}
	public void setLmsdnCode(String lmsdnCode) {
		this.lmsdnCode = lmsdnCode;
	}
	public String getDoNotLeafYn() {
		return doNotLeafYn;
	}
	public void setDoNotLeafYn(String doNotLeafYn) {
		this.doNotLeafYn = doNotLeafYn;
	}
	
}