package com.cware.netshopping.domain.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 주문취소요청 접수 내역
 * @author FIC05202
 *
 */
@SuppressWarnings("serial")
public class PaIntpCancellist implements Serializable {
	/** 주문매핑순번(TPAORDERM.PK) */
	private String mappingSeq;
	/** 인터파크 주문번호 */
	private String ordNo;
	/** 인터파크 주문순번 */
	private String ordSeq;
	/** 옵션상품유형 */
	private String optPrdTp;
	/** 주문진행단계(J004) */
	private String doFlag;
	/** 인터파크 클레임요청순번 */
	private String clmreqSeq;
	/** 제휴사구분 */
	private String paCode;
	/** 배송비번호 */
	private Integer delvsetlSeq;
	/** 클레임요청수량 */
	private Integer clmreqQty;
	/** 운송장번호 */
	private String slipNo;
	/** 인터파크 주문취소접수일자(생성일자) */
	private Date insertDate;
	/** 인터파크 주문취소요청 생성일자-포맷적용된 문자형 */
	private String insertDateString;
	/** 제휴사 배송사 코드 */
	private String paDelyGb;
	/** TV쇼핑 주문번호 */
	private String orderNo;
	/** TV쇼핑 상품순번 */
	private String orderGSeq;
	/** TV쇼핑 세트순번 */
	private String orderDSeq;
	/** TV쇼핑 처리순번 */
	private String orderWSeq;
	/** 상품구분(J006) */
	private String goodsGb;
	/** 운송장식별번호 */
	private String slipINo;
	/** 배송시작일자 */
	private Date shipStartDate;
	/** 상품코드 */
	private String goodsCode;
	/** 단품코드 */
	private String goodsdtCode;
	/** 제휴사 주문구분(ㅓ007) */
	private String paOrderGb;
	/** 배송구분(B005) */
	private String delyGb;
	
	private String paOrderNo;

	public String getMappingSeq() {
		return mappingSeq;
	}

	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public String getOrdSeq() {
		return ordSeq;
	}

	public void setOrdSeq(String ordSeq) {
		this.ordSeq = ordSeq;
	}

	public String getOptPrdTp() {
		return optPrdTp;
	}

	public void setOptPrdTp(String optPrdTp) {
		this.optPrdTp = optPrdTp;
	}

	public String getDoFlag() {
		return doFlag;
	}

	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}

	public String getClmreqSeq() {
		return clmreqSeq;
	}

	public void setClmreqSeq(String clmreqSeq) {
		this.clmreqSeq = clmreqSeq;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public Integer getDelvsetlSeq() {
		return delvsetlSeq;
	}

	public void setDelvsetlSeq(Integer delvsetlSeq) {
		this.delvsetlSeq = delvsetlSeq;
	}

	public Integer getClmreqQty() {
		return clmreqQty;
	}

	public void setClmreqQty(Integer clmreqQty) {
		this.clmreqQty = clmreqQty;
	}

	public String getSlipNo() {
		return slipNo;
	}

	public void setSlipNo(String slipNo) {
		this.slipNo = slipNo;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public String getInsertDateString() {
		return insertDateString;
	}

	public void setInsertDateString(String insertDateString) {
		this.insertDateString = insertDateString;
	}

	public String getPaDelyGb() {
		return paDelyGb;
	}

	public void setPaDelyGb(String paDelyGb) {
		this.paDelyGb = paDelyGb;
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

	public String getOrderDSeq() {
		return orderDSeq;
	}

	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}

	public String getOrderWSeq() {
		return orderWSeq;
	}

	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}

	public String getGoodsGb() {
		return goodsGb;
	}

	public void setGoodsGb(String goodsGb) {
		this.goodsGb = goodsGb;
	}

	public String getSlipINo() {
		return slipINo;
	}

	public void setSlipINo(String slipINo) {
		this.slipINo = slipINo;
	}

	public Date getShipStartDate() {
		return shipStartDate;
	}

	public void setShipStartDate(Date shipStartDate) {
		this.shipStartDate = shipStartDate;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsdtCode() {
		return goodsdtCode;
	}

	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}

	public String getPaOrderGb() {
		return paOrderGb;
	}

	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}

	public String getDelyGb() {
		return delyGb;
	}

	public void setDelyGb(String delyGb) {
		this.delyGb = delyGb;
	}

	public String getPaOrderNo() {
		return paOrderNo;
	}

	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}

}
