package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimInfos {
	private long claimNo;             
	private String claimYmdt;             
	private String claimCompleteYmdt;     
	private String claimType;             
	private long mallNo;                  
	private long memberNo;               
	private String orderNo;               
	private String refundPayType;         
	private String claimStatusType;       
	private long claimAmt;                
	private String returnWayType;         
	private String responsibleObjectType; 
	private String claimReasonType;       
	private String claimReasonDetail;     
	private long accumulationPayAmt;     
	private List<String> orderOptionNos;
	
	 
	public List<String> getOrderOptionNos() {
		return orderOptionNos;
	}

	public void setOrderOptionNos(List<String> orderOptionNos) {
		this.orderOptionNos = orderOptionNos;
	}


	public long getClaimNo() {
		return claimNo;
	}



	public void setClaimNo(long claimNo) {
		this.claimNo = claimNo;
	}



	public String getClaimYmdt() {
		return claimYmdt;
	}



	public void setClaimYmdt(String claimYmdt) {
		this.claimYmdt = claimYmdt;
	}



	public String getClaimCompleteYmdt() {
		return claimCompleteYmdt;
	}



	public void setClaimCompleteYmdt(String claimCompleteYmdt) {
		this.claimCompleteYmdt = claimCompleteYmdt;
	}



	public String getClaimType() {
		return claimType;
	}



	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}



	public long getMallNo() {
		return mallNo;
	}



	public void setMallNo(long mallNo) {
		this.mallNo = mallNo;
	}



	public long getMemberNo() {
		return memberNo;
	}



	public void setMemberNo(long memberNo) {
		this.memberNo = memberNo;
	}



	public String getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}



	public String getRefundPayType() {
		return refundPayType;
	}



	public void setRefundPayType(String refundPayType) {
		this.refundPayType = refundPayType;
	}



	public String getClaimStatusType() {
		return claimStatusType;
	}



	public void setClaimStatusType(String claimStatusType) {
		this.claimStatusType = claimStatusType;
	}



	public long getClaimAmt() {
		return claimAmt;
	}



	public void setClaimAmt(long claimAmt) {
		this.claimAmt = claimAmt;
	}



	public String getReturnWayType() {
		return returnWayType;
	}



	public void setReturnWayType(String returnWayType) {
		this.returnWayType = returnWayType;
	}



	public String getResponsibleObjectType() {
		return responsibleObjectType;
	}



	public void setResponsibleObjectType(String responsibleObjectType) {
		this.responsibleObjectType = responsibleObjectType;
	}



	public String getClaimReasonType() {
		return claimReasonType;
	}



	public void setClaimReasonType(String claimReasonType) {
		this.claimReasonType = claimReasonType;
	}



	public String getClaimReasonDetail() {
		return claimReasonDetail;
	}



	public void setClaimReasonDetail(String claimReasonDetail) {
		this.claimReasonDetail = claimReasonDetail;
	}



	public long getAccumulationPayAmt() {
		return accumulationPayAmt;
	}



	public void setAccumulationPayAmt(long accumulationPayAmt) {
		this.accumulationPayAmt = accumulationPayAmt;
	}

	@Override
	public String toString() {
		return "ClaimInfos [claimNo=" + claimNo + ", claimYmdt=" + claimYmdt + ", claimCompleteYmdt="
				+ claimCompleteYmdt + ", claimType=" + claimType + ", mallNo=" + mallNo + ", memberNo=" + memberNo
				+ ", orderNo=" + orderNo + ", refundPayType=" + refundPayType + ", claimStatusType=" + claimStatusType
				+ ", claimAmt=" + claimAmt + ", returnWayType=" + returnWayType + ", responsibleObjectType="
				+ responsibleObjectType + ", claimReasonType=" + claimReasonType + ", claimReasonDetail="
				+ claimReasonDetail + ", accumulationPayAmt=" + accumulationPayAmt + ", orderOptionNos="
				+ orderOptionNos + "]";
	}

	
}
