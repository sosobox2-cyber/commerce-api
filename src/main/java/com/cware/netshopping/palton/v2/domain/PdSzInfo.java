package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdSzInfo {
	String pdWdthSz; // 상품가로사이즈 (cm)
	String pdLnthSz; // 상품세로사이즈 (cm)
	String pdHghtSz; // 상품높이사이즈 (cm)
	String pckWdthSz; // 포장가로사이즈 (cm)
	String pckLnthSz; // 포장세로사이즈 (cm)
	String pckHghtSz; // 포장높이사이즈 (cm)

	public String getPdWdthSz() {
		return pdWdthSz;
	}

	public void setPdWdthSz(String pdWdthSz) {
		this.pdWdthSz = pdWdthSz;
	}

	public String getPdLnthSz() {
		return pdLnthSz;
	}

	public void setPdLnthSz(String pdLnthSz) {
		this.pdLnthSz = pdLnthSz;
	}

	public String getPdHghtSz() {
		return pdHghtSz;
	}

	public void setPdHghtSz(String pdHghtSz) {
		this.pdHghtSz = pdHghtSz;
	}

	public String getPckWdthSz() {
		return pckWdthSz;
	}

	public void setPckWdthSz(String pckWdthSz) {
		this.pckWdthSz = pckWdthSz;
	}

	public String getPckLnthSz() {
		return pckLnthSz;
	}

	public void setPckLnthSz(String pckLnthSz) {
		this.pckLnthSz = pckLnthSz;
	}

	public String getPckHghtSz() {
		return pckHghtSz;
	}

	public void setPckHghtSz(String pckHghtSz) {
		this.pckHghtSz = pckHghtSz;
	}

	@Override
	public String toString() {
		return "PdSzInfo [pdWdthSz=" + pdWdthSz + ", pdLnthSz=" + pdLnthSz + ", pdHghtSz=" + pdHghtSz + ", pckWdthSz="
				+ pckWdthSz + ", pckLnthSz=" + pckLnthSz + ", pckHghtSz=" + pckHghtSz + "]";
	}

}
