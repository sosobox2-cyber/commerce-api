package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SafetyCerts {

	// 통합 어린이 인증
	private SafetyCert child;

	// 통합 전기 인증
	private SafetyCert electric;

	// 통합 생활용품 인증
	private SafetyCert life;

	// 통합 위해 우려 제품 인증
	private SafetyCert harmful;

	public SafetyCert getChild() {
		return child;
	}

	public void setChild(SafetyCert child) {
		this.child = child;
	}

	public SafetyCert getElectric() {
		return electric;
	}

	public void setElectric(SafetyCert electric) {
		this.electric = electric;
	}

	public SafetyCert getLife() {
		return life;
	}

	public void setLife(SafetyCert life) {
		this.life = life;
	}

	public SafetyCert getHarmful() {
		return harmful;
	}

	public void setHarmful(SafetyCert harmful) {
		this.harmful = harmful;
	}

	@Override
	public String toString() {
		return "SafetyCerts [child=" + child + ", electric=" + electric + ", life=" + life + ", harmful=" + harmful
				+ "]";
	}

}
