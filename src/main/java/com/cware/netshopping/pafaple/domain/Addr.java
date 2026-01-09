package com.cware.netshopping.pafaple.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Addr {
	
	@JsonProperty("SenderName")
	private String senderName;
	@JsonProperty("FreeSalesPrice")
	private Integer freeSalesPrice;
	@JsonProperty("ExpressCode")
	private String expressCode;	
	@JsonProperty("RetExpressCode")
	private String retExpressCode;	
	@JsonProperty("SenderAddr")
	private String senderAddr;	
	@JsonProperty("Return_addr1")
	private String returnAddr1;	
	@JsonProperty("Return_addr2")
	private String returnAddr2;	
	@JsonProperty("Return_Zipcode")
	private String returnZipcode;	
	@JsonProperty("Return_Tel")
	private String returntTel;	
	@JsonProperty("SenderFee")
	private Integer senderFee;	
	@JsonProperty("ExtraFee")
	private Integer extraFee;	
	@JsonProperty("RetSenderFee")
	private Integer retSenderFee;	
	@JsonProperty("RetExtraFee")
	private Integer retExtraFee;
	@JsonProperty("SenderID")
	private Integer senderID;
	
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public Integer getFreeSalesPrice() {
		return freeSalesPrice;
	}
	public void setFreeSalesPrice(Integer freeSalesPrice) {
		this.freeSalesPrice = freeSalesPrice;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getRetExpressCode() {
		return retExpressCode;
	}
	public void setRetExpressCode(String retExpressCode) {
		this.retExpressCode = retExpressCode;
	}
	public String getSenderAddr() {
		return senderAddr;
	}
	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}
	public String getReturnAddr1() {
		return returnAddr1;
	}
	public void setReturnAddr1(String returnAddr1) {
		this.returnAddr1 = returnAddr1;
	}
	public String getReturnAddr2() {
		return returnAddr2;
	}
	public void setReturnAddr2(String returnAddr2) {
		this.returnAddr2 = returnAddr2;
	}
	public String getReturn_zipcode() {
		return returnZipcode;
	}
	public void setReturn_zipcode(String returnZipcode) {
		this.returnZipcode = returnZipcode;
	}
	public String getReturnt_tel() {
		return returntTel;
	}
	public void setReturnt_tel(String returntTel) {
		this.returntTel = returntTel;
	}
	public Integer getSenderFee() {
		return senderFee;
	}
	public void setSenderFee(Integer senderFee) {
		this.senderFee = senderFee;
	}
	public Integer getExtra_fee() {
		return extraFee;
	}
	public void setExtra_fee(Integer extraFee) {
		this.extraFee = extraFee;
	}
	public Integer getRetSenderFee() {
		return retSenderFee;
	}
	public void setRetSenderFee(Integer retSenderFee) {
		this.retSenderFee = retSenderFee;
	}
	public Integer getRetExtraFee() {
		return retExtraFee;
	}
	public void setRetExtraFee(Integer retExtraFee) {
		this.retExtraFee = retExtraFee;
	}
	public Integer getSenderID() {
		return senderID;
	}
	public void setSenderID(Integer senderID) {
		this.senderID = senderID;
	}	
	
}
