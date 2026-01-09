package com.cware.netshopping.patdeal.domain;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetail {
		// 주문상품
		@JsonProperty("orderProducts")
		private List<OrderProducts> orderProducts;
		
		@JsonProperty("shippings")
		private List<Shippings> shippings;
		
		@JsonProperty("claimInfos")
		private List<ClaimInfos> claimInfos;
		
		private String orderNo;
		private long mallNo;             
		private String pgType;           
		private String payType;          
		private String platformType;     
		private long memberNo;           
		private boolean isMemberOrder;    
		private long lastMainPayAmt;   
		private String currencyCode;    
		private double exchangeRate;     
		private String registerYmdt;  
		private String updateYmdt;    
		private String channelType;      
		private String trackingKey;      
		private String externalOrderNo;  
		private String orderMemo;        
//
//		// 에러 발생시 사용 
//		private String status;
//		private String error;
//		private String code;
//		
		
		
		public List<OrderProducts> getOrderProducts() {
			return orderProducts;
		}

		public void setOrderProducts(List<OrderProducts> orderProducts) {
			this.orderProducts = orderProducts;
		}
		
		
		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public long getMallNo() {
			return mallNo;
		}

		public void setMallNo(long mallNo) {
			this.mallNo = mallNo;
		}

		public String getPgType() {
			return pgType;
		}

		public void setPgType(String pgType) {
			this.pgType = pgType;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getPlatformType() {
			return platformType;
		}

		public void setPlatformType(String platformType) {
			this.platformType = platformType;
		}

		public long getMemberNo() {
			return memberNo;
		}

		public void setMemberNo(long memberNo) {
			this.memberNo = memberNo;
		}

		public boolean isMemberOrder() {
			return isMemberOrder;
		}

		public void setMemberOrder(boolean isMemberOrder) {
			this.isMemberOrder = isMemberOrder;
		}

		public long getLastMainPayAmt() {
			return lastMainPayAmt;
		}

		public void setLastMainPayAmt(long lastMainPayAmt) {
			this.lastMainPayAmt = lastMainPayAmt;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public double getExchangeRate() {
			return exchangeRate;
		}

		public void setExchangeRate(double exchangeRate) {
			this.exchangeRate = exchangeRate;
		}

		public String getRegisterYmdt() {
			return registerYmdt;
		}

		public void setRegisterYmdt(String registerYmdt) {
			this.registerYmdt = registerYmdt;
		}

		public String getUpdateYmdt() {
			return updateYmdt;
		}

		public void setUpdateYmdt(String updateYmdt) {
			this.updateYmdt = updateYmdt;
		}

		public String getChannelType() {
			return channelType;
		}

		public void setChannelType(String channelType) {
			this.channelType = channelType;
		}

		public String getTrackingKey() {
			return trackingKey;
		}

		public void setTrackingKey(String trackingKey) {
			this.trackingKey = trackingKey;
		}

		public String getExternalOrderNo() {
			return externalOrderNo;
		}

		public void setExternalOrderNo(String externalOrderNo) {
			this.externalOrderNo = externalOrderNo;
		}

		public String getOrderMemo() {
			return orderMemo;
		}

		public void setOrderMemo(String orderMemo) {
			this.orderMemo = orderMemo;
		}

		public List<Shippings> getShippings() {
			return shippings;
		}

		public void setShippings(List<Shippings> shippings) {
			this.shippings = shippings;
		}

		public List<ClaimInfos> getClaimInfos() {
			return claimInfos;
		}

		public void setClaimInfos(List<ClaimInfos> claimInfos) {
			this.claimInfos = claimInfos;
		}
		
		

//		public String getStatus() {
//			return status;
//		}
//
//		public void setStatus(String status) {
//			this.status = status;
//		}
//
//		public String getError() {
//			return error;
//		}
//
//		public void setError(String error) {
//			this.error = error;
//		}
//
//		public String getCode() {
//			return code;
//		}
//
//		public void setCode(String code) {
//			this.code = code;
//		}


}
