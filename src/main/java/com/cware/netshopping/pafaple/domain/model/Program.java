package com.cware.netshopping.pafaple.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Program extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String progCode;
	private String progName;
	private String bdBtime;
	private String bdEtime;
	private String bdImgUrl;
	private String hostStaff;
	private Item item;
	
	public String getProgCode() {
		return progCode;
	}
	public void setProgCode(String progCode) {
		this.progCode = progCode;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public String getBdBtime() {
		return bdBtime;
	}
	public void setBdBtime(String bdBtime) {
		this.bdBtime = bdBtime;
	}
	public String getBdEtime() {
		return bdEtime;
	}
	public void setBdEtime(String bdEtime) {
		this.bdEtime = bdEtime;
	}
	public String getBdImgUrl() {
		return bdImgUrl;
	}
	public void setBdImgUrl(String bdImgUrl) {
		this.bdImgUrl = bdImgUrl;
	}
	public String getHostStaff() {
		return hostStaff;
	}
	public void setHostStaff(String hostStaff) {
		this.hostStaff = hostStaff;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
}