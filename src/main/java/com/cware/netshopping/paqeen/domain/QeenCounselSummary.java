package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class QeenCounselSummary {
	
	private int id;
    private String type;
    private String status;
    private String orderId;
    private String sellerCompanyCode;
    private String sellerCompanyName;
    private Writer writer;
    private long createdAtMillis;
    private String title;
    
    @JsonProperty("isNew")
    private boolean isNew;
    
    private String note;
    private int totalCommentCount;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSellerCompanyCode() {
		return sellerCompanyCode;
	}
	public void setSellerCompanyCode(String sellerCompanyCode) {
		this.sellerCompanyCode = sellerCompanyCode;
	}
	public String getSellerCompanyName() {
		return sellerCompanyName;
	}
	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
	}
	public long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getTotalCommentCount() {
		return totalCommentCount;
	}
	public void setTotalCommentCount(int totalCommentCount) {
		this.totalCommentCount = totalCommentCount;
	}
	
}
