package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealClaimList extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	  private long claimNo;                
	  private Timestamp claimYmdt;         
	  private Timestamp claimCompleteYmdt;
	  private long mallNo;                 
	  private long memberNo;               
	  private String claimType;            
	  private String orderNo;              
	  private String refundType;      
	  private String claimStatusType;     
	  private long claimAmt;             
	  private String returnWayType;
	  private String orderOptionNos;      
	  private String responsibleObjectType; 
	  private String claimReasonType;       
	  private String claimReasonDetail;     
	  private long orderCnt;             
	  private String optionManagementCd;    
	  private String refundName;        
	  private String refundContact1;
	  private String refundContact2;        
	  private String refundZipCd;          
	  private String refundJibunAddress;   
	  private String refundAddress;         
	  private String refundDetailAddress;  
	  private String receiverName;          
	  private String receiverContact1;      
	  private String receiverContact2;      
	  private String receiverZipCd;        
	  private String receiverJibunAddress; 
	  private String receiverAddress;       
	  private String receiverDetailAddress;
	  private String deliveryMemo;   
	  private long orderOptionNo;
	  private Timestamp insertDate;          
	  private Timestamp modifyDate;      
	  private long returnDeliveryAmt;
	  private long remoteDeliveryAmt;
	  private String paOrderGb;
	  private String shippingNo;
	  private String originShippinNo;
	  
	  private String procFlag;
	  private String cancelProcNote;
	  private String cancelProcId;
	  private String procNote;
		
	public long getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(long claimNo) {
		this.claimNo = claimNo;
	}
	public Timestamp getClaimYmdt() {
		return claimYmdt;
	}
	public void setClaimYmdt(Timestamp claimYmdt) {
		this.claimYmdt = claimYmdt;
	}
	public Timestamp getClaimCompleteYmdt() {
		return claimCompleteYmdt;
	}
	public void setClaimCompleteYmdt(Timestamp claimCompleteYmdt) {
		this.claimCompleteYmdt = claimCompleteYmdt;
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
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
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
	public String getOrderOptionNos() {
		return orderOptionNos;
	}
	public void setOrderOptionNos(String orderOptionNos) {
		this.orderOptionNos = orderOptionNos;
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
	public long getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(long orderCnt) {
		this.orderCnt = orderCnt;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getRefundName() {
		return refundName;
	}
	public void setRefundName(String refundName) {
		this.refundName = refundName;
	}
	public String getRefundContact1() {
		return refundContact1;
	}
	public void setRefundContact1(String refundContact1) {
		this.refundContact1 = refundContact1;
	}
	public String getRefundContact2() {
		return refundContact2;
	}
	public void setRefundContact2(String refundContact2) {
		this.refundContact2 = refundContact2;
	}
	public String getRefundZipCd() {
		return refundZipCd;
	}
	public void setRefundZipCd(String refundZipCd) {
		this.refundZipCd = refundZipCd;
	}
	public String getRefundJibunAddress() {
		return refundJibunAddress;
	}
	public void setRefundJibunAddress(String refundJibunAddress) {
		this.refundJibunAddress = refundJibunAddress;
	}
	public String getRefundAddress() {
		return refundAddress;
	}
	public void setRefundAddress(String refundAddress) {
		this.refundAddress = refundAddress;
	}
	public String getRefundDetailAddress() {
		return refundDetailAddress;
	}
	public void setRefundDetailAddress(String refundDetailAddress) {
		this.refundDetailAddress = refundDetailAddress;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverContact1() {
		return receiverContact1;
	}
	public void setReceiverContact1(String receiverContact1) {
		this.receiverContact1 = receiverContact1;
	}
	public String getReceiverContact2() {
		return receiverContact2;
	}
	public void setReceiverContact2(String receiverContact2) {
		this.receiverContact2 = receiverContact2;
	}
	public String getReceiverZipCd() {
		return receiverZipCd;
	}
	public void setReceiverZipCd(String receiverZipCd) {
		this.receiverZipCd = receiverZipCd;
	}
	public String getReceiverJibunAddress() {
		return receiverJibunAddress;
	}
	public void setReceiverJibunAddress(String receiverJibunAddress) {
		this.receiverJibunAddress = receiverJibunAddress;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverDetailAddress() {
		return receiverDetailAddress;
	}
	public void setReceiverDetailAddress(String receiverDetailAddress) {
		this.receiverDetailAddress = receiverDetailAddress;
	}
	public String getDeliveryMemo() {
		return deliveryMemo;
	}
	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}
	public long getOrderOptionNo() {
		return orderOptionNo;
	}
	public void setOrderOptionNo(long orderOptionNo) {
		this.orderOptionNo = orderOptionNo;
	}
	public long getReturnDeliveryAmt() {
		return returnDeliveryAmt;
	}
	public void setReturnDeliveryAmt(long returnDeliveryAmt) {
		this.returnDeliveryAmt = returnDeliveryAmt;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getOriginShippinNo() {
		return originShippinNo;
	}
	public void setOriginShippinNo(String originShippinNo) {
		this.originShippinNo = originShippinNo;
	}
	public String getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}
	public long getRemoteDeliveryAmt() {
		return remoteDeliveryAmt;
	}
	public void setRemoteDeliveryAmt(long remoteDeliveryAmt) {
		this.remoteDeliveryAmt = remoteDeliveryAmt;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getCancelProcNote() {
		return cancelProcNote;
	}
	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}
	public String getCancelProcId() {
		return cancelProcId;
	}
	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}
	public String getProcNote() {
		return procNote;
	}
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}
	
	
}
