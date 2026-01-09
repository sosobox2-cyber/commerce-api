package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PurPsbQtyInfo {
	String itmByMinPurYn; // 단품별최소구매여부 [Y, N]

	// 단품별최소구매수량
	// 단품별최소구매여부가 Y인 경우 필수입력한다.
	Long itmByMinPurQty;

	String itmByMinPurMtpYn; // 단품별최소구매배수여부
	String itmByMaxPurPsbQtyYn; // 단품별최대구매가능수량여부 [Y, N]

	// 단품별최대구매수량
	// 단품별최대구매가능수량여부가 Y인 경우 필수입력한다.
	Long maxPurQty;

	// 단품별최대구매제한구분코드 [공통코드 : MAX_PUR_LMT_TYP_CD]
	// ONCE 1회제한
	// PERIOD 기간제한
	// FIXED 특정일자 제한
	// 미입력 시 기간제한(PERIOD)로 적용되고 단품별최대구매제한기간(maxPurLmtPrd)은 1일로 설정 된다.
	String maxPurLmtTypCd;

	// 단품별최대구매제한기간
	// 단품별최대구매제한구분코드가 기간제한(PERIOD)일 경우 필수입력한다.
	Long maxPurLmtPrd;

	// 단품별최대구매제한시작일자
	// 단품별최대구매제한구분코드가 특정일자 제한(FIXED)일 경우 필수입력한다. [YYYYMMDDHH24MISS ex)
	// 20190801100000]
	String maxPurLmtStrtDttm;

	// 단품별최대구매제한종료일자
	// 단품별최대구매제한구분코드가 특정일자 제한(FIXED)일 경우 필수입력한다. [YYYYMMDDHH24MISS ex)
	// 20190801100000]
	String maxPurLmtEndDttm;

	public String getItmByMinPurYn() {
		return itmByMinPurYn;
	}

	public void setItmByMinPurYn(String itmByMinPurYn) {
		this.itmByMinPurYn = itmByMinPurYn;
	}

	public Long getItmByMinPurQty() {
		return itmByMinPurQty;
	}

	public void setItmByMinPurQty(Long itmByMinPurQty) {
		this.itmByMinPurQty = itmByMinPurQty;
	}

	public String getItmByMinPurMtpYn() {
		return itmByMinPurMtpYn;
	}

	public void setItmByMinPurMtpYn(String itmByMinPurMtpYn) {
		this.itmByMinPurMtpYn = itmByMinPurMtpYn;
	}

	public String getItmByMaxPurPsbQtyYn() {
		return itmByMaxPurPsbQtyYn;
	}

	public void setItmByMaxPurPsbQtyYn(String itmByMaxPurPsbQtyYn) {
		this.itmByMaxPurPsbQtyYn = itmByMaxPurPsbQtyYn;
	}

	public Long getMaxPurQty() {
		return maxPurQty;
	}

	public void setMaxPurQty(Long maxPurQty) {
		this.maxPurQty = maxPurQty;
	}

	public String getMaxPurLmtTypCd() {
		return maxPurLmtTypCd;
	}

	public void setMaxPurLmtTypCd(String maxPurLmtTypCd) {
		this.maxPurLmtTypCd = maxPurLmtTypCd;
	}

	public Long getMaxPurLmtPrd() {
		return maxPurLmtPrd;
	}

	public void setMaxPurLmtPrd(Long maxPurLmtPrd) {
		this.maxPurLmtPrd = maxPurLmtPrd;
	}

	public String getMaxPurLmtStrtDttm() {
		return maxPurLmtStrtDttm;
	}

	public void setMaxPurLmtStrtDttm(String maxPurLmtStrtDttm) {
		this.maxPurLmtStrtDttm = maxPurLmtStrtDttm;
	}

	public String getMaxPurLmtEndDttm() {
		return maxPurLmtEndDttm;
	}

	public void setMaxPurLmtEndDttm(String maxPurLmtEndDttm) {
		this.maxPurLmtEndDttm = maxPurLmtEndDttm;
	}

	@Override
	public String toString() {
		return "PurPsbQtyInfo [itmByMinPurYn=" + itmByMinPurYn + ", itmByMinPurQty=" + itmByMinPurQty
				+ ", itmByMinPurMtpYn=" + itmByMinPurMtpYn + ", itmByMaxPurPsbQtyYn=" + itmByMaxPurPsbQtyYn
				+ ", maxPurQty=" + maxPurQty + ", maxPurLmtTypCd=" + maxPurLmtTypCd + ", maxPurLmtPrd=" + maxPurLmtPrd
				+ ", maxPurLmtStrtDttm=" + maxPurLmtStrtDttm + ", maxPurLmtEndDttm=" + maxPurLmtEndDttm + "]";
	}

}
