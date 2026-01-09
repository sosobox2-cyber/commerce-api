package com.cware.netshopping.domain.paintp.xml;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 주문내역조회 > 주문정보 > 주문상품정보 xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "PRD")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpOrderProductVO extends PaIntpProductVO {
	private static final long serialVersionUID = 1L;
	
	/** 옵션명(선택형+입력명) */
	@XmlElement(name = "OPT_NM")
	private String optNm;
	
	/** 옵션명(선택형) */
	@XmlElement(name = "SEL_OPT_NM")
	private String selOptNm;
	
	/** 옵션명(입력형) */
	@XmlElement(name = "IN_OPT_NM")
	private String inOptNm;
	
	/** 실판매단가 */
	@XmlElement(name = "SALE_UNITCOST")
	private double saleUnitcost;
	
	/** 선할인단가 */
	@XmlElement(name = "PRE_USE_UNITCOST")
	private double preUseUnitcost;
	
	/** 선할인금액 */
	@XmlElement(name = "PRE_USE_AMT")
	private double preUseAmt;
	
	/** 주문수량 */
	@XmlElement(name = "ORD_QTY")
	private long ordQty;
	
	/** 주문금액 */
	@XmlElement(name = "ORD_AMT")
	private double ordAmt;
	
	/** 상태일자(출고지시일자, yyyyMMddHHmiss) */
	@XmlElement(name = "ORDCLM_STAT_DTS")
	private String ordclmStatDts;
	
	/** 인터파크 부담 쿠폰할인단가 */
	@XmlElement(name = "DC_COUPON_AMT")
	private double dcCouponAmt;
	
	/** 업체 부담 쿠폰할인단가 */
	@XmlElement(name = "ENTR_DC_COUPON_AMT")
	private double entrDcCouponAmt;
	
	/** 쿠폰할인단가 */
	@XmlElement(name = "TOT_DC_COUPON_AMT")
	private double totDcCouponAmt;
	
	/** 판매자 즉시 할인 단가 */
	@XmlElement(name = "ENTR_DIS_UNIT_COST")
	private double entrDisUnitCost;
	
	/** I-POINT 할인 단가 */
	@XmlElement(name = "IPOINT_DC_UNITCOST")
	private double ipointDcUnitcost;
	
	/** 실판매단가2(실판매단가 - I-POINT 할인단가) */
	@XmlElement(name = "REAL_SALE_UNITCOST")
	private double realSaleUnitcost;
	
	/** 배송비착불여부(Y:착불, N:선불,무료) */
	@XmlElement(name = "IS_COLLECTED")
	private String isCollected;
	
	/** 배송비번호 */
	@XmlElement(name = "DELVSETL_SEQ")
	private String delvsetlSeq;
	
	/** 상품명 */
	@XmlElement(name = "PRD_NM")
	private String prdNm;
	
	/** 해외구매대행여부 */
	@XmlElement(name = "ABROAD_BS_YN")
	private String abroadBsYn;
	
	/** 배송완료일자(yyyyMMdd) */
	@XmlElement(name = "DELV_COMPLETE_DT")
	private String delvCompleteDt;
	
	/** 현재 상태
	 * 50 발주확인
	 * 70 배송중
	 * 75 배송완료
	 * 80 구매확정
	 */
	@XmlElement(name = "CURRENT_STATE", nillable = true, required = false)
	private String currentState;

	public String getOptNm() {
		return optNm;
	}

	public void setOptNm(String optNm) {
		this.optNm = optNm;
	}

	public String getSelOptNm() {
		return selOptNm;
	}

	public void setSelOptNm(String selOptNm) {
		this.selOptNm = selOptNm;
	}

	public String getInOptNm() {
		return inOptNm;
	}

	public void setInOptNm(String inOptNm) {
		this.inOptNm = inOptNm;
	}

	public double getSaleUnitcost() {
		return saleUnitcost;
	}

	public void setSaleUnitcost(double saleUnitcost) {
		this.saleUnitcost = saleUnitcost;
	}

	public double getPreUseUnitcost() {
		return preUseUnitcost;
	}

	public void setPreUseUnitcost(double preUseUnitcost) {
		this.preUseUnitcost = preUseUnitcost;
	}

	public double getPreUseAmt() {
		return preUseAmt;
	}

	public void setPreUseAmt(double preUseAmt) {
		this.preUseAmt = preUseAmt;
	}

	public long getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(long ordQty) {
		this.ordQty = ordQty;
	}

	public double getOrdAmt() {
		return ordAmt;
	}

	public void setOrdAmt(double ordAmt) {
		this.ordAmt = ordAmt;
	}

	public String getOrdclmStatDts() {
		return ordclmStatDts;
	}

	public void setOrdclmStatDts(String ordclmStatDts) {
		this.ordclmStatDts = ordclmStatDts;
	}

	public double getDcCouponAmt() {
		return dcCouponAmt;
	}

	public void setDcCouponAmt(double dcCouponAmt) {
		this.dcCouponAmt = dcCouponAmt;
	}

	public double getEntrDcCouponAmt() {
		return entrDcCouponAmt;
	}

	public void setEntrDcCouponAmt(double entrDcCouponAmt) {
		this.entrDcCouponAmt = entrDcCouponAmt;
	}

	public double getTotDcCouponAmt() {
		return totDcCouponAmt;
	}

	public void setTotDcCouponAmt(double totDcCouponAmt) {
		this.totDcCouponAmt = totDcCouponAmt;
	}

	public double getEntrDisUnitCost() {
		return entrDisUnitCost;
	}

	public void setEntrDisUnitCost(double entrDisUnitCost) {
		this.entrDisUnitCost = entrDisUnitCost;
	}

	public double getIpointDcUnitcost() {
		return ipointDcUnitcost;
	}

	public void setIpointDcUnitcost(double ipointDcUnitcost) {
		this.ipointDcUnitcost = ipointDcUnitcost;
	}

	public double getRealSaleUnitcost() {
		return realSaleUnitcost;
	}

	public void setRealSaleUnitcost(double realSaleUnitcost) {
		this.realSaleUnitcost = realSaleUnitcost;
	}

	public String getIsCollected() {
		return isCollected;
	}

	public void setIsCollected(String isCollected) {
		this.isCollected = isCollected;
	}

	public String getDelvsetlSeq() {
		return delvsetlSeq;
	}

	public void setDelvsetlSeq(String delvsetlSeq) {
		this.delvsetlSeq = delvsetlSeq;
	}

	public String getPrdNm() {
		return prdNm;
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}

	public String getAbroadBsYn() {
		return abroadBsYn;
	}

	public void setAbroadBsYn(String abroadBsYn) {
		this.abroadBsYn = abroadBsYn;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getDelvCompleteDt() {
		return delvCompleteDt;
	}

	public void setDelvCompleteDt(String delvCompleteDt) {
		this.delvCompleteDt = delvCompleteDt;
	}
	
}
