package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PaFapleOrderSettlement {
	
	private String paFaplesettlementNo;           //패션플러스 정산데이터 SEQ
	private String orderId;                        //주문번호
	private String orderSeq;                       //주문순번
	private String orderType ;                     //주문유형
	private String deliveryDate;                   //배송출발일
	private String itemNo;                         //품목번호
	private String itemName;                       //품목명
	private String attr1Name;                      //속성1
	private String attr2Name;                      //속성2
	private long   margin;                          //마진
	private long   qty;                             //수량
	private long   salesAmt;                       //판매금액
	private long   purchaseAmt;                    //구매금액
	private long   profitsAmt;                     //판매수수료
	private String salecpon;                     //업체부담쿠폰사용
	private String oldOrderId;                 //이전주문번호 
	private long   couponFapleAmt;             //패션플러스부담쿠폰액
	private String completeDate;                   //배송완료일
	private long   giveAmt;             //지급대상액
	private String brandIdSalesName;             //실적브랜드명
	private String optId;                          //패플옵션코드
	
	public String getPaFaplesettlementNo() {
		return paFaplesettlementNo;
	}
	public void setPaFaplesettlementNo(String paFaplesettlementNo) {
		this.paFaplesettlementNo = paFaplesettlementNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAttr1Name() {
		return attr1Name;
	}
	public void setAttr1Name(String attr1Name) {
		this.attr1Name = attr1Name;
	}
	public String getAttr2Name() {
		return attr2Name;
	}
	public void setAttr2Name(String attr2Name) {
		this.attr2Name = attr2Name;
	}
	public long getMargin() {
		return margin;
	}
	public void setMargin(long margin) {
		this.margin = margin;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public long getSalesAmt() {
		return salesAmt;
	}
	public void setSalesAmt(long salesAmt) {
		this.salesAmt = salesAmt;
	}
	public long getPurchaseAmt() {
		return purchaseAmt;
	}
	public void setPurchaseAmt(long purchaseAmt) {
		this.purchaseAmt = purchaseAmt;
	}
	public long getProfitsAmt() {
		return profitsAmt;
	}
	public void setProfitsAmt(long profitsAmt) {
		this.profitsAmt = profitsAmt;
	}
	public String getSalecpon() {
		return salecpon;
	}
	public void setSalecpon(String salecpon) {
		this.salecpon = salecpon;
	}
	public String getOldOrderId() {
		return oldOrderId;
	}
	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}
	public long getCouponFapleAmt() {
		return couponFapleAmt;
	}
	public void setCouponFapleAmt(long couponFapleAmt) {
		this.couponFapleAmt = couponFapleAmt;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public long getGiveAmt() {
		return giveAmt;
	}
	public void setGiveAmt(long giveAmt) {
		this.giveAmt = giveAmt;
	}
	public String getBrandIdSalesName() {
		return brandIdSalesName;
	}
	public void setBrandIdSalesName(String brandIdSalesName) {
		this.brandIdSalesName = brandIdSalesName;
	}
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}

	
	
	
}