package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class QeenCounselDetail {
	
	private int id;
	private Writer writer;
	private long createdAtMillis;
	private String status;
	private String sellerCompanyCode;
	private String sellerCompanyName;
	private String title;
	private String type;
	private String content;
	
	@JsonProperty("isNew")
	private boolean isNew;
	
	private String writerType;
	private Long completedAtMillis;
	private List<String> fileUrls;
	private OrderHistory orderHistory;
	private DeliveryRequest deliveryRequest;
	private Object refundAccount;
	private List<String> orderLineIds;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getWriterType() {
		return writerType;
	}
	public void setWriterType(String writerType) {
		this.writerType = writerType;
	}
	public Long getCompletedAtMillis() {
		return completedAtMillis;
	}
	public void setCompletedAtMillis(Long completedAtMillis) {
		this.completedAtMillis = completedAtMillis;
	}
	public List<String> getFileUrls() {
		return fileUrls;
	}
	public void setFileUrls(List<String> fileUrls) {
		this.fileUrls = fileUrls;
	}
	public OrderHistory getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(OrderHistory orderHistory) {
		this.orderHistory = orderHistory;
	}
	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}
	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}
	public Object getRefundAccount() {
		return refundAccount;
	}
	public void setRefundAccount(Object refundAccount) {
		this.refundAccount = refundAccount;
	}
	public List<String> getOrderLineIds() {
		return orderLineIds;
	}
	public void setOrderLineIds(List<String> orderLineIds) {
		this.orderLineIds = orderLineIds;
	}
	
	
	
}
