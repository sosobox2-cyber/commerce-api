package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QualityFile {
	// 품질 검증 파일 구분 코드 (commCd:I045)
	// 61 원산지증명서
	// 65 수입신고필증
	// 63 KC성적서
	// 64 유기농인증
	// 65 수입신고필증
	// 66 광고심의필
	// 6B 기타
	String itemDescDivCd;
	String imgFileNm; // 이미지 파일 위치

	public String getItemDescDivCd() {
		return itemDescDivCd;
	}

	public void setItemDescDivCd(String itemDescDivCd) {
		this.itemDescDivCd = itemDescDivCd;
	}

	public String getImgFileNm() {
		return imgFileNm;
	}

	public void setImgFileNm(String imgFileNm) {
		this.imgFileNm = imgFileNm;
	}

	@Override
	public String toString() {
		return "QualityFile [itemDescDivCd=" + itemDescDivCd + ", imgFileNm=" + imgFileNm + "]";
	}

}
