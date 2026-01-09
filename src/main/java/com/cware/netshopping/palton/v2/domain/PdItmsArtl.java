package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdItmsArtl {
	String pdArtlCd; // 상품항목코드

	// 상품항목내용
	// 해당 고시정보항목의 항목값을 입력한다.
	String pdArtlCnts;

	public String getPdArtlCd() {
		return pdArtlCd;
	}

	public void setPdArtlCd(String pdArtlCd) {
		this.pdArtlCd = pdArtlCd;
	}

	public String getPdArtlCnts() {
		return pdArtlCnts;
	}

	public void setPdArtlCnts(String pdArtlCnts) {
		this.pdArtlCnts = pdArtlCnts;
	}

	@Override
	public String toString() {
		return "PdItmsArtl [pdArtlCd=" + pdArtlCd + ", pdArtlCnts=" + pdArtlCnts + "]";
	}

}
