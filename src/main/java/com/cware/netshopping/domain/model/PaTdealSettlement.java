package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PaTdealSettlement {
	
	private long	mallOptionNo;			//상품옵션번호
	private String	mallName;				// 쇼핑몰명
	private long	commissionRate;			// 수수료율
	private long	partnerDiscountAmt;		// 추가/상품/사은품할인 파트너분담
	private String  companyName;			// 법인명
	private String 	settlementYmdt;			// 배송완료일/ 구매확정일
	private String 	memo;					// 메모
	private long	mallDeliveryAdjustAmt; 	// 배송비금액조정 쇼핑몰부담
	private long 	refundAdjustAmt;		// 환불보류 금액조정
	private long	purchasePrice;			// 공급가(또는 매입가)액(O)
	private long	mallProductNo;			// 상품번호
	private String	productName;			// 상품명
	private long 	mallDiscountAmt;		// 추가/상품/사은품할인 쇼핑몰분담
	private long	partnerProductAdjustAmt;	// 상품금액조정 파트너부담(E)
	private long	partnerNo;					// 파트너 번호 
	private String	settlementDeliveryTypeLabel;	// 배송구분(배송번호)
	private long	partnerDeliveryAdjustAmt;		// 배송비금액조정 파트너부담(F)
	private long	mallDeliveryAmt;				// 쇼핑몰배송비(G)
	private	long	immediateDiscountedPrice;		// 판매가(할인적용가)
	private long	seq;							// seq
	private String 	valueAddedTaxTypeLabel;			// 과세유형(DUTY: 과세, DUTYFREE: 면세, SMALL: 영세)
	private String 	orderNo;						// 주문번호 
	private String 	partnerName;					// 파트너사
	private long 	commissionAmt;					// 판매수수료(B2=A*B1 or A-O)
	private	long 	orderProductOptionNo;			// 주문옵션번호
	private	long 	orderCnt;						// 판매수량
	private long 	mallProductAdjustAmt;			// 상품금액조정 쇼핑몰부담
	private long 	settlementAmt;					// 정산예상금액(I=A-B2+C-D-E-F-G+H)
	private long 	salesAmt;						// 판매액(A)
	private long 	deliveryAmt;					// 배송비(C)
	private long 	orderProductNo;					// 주문상품번호
	private String  refundYmdt;						// 환불완료일 (nullable)
	private	String  partnerType; 					// 파트너 구분	
	private String  optionManagementCd;				// 옵션관리코드 (nullable)
	private long 	refundCnt;						// 환불수량
	private String 	payYmdt;						// 결제완료일
	private String 	option;						// 옵션명 (nullable)
	private String  orderGb;					// 별도 추가 값 --> 배송비 구분 (초도배송비 : 1 , 반품 배송비 : 2)
	private String  shippingNo;					// 별도 추가 값 --> 배송번호 
	public long getMallOptionNo() {
		return mallOptionNo;
	}
	public void setMallOptionNo(long mallOptionNo) {
		this.mallOptionNo = mallOptionNo;
	}
	public String getMallName() {
		return mallName;
	}
	public void setMallName(String mallName) {
		this.mallName = mallName;
	}
	public long getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(long commissionRate) {
		this.commissionRate = commissionRate;
	}
	public long getPartnerDiscountAmt() {
		return partnerDiscountAmt;
	}
	public void setPartnerDiscountAmt(long partnerDiscountAmt) {
		this.partnerDiscountAmt = partnerDiscountAmt;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSettlementYmdt() {
		return settlementYmdt;
	}
	public void setSettlementYmdt(String settlementYmdt) {
		this.settlementYmdt = settlementYmdt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public long getMallDeliveryAdjustAmt() {
		return mallDeliveryAdjustAmt;
	}
	public void setMallDeliveryAdjustAmt(long mallDeliveryAdjustAmt) {
		this.mallDeliveryAdjustAmt = mallDeliveryAdjustAmt;
	}
	public long getRefundAdjustAmt() {
		return refundAdjustAmt;
	}
	public void setRefundAdjustAmt(long refundAdjustAmt) {
		this.refundAdjustAmt = refundAdjustAmt;
	}
	public long getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public long getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(long mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getMallDiscountAmt() {
		return mallDiscountAmt;
	}
	public void setMallDiscountAmt(long mallDiscountAmt) {
		this.mallDiscountAmt = mallDiscountAmt;
	}
	public long getPartnerProductAdjustAmt() {
		return partnerProductAdjustAmt;
	}
	public void setPartnerProductAdjustAmt(long partnerProductAdjustAmt) {
		this.partnerProductAdjustAmt = partnerProductAdjustAmt;
	}
	public long getPartnerNo() {
		return partnerNo;
	}
	public void setPartnerNo(long partnerNo) {
		this.partnerNo = partnerNo;
	}
	public String getSettlementDeliveryTypeLabel() {
		return settlementDeliveryTypeLabel;
	}
	public void setSettlementDeliveryTypeLabel(String settlementDeliveryTypeLabel) {
		this.settlementDeliveryTypeLabel = settlementDeliveryTypeLabel;
	}
	public long getPartnerDeliveryAdjustAmt() {
		return partnerDeliveryAdjustAmt;
	}
	public void setPartnerDeliveryAdjustAmt(long partnerDeliveryAdjustAmt) {
		this.partnerDeliveryAdjustAmt = partnerDeliveryAdjustAmt;
	}
	public long getMallDeliveryAmt() {
		return mallDeliveryAmt;
	}
	public void setMallDeliveryAmt(long mallDeliveryAmt) {
		this.mallDeliveryAmt = mallDeliveryAmt;
	}
	public long getImmediateDiscountedPrice() {
		return immediateDiscountedPrice;
	}
	public void setImmediateDiscountedPrice(long immediateDiscountedPrice) {
		this.immediateDiscountedPrice = immediateDiscountedPrice;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getValueAddedTaxTypeLabel() {
		return valueAddedTaxTypeLabel;
	}
	public void setValueAddedTaxTypeLabel(String valueAddedTaxTypeLabel) {
		this.valueAddedTaxTypeLabel = valueAddedTaxTypeLabel;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public long getCommissionAmt() {
		return commissionAmt;
	}
	public void setCommissionAmt(long commissionAmt) {
		this.commissionAmt = commissionAmt;
	}
	public long getOrderProductOptionNo() {
		return orderProductOptionNo;
	}
	public void setOrderProductOptionNo(long orderProductOptionNo) {
		this.orderProductOptionNo = orderProductOptionNo;
	}
	public long getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(long orderCnt) {
		this.orderCnt = orderCnt;
	}
	public long getMallProductAdjustAmt() {
		return mallProductAdjustAmt;
	}
	public void setMallProductAdjustAmt(long mallProductAdjustAmt) {
		this.mallProductAdjustAmt = mallProductAdjustAmt;
	}
	public long getSettlementAmt() {
		return settlementAmt;
	}
	public void setSettlementAmt(long settlementAmt) {
		this.settlementAmt = settlementAmt;
	}
	public long getSalesAmt() {
		return salesAmt;
	}
	public void setSalesAmt(long salesAmt) {
		this.salesAmt = salesAmt;
	}
	public long getDeliveryAmt() {
		return deliveryAmt;
	}
	public void setDeliveryAmt(long deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
	}
	public long getOrderProductNo() {
		return orderProductNo;
	}
	public void setOrderProductNo(long orderProductNo) {
		this.orderProductNo = orderProductNo;
	}
	public String getRefundYmdt() {
		return refundYmdt;
	}
	public void setRefundYmdt(String refundYmdt) {
		this.refundYmdt = refundYmdt;
	}
	public String getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public long getRefundCnt() {
		return refundCnt;
	}
	public void setRefundCnt(long refundCnt) {
		this.refundCnt = refundCnt;
	}
	public String getPayYmdt() {
		return payYmdt;
	}
	public void setPayYmdt(String payYmdt) {
		this.payYmdt = payYmdt;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getOrderGb() {
		return orderGb;
	}
	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	public String getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}
	
	
}