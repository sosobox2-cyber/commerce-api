package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Clrchip {
	// 원본이미지파일명(경로명)
	// 파일명을 포함한 다운로드가 가능한 경로를 입력한다.
	// ex) http://abc.com/12/34/56/78_90.jpg
	String origImgFileNm;

	public String getOrigImgFileNm() {
		return origImgFileNm;
	}

	public void setOrigImgFileNm(String origImgFileNm) {
		this.origImgFileNm = origImgFileNm;
	}

	@Override
	public String toString() {
		return "Clrchip [origImgFileNm=" + origImgFileNm + "]";
	}

}
