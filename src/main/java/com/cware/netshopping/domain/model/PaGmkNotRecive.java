package com.cware.netshopping.domain.model;


import java.sql.Timestamp;
import com.cware.framework.core.basic.AbstractModel;

public class PaGmkNotRecive extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
       
    private String counselSeq; //상담 연동번호 
    private String payNo;   //장바구니 번호
    private String paCode;
    private String paGroupCode;
    private String contrNo;  //G마켓 주문번호
    private String seq;
    private String orderNo; //스토아 주문번호
    private String orderGSeq; //G-seq
    private String orderDSeq; //D-seq
    private String transYn  ; //지마켓 전송 여부
    private String preCanYn ; //기철회 여부
    private Timestamp claimSolveDate; //미수령 철회일 (지마켓이 가지고 있는 데이터)
    private String claimSolveGb;  //철회 주체 0 : 구매자, 1 : CS, 2 :판매자
    private Timestamp claimDate;  //미수령 신고일
    private String claimReason;   //미수령 신고사유 
    private String claimReasonDetail; //미수령 신고 사유(고객 입력값)
    private Timestamp claimCancelDate;//판매자가 철회한 날짜
    private String claimCancelType;   //미수령 철회 방식 타입  1: 송장번호 재입력  2. 철회요청 메시지입력
    private String delyCode;		  //택배사 코드
    private String delyNo;		   	  //송장번호
    private String claimCancelReason; //미수령 철회 메세지
    private Timestamp transDate;	  //연동일
	    
    
    
    
    public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getCounselSeq() {
		return counselSeq;
	}
	public void setCounselSeq(String counselSeq) {
		this.counselSeq = counselSeq;
	}
    
    public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getContrNo() {
		return contrNo;
	}
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public Timestamp getClaimCancelDate() {
		return claimCancelDate;
	}
	public void setClaimCancelDate(Timestamp claimCancelDate) {
		this.claimCancelDate = claimCancelDate;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getTransYn() {
		return transYn;
	}
	public void setTransYn(String transYn) {
		this.transYn = transYn;
	}
	public String getPreCanYn() {
		return preCanYn;
	}
	public void setPreCanYn(String preCanYn) {
		this.preCanYn = preCanYn;
	}
	public Timestamp getClaimSolveDate() {
		return claimSolveDate;
	}
	public void setClaimSolveDate(Timestamp claimSolveDate) {
		this.claimSolveDate = claimSolveDate;
	}
	public String getClaimSolveGb() {
		return claimSolveGb;
	}
	public void setClaimSolveGb(String claimSolveGb) {
		this.claimSolveGb = claimSolveGb;
	}
	public Timestamp getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(Timestamp claimDate) {
		this.claimDate = claimDate;
	}
	public String getClaimReason() {
		return claimReason;
	}
	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}
	public String getClaimReasonDetail() {
		return claimReasonDetail;
	}
	public void setClaimReasonDetail(String claimReasonDetail) {
		this.claimReasonDetail = claimReasonDetail;
	}
	public String getClaimCancelType() {
		return claimCancelType;
	}
	public void setClaimCancelType(String claimCancelType) {
		this.claimCancelType = claimCancelType;
	}
	public String getDelyCode() {
		return delyCode;
	}
	public void setDelyCode(String delyCode) {
		this.delyCode = delyCode;
	}
	public String getDelyNo() {
		return delyNo;
	}
	public void setDelyNo(String delyNo) {
		this.delyNo = delyNo;
	}
	public String getClaimCancelReason() {
		return claimCancelReason;
	}
	public void setClaimCancelReason(String claimCancelReason) {
		this.claimCancelReason = claimCancelReason;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
    
}
