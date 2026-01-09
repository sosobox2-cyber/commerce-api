package com.cware.netshopping.domain.model;


import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmkCancel extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    
    private String paCode;			//제휴사 번호
    private String paGroupCode;		//제휴사 그룹 코드(02:G마켓 03:Auction 04:G9)
    private String payNo; 			//장바구니번호(결제번호)
    private String groupNo;			//묶음배송비정책번호(배송번호)
    private String contrNo;			//주문번호
    private String contrNoSeq;		//주문번호 순번 
    private String withDrawYn; 		//철회여부
    private String procNo;			//현재 처리 상태값
    private String goodsNo;			//EMS상품번호
    private String siteGoodsNo;		//사이트 상품번호
    private String requestUser;		//취소신청자(0:없음 1:구매자 2:판매자 3:고객센터 4:기타)
    private String approveUser;		//취소처리자(0:없음 1:구매자 2:판매자 3:고객센터 4:기타)
    private String cancelStatus;	//반품/교환상태
    private	Timestamp orderDate;	//주문일자
    private Timestamp payDate;		//결제일
    private Timestamp requestDate;  //취소 신청일
    private Timestamp approvalDate; //취소 승인일
    private Timestamp completeDate;	//취소 완료일
    private Timestamp withDrawDate; //취소 철회일
    private String reason;			//취소 사유(취소 사유( 0: 판매자귀책 1: 구매자귀책 2:기타))
    private String reasonDetail;	//취소 상세사유
    private double addShippingFee;	//추가로 발생하는 배송비
	
    
    public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getContrNo() {
		return contrNo;
	}
	public String getProcNo() {
		return procNo;
	}
	public void setProcNo(String procNo) {
		this.procNo = procNo;
	}
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getContrNoSeq() {
		return contrNoSeq;
	}
	public void setContrNoSeq(String contrNoSeq) {
		this.contrNoSeq = contrNoSeq;
	}
	public String getWithDrawYn() {
		return withDrawYn;
	}
	public void setWithDrawYn(String withDrawYn) {
		this.withDrawYn = withDrawYn;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSiteGoodsNo() {
		return siteGoodsNo;
	}
	public void setSiteGoodsNo(String siteGoodsNo) {
		this.siteGoodsNo = siteGoodsNo;
	}
	public String getRequestUser() {
		return requestUser;
	}
	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}
	public String getApproveUser() {
		return approveUser;
	}
	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}
	public String getCancelStatus() {
		return cancelStatus;
	}
	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public Timestamp getPayDate() {
		return payDate;
	}
	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	public Timestamp getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}
	public Timestamp getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Timestamp completeDate) {
		this.completeDate = completeDate;
	}
	public Timestamp getWithDrawDate() {
		return withDrawDate;
	}
	public void setWithDrawDate(Timestamp withDrawDate) {
		this.withDrawDate = withDrawDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReasonDetail() {
		return reasonDetail;
	}
	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}
	public double getAddShippingFee() {
		return addShippingFee;
	}
	public void setAddShippingFee(double addShippingFee) {
		this.addShippingFee = addShippingFee;
	}

}
