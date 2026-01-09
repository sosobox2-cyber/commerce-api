package com.cware.netshopping.domain.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cware.netshopping.common.Constants;

/**
 * 주문취소 대상 목록 주요항목
 * @author FIC05202
 *
 */
@SuppressWarnings("serial")
public class PaIntpCancellistKey implements Serializable {

	/** 인터파크 주문번호 */
	private String ordNo;
	
	/** 인터파크 주문순번 */
	private String ordSeq;
	
	/** 배송비전호 */
	private Integer delvsetlSeq;
	
	/** 클레임요청순번 */
	private String clmreqSeq;
	
	/** 클레임요청수량 */
	private Integer clmreqQty;
	
	/** 옵션상품유형 */
	private String optPrdTp;
	
	/** 제휴 구분 */
	private String paCode;

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

	public Integer getDelvsetlSeq() {
		return delvsetlSeq;
	}

	public void setDelvsetlSeq(Integer delvsetlSeq) {
		this.delvsetlSeq = delvsetlSeq;
	}

	public String getClmreqSeq() {
		return clmreqSeq;
	}

	public void setClmreqSeq(String clmreqSeq) {
		this.clmreqSeq = clmreqSeq;
	}

	public Integer getClmreqQty() {
		return clmreqQty;
	}

	public void setClmreqQty(Integer clmreqQty) {
		this.clmreqQty = clmreqQty;
	}

	public String getOptPrdTp() {
		return optPrdTp;
	}

	public void setOptPrdTp(String optPrdTp) {
		this.optPrdTp = optPrdTp;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

}
