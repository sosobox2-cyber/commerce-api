package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Orderm extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orderNo;
	private String custNo;
	private Timestamp orderDate;
	private String orderMedia;
	private String password;
	private String withCode;
	private String ipAddr;
	private String membGb;
	private String employeeId;
	
	private String senderName;
	private String channel_id;
    private String channel_id_org;
    private String channel;

	public String getOrderNo() { 
		return this.orderNo;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public Timestamp getOrderDate() { 
		return this.orderDate;
	}
	public String getOrderMedia() { 
		return this.orderMedia;
	}
	public String getPassword() { 
		return this.password;
	}
	public String getWithCode() { 
		return this.withCode;
	}
	public String getIpAddr() { 
		return this.ipAddr;
	}
	public String getMembGb() { 
		return this.membGb;
	}
	public String getEmployeeId() { 
		return this.employeeId;
	}

	public void setOrderNo(String orderNo) { 
		this.orderNo = orderNo;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setOrderDate(Timestamp orderDate) { 
		this.orderDate = orderDate;
	}
	public void setOrderMedia(String orderMedia) { 
		this.orderMedia = orderMedia;
	}
	public void setPassword(String password) { 
		this.password = password;
	}
	public void setWithCode(String withCode) { 
		this.withCode = withCode;
	}
	public void setIpAddr(String ipAddr) { 
		this.ipAddr = ipAddr;
	}
	public void setMembGb(String membGb) { 
		this.membGb = membGb;
	}
	public void setEmployeeId(String employeeId) { 
		this.employeeId = employeeId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getChannel_id_org() {
		return channel_id_org;
	}
	public void setChannel_id_org(String channel_id_org) {
		this.channel_id_org = channel_id_org;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
}
