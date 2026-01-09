package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import com.cware.framework.core.basic.AbstractModel;

public class PaGmkClaim extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    
    private String paCode;					// 제휴사 번호
    private String paGroupCode;				// 제휴사 그룹 코드  02: G마켓 , 03 : Auction , 04 : G9
    private String payNo;					// 장바구니번호(결제번호)
    private String groupNo;					// 묶음배송비정책번호(배송번호)
    private String contrNo;					// 주문번호
    private String contrNoSeq;				// 주문번호순번
    private String goodsNo;					// EMS상품번호
    private String siteGoodsNo;				// 사이트상품번호
    private String requestUser;				// 반품/교환신청자(0:없음 1:구매자 2:판매자 3:고객센터 4:기타)    
    private String goodsStatus;				// 상품상태(0:알수없음 1:포장개봉미사용 2:포장개봉사용 3:미개봉)
    private String reason;					// 반품/교환사유( 0: 판매자귀책 1: 구매자귀책 2:기타)
    private String reasonCode;				// 반품/교환 클레임 요청 사유 코드
    private String reasonDetail;			// 반품/교환사유 상세
    private String approveUser;				// 반품/교환처리자(0:없음 1:구매자 2:판매자 3:고객센터 4:기타)
    private String claimStatus;				// 반품/교환상태
    private String isFastRefund;			// 빠른환불여부(TRUE :빠른환불 FALSE : 빠른환불 대상아님)
    private String paOrderGb;				// 반품/교환 구분
    
    private Timestamp orderDate;			// 주문일자
    private Timestamp payDate;				// 결제일자
    private Timestamp deliveryEndDate;		// 배송완료
    private Timestamp requestDate;			// 반품/교환신청일
    private Timestamp withDrawDate;			// 반품/교환철회일
    private Timestamp pickupEndDate;		// 반품/교환수거완료일
    private Timestamp holdDate;				// 반품/교환환불보류일

    private String isHold;					// 반품/교환환불보류설정여부
    private String holdReason;				// 반품/교환환불보류사유
    private Timestamp holdFreeDate;			// 환불/교환보류해제일
    private Timestamp approveDate;			// 환불승인일(반품)
    private Timestamp approveEndDate;		// 환불완료일(반품)

    private double shippingFee;				// 최초배송비금액
    private String whoShippingFee;			// 최초배송비지불주체(1:이베이부담 2:구매자부담 3:판매자부담)
    private String deliveryCompCode;		// 원배송택배사코드 
    private String invoiceNo;				// 원배송 송장번호
    private String pickupStatus;			// 반품/교환수거상태(0:없음 1:이베이수거지시 2:이베이수거지시아님)

    private String delyName;				// 반품/교환수거택배사명
    private String delyNo;					// 반품/교환송장번호
    private String delyDuty;				// 배송비지불주체(1:이베이부담 2:구매자부담 3:판매자부담)
    private double returnShippingFee;		// 반품배송비금액
    private String returnShippingFeeWay;	// 반품배송비결제수단(1:바로결제 2:환불금 차감 3:판매자 직접결제 4:상품에 동봉 5:스마일캐쉬 6:기타)
    private String whoAddReturnShippingFee; // 반품 추가 지불 구체(1:이베이부담 2:구매자부담 3:판매자부담)	 
    private double addReturnShippingFee;    // 반품추가비금액	 
    private String addReturnShippingFeeWay; // 반품추가배송비결제수단(1:바로결제 2:환불금 차감 3:판매자 직접결제 4:상품에 동봉 5:스마일캐쉬 6:기타)
    
    private String receiverHpNo;			// 반품/교환회수인휴대폰(판매자정보)
    private String receiverTelNo;			// 반품/교환회수인전화번호(판매자정보)
    private String receiverZipCode;			// 반품/교환회수지우편번호(판매자정보)
    private String receiverAddr;			// 반품회수지주소1+2(판매자정보)
    private String receiverAddr1;			// 반품/교환회수지주소1(판매자정보)
    private String receiverAddr2;			// 반품/교환회수지주소2(판매자정보)
    
    private String senderHpNo;				// 반품/교환수령인/발송인휴대폰
    private String senderTelNo;				// 반품/교환수령인/발송인전화번호
    private String senderZipCode; 			// 반품/교환수거지우편번호
    private String senderAddr;				// 반품수거지주소1+2	 
    private String senderAddr1;				// 반품/교환수거지주소1
    private String senderAddr2;				// 반품/교환수거지주소
    
    private String fastRefundStatus;		// 빠른환불보상상태
    private String exchStatus;				// 교환상태 (1:교환요청 2:교환수거완료 3:교환환불보류 4:교환환불완료 5교환철회)
    private Timestamp exchResendDate;		// 교환재발송일
    private Timestamp exchResendEndDate;	// 교환재발송배송완료일
    private Timestamp exchEndDate;			// 교환완료일
    private double exchShippingFee;			// 교환배송비금액
    private String exchWhoShippingFee;		// 교환배송비지불주체
    private String exchShippingFeeWay;	// 교환배송비결제수단
    private Timestamp exchDeliveryEndDate;  // 원배송완료일
    private String exchDeliveryCompName;	// 교환재발송택배사명
    private String exchInvoiceNo;			// 교환재발송송장번호
    private String exchReceiverName;		// 재발송수령인명
    private String exchReceiverHpNo;		// 재발송수령인휴대폰
    private String exchReceiverTelNo;		// 재발송수령인전화번호
    private String exchReceiverZipCode;		// 재배송지우편번호

    private String exchReceiverAddr;		// 재배송지주소1+2
    private String exchReceiverAddr1;		// 재배송지주소1
    private String exchReceiverAddr2;		// 재배송지주소2

    private String receiverName;			// 반품/교환회수인명(판매자정보)
    private String senderName;				// 반품/교환수령인명/발송인명
    
    
    
    
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
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
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getContrNoSeq() {
		return contrNoSeq;
	}
	public void setContrNoSeq(String contrNoSeq) {
		this.contrNoSeq = contrNoSeq;
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
	public String getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
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
	public String getApproveUser() {
		return approveUser;
	}
	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getIsFastRefund() {
		return isFastRefund;
	}
	public void setIsFastRefund(String isFastRefund) {
		this.isFastRefund = isFastRefund;
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
	public Timestamp getDeliveryEndDate() {
		return deliveryEndDate;
	}
	public void setDeliveryEndDate(Timestamp deliveryEndDate) {
		this.deliveryEndDate = deliveryEndDate;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	public Timestamp getWithDrawDate() {
		return withDrawDate;
	}
	public void setWithDrawDate(Timestamp withDrawDate) {
		this.withDrawDate = withDrawDate;
	}
	public Timestamp getPickupEndDate() {
		return pickupEndDate;
	}
	public void setPickupEndDate(Timestamp pickupEndDate) {
		this.pickupEndDate = pickupEndDate;
	}
	public Timestamp getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(Timestamp holdDate) {
		this.holdDate = holdDate;
	}
	public String getIsHold() {
		return isHold;
	}
	public void setIsHold(String isHold) {
		this.isHold = isHold;
	}
	public String getHoldReason() {
		return holdReason;
	}
	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}
	public Timestamp getHoldFreeDate() {
		return holdFreeDate;
	}
	public void setHoldFreeDate(Timestamp holdFreeDate) {
		this.holdFreeDate = holdFreeDate;
	}
	public Timestamp getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Timestamp approveDate) {
		this.approveDate = approveDate;
	}
	public Timestamp getApproveEndDate() {
		return approveEndDate;
	}
	public void setApproveEndDate(Timestamp approveEndDate) {
		this.approveEndDate = approveEndDate;
	}
	public double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getWhoShippingFee() {
		return whoShippingFee;
	}
	public void setWhoShippingFee(String whoShippingFee) {
		this.whoShippingFee = whoShippingFee;
	}
	public String getDeliveryCompCode() {
		return deliveryCompCode;
	}
	public void setDeliveryCompCode(String delivertCompCode) {
		this.deliveryCompCode = delivertCompCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPickupStatus() {
		return pickupStatus;
	}
	public void setPickupStatus(String pickupStatus) {
		this.pickupStatus = pickupStatus;
	}
	public String getDelyName() {
		return delyName;
	}
	public void setDelyName(String delyName) {
		this.delyName = delyName;
	}
	public String getDelyNo() {
		return delyNo;
	}
	public void setDelyNo(String delyNo) {
		this.delyNo = delyNo;
	}
	public String getDelyDuty() {
		return delyDuty;
	}
	public void setDelyDuty(String delyDuty) {
		this.delyDuty = delyDuty;
	}
	public double getReturnShippingFee() {
		return returnShippingFee;
	}
	public void setReturnShippingFee(double returnShippingFee) {
		this.returnShippingFee = returnShippingFee;
	}
	public String getReturnShippingFeeWay() {
		return returnShippingFeeWay;
	}
	public void setReturnShippingFeeWay(String returnShippingFeeWay) {
		this.returnShippingFeeWay = returnShippingFeeWay;
	}
	public String getWhoAddReturnShippingFee() {
		return whoAddReturnShippingFee;
	}
	public void setWhoAddReturnShippingFee(String whoAddReturnShippingFee) {
		this.whoAddReturnShippingFee = whoAddReturnShippingFee;
	}
	public double getAddReturnShippingFee() {
		return addReturnShippingFee;
	}
	public void setAddReturnShippingFee(double addReturnShippingFee) {
		this.addReturnShippingFee = addReturnShippingFee;
	}
	public String getAddReturnShippingFeeWay() {
		return addReturnShippingFeeWay;
	}
	public void setAddReturnShippingFeeWay(String addReturnShippingFeeWay) {
		this.addReturnShippingFeeWay = addReturnShippingFeeWay;
	}
	public String getReceiverHpNo() {
		return receiverHpNo;
	}
	public void setReceiverHpNo(String receiverHpNo) {
		this.receiverHpNo = receiverHpNo;
	}
	public String getReceiverTelNo() {
		return receiverTelNo;
	}
	public void setReceiverTelNo(String receiverTelNo) {
		this.receiverTelNo = receiverTelNo;
	}
	public String getReceiverZipCode() {
		return receiverZipCode;
	}
	public void setReceiverZipCode(String receiverZipCode) {
		this.receiverZipCode = receiverZipCode;
	}
	public String getReceiverAddr() {
		return receiverAddr;
	}
	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}
	public String getReceiverAddr1() {
		return receiverAddr1;
	}
	public void setReceiverAddr1(String receiverAddr1) {
		this.receiverAddr1 = receiverAddr1;
	}
	public String getReceiverAddr2() {
		return receiverAddr2;
	}
	public void setReceiverAddr2(String receiverAddr2) {
		this.receiverAddr2 = receiverAddr2;
	}
	public String getSenderHpNo() {
		return senderHpNo;
	}
	public void setSenderHpNo(String senderHpNo) {
		this.senderHpNo = senderHpNo;
	}
	public String getSenderTelNo() {
		return senderTelNo;
	}
	public void setSenderTelNo(String senderTelNo) {
		this.senderTelNo = senderTelNo;
	}
	public String getSenderZipCode() {
		return senderZipCode;
	}
	public void setSenderZipCode(String senderZipCode) {
		this.senderZipCode = senderZipCode;
	}
	public String getSenderAddr() {
		return senderAddr;
	}
	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}
	public String getSenderAddr1() {
		return senderAddr1;
	}
	public void setSenderAddr1(String senderAddr1) {
		this.senderAddr1 = senderAddr1;
	}
	public String getSenderAddr2() {
		return senderAddr2;
	}
	public void setSenderAddr2(String senderAddr2) {
		this.senderAddr2 = senderAddr2;
	}
	public String getFastRefundStatus() {
		return fastRefundStatus;
	}
	public void setFastRefundStatus(String fastRefundStatus) {
		this.fastRefundStatus = fastRefundStatus;
	}
	public String getExchStatus() {
		return exchStatus;
	}
	public void setExchStatus(String exchStatus) {
		this.exchStatus = exchStatus;
	}
	public Timestamp getExchResendDate() {
		return exchResendDate;
	}
	public void setExchResendDate(Timestamp exchResendDate) {
		this.exchResendDate = exchResendDate;
	}
	public Timestamp getExchResendEndDate() {
		return exchResendEndDate;
	}
	public void setExchResendEndDate(Timestamp exchResendEndDate) {
		this.exchResendEndDate = exchResendEndDate;
	}
	public Timestamp getExchEndDate() {
		return exchEndDate;
	}
	public void setExchEndDate(Timestamp exchEndDate) {
		this.exchEndDate = exchEndDate;
	}
	public double getExchShippingFee() {
		return exchShippingFee;
	}
	public void setExchShippingFee(double exchShippingFee) {
		this.exchShippingFee = exchShippingFee;
	}
	public String getExchWhoShippingFee() {
		return exchWhoShippingFee;
	}
	public void setExchWhoShippingFee(String exchWhoShippingFee) {
		this.exchWhoShippingFee = exchWhoShippingFee;
	}
	public String getExchShippingFeeWay() {
		return exchShippingFeeWay;
	}
	public void setExchShippingFeeWay(String exchShippingFeeWay) {
		this.exchShippingFeeWay = exchShippingFeeWay;
	}
	public Timestamp getExchDeliveryEndDate() {
		return exchDeliveryEndDate;
	}
	public void setExchDeliveryEndDate(Timestamp exchDeliveryEndDate) {
		this.exchDeliveryEndDate = exchDeliveryEndDate;
	}
	public String getExchDeliveryCompName() {
		return exchDeliveryCompName;
	}
	public void setExchDeliveryCompName(String exchDeliveryCompName) {
		this.exchDeliveryCompName = exchDeliveryCompName;
	}
	public String getExchInvoiceNo() {
		return exchInvoiceNo;
	}
	public void setExchInvoiceNo(String exchInvoiceNo) {
		this.exchInvoiceNo = exchInvoiceNo;
	}
	public String getExchReceiverName() {
		return exchReceiverName;
	}
	public void setExchReceiverName(String exchReceiverName) {
		this.exchReceiverName = exchReceiverName;
	}
	public String getExchReceiverHpNo() {
		return exchReceiverHpNo;
	}
	public void setExchReceiverHpNo(String exchReceiverHpNo) {
		this.exchReceiverHpNo = exchReceiverHpNo;
	}
	public String getExchReceiverTelNo() {
		return exchReceiverTelNo;
	}
	public void setExchReceiverTelNo(String exchReceiverTelNo) {
		this.exchReceiverTelNo = exchReceiverTelNo;
	}
	public String getExchReceiverZipCode() {
		return exchReceiverZipCode;
	}
	public void setExchReceiverZipCode(String exchReceiverZipCode) {
		this.exchReceiverZipCode = exchReceiverZipCode;
	}
	public String getExchReceiverAddr() {
		return exchReceiverAddr;
	}
	public void setExchReceiverAddr(String exchReceiverAddr) {
		this.exchReceiverAddr = exchReceiverAddr;
	}
	public String getExchReceiverAddr1() {
		return exchReceiverAddr1;
	}
	public void setExchReceiverAddr1(String exchReceiverAddr1) {
		this.exchReceiverAddr1 = exchReceiverAddr1;
	}
	public String getExchReceiverAddr2() {
		return exchReceiverAddr2;
	}
	public void setExchReceiverAddr2(String exchReceiverAddr2) {
		this.exchReceiverAddr2 = exchReceiverAddr2;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
    
    
   
}
















